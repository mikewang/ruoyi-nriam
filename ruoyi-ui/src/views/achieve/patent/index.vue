<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="年份" prop="year">
          <el-date-picker
            type="year"
            format="yyyy"
            value-format="yyyy"
            v-model="queryParams.patentYear"
            placeholder="请输入"
            clearable
            size="small"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="所属团队" prop="teamname">
          <el-autocomplete class="input-with-select"
                           v-model="queryParams.teamname"
                           :fetch-suggestions="queryTeamListSearch"
                           placeholder="请输入团队名称"
                           clearable
                           size="small"
                           style="width: 240px"
                           @select="handleSelectTeam"
          >
          </el-autocomplete>
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
            v-hasPermi="['achieve:patent:list']"
          >新增
          </el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['achieve:patent:list']"
          >导出
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="achieveList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column label="专利名称" align="center" prop="patentname" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span v-if="scope.row.statusColor === -1" style="color:red">{{ scope.row.patentname }}</span>
            <span v-else-if="scope.row.statusColor === 1" style="color:green">{{ scope.row.patentname }}</span>
            <span v-else>{{ scope.row.patentname }}</span>
          </template>
        </el-table-column>
        <el-table-column label="专利号" align="center" prop="patentcode" width="200"/>

        <el-table-column label="专利类型" align="center" prop="patenttype" width="100"/>
        <el-table-column label="专利授权日期" align="center" prop="passtime" width="100"/>

        <el-table-column label="专利人" align="center" prop="authors" />

        <el-table-column label="所属团队" align="center" prop="teamname" width="100"/>
        <el-table-column label="状态" align="center" prop="statuslinktext" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.statusColor === -1" style="color:red"
                  :key="Math.random()">{{ scope.row.statuslinktext }}</span>
            <span v-else-if="scope.row.statusColor === 1" style="color:green"
                  :key="Math.random()">{{ scope.row.statuslinktext }}</span>
            <span v-else :key="Math.random()">{{ scope.row.statuslinktext }}</span>
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
              v-hasPermi="['achieve:patent:list']"
            >查看
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

  </div>
</template>

<script>
import {listAchieve} from "@/api/achieve/patent";
import {listTeam} from "@/api/project/team";


export default {
  name: "patent",
  // components: {  },
  data() {
    return {
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
      // 用户表格数据
      achieveList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      teamListOptions: [],
      AchieveStatus: {DaiQueRen: 36, BuTongGuo: 38, ZhengChang: 37, YiShanChu: 39},
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        teamid: 0,
        teamname: undefined,
        projectyear: "2021",
        projectname: undefined
      },
      // 表单参数
      form: {},
      // 状态为在研的项目，申请审核 功能按钮是否显示？
      timer: '',
      // 表单校验
      rules: {}
    };
  },
  watch: {

    form() {

    }

  },
  created() {
    this.getList();
    listTeam().then(response => {
      console.log(response);
      this.teamListOptions = response.rows;

    });
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listAchieve(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          console.log("response is ", response);
          this.achieveList = response.rows;
          this.total = response.total;
          this.loading = false;
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
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.patentid);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({path: '/achieve/patent'});
    },

    handleUpdate(row) {
      const projectid = row.projectid
      console.log("edit project id is ", projectid);
      this.$router.push({path: '/achieve/patent' + projectid});
    },

    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有项目的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        // return exportUser(queryParams);
      }).then(response => {
        // this.download(response.msg);
      });
    },

    queryTeamListSearch(queryString, cb) {

      const queryParams = {
        pageNum: 1,
        pageSize: 30,
        teamname: queryString
      };
      listTeam(queryParams).then(response => {
          const teamListOptions = [];
          const teamList = response.rows;
          teamList.forEach(function (team) {
            const item = {"value": team.teamname, "teamid": team.teamid};
            teamListOptions.push(item);
          });
          cb(teamListOptions);
        }
      );
    },

    handleSelectTeam(team) {
      console.log("handleSelectTeam is " + team["value"]);
      this.queryParams.teamid = team["teamid"];
      this.queryParams.teamname = team["value"];
    }
  }
};
</script>

<style scoped>

</style>
