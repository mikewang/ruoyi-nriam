<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" :rules="queryRules" ref="queryForm" :inline="true" v-show="showSearch"
               label-width="168px">
        <el-form-item label="筛选条件: 年度" prop="performanceyear">
          <el-date-picker type="year" value-format="yyyy" v-model="queryParams.performanceyear" clearable/>
        </el-form-item>
        <el-form-item label="所属团队" prop="teamid">
          <!-- 所属团队组件-->
          <team-data :selectedTeamId="queryParams.teamid" @changeTeamId="selectTeamId"></team-data>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span=12> 团队: {{ this.queryParams.teamidlinktext }}</el-col>
        <el-col :span=4> 年度: {{ this.queryParams.performanceyear }}</el-col>
        <el-col :span=4> 分数: {{ this.performancePointsSum }}</el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="performanceList" :span-method="onSpanMethod">
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="团队名称" align="center" prop="teamidlinktext" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="年度" align="center" prop="performanceyear" width="100"/>

        <el-table-column label="一级指标" align="center" prop="level1name"/>
        <el-table-column label="二级指标" align="center" prop="level2name" width="180"/>

        <el-table-column label="类型" align="center" prop="indicatortype" width="100"/>
        <el-table-column label="详情" align="center" prop="indicatortypeDetail" width="100">
          <template slot-scope="scope">
            <a href="#" @click="clickIndicatortypeDetail(scope.row)"
               style="color:blue; text-decoration-line:underline">{{ scope.row.indicatortypeDetail }}</a>
          </template>
        </el-table-column>
        <el-table-column label="评分依据" align="center" prop="description" width="400"/>

        <el-table-column label="分数" align="center" prop="points" width="100">

          <template slot-scope="scope">
            <span v-if="scope.row.logcount === 0">{{ scope.row.points }}</span>
            <span v-else><a href="#" @click="clickTeamperformancePoints(scope.row)"
                            style="color:blue; text-decoration-line:underline">{{ scope.row.points }}</a></span>
          </template>

        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.status === '待确认'" style="color:red">{{ scope.row.status }}</span>
            <span v-else>{{ scope.row.status }}</span>
          </template>
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="160"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['performance:verifyteam:list']"
            >修改
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleDelete(scope.row)"
              v-hasPermi="['performance:verifyteam:list']"
            >删除
            </el-button>
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
    </el-row>
    <el-row>
      <br/>
      <br/>
      <br/>
    </el-row>
    <el-row>
      <el-col :span="24" align="center">
        <el-button  v-if="hidden.sendConfirmBtn === false"  type="success" @click="handleSendConfirm">要求团队确认</el-button>
        <span v-if="hidden.sendConfirmLabel === false">{{sendConfirmText}}  </span>

        <el-button type="primary" @click="handleExportExcel">导出Excel</el-button>

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
import {getVerifyTeamConfirmrequest, getVerifyTeamPointsSum, listVerifyTeam, exportVerifyTeam, addVerifyTeamConfirmrequest, updateVerifypoints, getVerifypoints,deleteVerifyTeam} from "@/api/performance/teamperformance";
import TeamData from "../../public/team-data";
import {spanRow} from "@/api/performance/spanRow";
import {exportUser} from "@/api/system/user";

export default {
  name: "verifyteam_index",
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
      // 总条数
      total: 0,
      // 表格数据
      performanceList: [],
      performanceListOption: [],
      performancePointsSum: 0,

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
        pageSize: 10,
        teamid : undefined,
        teamidlinktext : undefined,
        performanceyear:undefined

      },
      queryRules: {
        performanceyear: [
          {required: true, message: "年度不能为空", trigger: "blur"}
        ],
        teamid: [
          {required: true, message: "所属团队不能为空", trigger: "blur"}
        ]
      },
      //
      sendConfirmText:"",
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

    this.loading = false;
    this.hidden = {sendConfirmBtn:true,sendConfirmLabel:true };
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listVerifyTeam(this.queryParams).then(response => {
          console.log("response is ", response);
          this.performanceList = response.rows;
          this.performanceListOption = [{index: 1, field: "teamidlinktext"}, {
            index: 2,
            field: "performanceyear"
          }, {index: 3, field: "level1name"}, {index: 4, field: "level2name"}];
          this.total = response.total;

          getVerifyTeamPointsSum(this.queryParams).then(response => {

            this.performancePointsSum = response.data;

            getVerifyTeamConfirmrequest(this.queryParams).then(response => {
              console.log("getVerifyTeamConfirmrequest response is ", response.data.status);

              if (response.data.status === null ||response.data.status === "" || response.data.status === undefined) {
                this.hidden.sendConfirmBtn = false;
                this.hidden.sendConfirmLabel = true;

              }
              else  if (response.data.status === "待确认") {
                this.hidden.sendConfirmBtn = true;
                this.hidden.sendConfirmLabel = false;
                this.sendConfirmText =  "已要求团队“" + this.queryParams.teamidlinktext + "”确认绩效考核结果";
              }
              if (response.data.status === "已确认") {
                this.hidden.sendConfirmBtn = false;
                this.hidden.sendConfirmLabel = false;
              }

              this.loading = false;
            });

          });

        }
      );
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

    onSpanMethod({row, column, rowIndex, columnIndex}) {
      return spanRow(
        {row, column, rowIndex, columnIndex},
        this.performanceList,
        this.performanceListOption
      )
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
