<template>
  <div class="app-container">

    <el-row :gutter="20">
      <h2 style="text-align:center">农业部南京农业机械化研究所外拨款审批运转单</h2>
    </el-row>
    <el-row :gutter="20">
      <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="160px" :key="timer">
        <template>
          <el-row>
            <el-col :span="16">
              <el-form-item label="拨付单号" prop="sheetcode">
                <el-input readonly v-model="form.sheetcode"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="经办人" prop="sheetuserid">
                <img :src="form.sheetuseridImage" min-width="120" height="60"/>
              </el-form-item>
            </el-col>

          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item readonly label="合同名称" prop="contractname">
                <el-input readonly v-model="form.contractinfo.contractname"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="合同编号" prop="contractcode">
                <el-input readonly v-model="form.contractinfo.contractcode"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item label="所属部门" prop="organizationIDLinkText">
                <el-input readonly v-model="form.projectinfo.organizationIDLinkText"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目负责人" prop="projectManagerIDLinkText">
                <el-input readonly v-model="form.projectinfo.projectManagerIDLinkText"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item label="项目名称" prop="projectname">
                <el-input readonly v-model="form.projectinfo.projectname"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="课题编号" prop="subjectcode">
                <el-input readonly v-model="form.projectinfo.subjectcode"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-tag>外拨款情况</el-tag>
          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item label="协作单位名称" prop="supplieridlinktext">
                <el-input readonly v-model="form.contractinfo.supplieridlinktext"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="本次付款对应期数" prop="thispaytimes">
                <el-input readonly v-model="form.contractinfo.thispaytimes"/>
              </el-form-item>
            </el-col>

          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="主要协作内容" prop="reason">
                <el-input readonly v-model="form.contractinfo.reason"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="付款方式" prop="contractpayList">
                <template v-for="item in form.contractinfo.contractpayList">
                  <span>{{ item.timesname }}: {{ item.percentmoney }}</span> <br/>
                </template>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="" prop="hejiZong">
                <span>外拨款总额（{{ form.hejiZong }}）元 已付外拨款（{{ form.hejiYiqian + form.hejiBennian }}）元 本次外拨款（{{ form.hejiBenci }}）元</span>
              </el-form-item>
            </el-col>
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
          <el-row v-bind:hidden="hidden.acceptance">
            <el-col :span="24">
              <el-form-item label="" prop="firsttime">
                <span>  {{ form.firsttime }}</span>
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

          <el-button @click="closeForm">取 消</el-button>
        </el-col>
      </el-row>
    </el-row>

  </div>
</template>

<script>
import {getProject, listAftersetup, listProjectdoc} from "@/api/project/project";
import {listData} from "@/api/system/dict/data";
import {
  confirmAuditSheet,
  deleteSheet,
  getSheetAuditRecord,
  getSheetBudgetpayRecord,
  getSheetSupplier,
  getSheetSupplierById
} from "@/api/audit/audit";
import {handleUploadReview} from "@/api/achieve/basdoc";
import {getSignpic} from "@/api/audit/signpic"


import {
  addContract,
  confirmContract,
  deleteContract,
  downloadTemplateDoc,
  getContract,
  getContractPaysheet,
  listContractdoc,
  updateContract,
  uploadFile
} from "@/api/audit/contract"

export default {
  name: "paysheet",
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

      contractTypeOptions: [],
      contractTypeList: [],

      basicfileList1: [],
      basicfileList2: [],
      basicfileList3: [],

      contractuploadfileList: [],

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
      form: {contractinfo:{contractname:undefined}, projectinfo: {projectname: undefined}, supplierinfo: {suppliername: undefined}},
      timer: '',
      // 表单校验
      rules: {

      }
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

    console.log("sheetid is " + sheetid);
    this.getData(sheetid);

  },
  methods: {
    /** 查询信息 */
    getData(sheetid) {

      this.loading = true;
      console.log("loading is begin, sheetid is ", sheetid);

      const this_ = this;

      getContractPaysheet(sheetid).then(response => {

        console.log("getContractPaysheet response data is ", response.data);

        const paysheet = response.data;

        console.log("paysheet thispaytimes is ", paysheet.thispaytimes);

        getSignpic(paysheet.sheetuserid).then(response => {
          console.log("getSignpic response is ", response);
          // 附加属性，为了操作方便。
          paysheet.sheetuseridImage = response.data.signpicName;

          let contractid = paysheet.relatedcontractid;

          getContract(contractid).then(response => {

            console.log("getContract response data is ", response.data);

            const contract = response.data;
            paysheet.contractinfo = contract;

            getProject(contract.projectid).then(response2 => {
              const project = response2.data;
              console.log("getProject response2 data is ", response2.data);
              paysheet.projectinfo = project;

              let auditRecord = {sheettype: "合同", sheetid: ""};

              if (paysheet.thispaytimes === "1") {
                auditRecord = {sheettype: "合同", sheetid: paysheet.contractinfo.contractid};
              } else {
                auditRecord = {sheettype: "合同拨付单", sheetid: paysheet.sheetid};
              }
              getSheetAuditRecord(auditRecord).then(response => {
                console.log("getSheetAuditRecord response data is ", response.data);
                paysheet.sheetAuditRecordList = response.data;

                if (paysheet.thispaytimes === "1" && paysheet.contractinfo.firstpaytime !== "") {
                  paysheet.firsttime = paysheet.contractinfo.firstpaytime;

                }
                else if (paysheet.thispaytimes === "1") {
                  let last_auditrecord = paysheet.sheetAuditRecordList.last;
                  paysheet.firsttime = last_auditrecord.audittime;
                }

                  // 最后赋值。
                this_.form = paysheet;

                this_.configTemplateStatus();

                this_.loading = false;
              });


            });

          });
        });


        return;

        console.log("getContract projectid is ", contract.projectid);

        getProject(contract.projectid).then(response2 => {
          const project = response2.data;
          console.log("getProject response2 data is ", response2.data);
          this_.form.projectinfo = project;

          listProjectdoc({projectid: this_.form.projectinfo.projectid}).then(response => {

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

          let supplierid = this_.form.supplierid;
          console.log("this_.form.supplierid is ", supplierid);

          getSheetSupplierById(supplierid).then(response => {

            this_.form.supplierinfo = response.data;

          });

          listContractdoc({contractid: this_.form.contractid}).then(response => {

            console.log("listContractdoc is ", response.data);
            this_.form.contractdocList = response.data;

            const doclist = [];

            for (let i = 0; i < this_.form.contractdocList.length; i++) {
              let item = this_.form.contractdocList[i];
              doclist.push({"name": item.docname, "url": item.docid});
            }

            this_.contractuploadfileList = doclist;


            getSignpic(this_.form.contractuserid).then(response => {
              console.log("getSignpic response is ", response);
              this_.form.OPINION_JINGBAN = "经与项目负责人和乙方负责人沟通，双方同意签订本合同。合同内容真实。";
              this_.form.sheetuseridImage = response.data.signpicName;

              console.log("this.form is ", this_.form);


              this_.configTemplateStatus();

              this_.loading = false;
            });

          });
        });
      });

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
        printBtn: true,
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
        contractid: undefined,
        contractcode: undefined,
        contractname: undefined,
        contracttype: undefined,
        contracttypelinktext: undefined,

        projectid: undefined, // 为了rules校验。
        projectinfo: {projectid: undefined, projectname: undefined},
        supplierid: undefined,
        supplierinfo: {suppliername: undefined},

        paytotaltimes: 2,
        contractpayList: [],
        reason: undefined,

        sheetuserid: undefined,
        sheetuseridlinktext: undefined,
        sheettime: undefined,
        sheetstatus: this.SheetStatus.XinJianZhong,
        sheetstatuslinktext: "新建中",

        projectdocList: [],
        contractdocList: [],

        sheetuseridImage: undefined,
        sheetAuditRecordList: []

      };

      this.changePaytotaltimes(this.form.paytotaltimes);
      this.form.contractuseridlinktext = this.$store.getters.realName
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

    changeContractType(value) {
      console.log("changeContractType value is " + value);
      if (value) {
        this.form.contracttype = value;
        for (let i = 0; i < this.contractTypeOptions.length; i++) {
          let item = this.contractTypeOptions[i];
          if (item.id == value) {
            this.form.contracttypelinktext = item.value;
            break;
          }
        }
      } else {
        this.form.contracttype = undefined;
        this.form.contracttypelinktext = undefined;
      }
    },


    loadContractTypeOptions() {
      listData({"dictType": "合同类型"}).then(response => {
        console.log(response);

        var listOptions = [];
        response.rows.sort(function (a, b) {
          return a.dictValue < b.dictValue
        }).forEach(function (item) {
          const contracttype = {value: item.dictLabel, id: item.dictValue};
          listOptions.push(contracttype);
        });

        this.contractTypeList = listOptions;
        this.contractTypeOptions = listOptions;

      });
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
            this.form.projectinfo = project;
            console.log("changeProject is " + this.form.projectinfo.projectname);

            listProjectdoc({projectid: this.form.projectinfo.projectid}).then(response => {
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
        this.form.projectinfo = {projectid: undefined, projectname: undefined};

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

      getSheetSupplier(queryParams).then(response => {
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

      this.form.supplierid = value;

      if (value) {

        for (let i = 0; i < this.supplierOptions.length; i++) {
          let supplier = this.supplierOptions[i];
          if (supplier.supplierid === value) {
            console.log(supplier);
            this.form.supplierinfo = supplier;
          }
        }
      } else {
        this.form.supplierinfo = {suppliername: undefined};
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


    // 上传 合同文本。

    requestUploadDoc: function (params) {

      if (this.form.contractid === undefined) {

        return this.$confirm(`合同没有保存？`);
      }
      let file = params.file;
      console.log(file, " contractid is ", this.form.contractid);
      let formData = new FormData();
      formData.append('file', file);
      formData.append("name", "");
      formData.append("attachToType", "");
      formData.append("docType", "");
      formData.append("contractid", this.form.contractid);
      uploadFile(formData).then(response => {
        console.log("response.name is ", response.name);
        console.log("response.url is ", response.url);
        this.contractuploadfileList.push({name: response.name, url: response.url});

        this.form.contractdocList.push({docid: response.url, docname: response.name});

        this.hidden.submitBtn = false;
      });
    },

    beforeRemoveDoc(file) {
      let index = this.contractuploadfileList.indexOf(file);
      console.log("beforeRemove2 index=" + index, file.name);
      return true;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleUploadRemoveDoc(file) {

      let today = new Date();

      let docid = file.url;

      let index = this.contractuploadfileList.indexOf(file);
      if (index !== -1) {
        this.contractuploadfileList.splice(index, 1);
        this.form.contractdocList.splice(index, 1);
      }

      console.log("handleUploadRemove index=" + index, file.name, today.toDateString());

    },

    handleReviewDoc(file) {

    },

    beforeUploadDoc(file) {
      if (this.form.contractid === undefined) {
        this.msgError("合同还没有保存");
        return false;
      }

      let x = true;
      console.log("fileList is ", this.contractuploadfileList);
      for (let i = 0; i < this.contractuploadfileList.length; i++) {
        let item = this.contractuploadfileList[i];
        if (item.name === file.name) {
          console.log("file existed now ,", file.name);
          x = false;
          break;
        }
      }
      return x;

    },

    downloadContractdocTemplate() {

      console.log("this.form.contractid is ", this.form.contractid);
      let contractid = this.form.contractid;
      if (contractid !== undefined) {
        downloadTemplateDoc({"contractid": contractid}).then(response => {

          console.log("response is ", response);

          var fileURL = window.URL.createObjectURL(new Blob([response]));
          var fileLink = document.createElement('a');

          fileLink.href = fileURL;
          fileLink.setAttribute('download', this.form.contractname + ".doc");
          document.body.appendChild(fileLink);

          fileLink.click();
          URL.revokeObjectURL(fileURL);

        }).catch(console.error);
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

          if (this_.opcode === "query") {
            // 处理掉添加， 为了更新或修改。

            confirmContract(this.form).then(result => {
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

    printContractdoc() {
      this.msgError("开发中，打印合同正文。");
    },

    clickPaysheet() {

      const path = '/contract/paysheet/' + this.form.contractid;
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

</style>
