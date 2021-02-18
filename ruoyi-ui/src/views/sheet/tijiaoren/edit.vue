<template>
  <div class="app-container">

    <el-row :gutter="20">
      <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="160px" :key="timer">
        <template>
          <el-row v-bind:hidden="hidden.confirm">
            <el-col :span="8">
              <el-form-item label="拨付单号" prop="sheetcode">
                <el-input readonly v-model="form.sheetcode"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="经办人" prop="sheetuseridlinktext">
                <el-input readonly v-model="form.sheetuseridlinktext"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="经办时间" prop="sheettime">
                <el-input readonly v-model="form.sheettime"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item v-if="readonly.basic == false" label="项目名称" prop="projectname">
                <el-select v-model="form.projectname" placeholder="请选择"
                           style="display:block;" clearable @clear="clearProjectid" @change="changeProjectid"
                           filterable :filter-method="filterProjectOptions" :show-overflow-tooltip="true">
                  <el-option
                    v-for="item in projectOptions"
                    :key="item.projectid"
                    :label="item.projectname"
                    :value="item.projectid"/>
                </el-select>
              </el-form-item>
              <el-form-item v-else label="项目名称" prop="projectname">
                <el-input readonly v-model="form.projectname"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目周期" prop="projectDateRange">
                <el-input readonly v-model="form.projectDateRange"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="课题编号" prop="subjectcode">
                <el-input readonly v-model="form.subjectcode"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目类型" prop="projectTypeLinkText">
                <el-input readonly v-model="form.projectTypeLinkText"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目负责人" prop="projectManagerIDLinkText">
                <el-input readonly v-model="form.projectManagerIDLinkText"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所属部门" prop="organizationIDLinkText">
                <el-input readonly v-model="form.organizationIDLinkText" :show-overflow-tooltip="true"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="项目申报书" prop="basicfileList1">
                <el-upload action="#" :before-remove="beforeRemove1" :on-preview="handleReview"
                           :file-list="basicfileList1"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目合同" prop="basicfileList2">
                <el-upload action="#" :before-remove="beforeRemove2" :on-preview="handleReview"
                           :file-list="basicfileList2"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="实施方案" prop="basicfileList3">
                <el-upload action="#" :before-remove="beforeRemove3" :on-preview="handleReview"
                           :file-list="basicfileList3"/>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item label="相关性说明" prop="reason">
                <el-input v-bind:readonly="readonly.basic" v-model="form.reason" placeholder="" type="textarea"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="状态" prop="sheetstatuslinktext">
                <el-input v-model="form.sheetstatuslinktext" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="信息输入人员" prop="sheetuseridlinktext">
                <el-input v-model="form.sheetuseridlinktext" disabled/>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template>
          <el-row>
            <el-col :span="24">
              <el-form-item label="项目协作单位" prop="budgetpayList">
                <el-row :gutter="10" class="mb8" v-bind:hidden="readonly.basic">
                  <el-col :span="1.5">
                    <el-button plain
                               type="primary"
                               icon="el-icon-plus"
                               size="mini"
                               @click="handleBudgetpayAdd"
                               v-hasPermi="['sheet:tijiaoren:list']"
                    >新增
                    </el-button>
                  </el-col>
                </el-row>
                <el-table :data="form.budgetpayList"
                          @selection-change="handleBudgetpaySelectionChange"
                          style="display:block;" show-summary>
                  <el-table-column type="index" width="100px" align="center"/>
                  <el-table-column label="项目协作单位名称" align="center" prop="suppliername"
                                   :show-overflow-tooltip="true" width="300px">
                  </el-table-column>
                  <el-table-column label="拨付协作单位经费情况（元）" align="center">
                    <el-table-column label="预算总拨付经费" align="center" prop="zong"/>
                    <el-table-column label="实际拨付经费" align="center">
                      <el-table-column label="小计" align="center" prop="xiaoji"/>
                      <el-table-column label="以前年度已拨" align="center" prop="yiqian"/>
                      <el-table-column label="本年已拨" align="center" prop="bennian"/>
                      <el-table-column label="本次拨付" align="center" prop="benci"/>
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
                            @click="handleBudgetpayUpdate(scope.row)"
                            v-hasPermi="['sheet:tijiaoren:list']"
                          >编辑
                          </el-button>
                          <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleBudgetpayDelete(scope.row)"
                            v-hasPermi="['sheet:tijiaoren:list']"
                          >删除
                          </el-button>
                        </template>
                      </el-table-column>
                    </el-table-column>
                  </el-table-column>
                </el-table>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="本次拨付经费" prop="sheetuseridlinktext">

                <span>人民币{{ form.daxie }}</span>
              </el-form-item>

            </el-col>
          </el-row>
        </template>


        <template>
          <el-row>
            <el-col :span="24">
              <el-form-item label="历史拨付记录" prop="budgetpayRecordList">

                <el-table :data="form.budgetpayRecordList" style="display:block;">
                  <el-table-column type="index" width="100" align="center"/>
                  <el-table-column label="项目名称" align="center" prop="projectidlinktext"
                                   :show-overflow-tooltip="true">
                  </el-table-column>
                  <el-table-column label="协作单位" align="center" prop="suppliername"
                                   :show-overflow-tooltip="true">
                  </el-table-column>
                  <el-table-column label="拨付金额" align="center" prop="benci"
                                   :show-overflow-tooltip="true">
                  </el-table-column>
                  <el-table-column label="审核时间" align="center" prop="audittime"
                                   :show-overflow-tooltip="true">
                  </el-table-column>

                </el-table>
                <pagination
                  v-show="queryRecordTotal>0"
                  :total="queryRecordTotal"
                  :page.sync="queryRecordParams.pageNum"
                  :limit.sync="queryRecordParams.pageSize"
                  @pagination="getSheetBudgetpayRecordList"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </template>
        <template>
          <el-row v-bind:hidden="hidden.acceptance">
            <el-col :span="24">
              <el-form-item label="经办人签名" prop="sheetuseridlinktext">
                <img :src="form.sheetuseridImage" min-width="120" height="60"/>
                <span>  {{ form.sheettime }}</span>
              </el-form-item>
            </el-col>
          </el-row>
        </template>
        <template>
          <el-row v-for="audit in form.sheetAuditRecordList" v-bind:hidden="hidden.acceptance">
            <el-col :span="24">
              <el-form-item :label="audit.audittypeName">
                <span>  {{ audit.auditresultName }}</span>  <span> <img :src="audit.signpicName" min-width="120" height="60"/>  {{ audit.audittime }}</span>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template>
          <el-row v-if="this.hidden.confirm == false">
            <el-form-item label="审批意见" prop="confirmResult">
              <el-row>
                <el-col :span="16">
                  <template>
                    <span>审核结果 </span>
                    <el-radio-group v-model="form.confirmResult">
                      <el-radio :label="1">通过</el-radio>
                      <el-radio :label="2">不通过</el-radio>
                    </el-radio-group>
                  </template>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="16">
                  <template>
                    <span>审核意见</span>
                    <el-input v-model="form.confirmNote" placeholder="请输入意见" type="textarea"/>
                  </template>
                </el-col>
              </el-row>
            </el-form-item>
          </el-row>
        </template>

      </el-form>


      <el-row>
        <el-col :span="24" align="center">
          <el-button v-if="hidden.confirmBtn === false" type="success" @click="confirmForm">确定审批</el-button>
          <el-button v-if="hidden.submitBtn === false" type="success" @click="submitForm">提交审核</el-button>
          <el-button v-if="hidden.changeBtn === false" type="primary" @click="changeForm">修改信息</el-button>
          <el-button v-if="hidden.deleteBtn === false" type="danger" @click="deleteForm">删除</el-button>

          <el-button @click="closeForm">取 消</el-button>
        </el-col>
      </el-row>


    </el-row>


    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="budgetpayFormTitle" :visible.sync="budgetpayFormOpen" width="600px" append-to-body>
      <el-form ref="budgetpayForm" :model="budgetpayForm" :rules="budgetpayRules" label-width="250px" :key="timer">
        <el-row>
          <el-col :span="24">
            <el-form-item label="项目协作单位名称" prop="supplierid" label-width="150px">
              <el-select v-model="budgetpayForm.supplierid" placeholder="请选择"
                         style="display:block;" clearable @clear="clearSupplierid" @change="changeSupplierid"
                         filterable :filter-method="filterSupplierOptions" :show-overflow-tooltip="true">
                <el-option
                  v-for="item in supplierOptions"
                  :key="item.supplierid"
                  :label="item.suppliername"
                  :value="item.supplierid"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="预算总拨付经费（元）" prop="zong">
              <el-input type="number" v-model="budgetpayForm.zong" placeholder="请输入"
                        v-bind:readonly="budgetpayForm.getIfFirstPayed"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="实际拨付经费.小计（元）" prop="xiaoji">
              <el-input readonly v-model="budgetpayForm.xiaoji" placeholder="请输入"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="实际拨付经费.以前年度已拨（元）" prop="yiqian">
              <el-input readonly v-model="budgetpayForm.yiqian" placeholder="请输入"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="实际拨付经费.本年已拨（元）" prop="bennian">
              <el-input readonly v-model="budgetpayForm.bennian" placeholder="请输入"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="实际拨付经费.本次拨付（元）" prop="benci">
              <el-input type="number" v-model="budgetpayForm.benci" placeholder="请输入"/>
            </el-form-item>
          </el-col>

        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBudgetpayForm">确 定</el-button>
        <el-button @click="cancelBudgetpayForm">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {getProject, listAftersetup, listProjectdoc} from "@/api/project/project";
import {
  addSheet,
  deleteSheet,
  getSheet,
  getSheetAuditRecord,
  getSheetBudgetpay,
  getSheetBudgetpayRecord,
  listSheetSupplier,
  updateSheet,
  confirmAudit3Sheet
} from "@/api/sheet/sheet";
import {handleUploadReview} from "@/api/achieve/basdoc";
import {getSignpic} from "@/api/audit/signpic"

export default {
  name: "sheet_tijiaoren_edit",
  data() {
    return {
      // 各个组件的只读和隐藏属性控制
      readonly: {},
      hidden: {},
      opcode: undefined,
      applyid: undefined,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      // 数据字典  专利类型

      projectOptions: [],
      projectList: [],

      supplierOptions: [],
      supplierList: [],


      basicfileList1: [],
      basicfileList2: [],
      basicfileList3: [],

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

      // 日期范围
      // 查询参数
      // 表单参数
      form: {},
      timer: '',
      // 表单校验
      rules: {
        projectname: [
          {required: true, message: "项目名称不能为空", trigger: "blur"}
        ]
      },

      budgetpayFormTitle: "",
      budgetpayFormOpen: false,
      budgetpayForm: {},
      budgetpayRules: {
        benci: [
          {required: true, message: "本次拨付不能为空", trigger: "blur"}
        ]
      },
      // 条数
      queryRecordTotal: 0,
      // 查询参数
      queryRecordParams: {
        pageNum: 1,
        pageSize: 10,
        sheettype: "拨付单",
        projectid: undefined,
        supplieridList: undefined
      },
    };

  },

  mounted() {
    console.log("mounted this.$route.meta is ", this.$route.meta);
  },

  beforeCreate() {
    console.log(" beforeCreate this.$route.meta is ", this.$route.meta);
    const sheetid = this.$route.params && this.$route.params.sheetid;

  },
  created() {
    console.log(" created this.$route.meta is ", this.$route.meta);
    this.resetTemplateStatus();
    console.log(" created this.$route.params is ", this.$route.params);

    var sheetid = this.$route.params && this.$route.params.sheetid;
    if (sheetid === undefined || Number(sheetid) === 0) {
      sheetid = undefined;
    } else {

    }

    this.opcode = this.$route.meta.opcode;

    this.getData(sheetid);

  },
  methods: {
    /** 查询信息 */
    getData(sheetid) {

      this.loading = true;
      console.log("loading is begin, sheetid is ", sheetid);

      if (sheetid === undefined) {
        this.reset();
        this.configTemplateStatus();
        this.loadProjectOptions("");
        this.loading = false;

      } else {
        const this_ = this;

        getSheet(sheetid).then(response => {
          console.log("getSheet response is ", response.data);

          const sheet = response.data;

          this_.form = sheet;

          const projectid = sheet.projectid;

          getProject(projectid.toString()).then(response => {
            console.log("getProject response is ", response.data);
            const project = response.data;

            this_.form.projectname = project.projectname;
            this_.form.projectDateRange = project.projectDateRange;
            this_.form.subjectcode = project.subjectcode;
            this_.form.projectTypeLinkText = project.projectTypeLinkText;
            this_.form.projectManagerIDLinkText = project.projectManagerIDLinkText;
            this_.form.organizationIDLinkText = project.organizationIDLinkText;

            listProjectdoc({projectid: this.form.projectid}).then(response => {

              let rows = response.data;
              console.log("listProjectdoc is ", rows);
              this_.form.projectdocList = rows;
              this_.basicfileList1 = this_.filterProjectdoc("项目申报书");
              console.log("项目申报书 is ", this_.basicfileList1);
              this_.basicfileList2 = this_.filterProjectdoc("项目合同");
              console.log("项目合同 is ", this_.basicfileList2);
              this_.basicfileList3 = this_.filterProjectdoc("实施方案");
              console.log("实施方案 is ", this_.basicfileList3);
            });
          });

          getSheetBudgetpay(sheetid).then(response => {
            console.log("getSheetBudgetpay response is ", response.data);
            this_.form.budgetpayList = response.data;

            //显示历史付款记录(列表里选择的供应商付款记录都要列出来)

            this_.queryRecordParams.projectid = projectid;

            let ids = new Array();
            for (let k = 0; k < this_.form.budgetpayList.length; k++) {
              let item = this_.form.budgetpayList[k];
              ids.push(item.supplierid);
            }
            this_.queryRecordParams.supplieridList = ids;

            console.log("getSheetBudgetpay record is ", this_.queryRecordParams);
            this_.getSheetBudgetpayRecordList();

          });

          getSignpic(this_.form.sheetuserid).then(response => {
            console.log("response is ", response);
            this_.form.sheetuseridImage = response.data.signpicName;
          });

          getSheetAuditRecord({"sheettype": "拨付单", "sheetid": this_.form.sheetid}).then(response => {
            console.log("getSheetAuditRecord response is ", response);
            this_.form.sheetAuditRecordList = response.data;

          });

          console.log("this.form is ", this_.form);

          this_.configTemplateStatus();

          this_.loading = false;

          // 获取 审核结果信息。

        });
      }

    },

    getSheetBudgetpayRecordList() {

      getSheetBudgetpayRecord(this.queryRecordParams).then(response => {
        this.form.budgetpayRecordList = response.rows;
        this.queryRecordTotal = response.total;
        console.log("getSheetBudgetpayRecord response is", response.rows);

      });
    },

    resetTemplateStatus() {
      // 初始化各个组件的状态。
      this.readonly = {basic: true, acceptance: true, confirm: true};
      this.hidden = {
        basic: false,
        acceptance: true,
        confirm: true,
        saveBtn: true,
        changeBtn: true,
        deleteBtn: true,
        submitBtn: true,
        changeAcceptanceBtn: true,
        returnBtn: true,
        confirmBtn: true,
        addAcceptanceBtn: true
      };
    },

    configTemplateStatus() {
      this.resetTemplateStatus();
      console.log("configTemplateStatus is ", this.form.sheetstatus);
      if (this.form.sheetstatus === this.SheetStatus.XinJianZhong) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("add") !== -1) {
          this.readonly.basic = false;
          this.hidden.submitBtn = false;
        } else if (this.opcode.indexOf("query") !== -1) {
          this.hidden.changeBtn = false;
          this.hidden.deleteBtn = false;
        } else if (this.opcode.indexOf("confirm") !== -1) {
          this.readonly.confirm = false;
          this.hidden.confirm = false;
          this.hidden.submitBtn = false;
        }
      } else if (this.form.sheetstatus === this.SheetStatus.XiangMuShenPi) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        } else if (this.opcode.indexOf("audit") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm = false;
          this.hidden.confirmBtn = false;
        }
      } else if (this.form.sheetstatus === this.SheetStatus.NoPass) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {

          this.hidden.acceptance = false;

          this.hidden.changeBtn = false;
          this.hidden.deleteBtn = false;
        }
      } else {

      }
    },


    // 表单重置
    reset() {
      this.form = {
        sheetid: undefined,
        sheetcode: undefined,
        sheettype: undefined,

        projectid: undefined,
        projectname: undefined,
        projectDateRange: undefined,
        subjectcode: undefined,
        projectmanagerid: undefined,
        projectManagerIDLinkText: undefined,
        organizationid: undefined,
        organizationIDLinkText: undefined,
        projecttype: undefined,
        projectTypeLinkText: undefined,

        reason: undefined,

        sheetuserid: undefined,
        sheetuseridlinktext: undefined,
        sheettime: undefined,
        sheetstatus: this.SheetStatus.XinJianZhong,
        sheetstatuslinktext: "新建中",

        projectdocList: [],
        // 拨付的当前记录
        budgetpayList: [],
        // 拨付的历史记录
        budgetpayRecordList: [],
        sheetuseridImage: undefined,
        sheetAuditRecordList: []

      };

      this.resetForm("form");
    },


    createFilter(v) {
      return (item) => {
        const queryString = v.toLowerCase();

        let x = false;

        const keys = Object.keys(item);
        for (let i = 0; i < keys.length; i++) {
          let key = keys[i];
          let value = item[key];
          let pp = -1;
          if (value !== undefined && value !== null) {
            pp = value.toString().indexOf(queryString);
          }
          if (pp != -1) {
            x = true;
            break;
          }
        }
        return x;
      };
    },


    clearProjectid() {

    },
    changeProjectid(value) {
      console.log("changeProjectid value is " + value);
      if (value) {
        this.form.projectid = value;
        for (let i = 0; i < this.projectOptions.length; i++) {
          let project = this.projectOptions[i];
          if (project.projectid === value) {
            this.form.projectname = project.projectname;
            this.form.projectDateRange = project.projectDateRange;
            this.form.subjectcode = project.subjectcode;
            this.form.projectTypeLinkText = project.projectTypeLinkText;
            this.form.projectManagerIDLinkText = project.projectManagerIDLinkText;
            this.form.organizationIDLinkText = project.organizationIDLinkText;
            this.form.organizationid = project.organizationid;
            this.form.projectmanagerid = project.projectmanagerid;

            listProjectdoc({projectid: this.form.projectid}).then(response => {
              const this_ = this;
              let rows = response.data;
              console.log("listProjectdoc is ", rows);
              this.form.projectdocList = rows;
              this.basicfileList1 = this.filterProjectdoc("项目申报书");
              console.log("项目申报书 is ", this.basicfileList1);
              this.basicfileList2 = this.filterProjectdoc("项目合同");
              console.log("项目合同 is ", this.basicfileList2);
              this.basicfileList3 = this.filterProjectdoc("实施方案");
              console.log("实施方案 is ", this.basicfileList3);

            });
            break;
          }
        }

      } else {
        this.form.projectid = undefined;
        this.form.projectname = undefined;
      }
    },

    loadProjectOptions(queryString) {
      // 在线查询。
      const queryParams = {
        pageNum: 1,
        pageSize: 30,
        projectname: queryString
      };
      listAftersetup(queryParams).then(response => {
          const projectOptions = [];
          const projectList = response.data;
          console.log("response is ", response)
          for (let i = 0; i < projectList.length; i++) {
            let project = projectList[i];

            const keys = Object.keys(project);
            for (let j = 0; j < keys.length; j++) {
              let key = keys[j];
              let vv = project[key];
              //  console.log("project."+ key + " = " , vv);
            }

            // console.log("project is ", project);
            //  const item = {"projectid": project.projectid, "projectname": project.projectname};
            projectOptions.push(project);
          }
          this.projectOptions = projectOptions;
        }
      );
    },

    filterProjectOptions(queryString) {
      console.log("filter value is " + queryString);
      console.log("projectList is ", this.projectList);

      let options = [];
      if (this.projectList.length > 10000) {
        options = this.projectList;
        this.projectOptions = queryString ? options.filter(this.createFilter(queryString)) : options;
      } else {
        // 在线查询。
        this.loadProjectOptions(queryString);
      }
    },

    loadSupplierOptions(queryString) {
      // 在线查询。
      const queryParams = {
        pageNum: 1,
        pageSize: 30,
        suppliername: queryString
      };

      listSheetSupplier(queryParams).then(response => {
          const supplierOptions = [];
          const supplierList = response.data;
          // console.log("response is ", response)
          for (let i = 0; i < supplierList.length; i++) {
            let supplier = supplierList[i];

            const keys = Object.keys(supplier);
            for (let j = 0; j < keys.length; j++) {
              let key = keys[j];
              let vv = supplier[key];
            }

            // console.log("supplier is ", supplier);
            supplierOptions.push(supplier);
          }
          this.supplierOptions = supplierOptions;
        }
      );
    },

    clearSupplierid() {

    },

    changeSupplierid(value) {
      if (value) {
        this.resetBudgetpayForm();
        this.budgetpayForm.supplierid = value;
        for (let i = 0; i < this.supplierOptions.length; i++) {
          let supplier = this.supplierOptions[i];
          if (supplier.supplierid === value) {
            this.budgetpayForm.suppliername = supplier.suppliername;
            let queryParams = {
              pageNum: 1,
              pageSize: 10,
              sheettype: "拨付单",
              projectid: this.form.projectid,
              supplieridList: [value]
            };
            getSheetBudgetpayRecord(queryParams).then(response => {
              const budgetpay = {
                payid: undefined,
                supplierid: value,
                suppliername: supplier.suppliername,
                zong: 0.0,
                xiaoji: 0.0,
                yiqian: 0.0,
                bennian: 0.0,
                benci: 0.0,
                getIfFirstPayed: false
              };

              console.log("getSheetBudgetpayRecord is ", response.rows);
              const budgetpayRecordList = response.rows;
              if (budgetpayRecordList.length > 0) {
                budgetpay.getIfFirstPayed = true;
                let currDate = new Date();
                let currYear = currDate.getFullYear();
                for (let i = 0; i < budgetpayRecordList.length; i++) {
                  let item = budgetpayRecordList[i];
                  let audityear = parseInt(item.audittime.substr(1, 4));
                  if (audityear < currYear) {
                    budgetpay.yiqian = budgetpay.yiqian + item.benci;
                  } else {
                    budgetpay.bennian = budgetpay.bennian + item.benci;
                  }
                  budgetpay.xiaoji = budgetpay.xiaoji + item.benci;
                  budgetpay.zong = item.zong;
                }
              } else {
                budgetpay.getIfFirstPayed = false;
              }

              this.budgetpayForm = budgetpay;
              console.log("change supplier when this.budgetpayForm is ", this.budgetpayForm);
            });

            break;
          }
        }
      } else {
        this.resetBudgetpayForm();
      }
    },

    filterSupplierOptions(queryString) {
      console.log("filter value is " + queryString);
      console.log("projectList is ", this.supplierOptions);

      let options = [];
      if (this.supplierList.length > 10000) {
        options = this.supplierList;
        this.supplierOptions = queryString ? options.filter(this.createFilter(queryString)) : options;
      } else {
        // 在线查询。
        this.loadSupplierOptions(queryString);

      }
    },


    filterProjectdoc(doctype) {
      const doclist = [];

      if (doctype !== "") {
        for (let i = 0; i < this.form.projectdocList.length; i++) {
          let item = this.form.projectdocList[i];
          if (item.doctype === doctype) {
            doclist.push({"name": item.docname, "url": item.docid});
          }
        }
      }
      return doclist;
    },

    /* 项目申报书 */
    beforeRemove1(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeRemove1 index=" + index, file.name);
      return false;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleReview(file) {
      this.$confirm('是否确认下载"' + file.name + '"的文件?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {

        handleUploadReview(file);

      }).then(() => {
        this.msgSuccess("下载开始");
      })

    },

    /* 项目合同 */
    beforeRemove2(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeRemove2 index=" + index, file.name);
      return false;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    /* 实施方案 */
    beforeRemove3(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeRemove2 index=" + index, file.name);
      return false;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },


    /** 关闭按钮 */
    closeForm() {

      // this.reset();
      this.$router.go(-1)// 返回
      //关闭子页面

      this.$store.state.tagsView.visitedViews.splice(this.$store.state.tagsView.visitedViews.findIndex(item => item.path === this.$route.path), 1)
      this.$router.push(this.$store.state.tagsView.visitedViews[this.$store.state.tagsView.visitedViews.length - 1].path)
    },

    /** 删除按钮 */
    deleteForm() {

      const sheetid = this.form.sheetid;

      this.$confirm('是否确认删除"' + this.form.projectname + '"的拨付单?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        deleteSheet(sheetid);
      }).then(() => {
        this.msgSuccess("删除成功");
        this.closeForm();
      });
    },


    /*提交 审核 按钮*/
    submitForm: function () {

      const this_ = this;

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          this_.form.operateCode = 2; // 提交审核代码 提交给后端。

          if (this_.opcode === "add") {
            // 处理掉添加， 为了更新或修改。
            this_.form.budgetpayList.forEach(function (item) {
              if (item.payid < 0) {
                item.payid = undefined;
              }
            });

            if (this_.form.sheetid === undefined) {
              addSheet(this.form).then(response => {
                if (response.data === 0) {
                  this_.msgError("提交审核失败");
                  this_.form.sheetid = undefined;
                } else {
                  this.msgSuccess("提交审核成功");
                  this.form.sheetid = response.data;

                }
              }).then(() => {

                this.closeForm();
              });
            } else {
              updateSheet(this.form).then(response => {

              }).then(() => {
                this.msgSuccess("提交审核成功");
                this.closeForm();
              });

            }
          } else if (this.opcode === "confirm") {
            console.log("confirmResult is " + this.form.confirmResult);

            if (this_.form.confirmResult == undefined) {
              this.msgError("请选择审核结果");
              return;
            }

            this_.form.confirmUserid = this_.$store.getters.userId;
            const result = this_.form.confirmResult;

            if (result === 1) {
              this_.$confirm('是否确认审核 通过?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              }).then(function () {
                return confirmPatent(this_.form);
              }).then(() => {
                this_.closeForm();
                this_.msgSuccess("审核通过，完成");
              });
            } else if (result === 2) {

              var note = this_.form.confirmNote;
              console.log("confirmNote is ", note);
              if (note !== null && note !== undefined && note.trim() !== '') {
                this_.$confirm('是否确认审核 不通过?', "警告", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning"
                }).then(function () {
                  return confirmPatent(this_.form);
                }).then(() => {
                  this_.closeForm();
                  this_.msgSuccess("审核不通过 完成");
                });
              } else {
                this_.$confirm('您选择的结果为 “不通过”, 请输入意见！', "警告", {
                  confirmButtonText: "确定",
                  type: "warning"
                }).then(function () {

                });
              }
            }
          }


        }
      });
    },

    /** 编辑模式按钮 */
    changeForm() {
      this.form.sheetstatus = this.SheetStatus.XiangMuShenPi;
      this.opcode = "add";
      this.configTemplateStatus();
    },

    confirmForm() {
      if (this.form.confirmResult == undefined) {
        this.msgError("请选择审核结果");
        return;
      }

      const this_ = this;

      this_.form.confirmUserid = this_.$store.getters.userId;
      const result = this_.form.confirmResult;

      if (result === 1) {
        this_.$confirm('是否确认审核 通过?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return confirmAudit3Sheet(this_.form);
        }).then(() => {
          this_.closeForm();
          this_.msgSuccess("审核成功");
        });
      }
      else if (result === 2) {

        const note = this_.form.confirmNote;
        console.log("confirmNote is ", note);
        if (note !== null && note !== undefined && note.trim() !== '' ) {
          this_.$confirm('是否确认审核 不通过?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {
            return  confirmAudit3Sheet(this_.form);
          }).then(() => {
            this_.closeForm();
            this_.msgSuccess("审核不通过 完成");
          });
        }
        else {
          this_.$confirm('您选择的结果为 “不通过”, 请输入意见！', "警告", {
            confirmButtonText: "确定",
            type: "warning"
          }).then(function () {

          });
        }
      }



    },

    /* 主持的项目 子 form */

    // 多选框选中数据
    handleBudgetpaySelectionChange(selection) {
      // this.ids = selection.map(item => item.teamid);
      // this.single = selection.length != 1;
      // this.multiple = !selection.length;
    },

    handleBudgetpayAdd() {
      if (this.form.projectid === undefined) {
        this.msgError("没有选择项目");
        return;
      }
      this.budgetpayFormOpen = true;
      this.budgetpayFormTitle = "添加项目协作单位";
      this.resetBudgetpayForm();
      this.loadSupplierOptions("");
    },

    handleBudgetpayUpdate(row) {
      this.budgetpayFormOpen = true;
      this.budgetpayFormTitle = "编辑项目协作单位";
      const budgetpay = {
        payid: row.payid,
        supplierid: row.supplierid,
        zong: parseFloat(row.zong),
        xiaoji: parseFloat(row.xiaoji),
        yiqian: parseFloat(row.yiqian),
        bennian: parseFloat(row.bennian),
        benci: parseFloat(row.benci),
        getIfFirstPayed: false
      };

      this.budgetpayForm = budgetpay;

      this.loadSupplierOptions("");

    },


    /**  删除按钮操作 */
    handleBudgetpayDelete(row) {
      const this_ = this;
      const suppliername = row.suppliername
      this.$confirm('是否确认删除"' + suppliername + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        const index = this_.form.budgetpayList.indexOf(row);
        this_.form.budgetpayList.splice(index, 1);
      }).then(() => {
        this.msgSuccess("删除成功");
      });
    },

    cancelBudgetpayForm: function () {
      this.budgetpayFormOpen = false;
      this.resetBudgetpayForm();
    },

    submitBudgetpayForm: function () {

      const this_ = this;
      this_.$refs["budgetpayForm"].validate(valid => {
        if (valid) {
          if (this.budgetpayForm.benci < 200) {
            this.msgError("本次拨付金额小于200，请核对！请注意金额的单位为'元'");
            return;
          }

          let uniqueSupplier = true;
          for (let i = 0; i < this.form.budgetpayList.length; i++) {
            let item = this.form.budgetpayList[i];
            if (this.budgetpayForm.payid === undefined) {
              if (this.budgetpayForm.supplierid === item.supplierid) {
                uniqueSupplier = false;
                break;
              }
            } else {
              if (this.budgetpayForm.payid !== item.payid && this.budgetpayForm.supplierid === item.supplierid) {
                uniqueSupplier = false;
                break;
              }
            }
          }

          if (uniqueSupplier === false) {
            this.msgError(this.budgetpayForm.suppliername + " 已存在相同的协作单位");
            return;
          }

          this.budgetpayForm.xiaoji = parseFloat(this.budgetpayForm.xiaoji) + parseFloat(this.budgetpayForm.benci);

          if (this.budgetpayForm.xiaoji > this.budgetpayForm.zong) {
            this.msgError(this.budgetpayForm.suppliername + " 小计金额大于预算总经费，请核对！");
            return;
          }

          if (this_.budgetpayForm.payid !== undefined) {
            this_.form.budgetpayList.forEach(function (item) {
              if (item.payid == this_.budgetpayForm.payid) {
                item.supplierid = this_.budgetpayForm.supplierid;
                item.suppliername = this_.budgetpayForm.suppliername;
                item.zong = parseFloat(this_.budgetpayForm.zong);
                item.xiaoji = parseFloat(this_.budgetpayForm.xiaoji);
                item.yiqian = parseFloat(this_.budgetpayForm.yiqian);
                item.bennian = parseFloat(this_.budgetpayForm.bennian);
                item.benci = parseFloat(this_.budgetpayForm.benci);
              }
            });
            this.msgSuccess("修改成功");
          } else {
            let payid = 0;
            this_.form.budgetpayList.forEach(function (item) {
              if (item.payid !== undefined && item.payid < payid) {
                payid = item.payid;
              }
            });
            payid = payid - 1;
            const pay2 = {
              payid: payid,
              supplierid: this_.budgetpayForm.supplierid,
              suppliername: this_.budgetpayForm.suppliername,
              zong: parseFloat(this_.budgetpayForm.zong),
              xiaoji: parseFloat(this_.budgetpayForm.xiaoji),
              yiqian: parseFloat(this_.budgetpayForm.yiqian),
              bennian: parseFloat(this_.budgetpayForm.bennian),
              benci: parseFloat(this_.budgetpayForm.benci)
            };
            console.log("添加协作单位", pay2);
            this_.form.budgetpayList.push(pay2);
            this.msgSuccess("添加成功");
          }
          console.log("submit budgetpayForm is ", this.budgetpayForm);
          this_.budgetpayFormOpen = false;
          this.resetBudgetpayForm();
        }
      });
    },

    // 表单重置
    resetBudgetpayForm() {
      this.budgetpayForm = {
        payid: undefined,
        supplierid: undefined,
        zong: 0.0,
        xiaoji: 0.0,
        yiqian: 0.0,
        bennian: 0.0,
        benci: 0.0,
        getIfFirstPayed: false
      };
      this.resetForm("budgetpayForm");
    },

  }
}
</script>

<style scoped>

</style>
