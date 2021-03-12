<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-tag> 待审批的“四技合同申请”列表</el-tag>
    </el-row>
    <el-row :gutter="20">
      <el-table v-loading="loading" :data="contractList" stripe>

        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="合同类型" align="center" prop="fourtechtype" :show-overflow-tooltip="true" width="150">
        </el-table-column>
        <el-table-column label="经办人" align="center" prop="sheetuseridlinktext" width="150"/>

        <el-table-column label="提交时间" align="center" prop="sheettime" width="100"/>
        <el-table-column label="项目名称" align="center" prop="fourtechname"/>

        <el-table-column label="部门名称" align="center" prop="organizationidlinktext" width="100"/>
        <el-table-column label="合同金额" align="center" prop="fourtechmoney" width="100"/>
        <el-table-column label="乙方单位" align="center" prop="coperationunit" width="100"/>
        <el-table-column label="合同状态" align="center" prop="sheetstatuslinktext" width="150">
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="160"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       icon="el-icon-edit"
                       @click="handleAuditFourtech(scope.row)"
                       v-hasPermi="['fourtech:audit2:list']"
            >审批
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
        v-show="total2>0"
        :total="total2"
        :page.sync="queryParams2.pageNum"
        :limit.sync="queryParams2.pageSize"
        @pagination="getContractList"
      />

    </el-row>

  </div>

</template>

<script>
import {listAudit2Fourtech} from "@/api/fourtech/fourtech";

export default {
  name: "sheet_audit2_index",
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
      // 总条数
      total2: 0,
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
      // 查询参数
      queryParams2: {
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
        ShenQingZuoFei: 34,
        ChuShen: 47
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
      console.log("queryParams2 is ", this.queryParams2);
      listAudit2Fourtech(this.addDateRange(this.queryParams2, this.dateRange)).then(response => {
          console.log("response2 is ", response);

          let sheetList = [];
          let rows = response.rows;
          for (let i = 0; i < rows.length; i++) {
            let row = rows[i];
            sheetList.push(row);
          }

          this.contractList = sheetList;
          this.total2 = response.total;
          this.loading = false;
        }
      );
    },

    handleAuditFourtech(row) {
      console.log("update row is  ", row);
      const path = '/fourtech/audit2/' + row.fourtechid;
      console.log("path is " + path);
      this.$router.push({path: path});
    }
  }
}
</script>

<style scoped>

</style>
