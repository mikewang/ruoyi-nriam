<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-tag>待审批的“参加单位拨付申请”列表 </el-tag>
    </el-row>
    <el-row :gutter="20">

      <el-table v-loading="loading" :data="sheetList" stripe>
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
              @click="handleAuditSheet(scope.row)"
              v-hasPermi="['sheet:audit4:list']"
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
        @pagination="getSheetList"
      />
    </el-row>

    <el-row :gutter="20">
    <el-tag>待审批的“合作单位拨付申请”列表  </el-tag>
    </el-row>
    <el-row :gutter="20">

      <el-table v-loading="loading" :data="contractList" stripe>

        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="合同名称" align="center" prop="contractname" :show-overflow-tooltip="true" width="150">
        </el-table-column>
        <el-table-column label="经办人" align="center" prop="contractuseridlinktext" width="150"/>

        <el-table-column label="提交时间" align="center" prop="contracttime" width="100"/>
        <el-table-column label="项目名称" align="center" prop="projectidlinktext"/>

        <el-table-column label="部门名称" align="center" prop="organizationidlinktext" width="100"/>
        <el-table-column label="合同金额" align="center" prop="contractmoney" width="100"/>
        <el-table-column label="乙方单位" align="center" prop="supplieridlinktext" width="100"/>
        <el-table-column label="合同状态" align="center" prop="sheetstatuslinktext"  width="150">
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
              v-hasPermi="['sheet:audit4:list']"
            >审批
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
          label="待审拨付单"
          align="center"
          width="160"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <span v-for="item in scope.row.paySheetItems">
              <span v-if="item.lab_yishen_visible">{{item.lab_yishen_text}}</span>
            <el-button v-if="item.lbtn_daishen_visible"
                       size="mini"
                       type="text"
                       icon="el-icon-edit"
                       @click="handleUpdate(scope.row)"
            >{{item.lbtn_daishen_text}}
            </el-button>
            </span>
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
import {listAudit4Sheet} from "@/api/sheet/sheet";
import {listAudit4Contract} from "@/api/sheet/contract";

export default {
  name: "sheet_audit4_index",
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
    this.getSheetList();
    this.getContractList();
  },
  methods: {
    getSheetList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listAudit4Sheet(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          console.log("response is ", response);
          let sheetList = response.rows;
          this.sheetList = sheetList;
          this.total = response.total;
          this.loading = false;
        }
      );
    },

    getContractList() {
      this.loading = true;
      console.log("queryParams2 is ", this.queryParams2);
      listAudit4Contract(this.addDateRange(this.queryParams2, this.dateRange)).then(response => {
          console.log("response2 is ", response);
        //处理合同的列表。对于状态是已经完成的合同，要查询其所有下属拨付单的列表
        //对于待审的合同，"待审拨付单"单元格为空

        let sheetList = [];
        let rows = response.rows;
        for (let i=0; i < rows.length; i++) {
          let row = rows[i];

          // 按钮功能可视性判断。
          row.btn_audit = true;
          row.paySheetItems = [];
          //
          if (row.sheetstatus === this.SheetStatus.ShenPiWanCheng) {
            //审批合同的操作按钮不可见
            row.btn_audit = false;

            // 必须在这三个状态下 ，才显示 合同拨付单
            let paySheetItems = [];

            for (let j=0; j < row.paySheetList.length; j++) {

              let pay = row.paySheetList[j];

              if (pay.sheetstatus === this.SheetStatus.ShenPiWanCheng) {

                let lab_yishen_visible = true;
                let lbtn_daishen_visible = false;
                let lab_yishen_text = "第" + (j+1).toString() + "单(已审)";
                let lbtn_daishen_text = "";

                let paysheetItem = {lab_yishen_visible:lab_yishen_visible, lbtn_daishen_visible:lbtn_daishen_visible,lab_yishen_text:lab_yishen_text,lbtn_daishen_text:lbtn_daishen_text};

                paySheetItems.push(paysheetItem);

              }
              else {
                let lab_yishen_visible = false;
                let lbtn_daishen_visible = true;
                let lab_yishen_text = "";
                let lbtn_daishen_text = "第" + (j+1).toString()+ "单(待审)";
                let paysheetItem = {lab_yishen_visible:lab_yishen_visible, lbtn_daishen_visible:lbtn_daishen_visible,lab_yishen_text:lab_yishen_text,lbtn_daishen_text:lbtn_daishen_text};

                paySheetItems.push(paysheetItem);
              }
            }
            row.paySheetItems = paySheetItems; // 合同拨付单。
          }

          sheetList.push(row);
        }

        this.contractList = sheetList;
          this.total2 = response.total;
          this.loading = false;



        }
      );
    },

    handleAuditSheet(row) {
      console.log("update row is  ", row);
      const path = '/sheet/audit4/' + row.sheetid;
      console.log("path is " + path);
      this.$router.push({path: path});
    },
    handleAuditContract(row) {
      console.log("update row is  ", row);
      const path = '/contract/audit4/' + row.contractid;
      console.log("path is " + path);
      this.$router.push({path: path});
    }
  }
}
</script>

<style scoped>

</style>
