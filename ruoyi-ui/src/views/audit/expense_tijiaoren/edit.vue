<template>
  <div class="app-container">

    <el-row :gutter="20">
      <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="160px" :key="timer">
        <template>
          <el-row>
            <el-col :span="16">
              <el-form-item label="经费使用名称" prop="expensename">
                <el-input v-model="form.expensename"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="经费单编号" prop="expensesheetcode">
                <el-input readonly v-model="form.expensesheetcode"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
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
            <el-col :span="8">
              <el-form-item label="课题编号" prop="subjectcode">
                <el-input readonly v-model="form.projectinfo.subjectcode"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>

            <el-col :span="8">
              <el-form-item label="所属项目" prop="projectid">
                <!-- 所属项目组件-->
                <project-data :readonly="readonly.basic" :selected-project-data="form.projectinfo"
                              @changeProjectData="selectProjectData" :selected-option="projectSelectedOption"
                              :key="projectid"></project-data>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所属部门" prop="organizationidlinktext">
                <el-input readonly v-model="form.projectinfo.organizationidlinktext" :show-overflow-tooltip="true"
                          :key="form.projectid"/>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="项目周期" prop="projectDateRange">
                <el-input readonly v-model="form.projectinfo.projectDateRange"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目编号" prop="projectcode">
                <el-input readonly v-model="form.projectinfo.projectcode"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目负责人" prop="projectmanageridlinktext">
                <el-input readonly v-model="form.projectinfo.projectmanageridlinktext"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目类型" prop="projecttypelinktext">
                <el-input readonly v-model="form.projectinfo.projecttypelinktext"/>
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
                             :doc-list="basicfileList2" :readonly="true" :key="basicfileList1Key"></project-doc>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="实施方案" prop="basicfileList3">
                <project-doc :doc-type="DocTypeShishiFangan" @changeFileList="changeBasicDocList"
                             :doc-list="basicfileList3" :readonly="true" :key="basicfileList1Key"></project-doc>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="金额（元）" prop="money">
                <el-input v-model="form.money" type="number"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="大写金额" prop="daxie">
                <el-input readonly v-model="form.daxie" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="经费单状态" prop="sheetstatuslinktext">
                <el-input readonly v-model="form.sheetstatuslinktext" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item label="事由说明" prop="reason">
                <el-input v-bind:readonly="readonly.basic" v-model="form.reason" placeholder="" type="textarea"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
            <el-form-item label="发票单据数量" prop="vouchercount">
              <el-input v-model="form.vouchercount" type="number"/>
            </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="16">

              <el-form-item label="发票扫描件" prop="expenseFileList">
                <bas-doc :basdoc="basDocExpense" :readonly="readonly.basic" @changeFileList="changeExpenseFileList" :key="basDocExpense.relatedid" ></bas-doc>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template>

        </template>

        <template>
          <el-row v-bind:hidden="hidden.acceptance">
            <el-col :span="8">
              <el-form-item label="合同状态" prop="sheetstatuslinktext">
                <el-input v-model="form.sheetstatuslinktext" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="经办人签名" prop="contractuseridlinktext">
                <span>{{ form.OPINION_JINGBAN }}</span>
                <br/>
                <img :src="form.sheetuseridImage" min-width="120" height="60"/>
                <span>  {{ form.contracttime }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-divider></el-divider>
          </el-row>
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
          <el-row v-if="this.hidden.confirm == false">
            <el-form-item label="审批意见" prop="confirmResult">
              <el-row>
                <el-col :span="16">
                  <template>
                    <span>审批结果 </span>
                    <el-radio-group v-model="form.confirmResult" @change="changeConfirmResult">
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
          <el-row v-bind:hidden="hidden.applyDelete">
            <el-col :span="16">
              <el-form-item label="申请作废理由" prop="applyDeleteReason">
                <el-input v-model="form.applyDeleteReason" type="textarea" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="申请时间" prop="applyDeleteTime">
                <span>{{ form.applyDeleteTime }}</span>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template v-if="this.hidden.acceptance == false">
          <el-row>
            <el-col :span="24">
              <el-form-item label="" prop="sheetstatuslinktext">
                <el-button @click="clickContractdoc">合同正文</el-button>
                <span v-for="(item,index) in form.paysheetList">
                <el-button @click="clickPaysheet(item)">拨付单{{ index + 1 }}</el-button>
               </span>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

      </el-form>

      <el-row>
        <el-col :span="24" align="center">
          <el-button v-if="hidden.submitBtn === false" type="primary" @click="submitForm">提交审批</el-button>
          <el-button v-if="hidden.saveBtn === false" type="success" @click="saveForm">保存合同信息</el-button>
          <el-button v-if="hidden.confirmBtn === false" type="success" @click="confirmForm">确定审批</el-button>
          <el-button v-if="hidden.deleteBtn === false" type="danger" @click="deleteForm">删除</el-button>

          <el-button v-if="hidden.changeBtn === false" type="primary" @click="changeForm">修改后提交</el-button>
          <el-button v-if="hidden.printBtn === false" type="primary" @click="printContractdoc">打印合同正文</el-button>
          <el-button v-if="hidden.downloadprintBtn === false" type="primary" @click="printContractdoc">下载合同打印</el-button>
          <el-button v-if="hidden.nopassdeleteBtn === false" type="warning" @click="nopassDeleteApply">不同意</el-button>
          <el-button v-if="hidden.confirmdeleteBtn === false" type="primary" @click="confirmDeleteApply">确认作废</el-button>
          <el-button @click="closeForm">取 消</el-button>
        </el-col>
      </el-row>
    </el-row>


    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>

      <div slot="footer" class="dialog-footer">
        <el-button> 打印</el-button>
        <!--        <el-button type="primary" @click="submitJoinForm">确 定</el-button>-->
        <!--        <el-button @click="cancelJoinForm">取 消</el-button>-->
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listProjectdoc} from "@/api/project/project";

import ProjectData from "@/views/public/project-data";
import ProjectDoc from "@/views/public/project-doc";
import DictData from "@/views/public/dict-data";
import SupplierData from "@/views/public/supplier-data";
import BasDoc from "@/views/public/bas-doc";

import {deleteSheet, getSheetAuditRecord, getSheetBudgetpayRecord} from "@/api/audit/audit";
import {getSignpic} from "@/api/audit/signpic"
import {
  addContract,
  confirmAuditContract,
  deleteContract,
  downloadTemplateDoc,
  getContract,
  getContractConfirmNote,
  getContractdoc,
  listContractdoc,
  listContractPaysheet,
  submitContract,
  updateContract,
  uploadFile,
  confirmDeleteApplyContract,
  nopassDeleteApplyContract
} from "@/api/audit/contract"


export default {
  name: "expense_tijiaoren_edit",
  components: {BasDoc, ProjectData, ProjectDoc, DictData, SupplierData},
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
      contractdocHtml: '',

      // 数据字典  项目类型
      DictTypeNameContractType: "合同类型",
      projectSelectedOption: "aftersetup",

      // 数据字典
      DocTypeXiangmuShenbaoShu: "项目申报书",
      DocTypeXiangmuHetong: "项目合同",
      DocTypeShishiFangan: "实施方案",

      projectid: 0,
      supplierid: 0,

      // 数据字典
      basicfileList1: [],
      basicfileList2: [],
      basicfileList3: [],

      basicfileList1Key: '',

      basDocExpense: {relatedid: -2, attachtotype: "小额经费单", doctype: "发票扫描件"},

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
      form: {projectinfo: {projectid: undefined},docList:[]},
      timer: '',
      // 表单校验
      rules: {
        expensename: [
          {required: true, message: "经费使用名称不能为空", trigger: "blur"}
        ],
        projectid: [
          {required: true, message: "所属项目不能为空", trigger: "blur"}
        ],
        money: [
          {required: true, message: "金额不能为空", trigger: "blur"}
        ],
        reason: [
          {required: true, message: "主要协作内容不能为空", trigger: "blur"}
        ],
        vouchercount: [
          {required: true, message: "发票单据数量", trigger: "blur"}
        ],
      }
    };

  },

  mounted() {
    console.log("mounted this.$route.meta is ", this.$route.meta);
  },

  beforeCreate() {
    console.log(" beforeCreate this.$route.meta is ", this.$route.meta);
    const contractid = this.$route.params && this.$route.params.sheetid;

  },
  created() {
    console.log(" created this.$route.meta is ", this.$route.meta);
    this.resetTemplateStatus();
    console.log(" created this.$route.params is ", this.$route.params);

    let expensesheetid = this.$route.params && this.$route.params.sheetid;
    if (expensesheetid === undefined || Number(expensesheetid) === 0) {
      expensesheetid = undefined;
    } else {

    }

    this.opcode = this.$route.meta.opcode;

    this.getData(expensesheetid);

  },
  methods: {
    /** 查询信息 */
    getData(expensesheetid) {

      this.loading = true;
      console.log("loading is begin, expensesheetid is ", expensesheetid);

      if (expensesheetid === undefined) {
        this.reset();
        this.configTemplateStatus();
        this.loading = false;
      } else {
        const this_ = this;
        this.reset();
        getContract(expensesheetid).then(response => {

          console.log("getContract response data is ", response.data);

          const contract = response.data;

          contract.contracttype = contract.contracttype.toString();

          this_.form = contract;
          this_.projectid = contract.projectid;
          this_.supplierid = contract.supplierid;

          this_.configTemplateStatus();

          this_.loading = false;


        });
      }

    },

    changePaytotaltimes(value) {
      console.log("changePaytotaltimes is ", value);
      let contractpayList = [];
      if (value > 0) {
        let i = 1
        while (i <= value) {
          let pay = {times: i, timesname: '第' + i + '期金额', percentmoney: undefined};
          contractpayList.push(pay);
          i++;
        }
      }
      this.form.contractpayList = contractpayList;
    },

    changeConfirmResult(value) {
      console.log("changeConfirmResult is ", value);
      if (value === 1) {
        this.form.confirmNote = getContractConfirmNote(this.opcode);
      } else {

        this.form.confirmNote = "";
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
        applyDelete: true,
        saveBtn: true,
        changeBtn: true,
        deleteBtn: true,
        submitBtn: true,
        changeAcceptanceBtn: true,
        returnBtn: true,
        confirmBtn: true,
        printBtn: true,
        downloadprintBtn: true,
        nopassdeleteBtn: true,
        confirmdeleteBtn: true
      };
    },

    configTemplateStatus() {
      this.resetTemplateStatus();
      console.log("configTemplateStatus is ", this.form.sheetstatus);
      if (this.form.sheetstatus === this.SheetStatus.XinJianZhong) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("add") !== -1) {
          this.readonly.basic = false;
          this.hidden.saveBtn = false;
        } else if (this.opcode.indexOf("query") !== -1) {
          this.readonly.basic = false;
          this.hidden.saveBtn = false;
          this.hidden.submitBtn = false;
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
      }
      else if (this.form.sheetstatus === this.SheetStatus.ShenQingZuoFei) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        } else if (this.opcode.indexOf("audit") !== -1) {
          this.hidden.acceptance = false;
          this.readonly.confirm = false;
          this.hidden.applyDelete = false;
          this.hidden.nopassdeleteBtn = false;
          this.hidden.confirmdeleteBtn = false;
        }
      }
      else if (this.form.sheetstatus === this.SheetStatus.YiZuoFei) {
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
        contractid: undefined,
        contractcode: undefined,
        contractname: undefined,
        contracttype: undefined,
        contracttypelinktext: undefined,

        projectid: undefined, // 为了rules校验。
        projectinfo: {projectid: undefined},
        supplierid: undefined,
        supplierinfo: {supplierid: undefined},

        paytotaltimes: 2,
        contractpayList: [],
        reason: undefined,

        sheetuserid: undefined,
        sheetuseridlinktext: undefined,
        sheettime: undefined,
        sheetstatus: this.SheetStatus.XinJianZhong,
        sheetstatuslinktext: "新建中",

        docList: [],

        sheetuseridImage: undefined,
        sheetAuditRecordList: []

      };

      this.changePaytotaltimes(this.form.paytotaltimes);
      this.form.contractuseridlinktext = this.$store.getters.realName
      this.resetForm("form");
    },

    // 组件方法
    changeFormDictType(dict) {

      if (dict.type === this.DictTypeNameContractType) {
        console.log("changeFormDictType is ", dict);
        if (dict) {
          this.form.contracttype = dict.id;
        } else {
          this.form.contracttype = undefined;
        }
      } else {
        console.error("changeFormDictType  意外 is ", dict);
      }
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

          this_.basicfileList1Key = project.projectid;

        });


      } else {
        this.form.projectid = undefined;
        this.form.projectinfo = {projectid: undefined};
      }

    },
    // 组件方法
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

    /* 发票扫描件 */

    changeExpenseFileList(filelist) {
      if (this.form.docList == null) {
        this.form.docList = [];
      }
      console.log("发票扫描件 is ", filelist.length, this.form.docList.length);

      let doctype = "发票扫描件";

      for (let i=0; i < this.form.docList.length; i++) {
        let doc = this.form.docList[i];
        if (doc.doctype === doctype) {
          this.form.docList.splice(i,1);
        }
      }

      for (let j=0; j < filelist.length; j++) {
        let file = filelist[j];
        let doc = {docid: file.url, doctype: doctype};
        console.log("this docList push is ",this.form.docList, file, doc);

        this.form.docList.push(doc);
      }

    },

    // 组件方法
    selectSupplierData(supplier) {
      console.log("selectSupplierData is ", supplier);
      this.form.supplierid = undefined;
      this.form.supplierinfo = {supplierid: undefined};

      const this_ = this;

      if (supplier) {
        this_.form.supplierid = supplier.supplierid;
        this_.form.supplierinfo = supplier;
        this_.supplierid = supplier.supplierid;

        let contract = this_.form;

        listContractdoc({contractid: contract.contractid}).then(response => {

          console.log("listContractdoc is ", response.data);

          if (response.data !== null) {
            contract.contractdocList = response.data;
          } else {
            contract.contractdocList = [];
          }

          const doclist = [];

          for (let i = 0; i < contract.contractdocList.length; i++) {
            let item = contract.contractdocList[i];
            doclist.push({"name": item.docname, "url": item.docid});
          }

          this_.contractuploadfileList = doclist;

          getSignpic(contract.contractuserid).then(response => {
            console.log("getSignpic response is ", response);
            contract.OPINION_JINGBAN = "经与项目负责人和乙方负责人沟通，双方同意签订本合同。合同内容真实。";
            contract.sheetuseridImage = response.data.signpicName;

            let auditRecord = {sheettype: "合同", sheetid: contract.contractid};

            getSheetAuditRecord(auditRecord).then(response => {
              console.log("getSheetAuditRecord response data is ", response.data);
              contract.sheetAuditRecordList = response.data;

              // 得到所有的合同拨付单

              //读取合同对应的拨付单并绑定到repeat控件
              // DataSet paysheetds = im.GetPaySheetOfAContract(lab_ContractID.Text , false);
              // rpt_PaySheet.DataSource = paysheetds.Tables[0];
              // rpt_PaySheet.DataBind();
              // paysheetds.Dispose();
              listContractPaysheet(contract.contractid).then(response => {

                console.log("listContractPaysheet 合同拨付单 is ", response.data);

                contract.paysheetList = response.data;

                this_.projectid = contract.projectid;

                this_.loading = false;
              });
            });
          });
        });

      } else {
        this.supplierid = undefined;
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

      const contractid = this.form.contractid;

      this.$confirm('是否确认删除"' + this.form.contractname + '"?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        deleteContract(contractid).then(response => {
          this.msgSuccess("删除成功");
          this.closeForm();
        });
      });
    },

    saveForm() {
      const this_ = this;

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("save form is ", this.form);
          this_.form.operateCode = 2; // 提交审核代码 提交给后端。

          if (this_.opcode === "add" || this_.opcode === "query") {
            // 处理掉添加， 为了更新或修改。
            if (this_.form.contractid === undefined) {
              addContract(this.form).then(response => {
                console.log("response is ", response);
                if (response.data === 0) {
                  this_.msgError("保存失败");
                  this_.form.contractid = undefined;
                } else {
                  this.msgSuccess("保存成功" + response.data);
                  this.form.contractid = response.data;
                  this.opcode = "query";
                  this.configTemplateStatus();
                }
                //this.closeForm();
              });
            } else {
              updateContract(this_.form).then(result => {
                this_.msgSuccess("修改成功");
                //   this.closeForm();
              });

            }
          }
        }
      });


    },

    /*提交 审核 按钮*/
    submitForm: function () {

      const this_ = this;

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          this_.form.operateCode = 2; // 提交审核代码 提交给后端。

          if (this_.form.sheetstatus === 17 && this_.form.contractid !== undefined) {
            // 处理掉添加， 为了更新或修改。

            submitContract(this.form).then(result => {
              this.msgSuccess("提交审核成功");
              this.closeForm();
            });
          }
        }
      });
    },

    /** 编辑模式按钮 */
    changeForm() {
      const sheetid = this.form.sheetid;
      this.$confirm('是否确认删除"' + this.form.contractname + '"的拨付单,并 重新生成新的拨付单?', "警告", {
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

          confirmAuditContract(this_.form, this_.opcode).then(result => {
            console.log("audit opcode is " + this_.opcode + " result is ", result);
            if (result.code === 200) {
              this_.msgSuccess(result.msg);
              this_.closeForm();
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

            confirmAuditContract(this_.form, this_.opcode).then(result => {
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

    printContractdoc() {
      this.msgError("开发中，打印合同正文。");
    },

    nopassDeleteApply() {
      const this_ = this;
      this.$confirm('是否确认不同意?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {

        nopassDeleteApplyContract(this_.form, this_.opcode).then(result => {
          this_.closeForm();
          this_.msgSuccess("操作 完成");
        });
      });
    },

    confirmDeleteApply() {
      const this_ = this;
      this.$confirm('是否确认作废?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {

        confirmDeleteApplyContract(this_.form, this_.opcode).then(result => {
          this_.closeForm();
          this_.msgSuccess("操作 完成");
        });
      });
    },

    clickContractdoc() {
      this.open = true;
      getContractdoc(this.form.contractid).then(response => {
        console.log(response);
        this.contractdocHtml = response.data;
      });
    },

    clickPaysheet(item) {
      // 合同拨付单
      let sheetid = item.sheetid;
      const path = '/contract/paysheet/' + sheetid;
      console.log("path is " + path);
      this.$router.push({path: path});
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
            // 更新 历史记录
            this_.queryRecordParams.projectid = this_.form.projectid;

            let ids = new Array();
            for (let k = 0; k < this_.form.budgetpayList.length; k++) {
              let item = this_.form.budgetpayList[k];
              ids.push(item.supplierid);
            }
            this_.queryRecordParams.supplieridList = ids;

            console.log("getSheetBudgetpay record is ", this_.queryRecordParams);
            this_.getSheetBudgetpayRecordList();
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
            this_.getSheetBudgetpayRecordList();
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
a:hover {
  cursor: pointer
}
</style>
