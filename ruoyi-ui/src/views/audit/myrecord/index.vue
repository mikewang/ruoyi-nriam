<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="合同名称" prop="contractname">
          <el-input v-model="queryParams.contractname" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="sheetList" @selection-change="handleSelectionChange" stripe>
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="类型" align="center" prop="sheettype" width="100" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="编号" align="center" prop="code" width="200"/>
        <el-table-column label="合同名称" align="center" prop="name"/>
        <el-table-column label="合作单位" align="center" prop="supplieridlinktext" width="180"/>
        <el-table-column label="经办人" align="center" prop="sheetuseridlinktext" width="100"/>
        <el-table-column label="所属项目" align="center" prop="projectidlinktext" width="100"/>
        <el-table-column label="所属部门" align="center" prop="organizationidlinktext" width="100"/>
        <el-table-column label="金额" align="center" prop="money" width="100"/>
        <el-table-column label="审批时间" align="center" prop="audittime" width="100"/>
        <el-table-column label="单据状态" align="center" prop="sheetstatuslinktext" width="100">
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
              icon="el-icon-view"
              @click="handleUpdate(scope.row)"
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
import {listAuditMyrecord} from "@/api/audit/audit";


export default {
  name: "audit_myrecord_index",
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
      // 表格数据
      sheetList: [],
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
      SheetStatus: {
        NoPass: 2,
        XiangMuShenPi: 3,
        BuMenShenPi: 4,
        ChuShenPi: 5,
        FenGuanSuoShenPi: 6,
        SuoZhangShenPi: 7,
        ShenPiWanCheng: 8,
        YiZuoFei: 9,
        XinJianZhong: 17,
        YiQianDing: 30,
        FuKuanWanCheng: 31,
        ShenQingZuoFei: 34
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
      console.log("queryParams is ", this.queryParams);
      listAuditMyrecord(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          console.log("response is ", response);
          let sheetList = [];
          let rows = response.rows;
          for (let i=0; i < rows.length; i++) {
            let row = rows[i];
            sheetList.push(row);
          }
          this.sheetList = sheetList;
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
      this.ids = selection.map(item => item.contractid);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    handleUpdate(row) {
      console.log("myrecord row is  ", row);
      if (row.sheettype === "拨付单") {
        const path = '/audit/tijiaoren/' + row.sheetid;
        console.log("path is " + path);
        this.$router.push({path: path});
      }

    }

  }
};
</script>

<style scoped>

</style>
