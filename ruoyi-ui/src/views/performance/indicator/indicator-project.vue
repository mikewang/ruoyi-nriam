<template>
  <div class="app-container">
    <el-row>
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="项目类型" prop="projecttypelinktext">
          <el-input
            v-model="queryParams.projecttypelinktext"
            placeholder="请输入项目类型"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="handleQuery"
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
            type="primary"
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['performance:indicator:list']"
          >新增
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>
      <el-table v-loading="loading" :data="projectList">
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="项目类型" align="left" prop="projecttypelinktext"/>
        <el-table-column label="主持/参与" align="center" prop="jointype" width="120"/>
        <el-table-column label="分数" align="center" prop="points" width="120"/>
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
              v-hasPermi="['performance:indicator:list']"
            >修改
            </el-button>
            <el-button
              v-if="scope.row.userId !== 1"
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['performance:indicator:list']"
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
    <el-row>
       <br/>
      <br/>
      <br/>

    </el-row>
    <el-row>
      <!--科研项目组件 关联的考核指标：一级指标  二级指标  [lab_Type]-->
      <indicator-relation :indicatortype="relationType"></indicator-relation>

    </el-row>

    <!-- 添加或修改参数配置对话框 -->
    <div v-if="open">
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="项目类型" prop="projecttype">
              <!--项目类型 组件  但是不能清空-->
              <dict-data :dictTypeName="projecttypeName" :selectedDictValue="form.projecttype" @changeDictValue="selectProjectType"></dict-data>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="主持/参与" prop="jointype">

              <el-select v-model="form.jointype" placeholder="请选择" style="display:block;" clearable
                         @clear="clearJointypeValue" @change="changeJointypeValue">
                <el-option
                  v-for="item in jointypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分数" prop="points">
              <el-input v-model="form.points" type="number" placeholder="请输入"></el-input>
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

  </div>

</template>

<script>
import {listIndicatorProject, addIndicatorProject, updateIndicatorProject, deleteIndicatorProject} from "@/api/performance/indicator";
import IndicatorRelation from "./indicator-relation";
import DictData from "../../public/dict-data";

export default {
  name: "indicator-project",
  components :{"indicator-relation": IndicatorRelation,"dict-data": DictData},
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
      // 项目表格数据
      projectList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        projecttypelinktext: undefined
      },
      // 表单校验
      rules: {
        projecttype: [
          {required: true, message: "项目类型不能为空", trigger: "blur"}
        ],
        jointype: [
          {required: true, message: "主持/参与不能为空", trigger: "blur"}
        ],
        points: [
          {required: true, message: "分数不能为空", trigger: "blur"}
        ]
      },

      // 数据字典
      jointypeOptions: [{value: 1, label: "主持"}, {value: 2, label: "参与"}],
      // 初始化 关联指标。
      relationType: "科研项目",
      projecttypeName : "项目类型"

    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      listIndicatorProject(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.projectList = response.rows;
          this.total = response.total;

        // 最后结束刷新。
        this.loading = false;
        }
      );
    },

    // 表单重置
    reset() {
      this.form = {
        indicatorprojectid: undefined,
        projecttype: undefined,
        projecttypelinktext: undefined,
        jointype: undefined,
        points: undefined
      };
      this.resetForm("form");
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();

      this.title = "新增投入项目信息";
      this.open = true;
      this.form.projectType = undefined;

    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log(row);
      this.reset();

      this.title = "编辑投入项目信息";
      this.form.indicatorprojectid = row.indicatorprojectid;
      this.form.projecttype = row.projecttype.toString();
      this.form.jointype = row.jointype;
      this.form.points = row.points;
      this.open = true;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const itemstr = row.projecttypelinktext + " " + row.jointype;
      this.$confirm('是否确认删除 ' + itemstr + ' ?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return deleteIndicatorProject([row.indicatorprojectid]);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.indicatorprojectid != undefined) {
            updateIndicatorProject(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addIndicatorProject(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    // 组件传入的方法。
    selectProjectType(projectType) {

      console.log("子组件传入的 selectProjectType is ", projectType);

      this.form.projecttype = projectType.id;
      this.form.projectTypeLinkText = projectType.value;

    },



    clearJointypeValue() {
      console.log("clear jointype");
    },

    changeJointypeValue(value) {

      if (value) {
        this.form.jointype = value;
      } else {
        this.form.jointype = undefined;
      }

    },

    filterJointypeValue() {
      console.log("filter jointype");
    },


  }
}
</script>

<style scoped>

</style>
