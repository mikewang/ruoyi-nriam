<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form ref="queryParams"  :model="queryParams" :inline="true" v-show="showSearch" label-width="168px">
        <el-form-item label="经费使用名称" prop="expensename" >
          <el-input v-model="queryParams.expensename" clearable/>
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
            v-hasPermi="['expense:tijiaoren:list']"
          >新增
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="sheetList" @selection-change="handleSelectionChange" stripe>
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="经费单编号" align="center" prop="expensesheetcode"/>
        <el-table-column label="经费使用名称" align="center" prop="expensename" width="200"  :show-overflow-tooltip="true"/>
        <el-table-column label="提交时间" align="center" prop="sheettime"/>
        <el-table-column label="项目名称" align="center" prop="projectidlinktext" width="180"/>
        <el-table-column label="所属部门" align="center" prop="organizationidlinktext" width="180"/>
        <el-table-column label="金额" align="center" prop="money" width="100"/>
        <el-table-column label="经费单状态" align="center" prop="sheetstatuslinktext" width="100">
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
              v-hasPermi="['expense:tijiaoren:list']"
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
import {listTijiaorenExpense, firstpayContract, applydeleteContract} from "@/api/audit/expense";
import BasDoc from "@/views/public/bas-doc";
import ProjectData from "@/views/public/project-data";
import ProjectDoc from "@/views/public/project-doc";
import DictData from "@/views/public/dict-data";
import SupplierData from "@/views/public/supplier-data";


export default {
  name: "expense_tijiaoren_index",
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
      listTijiaorenExpense(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          console.log("response is ", response);
          let sheetList = [];
          let rows = response.rows;
          this.sheetList = rows;
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
