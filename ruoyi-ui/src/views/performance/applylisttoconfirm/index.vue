<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" :rules="queryRules" ref="queryForm" :inline="true" v-show="showSearch"
               label-width="168px">
        <el-form-item label="筛选条件: 年度" prop="year">
          <el-date-picker type="year" value-format="yyyy" v-model="queryParams.year" clearable/>
        </el-form-item>
        <el-form-item label="所属团队" prop="teamid">
          <!-- 所属团队组件-->
          <team-data :selected-team-id="queryParams.teamid" :join-team-user-id="undefined"
                     @changeTeamId="selectTeamId"></team-data>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">

        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="performanceList">
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="团队名称" align="center" prop="teamidlinktext" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="年度" align="center" prop="year" width="100"/>
        <el-table-column label="一级指标" align="center" prop="level1name"/>
        <el-table-column label="二级指标" align="center" prop="level2name" width="180"/>
        <el-table-column label="统计指标" align="center" prop="level3name"/>
        <el-table-column label="加分分数" align="center" prop="applyscores" width="100"/>
        <el-table-column label="申请人" align="center" prop="applyuseridlinktext" width="100"/>
        <el-table-column label="申请时间" align="center" prop="applytime" width="100">
        </el-table-column>
        <el-table-column label="状态" align="center" prop="applystatus" width="100">
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-check"
              @click="handleConfirm(scope.row)"
              v-hasPermi="['performance:applylisttoconfirm:list']"
            >审核
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
    <el-dialog v-if="open" :title="title" :visible.sync="open" width="800px" append-to-body>
      <template>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" :key="timer">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="所属团队" prop="teamid">
                <el-input readonly v-model="form.teamidlinktext" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="年度" prop="year">
                <el-input readonly v-model="form.year" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="一级指标" prop="level1name">
                <el-input readonly v-model="form.level1name" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="二级指标" prop="level2name">
                <el-input readonly v-model="form.level2name" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="统计指标" prop="level3name">
                <el-input readonly v-model="form.level3name" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请分数" prop="applyscores">
                <el-input type="number" v-model="form.applyscores" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="内容描述" prop="applycontent">
                <el-input readonly type="textarea" v-model="form.applycontent" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="证明材料" prop="fileList">
                <bas-doc @changeFileList="changeBasDocFileList" :basdoc="form.basdoc"
                         :editable="false"></bas-doc>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="申请时间" prop="applytime">
                <el-input readonly v-model="form.applytime" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请人" prop="applyuseridlinktext">
                <el-input readonly v-model="form.applyuseridlinktext" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="状态" prop="applystatus">
                <el-input readonly v-model="form.applystatus" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24" align="center">
              <el-form-item label="审批结果" prop="auditResult">
                <el-radio-group v-model="form.auditResult" >
                  <el-radio :label="1">通过</el-radio>
                  <el-radio :label="2">不通过</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24" align="center">
              <el-form-item label="审批意见" prop="auditopinion">
                <el-input v-model="form.auditopinion" placeholder="请输入意见" type="textarea"/>
              </el-form-item>
            </el-col>
          </el-row>

        </el-form>
      </template>

      <template>

      </template>

      <template>
        <el-row>
          <el-col :span="24" align="center">
            <el-button v-if="hidden.confirmBtn === false" type="success" @click="confirmForm">确 定</el-button>
            <el-button @click="closeForm">取 消</el-button>
          </el-col>
        </el-row>

      </template>

    </el-dialog>


  </div>
</template>

<script>
import {
  listApplylisttoconfirm,
  confirmAddscoreapply
} from "@/api/performance/teamperformance";
import BasDoc from "../../public/bas-doc"
import TeamData from "../../public/team-data";
import {spanRow} from "@/api/performance/spanRow";
import {praseStrEmpty} from "@/utils/ruoyi";


export default {
  name: "applylist_toconfirm_index",
  components: {"bas-doc": BasDoc, "team-data": TeamData},
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
      performancePointsSum: 0,

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

      },
      //

      // 表单参数
      form: {fileList: []},


      timer: '',
      // 表单校验
      rules: {
        year: [
          {required: true, message: "年度不能为空", trigger: "blur"}
        ],
        teamid: [
          {required: true, message: "所属团队不能为空", trigger: "blur"}
        ],
        applyscores: [
          {required: true, message: "申请分数不能为空", trigger: "blur"}
        ],
        applycontent: [
          {required: true, message: "内容描述不能为空", trigger: "blur"}
        ]
      }

    };
  },
  watch: {},
  created() {

    this.loading = false;
    this.hidden = {confirmBtn: false};
    this.queryParams.userid = this.$store.getters.userId;

  },
  methods: {
    /** 查询列表 */

    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listApplylisttoconfirm(this.queryParams).then(response => {
        console.log("response data is ", response);
        this.total = response.total;
        this.performanceList = response.rows;
        this.performanceListOption = [];

        this.loading = false;
      });
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


    handleConfirm(row) {
      console.log("审核加分申请", row);

      this.title = "审核加分申请";
      this.form = row;
      //加载文档
      this.form.fileList = [];
      const kid = this.form.applyid;
      const query = {attachtotype: "加分申请", relatedid: kid, doctype: "证明材料"};
      this.form.basdoc = query;
      this.form.lastApplyscores = this.form.applyscores;

      this.open = true;

    },

    // 组件方法
    changeBasDocFileList(fileList) {
      console.log("changeBasDocFileList is ", fileList);
      this.form.fileList = fileList;

    },

   /** 保存按钮 */
    confirmForm() {
      this.$refs["form"].validate(
        valid => {
          if (valid) {
            const this_ = this;
            if (this_.form.fileList.length === 0) {
              this_.msgError("保存失败，没有上传证明材料");
            }
            else if (this_.form.auditResult === undefined) {
              this_.msgError("保存失败，请选择审批结果");
            }
            else if (this_.form.auditResult === "2" && praseStrEmpty(this_.form.auditopinion) === "") {
              this_.msgError("保存失败，您选择的结果为“不通过”，请输入意见！");
            }
            else if (this_.form.lastApplyscores !== this_.form.applyscores && praseStrEmpty(this_.form.auditopinion) === "") {
              this_.msgError("保存失败，您设置的分数与原申请分数不同，请输入意见！");
            }
            else {

              confirmAddscoreapply(this_.form).then(response => {
                this_.msgSuccess("保存成功");
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
