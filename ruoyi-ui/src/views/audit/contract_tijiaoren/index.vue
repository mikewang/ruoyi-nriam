<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form ref="queryParams"  :model="queryParams" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="合同名称" prop="contractname" >
          <el-input v-model="queryParams.contractname" clearable/>
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
            v-hasPermi="['contract:tijiaoren:list']"
          >新增
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="sheetList" @selection-change="handleSelectionChange" stripe>
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="合同编号" align="center" prop="contractcode" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="合同名称" align="center" prop="contractname" width="200"/>
        <el-table-column label="提交时间" align="center" prop="contracttime"/>
        <el-table-column label="项目名称" align="center" prop="projectidlinktext" width="180"/>

        <el-table-column label="合同金额（元）" align="center" prop="contractmoney" width="100"/>
        <el-table-column label="乙方单位" align="center" prop="supplieridlinktext" width="100"/>
        <el-table-column label="合同状态" align="center" prop="sheetstatuslinktext" width="100">
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
              v-hasPermi="['contract:tijiaoren:list']"
            >查看
            </el-button>
            <el-button  v-if="scope.row.a_ApplyDelete"
              size="mini"
              type="text"
              icon="el-icon-remove"
              @click="handleApplyDelete(scope.row)"
              v-hasPermi="['contract:tijiaoren:list']"
            >申请作废
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="拨付单" align="center" prop="sheetstatus" width="100">
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
            <br/>
            <span>
              <el-button v-if="scope.row.btn_pay_visible"
                                     size="mini"
                                     type="text"
                                     icon="el-icon-edit"
                                     @click="handleUpdate(scope.row)"
                          >继续付款
            </el-button>
              <span v-if="scope.row.lab_paycomplete_visible">付款完成</span>
              <el-button v-if="scope.row.lbtn_firstpay_visible"
                         size="mini"
                         type="text"
                         icon="el-icon-edit"
                         @click="handleFirstPay(scope.row)"
              >第1单付款
            </el-button>
            </span>
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

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="applydeleteForm" :model="applydeleteForm" :rules="rules" label-width="160px" :key="timer">
        <el-row>
          <el-col :span="24">
            <el-form-item label="请输入申请作废的理由" prop="applyDeleteReason">
              <el-input v-model="applydeleteForm.applyDeleteReason" placeholder="" type="textarea"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitApplyDeleteForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {deleteContract, listTijiaorenContract, firstpayContract, applydeleteContract} from "@/api/audit/contract";
import BasDoc from "@/views/public/bas-doc";
import ProjectData from "@/views/public/project-data";
import ProjectDoc from "@/views/public/project-doc";
import DictData from "@/views/public/dict-data";
import SupplierData from "@/views/public/supplier-data";


export default {
  name: "contract_tijiaoren_index",
  components: {BasDoc, ProjectData, ProjectDoc, DictData, SupplierData},
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
      // 表单
      // 表单参数
      applydeleteForm: {},
      // 状态为在研的项目，申请审核 功能按钮是否显示？
      timer: '',
      DictTypeNameContractType: "合同类型",

      // 表单校验
      applydeleteRules: {
        applyDeleteReason: [
          {required: true, message: "理由不能为空", trigger: "blur"}
        ]
      }
    };
  },
  watch: {


  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listTijiaorenContract(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          console.log("response is ", response);
          let sheetList = [];
          let rows = response.rows;
          for (let i=0; i < rows.length; i++) {
            let row = rows[i];

            // 按钮功能可视性判断。
            let a_ApplyDelete = false;
            //对于刚审批完的合同，可以显示“申请作废”按钮
            if (row.sheetstatus === this.SheetStatus.ShenPiWanCheng && row.payedtimes === 1) {
              //表示只付过一次款，说明是刚审批完成的合同
              a_ApplyDelete = true;
            }
            row.a_ApplyDelete = a_ApplyDelete;


            // 拨付单下的功能按钮可见度
            row.btn_pay_visible = false;
            row.lab_paycomplete_visible = false;
            row.lbtn_firstpay_visible = false;
            row.paySheetItems = [];

            let ifDisplayPayButton = true;  //是否显示付款按钮的标志
            let ifDisplayPayCompleteLab = true;  //是否显示付款完成的标志

            //“审批完成”和“已签订”“付款完成”的合同，才查询其下属的拨付单的情况
            if (row.sheetstatus === this.SheetStatus.ShenPiWanCheng || row.sheetstatus === this.SheetStatus.YiQianDing || row.sheetstatus === this.SheetStatus.FuKuanWanCheng) {

              for (let j=0; j <row.paySheetList.length; j++) {
                let pay = row.paySheetList[j];
                if (pay.sheetstatus !== this.SheetStatus.ShenPiWanCheng) {
                   ifDisplayPayButton = false;  //是否显示付款按钮的标志
                   ifDisplayPayCompleteLab = false;  //是否显示付款完成的标志
                }
              }

              //只有一张拨付单，并且“首次付款时间”为空。说明是刚审批通过的合同
              if (row.paySheetList.length == 1 && (row.firstpaytime === "" || row.firstpaytime === null || row.firstpaytime === undefined)){

                row.btn_pay_visible = false;
                row.lab_paycomplete_visible = false;
                row.lbtn_firstpay_visible = true;

              }
              else {

                if (ifDisplayPayButton == true)   //说明都是已经审批完成的拨付单
                {
                  if (row.paytotaltimes === row.payedtimes)   //总次数和付款次数相同，不要显示“付款”按钮
                  {
                    ifDisplayPayButton = false;
                    ifDisplayPayCompleteLab = true;      //付款完成
                  }
                  else
                  {
                    ifDisplayPayButton = true;
                    ifDisplayPayCompleteLab = false;      //付款还未完成
                  }
                }
                row.lbtn_pay_visible = ifDisplayPayButton;
                row.lab_paycomplete_visible = ifDisplayPayCompleteLab;
              }
              // 必须在这三个状态下 ，才显示 合同拨付单
              let paySheetItems = [];

              for (let j=0; j < row.paySheetList.length; j++) {

                let pay = row.paySheetList[j];

                if (pay.sheetstatus === this.SheetStatus.ShenPiWanCheng) {

                  let lab_yishen_visible = true;
                  let lbtn_daishen_visible = false;
                  let lab_yishen_text = "第" + (j+1).toString() + "单(已审)";
                  let lbtn_daishen_text = "";

                  if (j ===0 && (row.firstpaytime === "" || row.firstpaytime === undefined)) {
                    let lab_yishen_text = "第1单";
                  }

                  let paysheetItem = {lab_yishen_visible:lab_yishen_visible, lbtn_daishen_visible:lbtn_daishen_visible,lab_yishen_text:lab_yishen_text,lbtn_daishen_text:lbtn_daishen_text};

                  paySheetItems.push(paysheetItem);

                }
                else if (pay.sheetstatus === this.SheetStatus.NoPass) {

                  let lab_yishen_visible = false;
                  let lbtn_daishen_visible = true;
                  let lab_yishen_text = "";
                  let lbtn_daishen_text = "第" + (j+1).toString()+ "单(不通过)";
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

              console.log("paySheetItems now ,", paySheetItems);
            }


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
      this.applydeleteForm = {};
      this.resetForm("applydeleteForm");
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

    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({path: '/contract/tijiaoren'});
    },

    handleUpdate(row) {
      console.log("update row is  ", row);
      const path = '/contract/tijiaoren/' + row.contractid;
      console.log("path is " + path);
      this.$router.push({path: path});
    },

    handleApplyDelete(row) {

      this.title = "申请作废合同";
      this.open = true;
      this.applydeleteForm.contractid = row.contractid;
      this.applydeleteForm.contractname = row.contractname;

    },

    submitApplyDeleteForm: function() {

      this.$refs["applydeleteForm"].validate(valid => {
        if (valid) {
          const this_ = this;
          this.$confirm('是否确定申请作废该合同？', "", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {

            console.log("submit applydeleteForm is ", this_.applydeleteForm);


            applydeleteContract(this_.applydeleteForm).then(response => {
              this_.open = false;
              this_.msgSuccess("操作完成");
              this_.getList();
            });

          }).catch(console.error);
          this_.open = false;
          console.log("submit applydeleteForm is ", this_.applydeleteForm);
        }
      });
    },

    handleFirstPay(row) {
      const this_ = this;
      this.$confirm('请确保已签订了纸质合同之后再执行此操作。是否确定已签订？', "第1单付款", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {

        firstpayContract(row).then(response => {
          this_.msgSuccess("操作完成");
          this_.getList();
        });

      }).catch(console.error);
    },

    /** 提交按钮 */
    submitForm: function () {

    },




  }
};

</script>

<style scoped>

</style>
