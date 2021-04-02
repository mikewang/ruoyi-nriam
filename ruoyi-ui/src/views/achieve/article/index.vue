<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="年份" prop="publishyear">
          <el-date-picker
            type="year"
            format="yyyy"
            value-format="yyyy"
            v-model="queryParams.publishyear"
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
            v-hasPermi="['achieve:article:list']"
          >新增
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="achieveList" @selection-change="handleSelectionChange">
        <el-table-column type="index" width="50" align="center" :index="indexMethod"/>
        <el-table-column label="著作名称" align="center" prop="articlename" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="著作类型" align="center" prop="articletypelinktext" width="100"/>
        <el-table-column label="出版年度" align="center" prop="publishyear" width="100"/>

        <el-table-column label="作者" align="center" prop="authors" />

        <el-table-column label="所属团队" align="center" prop="teamidlinktext" width="100"/>
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
              v-hasPermi="['achieve:article:list']"
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
import {listArticle} from "@/api/achieve/article";
import TeamData from "@/views/public/team-data";

export default {
  // 著作
  name: "achieve_article_index",
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
      achieveList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      AchieveStatus: {DaiQueRen: 36, BuTongGuo: 38, ZhengChang: 37, YiShanChu: 39},
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        teamid: undefined,
        passtime: "2021"
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
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listArticle(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
      this.ids = selection.map(item => item.articleid);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },
    indexMethod(index) {
      return (index + 1) + this.queryParams.pageSize*(this.queryParams.pageNum-1);
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({path: '/achieve/article'});
    },

    handleUpdate(row) {
      const articleid = row.articleid
      console.log("edit article id is ", articleid);
      this.$router.push({path: '/achieve/article/' + articleid});
    },

    // 组件方法
    selectTeamId(value) {
      console.log("handleSelectTeam is ", value);
      this.queryParams.teamid = value.id;
      this.queryParams.teamidlinktext = value.value;
      this.getList();
    }
  }
};
</script>

<style scoped>

</style>
