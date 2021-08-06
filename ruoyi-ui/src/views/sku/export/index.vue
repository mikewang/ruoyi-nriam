<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--用户数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="108px">
          <el-form-item label="导出时间起止" prop="exportTimes">
            <el-date-picker
              type="daterange"
              format="yyyy-mm-dd"
              value-format="yyyy-mm-dd"
              v-model="queryParams.exportTimes"
              placeholder="请输入"
              clearable
              size="small"
              style="width: 160px"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['sku:export:list']"
            >删除
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="skuList" @selection-change="handleSelectionChange" :key="timer">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="编号" align="center" prop="exportId" width="150"/>
          <el-table-column label="导出时间" align="center" prop="exportTime" width="160">
            <template slot-scope="scope">
              <span>{{ formateDate(scope.row.exportTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="文件状态" align="center" prop="status" width="160">
            <template slot-scope="scope">
              <span v-if="scope.row.status === 2" style="color:red">文件生成中</span>
              <span v-else>正常</span>
            </template>
          </el-table-column>
          <el-table-column label="导出路径" align="center" prop="exportPath" >

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
                icon="el-icon-download"
                @click="handleDownload(scope.row)"
                v-hasPermi="['sku:export:list']"
              >下载
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sku:export:list']"
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
      </el-col>
    </el-row>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="SKU名称" prop="skuName">
              <el-input v-model="form.skuName" placeholder="请输入SKU名称"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="创建时间">
              <el-date-picker
                v-model="form.created"
                size="small"
                style="width: 240px"
                value-format="yyyy-MM-dd"
                type="date"
                placeholder="选择日期"
              ></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {listSkuExport, deleteSkuExport, downloadSkuExport} from "@/api/sku/sku";

export default {
  name: "SkuExport",
  components: {},
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
      // 业务表格数据
      skuList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 日期范围
      dateRange: [],
      // 图片尺寸类别数据字典
      photosizeOptions: [],

      // 表单参数
      form: {},
      timer: '',

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        exportTime: undefined
      },
      // 表单校验
      rules: {
        skuName: [
          {required: true, message: "SKU名称不能为空", trigger: "blur"},
          {
            required: true,
            trigger: "change",
            validator: this.validateSkuName
          }
        ]
      },
      photoSizeLabel: "",
    };
  },
  watch: {},
  created() {
    console.log("this.$store.getters.realName is " + this.$store.getters.realName);

    this.getList();

  },
  methods: {
    /** 查询合同列表 */
    getList() {
      this.loading = true;
      console.log("this.queryParams is ", this.queryParams);
      listSkuExport(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.skuList = response.rows;
          console.log("listSku skuList is ", this.skuList);
          this.total = response.total;
          this.loading = false;
        }
      );
    },

    formateDate(datetime) {
      function addDateZero(num) {
        return (num < 10 ? "0" + num : num);
      }

      let d = new Date(datetime);
      let formatdatetime = d.getFullYear() + '-' + addDateZero(d.getMonth() + 1) + '-' + addDateZero(d.getDate()) + ' ' + addDateZero(d.getHours()) + ':' + addDateZero(d.getMinutes()) + ':' + addDateZero(d.getSeconds());
      let formatdate = d.getFullYear() + '-' + addDateZero(d.getMonth() + 1) + '-' + addDateZero(d.getDate());

      return formatdatetime;
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        exportId: undefined,
        skuName: undefined,
        status: "0",
        created: undefined
      };
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
      this.ids = selection.map(item => item.exportId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加SKU";
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.exportId !== undefined) {
            console.log("submitForm is ", this.form);
            updateSku(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSku(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    /** 修改按钮操作 */
    handleDownload(row) {
      this.reset();
      const exportId = row.exportId || this.ids;
      const this_ = this;

      this.$confirm('是否确认重新下载导出的文件?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        this_.loading = true;
        downloadSkuExport(exportId).then(response => {
          const blob = new Blob([response]);
          console.log("response.length is ", blob.size);
          if (blob.size < 10) {
            this_.msgError("下载失败");
          }
          else {
            const fileURL = window.URL.createObjectURL(blob);
            const fileLink = document.createElement('a');
            fileLink.href = fileURL;
            fileLink.setAttribute('download', "sku_export" + ".zip");
            document.body.appendChild(fileLink);
            fileLink.click();
            URL.revokeObjectURL(fileURL);
            this_.msgSuccess("下载成功");
          }

          this_.loading = false;
        });
      })

    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const exportIds = row.exportId || this.ids;
      this.$confirm('是否确认删除编号为"' + exportIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return deleteSkuExport(exportIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const this_ = this;
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所选数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        this_.loading = true;
        exportSku(queryParams).then(response => {
          const fileURL = window.URL.createObjectURL(new Blob([response]));
          const fileLink = document.createElement('a');
          fileLink.href = fileURL;
          fileLink.setAttribute('download', "sku_export" + ".zip");
          document.body.appendChild(fileLink);
          fileLink.click();
          URL.revokeObjectURL(fileURL);
          this_.loading = false;

        });
      })

    },

  }
};
</script>

