<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="queryParams.suppliername" clearable/>
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
            v-hasPermi="['srm:supplier:list']"
          >新增
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="supplierList" @selection-change="handleSelectionChange" stripe>
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="协作单位名称" align="center" prop="suppliername" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="地址" align="center" prop="address" width="200"/>

        <el-table-column label="联系人" align="center" prop="person1name"/>
        <el-table-column label="联系电话1" align="center" prop="person1phone1" width="180"/>

        <el-table-column label="电子邮件" align="center" prop="person1email" width="100"/>
        <el-table-column label="备注" align="center" prop="memo" width="100"/>
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
              v-hasPermi="['srm:supplier:list']"
            >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['srm:supplier:list']"
            >删除
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
import {listSheetSupplier} from "@/api/sheet/sheet";


export default {
  name: "srm_supplier_index",
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
      supplierList: [],
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
      listSheetSupplier(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          console.log("response is ", response);
          this.supplierList = response.rows;
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
      this.ids = selection.map(item => item.supplierid);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({path: '/srm/supplier'});
    },

    handleUpdate(row) {
      console.log("update row is  ", row);

      const path = '/srm/supplier/' + row.supplierid;
      console.log("path is " + path);
      this.$router.push({path: path});
    },

    handleDelete(row) {
      console.log("update row is  ", row);

      const path = '/srm/supplier/' + row.supplierid;
      console.log("path is " + path);
      this.$router.push({path: path});
    }

  }
};
</script>

<style scoped>

</style>
