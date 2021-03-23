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
          <team-data :selected-team-id="queryParams.teamid" :join-team-user-id="queryParams.userid" @changeTeamId="selectTeamId"></team-data>
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
        <el-button  v-if="hidden.confirmBtn === false"  type="success" @click="handleConfirm">确认考核结果</el-button>
      </el-col>

    </el-row>


    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <template >
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
import {listViewTeam, getVerifyTeamConfirmrequest, getVerifyTeamPointsSum, confirmVerifyTeamRequest, exportVerifyTeam, addVerifyTeamConfirmrequest, updateVerifypoints, getVerifypoints} from "@/api/performance/teamperformance";
import TeamData from "../../public/team-data";
import {spanRow} from "@/api/performance/spanRow";

export default {
  name: "viewteam_index",
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
        userid: undefined,
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
    this.hidden = {confirmBtn:true };
    this.queryParams.userid = this.$store.getters.userId;

  },
  methods: {
    /** 查询列表 */

    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listViewTeam(this.queryParams).then(response => {
          console.log("response is ", response);
          this.total = response.total;
          this.performanceList = response.rows;
          this.performanceListOption = [{index: 1, field: "teamidlinktext"}, {
            index: 2,
            field: "performanceyear"
          }, {index: 3, field: "level1name"}, {index: 4, field: "level2name"}];

          getVerifyTeamPointsSum(this.queryParams).then(response => {

            this.performancePointsSum = response.data;

            getVerifyTeamConfirmrequest(this.queryParams).then(response => {
              console.log("getVerifyTeamConfirmrequest response is ", response.data.status);

             if (response.data.status === "待确认") {
                this.hidden.confirmBtn = false;
              }
             else {
                this.hidden.confirmBtn = true;
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
    /** 团队确认 按钮 */
    handleConfirm() {

      const this_ = this;
      this.$refs["queryForm"].validate(valid => {
        if (valid) {
          this.$confirm('是否确认最终考核结果？', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {
            confirmVerifyTeamRequest(this_.queryParams);
          }).then(() => {
            this_.msgSuccess("确认成功");
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
