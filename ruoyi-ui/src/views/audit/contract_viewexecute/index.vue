<template>
  <div class="app-container">
    <el-row :gutter="20">

      <el-table v-loading="loading" :data="contractList" stripe>

        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="合同名称" align="center" prop="contractname" :show-overflow-tooltip="true" width="150">
        </el-table-column>
        <el-table-column label="项目名称" align="center" prop="projectidlinktext"/>
        <el-table-column label="部门名称" align="center" prop="organizationidlinktext" width="100"/>
        <el-table-column label="合同金额" align="center" prop="contractmoney" width="100"/>
        <el-table-column label="乙方单位" align="center" prop="supplieridlinktext" width="100"/>
        <el-table-column label="合同状态" align="center" prop="sheetstatuslinktext" width="150">
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="160"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button v-if="scope.row.btn_audit"
                       size="mini"
                       type="text"
                       icon="el-icon-edit"
                       @click="handleAuditContract(scope.row)"
                       v-hasPermi="['contract:applydelete:list']"
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
        @pagination="getContractList"
      />

    </el-row>

  </div>

</template>

<script>

import {listViewexecuteContract} from "@/api/audit/contract";

export default {
  name: "contract_viewexecute_index",
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
      // 表格数据
      contractList: [],
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
  created() {
    this.getContractList();
  },
  methods: {

    getContractList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listViewexecuteContract(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          console.log("response is ", response);
          //处理合同的列表。对于状态是已经完成的合同，要查询其所有下属拨付单的列表
          //对于待审的合同，"待审拨付单"单元格为空

          let sheetList = [];
          let rows = response.rows;
          for (let i = 0; i < rows.length; i++) {
            let row = rows[i];

            // 按钮功能可视性判断。
            row.btn_audit = true;
            row.paySheetItems = [];

            sheetList.push(row);
          }

          this.contractList = sheetList;
          this.total = response.total;
          this.loading = false;
        }
      );
    },


    handleAuditContract(row) {
      console.log("update row is  ", row);
      const path = '/contract/audit3/' + row.contractid;
      console.log("path is " + path);
      this.$router.push({path: path});
    }
  }
}
</script>

<style scoped>

</style>
