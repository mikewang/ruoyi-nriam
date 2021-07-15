<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--用户数据-->
      <el-col :span="24" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="108px">
          <el-form-item label="SKU名称" prop="skuName">
            <el-input
              v-model="queryParams.skuName"
              placeholder="请输入SKU名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="尺寸类别" prop="photoSizeValues">
            <el-select
              v-model="queryParams.photoSizeValues"
              placeholder="尺寸类别"
              clearable
              multiple
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="dict in photosizeOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button type="success " icon="el-icon-search" size="mini" @click="handleExcelBatchSearch">Excel批量搜索</el-button>
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
              v-hasPermi="['sku:sku:list']"
            >新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['sku:sku:list']"
            >删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['sku:sku:list']"
            >导出
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="skuList" @selection-change="handleSelectionChange" :key="timer">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="编号" align="center" prop="skuId">
            <template slot-scope="scope">
              <span v-if="scope.row.skuId === null" style="color: red">{{"没有"}}</span>
              <span v-else>{{scope.row.skuId}}</span>
            </template>
          </el-table-column>
          <el-table-column label="名称" align="center" prop="skuName"/>
          <el-table-column label="图片" align="center">
            <el-table-column v-for="(item, index) in photosizeOptions" :label="item.dictLabel" align="center">
              <template slot-scope="scope">
                <i class="el-icon-close" v-if="checkPhotoList(index,scope.row.photoList) === 0"></i>
                <i class="el-icon-check" v-else></i>
              </template>
            </el-table-column>

          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="created" width="160">
            <template slot-scope="scope">
              <span>{{ formateDate(scope.row.created) }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">

              <el-button
                v-if="scope.row.skuId != null"
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['sku:sku:list']"
              >编辑
              </el-button>
              <el-button
                v-if="scope.row.skuId != null"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['sku:sku:list']"
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
          <el-col :span="24">
            <el-form-item label="尺寸类型">
              <template>
                <el-tabs v-model="photoSizeLabel">
                  <el-tab-pane v-for="item in form.photoList" :label="item.photoSizeLabel" :name="item.photoSizeLabel"
                               :key="item.photoId">
                    <SkuPhoto :item="item" :key="item.photoId"></SkuPhoto>
                  </el-tab-pane>
                </el-tabs>
              </template>

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
                value-format="yyyy-MM-dd HH:mm:ss"
                type="datetime"
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

    <el-dialog :title="excelTitle" :visible.sync="excelOpen" width="600px" append-to-body>
      <el-form ref="form" :model="excelForm"  label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="Excel文件">
              <bas-doc :basdoc="ExcelBatchSearch" :readonly="false"  @changeFileList="changeExcelFileList" :key="timer" ></bas-doc>

            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitExcelForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>


</template>

<script>
import {addSku, getSku, listSku, updateSku, deleteSku, uniqueSku, exportSku} from "@/api/sku/sku";

import SkuPhoto from "@/views/public/sku-photo"
import BasDoc from "@/views/public/bas-doc";
import {changeDocList} from "@/api/public/basdoc";

export default {
  name: "Sku",
  components: {SkuPhoto, BasDoc},
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
        skuName: undefined,
        photoSizeValues: [],
        docidList: []
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
      // 弹出层标题
      excelTitle: "",
      // 是否显示弹出层
      excelOpen: false,
      excelForm: {},
      ExcelBatchSearch: "Excel批量搜索",
      docList: []
    };
  },
  watch: {},
  created() {
    console.log("this.$store.getters.realName is " + this.$store.getters.realName);

    this.getDicts("图片尺寸类别").then(response => {
      this.photosizeOptions = response.data;
      this.timer = Date.now();
      this.getList();
    });

  },
  methods: {
    /** 查询合同列表 */
    getList() {
      this.loading = true;
      console.log("this.queryParams is ", this.queryParams);

      listSku(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.skuList = response.rows;
          console.log("listSku skuList is ", this.skuList);
          this.total = response.total;
          this.loading = false;
        }
      );
    },

    checkPhotoList(index, photos) {
      // console.log("photos is ", photos);
      if (photos === null) {
        return  0;
      }

      let result = 0;
      const dictItem = this.photosizeOptions[index];
      for (let i = 0; i < photos.length; i++) {
        const photo = photos[i];
        // console.log("dictItem dictValue is ", dictItem.dictValue, "photosizeValue is ", photo.photoSizeValue);
        if (photo.photoSizeValue === dictItem.dictValue && photo.photoText != null) {
          result = 1;
          break;
        }
      }
      return result;
    },

    formateDate(datetime) {
      function addDateZero(num) {
        return (num < 10 ? "0" + num : num);
      }

      if (datetime === null) {
        return "";
      }

      let d = new Date(datetime);
      let formatdatetime = d.getFullYear() + '-' + addDateZero(d.getMonth() + 1) + '-' + addDateZero(d.getDate()) + ' ' + addDateZero(d.getHours()) + ':' + addDateZero(d.getMinutes()) + ':' + addDateZero(d.getSeconds());
      let formatdate = d.getFullYear() + '-' + addDateZero(d.getMonth() + 1) + '-' + addDateZero(d.getDate());

      return formatdatetime;
    },


    getSkuName(query) {
      return new Promise((resolve, reject) => {
        let res = uniqueSku(query);
        resolve(res);
      });
    },

    async validateSkuName(rule, value, callback) {
      if (!value) {
        callback(new Error("SKU名称不能为空"));
      } else {
        if (this.form.skuName === undefined) {
          callback();
        } else {
          let query = {skuName: value, skuId: this.form.skuId};
          let res = await this.getSkuName(query);
          console.log(res);
          if (res.data > 0) {
            callback(new Error("SKU名称重复"));
          } else {
            callback();
          }
        }

      }
    },


    // 取消按钮
    cancel() {
      this.open = false;
      this.excelOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        skuId: undefined,
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
      this.queryParams.docidList = [];
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.skuId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.form.photoList = [];
      for (let i = 0; i < this.photosizeOptions.length; i++) {
        let option = this.photosizeOptions[i];
        let photo = new Object();
        photo.photoId = null;
        photo.photoSizeLabel = option.dictLabel;
        photo.photoSizeValue = option.dictValue;
        photo.photoText = null;
        this.form.photoList.push(photo);

        if (i === 0) {
          this.photoSizeLabel = option.dictLabel;
        }
      }
      this.form.created = new Date();

      this.open = true;
      this.title = "添加SKU";
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.skuId !== undefined) {
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
    handleUpdate(row) {
      this.reset();
      const skuId = row.skuId || this.ids
      getSku(skuId).then(response => {
        console.log("update sku is ", response.data);
        this.form = response.data;
        console.log("this.form is ", this.form.photoList, "this.photosizeOptions is ", this.photosizeOptions);
        for (let i = 0; i < this.photosizeOptions.length; i++) {
          let option = this.photosizeOptions[i];

          if (i === 0) {
            this.photoSizeLabel = option.dictLabel;
          }

          let x = false;
          for (let j = 0; j < this.form.photoList.length; j++) {
            let photo = this.form.photoList[j];
            console.log("photo.photoSizeValue === option.photoSizeValue", photo.photoSizeValue , option.dictValue);
            if (photo.photoSizeValue === option.dictValue) {
              x = true;
              break;
            }
          }

          if (x === false) {
            let photo = new Object();
            photo.photoId = null;
            photo.photoSizeLabel = option.dictLabel;
            photo.photoSizeValue = option.dictValue;
            photo.photoText = null;
            this.form.photoList.push(photo);
          }

        }

        console.log("this.form is ", this.form.photoList);
        this.open = true;
        this.title = "修改SKU";
      });
    },

    /** 删除按钮操作 */
    handleDelete(row) {
      const skuIds = row.skuId || this.ids;
      this.$confirm('是否确认删除编号为"' + skuIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return deleteSku(skuIds);
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
          const blob = new Blob([response]);
          console.log("response.length is ", blob);
          if (blob.size < 10) {
            this_.msgError("导出失败");
          }
          else {
            const fileURL = window.URL.createObjectURL(blob);
            const fileLink = document.createElement('a');
            fileLink.href = fileURL;
            fileLink.setAttribute('download', "sku_export" + ".zip");
            document.body.appendChild(fileLink);
            fileLink.click();
            URL.revokeObjectURL(fileURL);
            this_.msgSuccess("导出成功");
          }
          this_.loading = false;
        });
      })

    },

    handleExcelBatchSearch() {
      this.docList = [];
      this.timer = (new Date()).toString();

      this.excelOpen = true;
      this.excelTitle = "上传excel文件批量查询";

    },


    changeExcelFileList(filelist) {
      if (this.docList == null) {
        this.docList = [];
      }

      let doctype = "Excel批量搜索";

      changeDocList(filelist, doctype, this.docList);

      console.log("filelist is ", filelist);
      console.log("this.docList is ", this.docList);

    },

    submitExcelForm() {

      console.log("Excel批量搜索 is ", this.queryParams.toString() );
      this.excelOpen = false;

      this.queryParams.docidList = [];
      for (let i=0; i< this.docList.length; i++){
        let doc = this.docList[i];
        this.queryParams.docidList.push(doc.docid);
      }

      this.queryParams.page = 1;
      this.getList();
      this.docList = [];
      this.timer = "";
    },


  }
};
</script>
