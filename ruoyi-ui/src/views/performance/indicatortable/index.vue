<template>
  <div class="app-container">
    <el-row :gutter="20">

      <!--父级数据-->
      <el-col :span="6" :xs="24">
        <div class="head-container" >
          <h4>绩效考核指标</h4>
        </div>
        <div class="head-container" >
          <el-tree
            :data="indicatorParentOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>

      <!--子级数据-->
      <el-col :span="18" :xs="24">
        <!--查询数据-->
        <el-form :model="queryParams" :rules="queryRules" ref="queryForm" :inline="true" v-show="showSearch"
                 label-width="168px">
          <el-form-item label="一级指标" prop="performanceyear">
            <el-input></el-input>
          </el-form-item>
          <el-form-item label="二级指标" prop="teamid">
            <el-input></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              v-hasPermi="['performance:indicatortable:list']"
            >新增
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="indicatorOptions" >
          <el-table-column label="指标类型" align="center" prop="indicatortype"  width="120">
          </el-table-column>

          <el-table-column label="一级指标" align="center" prop="level1name">
            <template slot-scope="scope">
              <a href="#" @click="clickIndicatortypeDetail(scope.row)"
                 style="color:blue; text-decoration-line:underline">{{ scope.row.level1name }}</a>
            </template>
          </el-table-column>
          <el-table-column label="二级指标" align="center" prop="level2name">
          <template slot-scope="scope">
            <a href="#" @click="clickIndicatortypeDetail(scope.row)"
               style="color:blue; text-decoration-line:underline">{{ scope.row.level2name }}</a>
          </template>
          </el-table-column>
          <el-table-column label="统计指标" align="center" prop="indicatorname" >
            <template slot-scope="scope">
              <a href="#" @click="clickIndicatortypeDetail(scope.row)"
                 style="color:blue; text-decoration-line:underline">{{ scope.row.indicatorname }}</a>
            </template>
          </el-table-column>
          <el-table-column label="同级指标内排序" align="center" prop="ordernumber" width="140"/>
          <el-table-column label="单项分值" align="center" prop="score" width="80">

          </el-table-column>
          <el-table-column label="是否自主申请" align="center" prop="ifselfapply" width="140">
            <template slot-scope="scope">
              <span v-if="scope.row.ifselfapply === true">是</span>
              <span v-else>否</span>
            </template>
          </el-table-column>

        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
        <el-row>
          <br/>
          <br/>
          <br/>
        </el-row>
      </el-col>
    </el-row>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <template v-if="scorelogList === undefined">
        <el-form ref="form" :model="form" :rules="scoreRules" label-width="100px" :key="timer">
          <el-row>
            <el-col :span="24">
              <el-form-item label="原分数" prop="oldscores">
                <el-input readonly v-model="form.oldscores" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="调整分数" prop="newscores">
                <el-input type="number" v-model="form.newscores" placeholder="请输入"/>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item label="调整理由" prop="reason">
                <el-input type="textarea" v-model="form.reason" placeholder="请输入"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitScoreForm">确 定</el-button>
          <el-button @click="cancelScoreForm">取 消</el-button>
        </div>
      </template>
      <template v-else>
        <el-table v-loading="loading" :data="scorelogList" >
          <el-table-column type="index" width="50" align="center"/>
          <el-table-column label="变动情况" align="center" prop="description" />
          <el-table-column label="调整理由" align="center" prop="reason"/>
          <el-table-column label="操作人" align="center" prop="realname" width="80"/>
          <el-table-column label="操作时间" align="center" prop="time" width="100"/>
        </el-table>

      </template>

    </el-dialog>


  </div>
</template>

<script>
import {exportVerifyTeam, addVerifyTeamConfirmrequest, updateVerifypoints, getVerifypoints,deleteVerifyTeam} from "@/api/performance/teamperformance";
import TeamData from "../../public/team-data";
import {spanRow} from "@/api/performance/spanRow";
import {listIndicatorTree} from "@/api/performance/indicator";

export default {
  name: "indicatortable_index",
  components: {"team-data": TeamData},
  data() {
    return {
      // 各个组件的只读和隐藏属性控制
      readonly: {},
      hidden: {},
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 父级树选项
      indicatorParentOptions: undefined,
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 总条数
      total: 0,
      // 表格数据
      indicatorList: [],
      indicatorOptions: [],

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      queryRules: {

      },

      // 表单参数
      form: {},
      scorelogList:[],


      timer: '',
      // 表单校验
      scoreRules: {
        newscores: [
          {required: true, message: "分数不能为空", trigger: "blur"}
        ],
        reason: [
          {required: true, message: "理由不能为空", trigger: "blur"}
        ]
      }
    };
  },
  watch: {

  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */

    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listIndicatorTree().then(response =>{
        console.log("response is ", response);
        this.indicatorList = response.data;

        this.indicatorParentOptions = [];
        const rows = response.data;
        for (let i=0; i<rows.length; i++){
          let item = rows[i];
          // console.log("type is " + item.type);

          let leve1list = [];
          for (let j=0; j < item.childList.length; j++) {
            let leve1item = item.childList[j];
            // console.log("level1 is " + leve1item.indicatorname);
            let level2list = [];
            for (let k=0; k < leve1item.childList.length; k++) {
              let level2item = leve1item.childList[k];
              // console.log("level2 is " + level2item.indicatorname);
              level2list.push({id: level2item.indicatorid, label:level2item.indicatorname, level:2});
            }

            let leve1dict = {id: leve1item.indicatorid, label: leve1item.indicatorname, children: level2list, level:1};
            leve1list.push(leve1dict);
          }

          let tree = {id:i+1, label:item.type, children: leve1list, level:0};
          this.indicatorParentOptions.push(tree);
        }
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {};
      this.resetForm("form");
    },


    // 筛选节点
    filterNode(value, data) {

    },
    // 节点单击事件
    handleNodeClick(data) {

      console.log("data.level is ", data.level, data.id, data.label, "indicatorList count is ",this.indicatorList.length);

      this.indicatorOptions = [];
      const this_ = this;

      if (data.level === 2) {

        this.indicatorList.forEach(function (item) {
          item.childList.forEach(function (level1item) {
            level1item.childList.forEach(function (level2item) {
              if (level2item.indicatorid === data.id) {
                level2item.childList.forEach(function (k){
                  // console.log(k);
                  k.indicatortype = item.type;
                  k.level1name = level1item.indicatorname;
                  k.level2name = level2item.indicatorname;
                  this_.indicatorOptions.push(k);
                });

                this_.indicatorOptions.sort(function (a, b) {
                  return a.ordernumber - b.ordernumber
                });
              }
            })

          });

        });

      }



    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.$refs["queryForm"].validate(valid => {
        if (valid) {
          this.getList();
        }
      });

    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },

    /** 新增按钮操作 */
    handleAdd() {

    },

    handleUpdate(row) {
      console.log("update row is  ", row);

      this.scorelogList = undefined;

      this.open = true;
      this.title = "调整分数";
      const score = {
        performanceid: row.performanceid,
        oldscores: row.points,
        newscores: undefined,
        reason: undefined
      };

      this.form  = score;

    },

    handleDelete(row) {
      console.log("update row is  ", row);
      const  this_ = this;
      this.$confirm('是否确认删除？', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        deleteVerifyTeam(row.performanceid);
      }).then(() => {
        this_.msgSuccess("删除成功");
        this_.getList();

      });

    },

    // 组件方法
    selectTeamId(value) {
      console.log("selectTeamId is ", value);
      this.queryParams.teamid = value.id;
      this.queryParams.teamidlinktext = value.value;

    },

    clickTeamperformancePoints(row) {
      console.log("clickTeamperformancePoints", row);

      this.loading = true;
      getVerifypoints({performanceid:row.performanceid}).then(response => {
        console.log("getVerifypoints is ", response.data);
        this.loading = false;
        this.open = true;
        this.title = "分数变动记录";
        this.scorelogList = response.data;

      });


    },

    clickIndicatortypeDetail(row) {
      console.log("clickIndicatortypeDetail", row);


    },
    /** 要求团队确认 按钮 */
    handleSendConfirm() {

      const this_ = this;
      this.$refs["queryForm"].validate(valid => {
        if (valid) {
          this.$confirm('是否确认已核对了所有绩效分数？', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {
            addVerifyTeamConfirmrequest(this_.queryParams);
          }).then(() => {
            this_.msgSuccess("确认成功");
            this_.getList();

          });
        }
      });


    },

    /** 导出按钮操作 按钮 */
    handleExportExcel() {
      this.$refs["queryForm"].validate(valid => {
        if (valid) {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return exportVerifyTeam(queryParams);
      }).then(response => {
        console.log("exportVerifyTeam is ", response);
        this.download(response.msg);
      })
        }
      });
    },

    cancelScoreForm: function () {
      this.open = false;
    },

    submitScoreForm: function () {
      const this_ = this;
      this_.$refs["form"].validate(valid => {
        if (valid) {
          updateVerifypoints(this_.form).then(response=> {
            this_.open = false;
            this_.msgSuccess("修改成功");
            this_.getList();

          });
        }
      });
    }
  }

};
</script>


<style scoped>

</style>
