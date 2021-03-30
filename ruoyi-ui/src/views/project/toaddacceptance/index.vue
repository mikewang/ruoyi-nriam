<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--用户数据-->
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="年份" prop="year">
          <el-date-picker
            type="year"
            format="yyyy"
            value-format="yyyy"
            v-model="queryParams.projectyear"
            placeholder="请输入"
            clearable
            size="small"
            style="width: 240px"
          />
        </el-form-item>
        <el-form-item label="所属团队" prop="teamid">
          <!-- 所属团队组件-->
          <team-data :selected-team-id="queryParams.teamid" :join-team-user-id="undefined" @changeTeamId="selectTeamId"></team-data>
        </el-form-item>
        <el-form-item label="项目名称" prop="projectname">
          <el-input v-model="queryParams.projectname" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="projectList" @selection-change="handleSelectionChange">
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="项目名称" align="center" prop="projectname"  :show-overflow-tooltip="true" >
        </el-table-column>
        <el-table-column label="项目编号" align="center" prop="projectcode" width="120"   />
        <el-table-column label="项目起止日期" align="center" prop="projectDateRange" width="250" />
        <el-table-column label="项目类型" align="center" prop="projecttypelinktext" width="300"/>
        <el-table-column label="负责人" align="center" prop="projectmanageridlinktext" width="100"/>
        <el-table-column label="项目状态" align="center" prop="statuslinktext" width="100">
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
              @click="handleToaddacceptance(scope.row)"
              v-hasPermi="['project:toaddacceptance:list']"
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
import {listToaddacceptance} from "@/api/project/project";
import TeamData from "@/views/public/team-data";

export default {
  // 补充验收材料
  name: "project_toaddacceptance_index",
  components: {"team-data": TeamData},
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
      projectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      teamListOptions: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        teamid: undefined,
        projectyear: undefined,
        projectname: undefined
      },
      // 表单参数
      form: {},
      timer: '',
      // 表单校验
      rules: { }
    };
  },
  watch: {

    form() {

    }

  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listToaddacceptance(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.projectList = response.rows;
          console.log(this.projectList);
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
      this.form = {

      };

      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      console.log("year is ", this.queryParams.projectyear);
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
      this.ids = selection.map(item => item.projectid);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    handleToaddacceptance(row) {
      const projectid = row.projectid
      console.log("toaddacceptance project id is ", projectid);
      this.$router.push({ path: '/project/toaddacceptance/' + projectid });
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
