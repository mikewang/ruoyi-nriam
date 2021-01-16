<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input
            v-model="deptName"
            placeholder="请输入部门名称"
            clearable
            size="small"
            prefix-icon="el-icon-search"
            style="margin-bottom: 20px"
          />
        </div>
        <div class="head-container">
          <el-tree
            :data="deptOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="合同编号" prop="contractNo">
            <el-input
              v-model="queryParams.contractNo"
              placeholder="请输入合同编号"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="合同名称" prop="contractName">
            <el-input
              v-model="queryParams.contractName"
              placeholder="请输入合同名称"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="queryParams.status"
              placeholder="合同状态"
              clearable
              size="small"
              style="width: 240px"
            >
              <el-option
                v-for="dict in statusOptions"
                :key="dict.dictValue"
                :label="dict.dictLabel"
                :value="dict.dictValue"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间">
            <el-date-picker
              v-model="dateRange"
              size="small"
              style="width: 240px"
              value-format="yyyy-MM-dd"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
            ></el-date-picker>
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
              v-hasPermi="['logis:contract:add']"
            >新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['logis:contract:edit']"
            >修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['logis:contract:remove']"
            >删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              v-hasPermi="['logis:contract:export']"
            >导出
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="contractList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="50" align="center"/>
          <el-table-column label="合同ID" align="center" prop="contractId"/>
          <el-table-column label="合同编号" align="center" prop="contractNo"/>
          <el-table-column label="合同名称" align="center" prop="contractName" :show-overflow-tooltip="true"/>
          <el-table-column label="用户姓名" align="center" prop="realName" :show-overflow-tooltip="true"/>
          <el-table-column label="部门" align="center" prop="dept.deptName" :show-overflow-tooltip="true"/>
          <el-table-column label="状态" align="center">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              ></el-switch>
            </template>
          </el-table-column>
          <el-table-column label="创建时间" align="center" prop="created" width="160">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
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
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['logis:contract:edit']"
              >修改
              </el-button>
              <el-button
                v-if="scope.row.userId !== 1"
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['logis:contract:remove']"
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
            <el-form-item label="合同编号" prop="contractNo">
              <el-input v-model="form.contractNo" placeholder="请输入合同编号"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="合同名称" prop="contractName">
              <el-input v-model="form.contractName" placeholder="请输入合同名称"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="合同文件" prop="contractFile">
              <el-upload action="#" :http-request="requestUpload" :on-remove="handleUploadRemove"
                         :on-preview="handleUploadPreview"
                         :file-list="form.fileList" :before-upload="beforeUpload" v-hasPermi="['logis:contract:edit']">
                <el-button size="small">点击上传<i class="el-icon-upload el-icon--right"></i></el-button>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="归属部门" prop="deptId">
              <treeselect v-model="form.deptId" :options="deptOptions" :show-count="true" placeholder="请选择归属部门"/>
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
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                  v-for="dict in statusOptions"
                  :key="dict.dictValue"
                  :label="dict.dictValue"
                >{{ dict.dictLabel }}
                </el-radio>
              </el-radio-group>
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
import {
  listContract,
  getContract,
  delContract,
  addContract,
  updateContract,
  exportContract,
  uploadContractFile, downloadContractFile
} from "@/api/logis/contract";
import {getToken} from "@/utils/auth";
import {treeselect} from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {uploadAvatar} from "@/api/system/user";

export default {
  name: "Contract",
  components: {Treeselect},
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
      // 用户表格数据
      contractList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      statusOptions: [],
      // 性别状态字典
      sexOptions: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deptId: undefined,
        realName: undefined,
        contractNo: undefined,
        contractName: undefined,
        status: undefined,
        created: undefined
      },
      // 表单校验
      rules: {
        contractNo: [
          {required: true, message: "合同编号不能为空", trigger: "blur"}
        ],
        contractName: [
          {required: true, message: "合同名称不能为空", trigger: "blur"}
        ]
      }
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    console.log("this.$store.getters.nickName is " + this.$store.getters.realName);
    this.getList();
    this.getTreeselect();
    this.getDicts("sys_normal_disable").then(response => {
      this.statusOptions = response.data;
    });
    this.getDicts("sys_user_sex").then(response => {
      this.sexOptions = response.data;
    });
  },
  methods: {
    /** 查询合同列表 */
    getList() {
      this.loading = true;
      listContract(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.contractList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },
    /** 查询部门下拉树结构 */
    getTreeselect() {
      treeselect().then(response => {
        this.deptOptions = response.data;
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id;
      this.getList();
    },
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用";
      this.$confirm('确认要"' + text + '""' + row.contractName + '"合同吗?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return changeUserStatus(row.contractId, row.status);
      }).then(() => {
        this.msgSuccess(text + "成功");
      }).catch(function () {
        row.status = row.status === "0" ? "1" : "0";
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        contractId: undefined,
        contractNo: undefined,
        contractName: undefined,
        userId: undefined,
        deptId: undefined,
        realName: undefined,
        status: "0",
        created: undefined,
        fileList: undefined
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
      this.ids = selection.map(item => item.contractId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getTreeselect();
      this.form.userId = this.$store.getters.userId;
      this.form.realName = this.$store.getters.nickName;
      this.open = true;
      this.title = "添加合同";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      const contractId = row.contractId || this.ids
      getContract(contractId).then(response => {
        console.log(response.data);
        this.form = response.data;
        console.log("this.form is ", this.form);
        this.open = true;
        this.title = "修改合同";
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.contractId !== undefined) {
            console.log("submitForm is ", this.form);
            updateContract(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addContract(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const contractIds = row.contractId || this.ids;
      this.$confirm('是否确认删除合同编号为"' + contractIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return delContract(userIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有合同数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportContract(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    },
    beforeUpload(file) {

      this.form.fileList.forEach(
        function (item) {
          if (item.name === file.name) {
            return false;
          }
        }
      );
      return true;

    },
    handleUploadError(error, file, fileList) {
    },
    handleUploadExceed(file, fileList) {
    },
    handleUploadSuccess(response, file, fileList) {
    },
    requestUpload: function (params) {
      let file = params.file;
      console.log(file);
      let formData = new FormData();
      formData.append('file', file);
      uploadContractFile(formData).then(response => {
        console.log("response", response.name);
        console.log("response", response.url);
        this.form.fileList = this.form.fileList.concat({name: response.name, url: response.url});
      });
    },
    handleUploadChange(file, fileList) {
      // this.form.fileList = fileList.slice(-10);
    },
    handleUploadRemove(file) {

      console.log("handleUploadRemove ", file.name);

      this.form.fileList.forEach((item, index) => {
        if (item.name === file.name) {
          this.form.fileList.splice(index, 1);
        }
      });

    },
    handleUploadPreview(file) {

      console.log("handleUploadPreview is ", file.url);

      // window.open('http://127.0.0.1:80/logis/contract/download' + "?file=" + file.url);

      downloadContractFile({"file": file.url}).then(response => {

        // var blob = new Blob([response], { type: 'multipart/form-data' });
        // var fileURL = window.URL.createObjectURL(blob);

        var fileURL = window.URL.createObjectURL(new Blob([response]));
        var fileLink = document.createElement('a');

        console.log("response.data is ", response);

        fileLink.href = fileURL;
        fileLink.setAttribute('download', file.name);
        document.body.appendChild(fileLink);

        fileLink.click();
        URL.revokeObjectURL(fileURL);

      }).catch(console.error);


    }
  }
};
</script>

