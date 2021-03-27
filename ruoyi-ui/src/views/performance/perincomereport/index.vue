<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" :rules="queryRules" ref="queryForm" :inline="true" v-show="showSearch"
               label-width="168px">
        <el-form-item label="筛选条件: 年度" prop="year">
          <el-date-picker type="year" value-format="yyyy" v-model="queryParams.year" clearable />
        </el-form-item>
        <el-form-item label="所属团队" prop="teamid">
          <!-- 所属团队组件-->
          <team-data :selected-team-id="queryParams.teamid" :join-team-user-id="queryParams.userid"
                     @changeTeamId="selectTeamId"></team-data>
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
            v-hasPermi="['performance:perincomereport:list']"
          >新增
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="performanceList" >
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="所属团队" align="center" prop="teamidlinktext" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="年度" align="center" prop="year" width="60"/>
        <el-table-column label="项目名称" align="center" prop="projectname"/>
        <el-table-column label="项目编号" align="center" prop="projectcode" width="180"/>
        <el-table-column label="负责人" align="center" prop="manageridlinktext" width="100">

        </el-table-column>

        <el-table-column label="起止时间" align="center" prop="period" width="100">

        </el-table-column>

        <el-table-column label="合同总金额（元）" align="center" prop="contractmoney" width="100">
        </el-table-column>
        <el-table-column label="今年到账经费（元）" align="center" prop="thisyearmoney" width="100">
        </el-table-column>
        <el-table-column label="类型" align="center" prop="type" width="180"/>
        <el-table-column label="状态" align="center" prop="status" width="180"/>
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
              v-hasPermi="['performance:perincomereport:list']"
              v-if="scope.row.status !== '已确认'"
            >编辑
            </el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-remove"
              @click="handleRemove(scope.row)"
              v-hasPermi="['performance:perincomereport:list']"
              v-if="scope.row.status !== '已确认'"
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

    <!-- 添加或修改对话框 -->
    <el-dialog v-if="open" :title="title" :visible.sync="open" width="800px"  :key="timer" >
      <template>
        <el-form ref="form" :model="form" :rules="rules" label-width="150px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="所属团队" prop="teamid">
                <!-- 所属团队组件-->
                <team-data :selected-team-id="form.teamid" :join-team-user-id="queryParams.userid"
                           @changeTeamId="selectFormTeamId"></team-data>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="年度" prop="year">
                <el-date-picker type="year" value-format="yyyy" v-model="form.year" clearable />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="项目名称" prop="projectname">
                <el-input v-model="form.projectname" ></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="项目编号" prop="projectcode">
                <el-input v-model="form.projectcode" placeholder=""  />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="负责人" prop="managerid">
                <user-data :selected-user-id="form.managerid" @changeUserData="changeFormUserData"></user-data>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="合同总金额（元）" prop="contractmoney">
                <el-input type="number" v-model="form.contractmoney" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="起止时间" prop="period">
                <el-input v-model="form.period"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="今年到账经费（元）" prop="thisyearmoney">
                <el-input type="number" v-model="form.thisyearmoney" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="类型" prop="type">
                <el-select v-model="form.type" placeholder="请选择类型" style="display:block;"
                           @change="changeContractTypeValue" >
                  <el-option
                    v-for="item in contractTypeOptions"
                    :key="item"
                    :label="item"
                    :value="item"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </template>
      <template>
        <el-row>
          <el-col :span="24" align="center">
            <el-button type="success" @click="saveForm">确 定</el-button>
            <el-button @click="closeForm">取 消</el-button>
          </el-col>
        </el-row>

      </template>

    </el-dialog>


  </div>
</template>

<script>
import {
  listIncomereport,updateIncomereport,addIncomereport
} from "@/api/performance/teamperformance";
import BasDoc from "../../public/bas-doc"
import TeamData from "../../public/team-data";
import {spanRow} from "@/api/performance/spanRow";
import {listIndicatorTree} from "@/api/performance/indicator";
import UserData from "@/views/public/user-data";


export default {
  name: "perincome_report_index",
  components: {UserData, "bas-doc": BasDoc, "team-data": TeamData, "user-data": UserData},
  data() {
    return {
      // 各个组件的只读和隐藏属性控制
      readonly: {},
      hidden: {},
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
      performanceList: [],
      performanceListOption: [],

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
        pageSize: 10,
        userid: undefined,
        teamid: undefined,
        teamidlinktext: undefined,
        year: undefined

      },
      queryRules: {
        year: [
          {required: true, message: "年度不能为空", trigger: "blur"}
        ],
        teamid: [
          {required: true, message: "所属团队不能为空", trigger: "blur"}
        ]
      },
      //
      timer: '',
      // 表单参数
      form: {},

      contractTypeOptions: ["科技产业开发收入", "技术转让收入"],

      // 表单校验
      rules: {
        teamid: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        year: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        projectname: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        projectcode: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        managerid: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        contractmoney: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        period: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        thisyearmoney: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        type: [
          {required: true, message: "不能为空", trigger: "blur"}
        ]
      }

    };
  },
  watch: {},
  created() {

    this.loading = false;
    this.queryParams.userid = this.$store.getters.userId;

  },
  methods: {
    /** 查询列表 */

    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listIncomereport(this.queryParams).then(response => {
        console.log("response data is ", response);
        this.total = response.total;
        this.performanceList = response.rows;
        this.performanceListOption = [];

        this.loading = false;
      });
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
      this.$refs["queryForm"].validate(valid => {
        if (valid) {
          this.getList();
        }
      });

    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },


    // 组件方法
    selectTeamId(value) {
      console.log("selectTeamId is ", value);
      this.queryParams.teamid = value.id;
      this.queryParams.teamidlinktext = value.value;
    },


    // 组件方法
    selectFormTeamId(value) {
      console.log("selectTeamId is ", value);
      this.form.teamid = value.id;
      this.form.teamidlinktext = value.value;
    },

    // 组件方法
    changeFormUserData(user) {
      console.log("changeFormUserData is ", user);
      this.form.managerid = user.userId;
      this.form.manageridlinktext = user.realName;
    },

    changeContractTypeValue(value){
      this.form.contracttype = value;
    },

    handleAdd() {
      this.reset();
      this.title = "新增成果转化收入信息";
      this.form = {};
      this.open = true;
    },

    handleUpdate(row){
      this.title = "成果转化收入信息";
      console.log("row is ", row);
      const form = row;
      form.year = form.year.toString();
      form.teamid = form.teamid === undefined ? undefined : form.teamid;

      this.form = form;
      this.open = true;
    },

    handleRemove(row){

    },

    /** 保存按钮 */
    saveForm() {

      this.$refs["form"].validate(
        valid => {
          if (valid) {
            const this_ = this;
            console.log("保存的数据为", this_.form);
            if (this_.form.incomeid === undefined|| this_.form.incomeid === null) {
              addIncomereport(this_.form).then(response => {
                this_.msgSuccess("保存成功");
                this_.open = false;
                this_.getList();
              });
            }
            else {
              updateIncomereport(this_.form).then(response => {
                this_.msgSuccess("修改成功");
                this_.open = false;
                this_.getList();
              });

            }

          }
        }
      );
    },


    /** 关闭按钮 */
    closeForm() {
      this.open = false;
    }



  }

};
</script>


<style scoped>

</style>
