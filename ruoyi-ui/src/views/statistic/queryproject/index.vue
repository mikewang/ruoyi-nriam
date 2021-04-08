<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--用户数据-->
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="168px">
        <el-form-item label="项目名称" prop="projectname">
          <el-input v-model="queryParams.projectname" clearable/>
        </el-form-item>
        <el-form-item label="项目编号" prop="projectcode">
          <el-input v-model="queryParams.projectcode" clearable/>
        </el-form-item>
        <el-form-item label="项目经费编号" prop="subjectcode">
          <el-input v-model="queryParams.subjectcode" clearable/>
        </el-form-item>
        <el-form-item label="项目类型" prop="projecttype">
          <dict-data :dict-type-name="DictTypeNameProjectType"
                     :selected-dict-value="queryParams.projecttype"
                     @changeDictValue="changeFormProjectTypeValue"></dict-data>
        </el-form-item>
        <el-form-item label="年份" prop="year">
          <el-date-picker
            type="year"
            format="yyyy"
            value-format="yyyy"
            v-model="queryParams.projectyear"
            placeholder="请输入"
            clearable
            size="small"
          />
        </el-form-item>
        <el-form-item label="项目所属部门" prop="organizationid">
          <dept-data :selected-dept-id="queryParams.organizationid"
                     @changeDeptId="changeFormDeptId"></dept-data>
        </el-form-item>
        <el-form-item label="所属团队" prop="teamid">
          <!-- 所属团队组件-->
          <team-data :selected-team-id="queryParams.teamid" :join-team-user-id="undefined"
                     @changeTeamId="selectTeamId"></team-data>
        </el-form-item>
        <el-form-item label="项目负责人" prop="projectmanagerid">
          <user-data :selected-user-id="queryParams.projectmanagerid"
                     @changeUserData="changeFormManagerValue"></user-data>
        </el-form-item>
        <el-form-item label="主持/参与" prop="jointype">
          <dict-data :dict-type-name="DictTypeNameJoinType"
                     :selected-dict-value="queryParams.jointype" @changeDictValue="changeJoinTypeValue"></dict-data>
        </el-form-item>

        <el-form-item label="项目状态" prop="status">
          <dict-data :dict-type-name="DictTypeNameProjectStatus"
                     :selected-dict-value="queryParams.status"
                     @changeDictValue="changeFormProjectStatusValue"></dict-data>
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
        <el-table-column type="index" width="50" align="center" :index="indexMethod"/>
        <el-table-column label="项目名称" align="center" prop="projectname" :show-overflow-tooltip="true">
          <template slot-scope="scope">
            <span v-if="scope.row.projectColor === -1" style="color:red">{{ scope.row.projectname }}</span>
            <span v-else-if="scope.row.projectColor === 1" style="color:green">{{ scope.row.projectname }}</span>
            <span v-else>{{ scope.row.projectname }}</span>
          </template>
        </el-table-column>

        <el-table-column label="项目编号" align="center" prop="projectcode" width="120" sortable/>
        <el-table-column label="项目起止日期" align="center" prop="projectDateRange" width="250" sortable>
        </el-table-column>
        <el-table-column label="项目类型" align="center" prop="projecttypelinktext" width="300" sortable/>
        <el-table-column label="负责人" align="center" prop="projectmanageridlinktext" width="100" sortable/>
        <el-table-column label="项目状态" align="center" prop="statuslinktext" width="100" sortable>
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
              @click="handleView(scope.row)"
              v-hasPermi="['project:project:list']"
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
import {listNormalProject} from "@/api/project/project";
import TeamData from "@/views/public/team-data";
import DeptData from "@/views/public/dept-data";
import DictData from "@/views/public/dict-data";
import UserData from "@/views/public/user-data";

export default {
  name: "project_query_index",
  components: {"team-data": TeamData, UserData, "dept-data": DeptData, "dict-data": DictData},
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

      ProjectStatus: {
        XinJianZhong: 48,
        DaiQueRen: 40,
        BuTongGuo: 44,
        ZaiYan: 41,
        JieTiDaiQueRen: 45,
        JietiBuTongGuo: 46,
        YiJieTi: 42,
        YiShanChu: 43
      },
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        teamid: undefined,
        projectyear: "2021",
        projectname: undefined
      },
      // 表单参数
      form: {},
      // 状态为在研的项目，申请审核 功能按钮是否显示？
      timer: '',
      // 数据字典
      DictTypeNameProjectType: "项目类型",
      // 数据字典
      DictTypeNameJoinType: "主持参与",

      DictTypeNameProjectStatus: "项目状态",

      // 表单校验
      rules: {}
    };
  },
  watch: {},
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listNormalProject(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
      this.form = {};

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
    indexMethod(index) {
      return (index + 1) + this.queryParams.pageSize*(this.queryParams.pageNum-1);
    },
    /** 新增按钮操作 */

    handleView(row) {
      const projectid = row.projectid
      console.log("edit project id is ", projectid);
      this.$router.push({path: '/project/project/' + projectid});
    },


    // 组件方法
    selectTeamId(value) {
      console.log("handleSelectTeam is ", value);
      this.queryParams.teamid = value.id;
      this.queryParams.teamidlinktext = value.value;
      this.getList();
    },

    changeFormManagerValue(value) {

      if (value) {
        this.queryParams.projectmanagerid = value.userId;
      } else {
        this.queryParams.projectmanagerid = undefined;
      }

    },

    changeJoinTypeValue(value) {

      if (value === undefined) {
        this.queryParams.jointype = undefined;
      } else {
        if (value.id === "1") {
          this.queryParams.jointype = 1;
        } else if (value.id === "2") {
          this.queryParams.jointype = 2;
        } else {

          console.error("changeJoinTypeValue is 意外 ", value);
        }

      }

      console.log("changeJoinTypeValue value is ", value.id, "this.form.jointype is ", this.queryParams.jointype);

      return;
    },

    changeFormProjectStatusValue(value) {

      this.queryParams.status = value.id;

      console.error("changeFormProjectStatusValue is 意外 ", value);
    },

    changeFormProjectTypeValue(value) {

      if (value) {
        this.queryParams.projecttype = value.id.toString();
      } else {
        this.queryParams.projecttype = undefined;
      }

    },

    changeFormDeptId(dept) {
      this.queryParams.organizationid = dept.deptId;

    },

  }
};
</script>

<style scoped>

</style>
