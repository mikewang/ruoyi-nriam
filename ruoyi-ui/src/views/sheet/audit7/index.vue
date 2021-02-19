<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-table v-loading="loading" :data="sheetList" @selection-change="handleSelectionChange" stripe>
        <el-table-header> 待审批的“参加单位拨付申请”列表</el-table-header>
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="单据编号" align="center" prop="sheetcode" :show-overflow-tooltip="true" width="150">
        </el-table-column>
        <el-table-column label="提交时间" align="center" prop="sheettime" width="150"/>

        <el-table-column label="提交人" align="center" prop="sheetuseridlinktext" width="100"/>
        <el-table-column label="项目名称" align="center" prop="projectidlinktext"/>

        <el-table-column label="部门" align="center" prop="organizationidlinktext" width="100"/>
        <el-table-column label="本次拨付金额" align="center" prop="hejiBenci" width="100"/>
        <el-table-column label="单据状态" align="center" prop="sheetstatuslinktext"  width="150">
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
              v-hasPermi="['sheet:audit7:list']"
            >审批
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
import {listAudit7Sheet} from "@/api/sheet/sheet";

export default {
  name: "sheet_audit7_index",
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
      // 科技成果类型
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined
      },
      // 表单参数
      form: {},
      // 状态为在研的项目，申请审核 功能按钮是否显示？
      timer: '',
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listAudit7Sheet(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          console.log("response is ", response);
          this.sheetList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    handleUpdate(row) {
      console.log("update row is  ", row);
      const path = '/sheet/tijiaoren/audit7/' + row.sheetid;
      console.log("path is " + path);
      this.$router.push({path: path});
    }
  }
}
</script>

<style scoped>

</style>
