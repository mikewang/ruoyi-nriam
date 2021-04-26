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
            <el-button v-if="scope.row.handleDeleteBtnHidden"
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

    <!-- 添加或修改协作单位对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="180px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="协作单位名称" prop="suppliername">
              <el-input v-model="form.suppliername" :show-overflow-tooltip="true"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="协作单位简称" prop="suppliershortname">
              <el-input v-model="form.suppliershortname" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="组织机构代码证号" prop="organizationcode">
              <el-input v-model="form.organizationcode" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="税务登记证号" prop="taxcode">
              <el-input v-model="form.taxcode" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开户银行" prop="bankname">
              <el-input v-model="form.bankname" :show-overflow-tooltip="true"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行账号" prop="banknumber">
              <el-input v-model="form.banknumber" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="协作单位地址" prop="address">
              <el-input v-model="form.address" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="网址" prop="website">
              <el-input v-model="form.website" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="单位负责人" prop="manager">
              <el-input v-model="form.manager" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位性质" prop="character">
              <el-select v-model="form.characterLinkText" placeholder="请选择" @change="changeCharacterValue">
                <el-option
                  v-for="dict in characterOptions"
                  :key="dict.dictValue"
                  :label="dict.dictLabel"
                  :value="dict.dictValue"
                ></el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="联系人" prop="person1name">
              <el-input v-model="form.person1name" placeholder="请输入"  />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="电子邮件" prop="person1email">
              <el-input v-model="form.person1email" placeholder="请输入邮箱"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="联系电话1" prop="person1phone1">
              <el-input v-model="form.person1phone1" placeholder="请输入"  />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话2" prop="person1phone2">
              <el-input v-model="form.person1phone2" placeholder="请输入"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="审核状态" prop="ifaudited">
              <el-input v-model="form.ifauditedText" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="信息输入人员" prop="createUserIDLinkText">
              <el-input v-model="form.createUserIDLinkText" readonly/>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row>
          <el-col :span="24">
            <el-form-item label="营业执照扫描件">
              <div class="user-info-head" @click="editCropper()"><img v-bind:src="orgImg.img" title="点击上传" class="img-lg" /></div>
              <el-dialog :title="imgTitle" :visible.sync="imgOpen" width="800px" append-to-body @opened="modalOpened">
                <el-row>
                  <el-col :xs="24" :md="12" :style="{height: '350px'}">
                    <vue-cropper
                      ref="cropper"
                      :img="orgImg.img"
                      :info="true"
                      :autoCrop="orgImg.autoCrop"
                      :autoCropWidth="orgImg.autoCropWidth"
                      :autoCropHeight="orgImg.autoCropHeight"
                      :fixedBox="orgImg.fixedBox"
                      @realTime="realTime"
                      v-if="visible"
                    />
                  </el-col>
                  <el-col :xs="24" :md="12" :style="{height: '350px'}">
                    <div class="upload-preview">
                      <img :src="previews.url" :style="previews.img" />
                    </div>
                  </el-col>
                </el-row>
                <br />
                <el-row>
                  <el-col :lg="2" :md="2">
                    <el-upload action="#" :http-request="requestUploadImg" :show-file-list="false" :before-upload="beforeUploadImg">
                      <el-button size="small">
                        选择
                        <i class="el-icon-upload el-icon--right"></i>
                      </el-button>
                    </el-upload>
                  </el-col>
                  <el-col :lg="{span: 1, offset: 2}" :md="2">
                    <el-button icon="el-icon-plus" size="small" @click="changeScale(1)"></el-button>
                  </el-col>
                  <el-col :lg="{span: 1, offset: 1}" :md="2">
                    <el-button icon="el-icon-minus" size="small" @click="changeScale(-1)"></el-button>
                  </el-col>
                  <el-col :lg="{span: 1, offset: 1}" :md="2">
                    <el-button icon="el-icon-refresh-left" size="small" @click="rotateLeft()"></el-button>
                  </el-col>
                  <el-col :lg="{span: 1, offset: 1}" :md="2">
                    <el-button icon="el-icon-refresh-right" size="small" @click="rotateRight()"></el-button>
                  </el-col>
                  <el-col :lg="{span: 2, offset: 6}" :md="2">
                    <el-button type="primary" size="small" @click="uploadImg()">提 交</el-button>
                  </el-col>
                </el-row>
              </el-dialog>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="其它附件">
              <el-upload action="#" :http-request="requestUpload2" :before-remove="beforeRemove2"
                         :on-remove="handleUploadRemove2" :on-preview="handleReview2"
                         :file-list="otherfileList" :before-upload="beforeUpload2"
              >
                <el-button size="small" >上传文件<i class="el-icon-upload el-icon--right"></i>
                </el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.memo" type="textarea" placeholder="请输入"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button v-if="form.supplierSubmitBtnHidden" type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {
  listSheetSupplier,
  getSheetSupplierById,
  uploadFile,
  addSheetSupplier,
  editSheetSupplier,
  deleteSheetSupplier
} from "@/api/audit/audit";
import {VueCropper} from "vue-cropper";


export default {
  name: "srm_supplier_index",
  components: { VueCropper },
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
      characterOptions: [{dictLabel:"事业",dictValue: 13 }, {dictLabel:"企业",dictValue: 14}],

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
      rules: {
        suppliername: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        suppliershortname: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        organizationcode: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        bankname: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        banknumber: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        character: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        address: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        person1name: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        person1phone1: [
          {required: true, message: "不能为空", trigger: "blur"}
        ]

      },

      // 弹出层标题
      imgTitle: "营业执照扫描件",
      // 是否显示弹出层
      imgOpen: false,
      // 是否显示cropper
      visible: false,
      orgImg: {
        img: "", //裁剪图片的地址
        autoCrop: true, // 是否默认生成截图框
        autoCropWidth: 200, // 默认生成截图框宽度
        autoCropHeight: 200, // 默认生成截图框高度
        fixedBox: false // 截图框大小 允许改变
      },
      previews: {},
      otherfileList: []
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
          for (let i=0; i < this.supplierList.length; i++) {
            let item = this.supplierList[i];
            if (item.createuserid === this.$store.getters.userId) {
              item.handleDeleteBtnHidden = true;
            }
            else {
              item.handleDeleteBtnHidden = false;
            }
          }

          this.total = response.total;
          this.loading = false;
        }
      );
    },

    // 提交
    submitForm() {

      this.form.docList = this.otherfileList;
      this.form.orgImgId = this.orgImg.docid;

      console.log("submitForm .form is ", this.form);
      if (this.form.supplierid == undefined) {
        addSheetSupplier(this.form).then(response => {
          this.open = false;
          this.reset();
          this.handleQuery();
        });
      }
      else {
        editSheetSupplier(this.form).then(response => {
          this.open = false;
          this.reset();
          this.handleQuery();
        });

      }

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
      this.reset();
      getSheetSupplierById().then(response => {
        this.form = response.data;
        // 默认值，C#隐藏未设置。
        this.form.type = 1;
        this.form.supplierlevel = 1;
        this.form.typename = "普通";
        this.form.levelname = "一级";
        this.form.organizationimg = "";
        this.form.ifauditedText = "未审核";
        this.form.supplierSubmitBtnHidden = true;

        this.open = true;
        this.title = "新增协作单位信息";
      });
    },

    handleUpdate(row) {
      this.reset();
      const supplierid = row.supplierid;
      getSheetSupplierById(supplierid).then(response => {
        console.log("response data is ", response.data);
        this.form = response.data;

        if (this.$store.getters.userId ===  this.form.createuserid) {
             this.form.supplierSubmitBtnHidden = true;
        }
        else {
            this.form.supplierSubmitBtnHidden = false;
        }

        this.orgImg.img = this.form.orgImgIdText;

        this.otherfileList = this.form.docList;

        if (this.otherfileList == undefined) {
          this.otherfileList = [];
        }
        console.log("this.otherfileList  is ", this.otherfileList );

        if (this.form.ifaudited) {
          this.form.ifauditedText = "已审核";
        }
        else {
          this.form.ifauditedText = "未审核";
        }

        this.open = true;
        this.title = "修改协作单位信息";
      });
    },

    handleDelete(row) {
      console.log("delete row is  ", row);
      const supplierid = row.supplierid;
      const  ids = [supplierid];

      const this_ = this;

      this.$confirm('是否确认删除"' + row.suppliername + '"的协作单位?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        deleteSheetSupplier(ids).then(response => {
          this_.msgSuccess("删除成功");
          this_.handleQuery();

        });
      });
    },

    changeCharacterValue(value) {
      if (value) {
        this.form.character = value;
      } else {
        this.form.character = undefined;
      }
    },


    // 编辑头像
    editCropper() {
      this.imgOpen = true;
    },
    // 打开弹出层结束时的回调
    modalOpened() {
      this.visible = true;
    },
    // 覆盖默认的上传行为
    requestUploadImg() {
    },
    // 向左旋转
    rotateLeft() {
      this.$refs.cropper.rotateLeft();
    },
    // 向右旋转
    rotateRight() {
      this.$refs.cropper.rotateRight();
    },
    // 图片缩放
    changeScale(num) {
      num = num || 1;
      this.$refs.cropper.changeScale(num);
    },
    // 上传预处理
    beforeUploadImg(file) {
      if (file.type.indexOf("image/") == -1) {
        this.msgError("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。");
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.orgImg.img = reader.result;
        };
      }
    },
    // 上传图片
    uploadImg() {
      this.$refs.cropper.getCropBlob(data => {
        console.log("data is ", data);
        let formData = new FormData();
        formData.append("file", data);
        formData.append("name", "wenjianmingch");
        formData.append("attachToType", "协作单位");
        formData.append("docType", "营业执照");

        uploadFile(formData).then(response => {
          console.log("response.name is ", response.name);
          console.log("response.url(docid) is ", response.url);
          console.log("response.fileUrl is ", response.fileUrl);
          this.imgOpen = false;
          this.orgImg.img = process.env.VUE_APP_BASE_API + response.fileUrl;
          this.orgImg.docid = response.url;
          this.msgSuccess("修改成功");
          this.visible = false;
        });

      });
    },
    // 实时预览
    realTime(data) {
      this.previews = data;
    },

    // 上传附件 协作单位
    beforeUpload2(file) {
      let x = true;
      console.log("fileList is ", this.otherfileList);
      for (let i = 0; i < this.otherfileList.length; i++) {
        let item = this.otherfileList[i];
        if (item.name === file.name) {
          console.log("file existed now ,", file.name);
          x = false;
          break;
        }
      }
      return x;

    },

    requestUpload2: function (params) {
      let file = params.file;
      console.log(file);
      let formData = new FormData();
      formData.append('file', file);
      formData.append("name", "wenjianmingch");
      formData.append("attachToType", "协作单位");
      formData.append("docType", "其它附件");
      uploadFile(formData).then(response => {
        console.log("response.name is ", response.name);
        console.log("response.url is ", response.url);
        this.otherfileList.push({name: response.name, url: response.url});
      });
    },

    beforeRemove2(file) {
      let index = this.otherfileList.indexOf(file);
      console.log("beforeRemove2 index=" + index, file.name);
      return true;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleUploadRemove2(file) {
     // handleUploadRemove(file, this.acceptfileList10,"协作单位", this.form.projectdocList );
    },

    handleReview2(file) {

    }
  }
};
</script>

<style scoped lang="scss">
.user-info-head {
  position: relative;
  display: inline-block;
  height: 120px;
}

.user-info-head:hover:after {
  content: '+';
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  color: #eee;
  background: rgba(0, 0, 0, 0.5);
  font-size: 24px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
  line-height: 110px;
}
</style>
