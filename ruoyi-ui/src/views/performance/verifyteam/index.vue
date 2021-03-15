<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" :rules="rules" ref="queryForm" :inline="true" v-show="showSearch"
               label-width="168px">
        <el-form-item label="筛选条件:年度" prop="performanceyear">
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
        <el-table-column label="详情" align="center" prop="teamid" width="100"/>
        <el-table-column label="评分依据" align="center" prop="description" width="400"/>

        <el-table-column label="分数" align="center" prop="points" width="100"/>
        <el-table-column label="状态" align="center" prop="status" width="100"/>
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
    </el-row>

  </div>
</template>

<script>
import {listVerifyTeam} from "@/api/performance/teamperformance";
import TeamData from "../../public/team-data";
import {spanRow} from "@/api/performance/spanRow";

export default {
  name: "verifyteam_index",
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
      // 表格数据
      performanceList: [],
      performanceListOption: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: undefined,
        pageSize: undefined
      },
      // 表单参数
      form: {},

      timer: '',
      // 表单校验
      rules: {
        performanceyear: [
          {required: true, message: "年度不能为空", trigger: "blur"}
        ],
        teamid: [
          {required: true, message: "所属团队不能为空", trigger: "blur"}
        ]
      }
    };
  },
  watch: {

    form() {

    }

  },
  created() {

    this.loading = false;
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listVerifyTeam(this.queryParams).then(response => {
          console.log("response is ", response);
          this.performanceList = response.data;
          this.performanceListOption = [{index: 1,field:"teamidlinktext"}, {index:2, field:"performanceyear"}, {index:3, field:"level1name"}, {index:4, field:"level2name"}];
          this.total = response.data.length;
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

    onSpanMethod({ row, column, rowIndex, columnIndex }) {
      return spanRow(
        { row, column, rowIndex, columnIndex },
        this.performanceList,
        this.performanceListOption
      )},


    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = undefined;
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
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.performanceid);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({path: '/performance/verifyteam'});
    },

    handleUpdate(row) {
      console.log("update row is  ", row);

      const path = '/performance/verifyteam/' + row.performanceid;
      console.log("path is " + path);
      this.$router.push({path: path});
    },

    handleDelete(row) {
      console.log("update row is  ", row);

      const path = '/performance/verifyteam/' + row.performanceid;
      console.log("path is " + path);
      this.$router.push({path: path});
    },
    selectTeamId(value) {

      console.log("selectTeamId is ", value);

      this.queryParams.teamid = value.id;

    }

  }
};
</script>


<style scoped>

</style>
