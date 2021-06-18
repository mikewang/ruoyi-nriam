<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form ref="queryParams"  :model="queryParams" :inline="true" v-show="showSearch" label-width="108px">
        <el-form-item label="合同名称" prop="contractname" >
          <el-input v-model="queryParams.contractname" clearable/>
        </el-form-item>
        <el-form-item label="合同编号" prop="contractcode">
          <el-input v-model="queryParams.contractcode" clearable/>
        </el-form-item>
        <el-form-item label="合同类型" prop="contracttype">
          <dict-data :dict-type-name="DictTypeNameContractType"
                     :selected-dict-value="queryParams.contracttype" :data-options="undefined"
                     @changeDictValue="changeFormDictType" ></dict-data>
        </el-form-item>
        <el-form-item label="所属项目" prop="projectid">
          <!-- 所属项目组件-->
          <project-data :selected-project-data="queryParams.projectinfo"
                        @changeProjectData="selectProjectData" :selected-option="projectSelectedOption"
                       ></project-data>
        </el-form-item>
        <el-form-item label="所属部门" prop="organizationid">
        <dept-data :selected-dept-id="queryParams.organizationid"
                   @changeDeptId="changeFormDeptId"></dept-data>
        </el-form-item>
        <el-form-item label="乙方单位" prop="supplierid" label-width="150px">
          <!-- 乙方单位组件-->
          <supplier-data  :selected-supplier-data="queryParams.supplierid"
                         @changeSupplierData="selectSupplierData" ></supplier-data>
        </el-form-item>
        <el-form-item label="审批通过日期" prop="passtimeRange">
          <el-date-picker
            v-model="queryParams.passtimeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="display:block;">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="经办人" prop="contractuserid">
          <user-data :selected-user-id="queryParams.contractuserid"
                     @changeUserData="changeContractuserid"></user-data>
        </el-form-item>
        <el-form-item label="合同状态" prop="sheetstatus">
          <dict-data :dict-type-name="DictTypeNameSheetStatus"
                     :selected-dict-value="queryParams.sheetstatus" :data-options="undefined"
                     @changeDictValue="changeFormDictType" ></dict-data>
        </el-form-item>
        <el-form-item label="合同状态" prop="sheetstatus">
          <dict-data :dict-type-name="DictTypeNameSheetStatus"
                     :selected-dict-value="queryParams.sheetstatus" :data-options="undefined"
                     @changeDictValue="changeFormDictType" ></dict-data>
        </el-form-item>

        <el-form-item label="是否付款完成" prop="ifAllPayed">
          <dict-data :dict-type-name="DictTypeNameIfAllPayed"
                     :selected-dict-value="queryParams.ifAllPayed" :data-options=ContractIfAllPayedOptions
                     @changeDictValue="changeFormDictType" ></dict-data>
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
          width="100"
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

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>

    </el-dialog>
  </div>
</template>

<script>
import {queryContract, listTijiaorenContract, firstpayContract, applydeleteContract} from "@/api/audit/contract";
import BasDoc from "@/views/public/bas-doc";
import ProjectData from "@/views/public/project-data";
import ProjectDoc from "@/views/public/project-doc";
import DictData from "@/views/public/dict-data";
import SupplierData from "@/views/public/supplier-data";
import {listProjectdoc} from "@/api/project/project";
import DeptData from "@/views/public/dept-data";
import UserData from "@/views/public/user-data";


export default {
  name: "contract_query_index",
  components: {BasDoc, ProjectData, ProjectDoc, DictData, SupplierData, "dept-data": DeptData, UserData},
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
      projectSelectedOption: "aftersetup",

      DictTypeNameSheetStatus: "单据状态",

      DictTypeNameIfAllPayed: "是否付款完成",
      ContractIfAllPayedOptions: [{dictLabel:"是", dictValue: 1},{dictLabel:"否" ,dictValue: 0}],


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
      console.log("queryParams params is ", this.queryParams.params);

      let query = this.addDateRange(this.queryParams, this.dateRange);

      if (query.passtimeRange != null) {
        query.params.passtimeRange = query.passtimeRange;
      }
      if (query.ifAllPayed != null) {
        query.params.ifAllPayed = query.ifAllPayed;
      }

      queryContract(query).then(response => {
          console.log("response is ", response);
          this.sheetList = response.rows;
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

    /** 按钮操作 */

    handleUpdate(row) {
      console.log("update row is  ", row);
      const path = '/contract/tijiaoren/' + row.contractid;
      console.log("path is " + path);
      this.$router.push({path: path});
    },




    // 组件方法
    changeFormDictType(dict) {

      if (dict.type === this.DictTypeNameContractType) {
        console.log("changeFormDictType is ", dict);
        if (dict) {
          this.queryParams.contracttype = dict.dictValue;
        } else {
          this.queryParams.contracttype = undefined;
        }
      }
      else  if (dict.type === this.DictTypeNameSheetStatus) {
        console.log("changeFormDictType is ", dict);
        if (dict) {
          this.queryParams.sheetstatus = dict.dictValue;
        } else {
          this.queryParams.sheetstatus = undefined;
        }
      }
      else  if (dict.type === this.DictTypeNameIfAllPayed) {
        console.log("changeFormDictType is ", dict);
        if (dict) {
          this.queryParams.ifAllPayed = dict.dictValue;
        } else {
          this.queryParams.ifAllPayed = undefined;
        }
      }
      else {
        console.error("changeFormDictType  意外 is ", dict);
      }
    },

    // 组件方法
    selectProjectData(project) {
      console.log("selectProjectData is ", project);
      this.queryParams.projectid = undefined;
      this.queryParams.projectinfo = {projectid: undefined};
    },


    // 组件方法
    changeFormDeptId(dept) {
      this.queryParams.organizationid = dept.deptId;

    },
    // 组件方法
    selectSupplierData(supplier) {
      console.log("selectSupplierData is ", supplier);
      this.queryParams.supplierid =  supplier.supplierid;

    },


    changeContractuserid(value) {

      if (value) {

        this.queryParams.contractuserid = value.userId;
      } else {
        this.queryParams.contractuserid = undefined;
      }

    },


  }
};

</script>

<style scoped>

</style>
