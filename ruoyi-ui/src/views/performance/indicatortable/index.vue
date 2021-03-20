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
            :data="indicatorTreeOptions"
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
        <el-form hidden :model="queryParams" :rules="queryRules" ref="queryForm" :inline="true" v-show="showSearch"
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

        <el-table v-loading="loading" :data="indicatorOptions" :key="timer">
          <el-table-column label="指标类型" align="center" prop="indicatortype"  width="120">
          </el-table-column>

          <el-table-column label="一级指标" align="center" prop="level1name">
            <template slot-scope="scope">
              <a href="#" @click="clickIndicatorRow(scope.row, 1)"
                 style="color:blue; text-decoration-line:underline">{{ scope.row.level1name }}</a>
            </template>
          </el-table-column>
          <el-table-column label="二级指标" align="center" prop="level2name">
          <template slot-scope="scope">
            <a href="#" @click="clickIndicatorRow(scope.row, 2)"
               style="color:blue; text-decoration-line:underline">{{ scope.row.level2name }}</a>
          </template>
          </el-table-column>
          <el-table-column label="统计指标" align="center" prop="indicatorname" >
            <template slot-scope="scope">
              <a href="#" @click="clickIndicatorRow(scope.row, 3)"
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

    <!-- 添加或修改对话框 v-if="open" 为了刷新组件属性-->
    <el-dialog v-if="open" :title="title" :visible.sync="open" width="600px" append-to-body>
      <!--自定义 组件-->
      <indicator-info :tree-options="this.indicatorTreeOptions" :info="this.form" :info-level="this.level"  @changeIndicatorInfo="changeIndicatorInfo"></indicator-info>
    </el-dialog>

  </div>
</template>

<script>
import TeamData from "../../public/team-data";
import IndicatorInfo from "../../public/indicator-info";
import {spanRow} from "@/api/performance/spanRow";
import {listIndicatorTree} from "@/api/performance/indicator";

export default {
  name: "indicator_table_index",
  components: {"team-data": TeamData, "indicator-info": IndicatorInfo},
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
      indicatorTreeOptions: undefined,
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 总条数
      total: 0,
      // 表格数据
      indicatorList: [],
      indicatorOptions: [],
      clickedNodeData:undefined,

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
      level: 1,

      timer: '',
      // 表单校验
      indicatorRules: {
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
      listIndicatorTree().then(response => {
        console.log("response is ", response);
        this.indicatorList = response.data;

        this.indicatorTreeOptions = [];
        const rows = response.data;
        for (let i = 0; i < rows.length; i++) {
          let row = rows[i];
          console.log("row is " + row);

          let leve1list = [];
          for (let j = 0; j < row.childList.length; j++) {
            let leve1item = row.childList[j];
            // console.log("level1 is " + leve1item.indicatorname);
            let level2list = [];
            for (let k = 0; k < leve1item.childList.length; k++) {
              let level2item = leve1item.childList[k];
              // console.log("level2 is " + level2item.indicatorname);
              level2list.push({id: level2item.indicatorid, label: level2item.indicatorname, level: 2});
            }

            let leve1dict = {id: leve1item.indicatorid, label: leve1item.indicatorname, children: level2list, level: 1};
            leve1list.push(leve1dict);
          }

          let tree = {id: i + 1, label: row.type, children: leve1list, level: 0};
          this.indicatorTreeOptions.push(tree);
        }


        if (this.clickedNodeData !== undefined) {
          this.handleNodeClick(this.clickedNodeData);
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

      console.log("data.level is ", data.level, data.id, data.label, "indicatorList count is ", this.indicatorList.length);

      this.clickedNodeData = data;
      this.indicatorOptions = [];
      const this_ = this;

      if (data.level === 2) {

        this.indicatorList.forEach(function (item) {
          item.childList.forEach(function (level1item) {
            //console.log("level1item is ", level1item);
            level1item.childList.forEach(function (level2item) {
              //console.log("level2item is ", level2item);
              if (level2item.indicatorid === data.id) {
                level2item.childList.forEach(function (k) {

                  k.indicatortype = item.type;
                  k.level1id = level1item.indicatorid;
                  k.level1name = level1item.indicatorname;
                  k.level2id = level2item.indicatorid;
                  k.level2name = level2item.indicatorname;
                  console.log("show item is ", k);
                  this_.indicatorOptions.push(k);
                });

                this_.indicatorOptions.sort(function (a, b) {
                  return a.ordernumber - b.ordernumber
                });
              }
            });

          });

        });

      } else if (data.level === 1) {

        this.indicatorList.forEach(function (item) {
          item.childList.forEach(function (level1item) {

            if (level1item.indicatorid === data.id) {
              level1item.childList.forEach(function (level2item) {
                if (level2item.childList === null || level2item.childList.length == 0) {
                  const k = level2item;
                  k.indicatortype = item.type;
                  k.level1id = level1item.indicatorid;
                  k.level1name = level1item.indicatorname;
                  k.level2id = level2item.indicatorid;
                  k.level2name = level2item.indicatorname;
                  this_.indicatorOptions.push(k);
                } else {
                  level2item.childList.forEach(function (k) {
                    // console.log(k);
                    k.indicatortype = item.type;
                    k.level1id = level1item.indicatorid;
                    k.level1name = level1item.indicatorname;
                    k.level2id = level2item.indicatorid;
                    k.level2name = level2item.indicatorname;
                    this_.indicatorOptions.push(k);
                  });

                  this_.indicatorOptions.sort(function (a, b) {
                    return a.ordernumber - b.ordernumber
                  });
                }
              });

            }
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
      this.title = "新增绩效考核指标信息";
      this.form = {indicatorid: undefined};
      this.open = true;
    },

    handleUpdate(row) {
      console.log("update row is  ", row);

    },

    // 组件方法
    changeIndicatorInfo(info) {
      console.log(info);

      if (info === null) {
        this.open = false;
      }
      else {
        this.open = false;
        this.getList();
        console.log("this.indicatorOptions count is ", this.indicatorOptions.length);

      }

    },

    clickIndicatorRow(row, level) {
      console.log("clickIndicatorRow level is " + level);
      console.log("update row is  ", row);

      this.title = "绩效考核指标信息";
      this.form = row;
      this.level = level;

      if (level === 1) {
        this.form.indicatorname = row.level1name;
      } else if (level === 2) {
        this.form.indicatorname = row.level2name;
      }

      this.open = true;

    }
  }


};
</script>


<style scoped>

</style>
