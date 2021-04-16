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
              <el-form-item label="项目名称" prop="projectid">
                <!-- 所属项目组件-->
                <project-data :readonly="readonly.basic" :selected-project-data="form.projectinfo"
                              @changeProjectData="selectProjectData" :selected-option="projectSelectedOption"
                              :key="projectid"></project-data>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目周期" prop="projectDateRange">
                <el-input readonly v-model="form.projectinfo.projectDateRange"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="课题编号" prop="subjectcode">
                <el-input readonly v-model="form.projectinfo.subjectcode"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目类型" prop="projecttypelinktext">
                <el-input readonly v-model="form.projectinfo.projecttypelinktext"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目负责人" prop="projectmanageridlinktext">
                <el-input readonly v-model="form.projectinfo.projectmanageridlinktext"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所属部门" prop="organizationidlinktext">
                <el-input readonly v-model="form.projectinfo.organizationidlinktext" :show-overflow-tooltip="true"
                          :key="form.projectid"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="项目申报书" prop="basicfileList1">
                <project-doc :doc-type="DocTypeXiangmuShenbaoShu" @changeFileList="changeBasicDocList"
                             :doc-list="basicfileList1" :readonly="true" :key="basicfileList1Key"></project-doc>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目合同" prop="basicfileList2">
                <project-doc :doc-type="DocTypeXiangmuHetong" @changeFileList="changeBasicDocList"
                             :doc-list="basicfileList2" :readonly="true"></project-doc>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="实施方案" prop="basicfileList3">
                <project-doc :doc-type="DocTypeShishiFangan" @changeFileList="changeBasicDocList"
                             :doc-list="basicfileList3" :readonly="true"></project-doc>
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
                               v-hasPermi="['audit:tijiaoren:list']"
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
                        <template slot-scope="scope" v-if="readonly.basic === false">
                          <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-edit"
                            @click="handleBudgetpayUpdate(scope.row)"
                            v-hasPermi="['audit:tijiaoren:list']"
                          >编辑
                          </el-button>
                          <el-button
                            size="mini"
                            type="text"
                            icon="el-icon-delete"
                            @click="handleBudgetpayDelete(scope.row)"
                            v-hasPermi="['audit:tijiaoren:list']"
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
              <el-form-item label="本次拨付经费" prop="daxie">
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
                  <el-table-column label="项目名称" align="center" prop="projectname"
                                   :show-overflow-tooltip="true">
                  </el-table-column>
                  <el-table-column label="协作单位" align="center" prop="suppliername"
                                   :show-overflow-tooltip="true">
                  </el-table-column>
                  <el-table-column label="拨付金额" align="center" prop="benci"
                                   :show-overflow-tooltip="true">
                  </el-table-column>
                  <el-table-column label="审批时间" align="center" prop="audittime"
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
            <el-col :span="16">
              <el-form-item :label="audit.audittypeName">
                <span>  {{ audit.auditresultName }}</span> <br/><span>  {{ audit.auditopinion }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <span> <img :src="audit.signpicName" min-width="120" height="60"/>  {{ audit.audittime }}</span>
            </el-col>
          </el-row>
        </template>

        <template>
          <el-row v-if="this.hidden.confirm == false">
            <el-form-item label="审批意见" prop="confirmResult">
              <el-row>
                <el-col :span="16">
                  <template>
                    <span>审批结果 </span>
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
                    <span>审批意见</span>
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
          <el-button v-if="hidden.changeBtn === false" type="primary" @click="changeForm">修改后提交</el-button>
          <el-button v-if="hidden.deleteBtn === false" type="danger" @click="deleteForm">删除</el-button>

          <el-button @click="closeForm">取 消</el-button>
        </el-col>
      </el-row>


    </el-row>


    <!-- 添加或修改菜单对话框 -->
    <el-dialog v-if="budgetpayFormOpen" :title="budgetpayFormTitle" :visible.sync="budgetpayFormOpen" width="600px" append-to-body>
      <el-form ref="budgetpayForm" :model="budgetpayForm" :rules="budgetpayRules" label-width="250px" :key="timer">
        <el-row>
          <el-col :span="24">
            <el-form-item label="项目协作单位名称" prop="supplierid" label-width="150px">
              <supplier-data :selected-supplier-data="budgetpayForm.supplierinfo"
                             @changeSupplierData="selectSupplierData"></supplier-data>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="联系人" prop="person1name">
              <el-input v-model="budgetpayForm.person1name" readonly/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="开户行" prop="bankname">
              <el-input v-model="budgetpayForm.bankname" readonly/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="银行账号" prop="banknumber">
              <el-input v-model="budgetpayForm.banknumber" readonly/>
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
              <el-input readonly v-model="budgetpayForm.xiaoji" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="实际拨付经费.以前年度已拨（元）" prop="yiqian">
              <el-input readonly v-model="budgetpayForm.yiqian" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="实际拨付经费.本年已拨（元）" prop="bennian">
              <el-input readonly v-model="budgetpayForm.bennian" placeholder=""/>
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
import {listProjectdoc} from "@/api/project/project";
import {
  addSheet,
  confirmAuditSheet,
  deleteSheet,
  getSheet,
  getSheetAuditRecord,
  getSheetBudgetpay,
  getSheetBudgetpayRecord,
  getSheetSupplier,
  updateSheet
} from "@/api/audit/audit";

import {getSignpic} from "@/api/audit/signpic";
import ProjectData from "@/views/public/project-data";
import ProjectDoc from "@/views/public/project-doc";
import SupplierData from "@/views/public/supplier-data";

export default {
  name: "audit_tijiaoren_edit",
  components: {ProjectData, ProjectDoc, SupplierData},
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

      // 数据字典  项目类型
      projectSelectedOption: "aftersetup",

      // 数据字典
      DocTypeXiangmuShenbaoShu: "项目申报书",
      DocTypeXiangmuHetong: "项目合同",
      DocTypeShishiFangan: "实施方案",

      projectid: undefined,
      supplierOptions: [],
      supplierList: [],

      basicfileList1: [],
      basicfileList2: [],
      basicfileList3: [],

      basicfileList1Key: '',

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
      form: undefined,
      timer: '',
      // 表单校验
      rules: {
        projectid: [
          {required: true, message: "项目名称不能为空", trigger: "blur"}
        ]
      },

      budgetpayFormTitle: "",
      budgetpayFormOpen: false,
      budgetpayForm: {},
      budgetpayRules: {
        zong: [
          {required: true, message: "预算总拨付经费不能为空", trigger: "blur"}
        ],
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

  },
  created() {
    console.log(" created this.$route.meta is ", this.$route.meta);
    console.log(" created this.$route.params is ", this.$route.params);

    let sheetid = this.$route.params && this.$route.params.sheetid;
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
      this.reset();

      if (sheetid === undefined) {
        this.configTemplateStatus();
        this.loading = false;
      } else {
        const this_ = this;
        console.log("loading is begin, sheetid is ", sheetid);
        getSheet(sheetid).then(response => {

          console.log("getSheet response data is ", response.data);

          const sheet = response.data;

          console.log("projectDateRange is ", sheet.projectinfo.projectDateRange);

          console.log("getSheet sheet.projectid is ", sheet.projectid);

          getSheetBudgetpay(sheetid).then(response3 => {
            console.log("getSheetBudgetpay response is ", response3.data);
            sheet.budgetpayList = response3.data;

            //显示历史付款记录(列表里选择的供应商付款记录都要列出来)

            this_.queryRecordParams.projectid = sheet.projectid;

            let ids = new Array();
            for (let k = 0; k < sheet.budgetpayList.length; k++) {
              let item = sheet.budgetpayList[k];
              ids.push(item.supplierid);
            }
            this_.queryRecordParams.supplieridList = ids;

            console.log("getSheetBudgetpay record is ", this_.queryRecordParams);
            this_.getSheetBudgetpayRecordList(sheet);

            getSignpic(sheet.sheetuserid).then(response => {
              console.log("response is ", response);
              sheet.sheetuseridImage = response.data.signpicName;

              getSheetAuditRecord({"sheettype": "拨付单", "sheetid": sheet.sheetid}).then(response => {
                console.log("getSheetAuditRecord response is ", response.data);
                sheet.sheetAuditRecordList = response.data;

                this_.form = sheet;
                this_.projectid = sheet.projectid;
                this_.time = Date.now().toString();

                this_.configTemplateStatus();

                this_.loading = false;

                console.log("this.form is ", this_.form);


              });
            });

          });

        });
      }

    },

    getSheetBudgetpayRecordList(sheet) {

      if (sheet === undefined) {
        sheet = this.form;
      }
      getSheetBudgetpayRecord(this.queryRecordParams).then(response => {
        sheet.budgetpayRecordList = response.rows;
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
          this.hidden.acceptance = false;
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
      } else if (this.form.sheetstatus === this.SheetStatus.BuMenShenPi) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        } else if (this.opcode.indexOf("audit") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm = false;
          this.hidden.confirmBtn = false;
        }
      } else if (this.form.sheetstatus === this.SheetStatus.ChuShenPi) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        } else if (this.opcode.indexOf("audit") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm = false;
          this.hidden.confirmBtn = false;
        }
      } else if (this.form.sheetstatus === this.SheetStatus.FenGuanSuoShenPi) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        } else if (this.opcode.indexOf("audit") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm = false;
          this.hidden.confirmBtn = false;
        }
      } else if (this.form.sheetstatus === this.SheetStatus.SuoZhangShenPi) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        } else if (this.opcode.indexOf("audit") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm = false;
          this.hidden.confirmBtn = false;
        }
      } else if (this.form.sheetstatus === this.SheetStatus.ShenPiWanCheng) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        }
      } else if (this.form.sheetstatus === this.SheetStatus.YiZuoFei) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

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
        projectinfo: {projectDateRange: undefined},

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

    // 组件方法
    selectProjectData(project) {
      console.log("selectProjectData is ", project);
      this.form.projectid = undefined;
      this.form.projectinfo = {projectid: undefined};

      const this_ = this;

      if (project) {
        this_.form.projectid = project.projectid;
        this_.form.projectinfo = project;
        this_.projectid = project.projectid;

        listProjectdoc({projectid: project.projectid}).then(response => {

          let rows = response.data;
          console.log("listProjectdoc is ", rows);
          this_.form.projectdocList = rows;

          this_.basicfileList1 = this_.form.projectdocList.filter(function (item) {
            return item.doctype === "项目申报书"
          });
          console.log("项目申报书 is ", this_.basicfileList1);

          this_.basicfileList2 = this_.form.projectdocList.filter(function (item) {
            return item.doctype === "项目合同"
          });
          console.log("项目合同 is ", this_.basicfileList2);

          this_.basicfileList3 = this_.form.projectdocList.filter(function (item) {
            return item.doctype === "实施方案"
          });
          console.log("实施方案 is ", this_.basicfileList3);

          this.basicfileList1Key = project.projectid;

        });
      } else {
        this.form.projectid = undefined;
        this.form.projectinfo = {projectid: undefined};
      }

    },

    changeBasicDocList(fileList, docType, operate, docid) {

      console.log("changeBasicDocList is ", docType, "fileList is ", fileList);
      /* 项目申报书 */
      if (docType === this.DocTypeXiangmuShenbaoShu) {
        this.basicdocList1 = fileList;
      }

      /* 项目合同 */
      if (docType === this.DocTypeXiangmuShenbaoShu) {
        this.basicdocList2 = fileList;
      }

      /* 实施方案 */
      if (docType === this.DocTypeShishiFangan) {
        this.basicdocList3 = fileList;
      }
    },

    // 组件方法
    selectSupplierData(supplier) {
      console.log("selectSupplierData is ", supplier);
      this.budgetpayForm.supplierid = undefined;
      this.budgetpayForm.supplierinfo = {supplierid: undefined};

      if (supplier) {
        this.budgetpayForm.suppliername = supplier.suppliername;
        this.budgetpayForm.person1name = supplier.person1name;
        this.budgetpayForm.bankname = supplier.bankname;
        this.budgetpayForm.banknumber = supplier.banknumber;

        const this_ = this;
        let queryParams = {
          pageNum: 1,
          pageSize: 10,
          sheettype: "拨付单",
          projectid: this.form.projectid,
          supplieridList: [supplier.supplierid]
        };
        getSheetBudgetpayRecord(queryParams).then(response => {
          const budgetpay = {
            payid: undefined,
            supplierid: supplier.supplierid,
            supplierinfo:{supplierid: supplier.supplierid},
            suppliername: supplier.suppliername,
            person1name: supplier.person1name,
            bankname: supplier.bankname,
            banknumber: supplier.banknumber,
            zong: this_.budgetpayForm.zong,
            xiaoji: this_.budgetpayForm.xiaoji,
            yiqian: this_.budgetpayForm.yiqian,
            bennian: this_.budgetpayForm.bennian,
            benci: this_.budgetpayForm.benci,
            getIfFirstPayed: false
          };

          console.log("getSheetBudgetpayRecord is ", response.rows);
          const budgetpayRecordList = response.rows;
          if (budgetpayRecordList.length > 0) {
            budgetpay.yiqian = 0.0;
            budgetpay.bennian = 0.0;
            budgetpay.xiaoji = 0.0;

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

          this_.budgetpayForm = budgetpay;
          console.log("change supplier when this.budgetpayForm is ", this_.budgetpayForm);
        });
      }
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

          if (this.form.budgetpayList.length === 0) {
            this.msgError("未选择项目协作单位");
            return;
          }

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
                  this_.msgSuccess("提交审核成功");
                  this_.form.sheetid = response.data;
                }
                this_.closeForm();
              });
            } else {
              updateSheet(this_.form).then(result => {
                this_.msgSuccess("提交审核成功");
                this_.closeForm();
              });

            }
          }
        }
      });
    },

    /** 编辑模式按钮 */
    changeForm() {
      const sheetid = this.form.sheetid;
      this.$confirm('是否确认删除"' + this.form.projectname + '"的拨付单,并 重新生成新的拨付单?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        deleteSheet(sheetid).then((result) => {
          console.log("result is ", result);
          this.form.sheetstatus = this.SheetStatus.XinJianZhong;
          this.form.sheetstatuslinktext = "新建中";
          this.form.sheetid = undefined;
          this.opcode = "add";
          this.configTemplateStatus();
        })
      });
    },

    confirmForm() {
      if (this.form.confirmResult == undefined) {
        this.msgError("请选择审批结果");
        return;
      }

      const this_ = this;

      this_.form.confirmUserid = this_.$store.getters.userId;
      const result = this_.form.confirmResult;

      if (result === 1) {
        this_.$confirm('是否确认审批 通过?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          console.log("audit opcode is " + this_.opcode);
          this_.loading = true;
          confirmAuditSheet(this_.form, this_.opcode).then(result => {
            console.log("audit opcode is " + this_.opcode + " result is ", result);
            if (result.code === 200) {
              this_.closeForm();
              this_.msgSuccess(result.msg);
            } else {
              this_.msgSuccess(result.msg);
            }
            this_.loading = false;

          });

        });
      } else if (result === 2) {

        const note = this_.form.confirmNote;
        console.log("confirmNote is ", note);
        if (note !== null && note !== undefined && note.trim() !== '') {
          this_.$confirm('是否确认审批 不通过?', "警告", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(function () {

            confirmAuditSheet(this_.form, this_.opcode).then(result => {
              this_.closeForm();
              this_.msgSuccess("审批不通过 完成");
            });
          });
        } else {
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
    },

    handleBudgetpayUpdate(row) {
      this.budgetpayFormOpen = true;
      this.budgetpayFormTitle = "编辑项目协作单位";
      const budgetpay = {
        payid: row.payid,
        supplierid: row.supplierid,
        supplierinfo: {supplierid: row.supplierid},
        zong: parseFloat(row.zong),
        xiaoji: parseFloat(row.xiaoji),
        yiqian: parseFloat(row.yiqian),
        bennian: parseFloat(row.bennian),
        benci: parseFloat(row.benci),
        getIfFirstPayed: false
      };

      this.budgetpayForm = budgetpay;

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
            // 更新 历史记录
            this_.queryRecordParams.projectid = this_.form.projectid;

            let ids = new Array();
            for (let k = 0; k < this_.form.budgetpayList.length; k++) {
              let item = this_.form.budgetpayList[k];
              ids.push(item.supplierid);
            }
            this_.queryRecordParams.supplieridList = ids;

            console.log("getSheetBudgetpay record is ", this_.queryRecordParams);
            this_.getSheetBudgetpayRecordList(this_.form);
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
            // 更新 历史记录
            this_.queryRecordParams.projectid = this_.form.projectid;

            let ids = new Array();
            for (let k = 0; k < this_.form.budgetpayList.length; k++) {
              let item = this_.form.budgetpayList[k];
              ids.push(item.supplierid);
            }
            this_.queryRecordParams.supplieridList = ids;

            console.log("getSheetBudgetpay record is ", this_.queryRecordParams);
            this_.getSheetBudgetpayRecordList(this_.form);
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
        supplierinfo: {supplierid: undefined},
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
