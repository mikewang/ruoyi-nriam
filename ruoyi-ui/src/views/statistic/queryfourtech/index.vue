<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="108px">
        <el-form-item label="合同编号" prop="fourtechcode">
          <el-input v-model="queryParams.fourtechcode"/>
        </el-form-item>
        <el-form-item label="项目名称" prop="fourtechname">
          <el-input v-model="queryParams.fourtechname" clearable/>
        </el-form-item>
        <el-form-item label="合同类型" prop="fourtechtype">
        <dict-data :dict-type-name="DictTypeNameFourtechType"
                   :selected-dict-value="queryParams.fourtechtype" :data-options=FourtechtypeOptions
                   @changeDictValue="changeFormDictType" ></dict-data>
        </el-form-item>
        <el-form-item label="所属部门" prop="organizationid">
          <dept-data :key="queryParams.fourtechid" :selected-dept-id="queryParams.organizationid"
                     @changeDeptId="changeFormDeptId"></dept-data>
        </el-form-item>
        <el-form-item label="项目负责人" prop="managerid">
          <user-data :selected-user-id="queryParams.managerid"
                     @changeUserData="changeFormManagerValue" :key="queryParams.managerid"></user-data>
        </el-form-item>
        <el-form-item label="合作单位" prop="coperationunit">
          <el-input  v-model="queryParams.coperationunit"/>
        </el-form-item>
        <el-form-item label="年份" prop="sheettime">
          <el-date-picker
            type="year"
            format="yyyy"
            value-format="yyyy"
            v-model="queryParams.sheettime"
            placeholder="请输入"
            clearable
            size="small"
          />
        </el-form-item>
        <el-form-item label="合同状态" prop="sheetstatus">
          <dict-data :dict-type-name="DictTypeNameSheetStatus"
                     :selected-dict-value="queryParams.sheetstatus" :data-options="undefined"
                     @changeDictValue="changeFormDictType" ></dict-data>
        </el-form-item>
        <el-form-item label="经办人" prop="sheetuserid">
          <user-data :selected-user-id="queryParams.sheetuserid"
                     @changeUserData="changeFormSheetuseridValue" :key="queryParams.sheetuserid"></user-data>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">

        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="sheetList" @selection-change="handleSelectionChange" stripe>
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="合同编号" align="center" prop="fourtechcode" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="项目名称" align="center" prop="fourtechname" width="200"/>

        <el-table-column label="合同类别" align="center" prop="fourtechtype"/>
        <el-table-column label="起止时间" align="center" prop="fourtechDateRange" width="180"/>

        <el-table-column label="合同金额（元）" align="center" prop="fourtechmoney" width="100"/>
        <el-table-column label="合作单位" align="center" prop="coperationunit" width="100"/>
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
              v-hasPermi="['fourtech:tijiaoren:list']"
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
import {queryFourtech} from "@/api/fourtech/fourtech";
import DictData from "@/views/public/dict-data";
import SupplierData from "@/views/public/supplier-data";
import DeptData from "@/views/public/dept-data";
import UserData from "@/views/public/user-data";
import BasDoc from "@/views/public/bas-doc";
import ProjectData from "@/views/public/project-data";
import ProjectDoc from "@/views/public/project-doc";

export default {
  name: "fourtech_query_index",
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
      // 表单参数
      form: {},
      // 状态为在研的项目，申请审核 功能按钮是否显示？
      timer: '',
      // 表单校验
      rules: {},

      // 数据字典  专利类型
      DictTypeNameFourtechType: "四技合同类型",
      FourtechtypeOptions: [{dictLabel:"技术开发合同", dictValue: "技术开发合同"},{dictLabel:"技术转让合同", dictValue: "技术转让合同"},{dictLabel:"技术服务合同", dictValue: "技术服务合同"},{dictLabel:"技术咨询合同", dictValue: "技术咨询合同"}],
      DictTypeNameSheetStatus: "单据状态",
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
      listTijiaorenFourtech(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
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
      this.ids = selection.map(item => item.fourtechid);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({path: '/fourtech/tijiaoren'});
    },

    handleUpdate(row) {
      console.log("update row is  ", row);

      const path = '/fourtech/tijiaoren/' + row.fourtechid;
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
      else  if (dict.type === this.DictTypeNameFourtechType) {
        console.log("changeFormDictType is ", dict);
        if (dict) {
          this.queryParams.fourtechtype = dict.dictValue;
        } else {
          this.queryParams.fourtechtype = undefined;
        }
      }
      else {
        console.error("changeFormDictType  意外 is ", dict);
      }
    },
    // 组件方法
    changeFormDeptId(dept) {
      this.queryParams.organizationid = dept.deptId;

    },

    // 组件方法
    changeFormManagerValue(value) {

      if (value) {

        this.queryParams.managerid = value.userId;
      } else {
        this.queryParams.managerid = undefined;
      }

    },

    // 组件方法
    changeFormSheetuseridValue(value) {

      if (value) {
        this.queryParams.sheetuserid = value.userId;
      } else {
        this.queryParams.sheetuserid = undefined;
      }

    },



  }
};
</script>

<style scoped>

</style>
