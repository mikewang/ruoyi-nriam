<template>
  <div class="app-container">

    <el-row :gutter="20">
      <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="160px" :key="timer">
        <template>
          <el-row >
            <el-col :span="8" v-bind:hidden="hidden.acceptance">
              <el-form-item label="合同编号" prop="fourtechcode">
                <el-input v-model="form.fourtechcode"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目名称" prop="fourtechname">
                <el-input  v-model="form.fourtechname"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="合同类型" prop="fourtechtype">
                <el-select v-bind:readonly="readonly.basic" v-model="form.fourtechtype" placeholder="请选择合同类型" style="display:block;"
                           @change="changeFourtechtypeValue">
                  <el-option
                    v-for="item in fourtechtypeOptions"
                    :key="item"
                    :label="item"
                    :value="item"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目负责人" prop="managerid">
                <el-select v-bind:readonly="readonly.basic" v-model="form.managerid" placeholder="请选择项目负责人" style="display:block;"
                           clearable @clear="clearManagerValue" @change="changeManagerValue"
                           filterable :filter-method="filterManagerOptions">
                  <el-option
                    v-for="item in userOptions"
                    :key="item.id"
                    :label="item.value"
                    :value="item.id"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所属部门" prop="organizationid">
                <el-select v-bind:readonly="readonly.basic" v-model="form.organizationid" placeholder="请选择所属部门" style="display:block;"
                           clearable @clear="clearDeptValue" @change="changeDeptValue"
                           filterable :filter-method="filterDeptOptions" :show-overflow-tooltip="true">
                  <el-option
                    v-for="item in deptOptions"
                    :key="item.id"
                    :label="item.value"
                    :value="item.id"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所属团队" prop="teamid">
                <el-select v-bind:readonly="readonly.basic" v-model="form.teamid" placeholder="请选择所属团队" style="display:block;"
                           clearable @clear="clearTeamValue" @change="changeTeamValue"
                           filterable :filter-method="filterTeamOptions">
                  <el-option
                    v-for="item in teamOptions"
                    :key="item.id"
                    :label="item.value"
                    :value="item.id"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="合作单位" prop="coperationunit">
                <el-input  v-model="form.coperationunit"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="合同金额（元）" prop="fourtechmoney">
                <el-input  v-model="form.fourtechmoney"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="大写金额" prop="daxie">
                <el-input v-model="form.daxie" disabled/>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="项目开始日期" prop="projectbegindate">
                <el-date-picker v-bind:readonly="readonly.basic" v-model="form.begindate" type="date" placeholder="请选择日期"
                                value-format="yyyy-MM-dd"
                                style="display:block;"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目结束日期" prop="enddate">
                <el-date-picker v-bind:readonly="readonly.basic" v-model="form.enddate" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd"
                                style="display:block;"></el-date-picker>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="经办人" prop="sheetuseridlinktext">
                <el-input v-model="form.sheetuseridlinktext" disabled/>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="合同状态" prop="sheetstatuslinktext">
                <el-input v-model="form.sheetstatuslinktext" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="经办人签名" prop="sheetuseridlinktext">
                <img :src="form.sheetuseridImage" min-width="120" height="60"/>
                <span>  {{ form.sheettime }}</span>
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

        </template>

        <template>
          <el-row v-bind:hidden="form.fourtechid === undefined">
            <el-col :span="16">
              <el-form-item label="上传合同正文" prop="contractuploadfileList">
                <el-upload action="#" :http-request="requestUploadDoc" :before-remove="beforeRemoveDoc"
                           :on-remove="handleUploadRemoveDoc" :on-preview="handleReviewDoc"
                           :file-list="contractuploadfileList" :before-upload="beforeUploadDoc"
                >
                  <el-button size="small">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>

                <a v-if="form.iftemplate" @click="downloadContractdocTemplate">下载合同模板</a>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-checkbox v-model="form.iftemplate" @change="changeIftemplate">上传非模板的合同（word和pdf）</el-checkbox>
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

        <template v-if="this.hidden.acceptance == false">
          <el-row>
            <el-col :span="24">
              <el-form-item label="" prop="sheetstatuslinktext">
                <el-button @click="clickContractdoc">合同正文</el-button>
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

          <el-button @click="closeForm">取 消</el-button>
        </el-col>
      </el-row>


    </el-row>


    <!-- 添加或修改菜单对话框 -->


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
  getSheetSupplier,
  updateSheet,
  confirmAuditSheet
} from "@/api/sheet/sheet";
import {addFourtech, updateFourtech, getFourtech,downloadTemplateDoc, uploadFile,listContractdoc} from "@/api/fourtech/fourtech";

import {handleUploadReview} from "@/api/achieve/basdoc";
import {getSignpic} from "@/api/audit/signpic"
import {listUser} from "@/api/system/user";
import {listDept} from "@/api/system/dept";
import {listTeam} from "@/api/project/team";
import {listContractPaysheet} from "@/api/sheet/contract";


export default {
  name: "fourtech_tijiaoren_edit",
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

      fourtechtypeOptions: ["技术开发合同","技术转让合同","技术服务合同","技术咨询合同"],
      fourtechtypeList: [],

      userOptions: [],
      userList: [],

      deptOptions: [],
      deptList: [],

      teamOptions: [],
      teamList: [],

      contractuploadfileList: [],

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
      form: {iftemplate:true},
      timer: '',
      // 表单校验
      rules: {
        fourtechname: [
          {required: true, message: "项目名称不能为空", trigger: "blur"}
        ],
        fourtechtype: [
          {required: true, message: "合同类型不能为空", trigger: "blur"}
        ],
        managerid: [
          {required: true, message: "项目负责人不能为空", trigger: "blur"}
        ],
        organizationid: [
          {required: true, message: "所属部门不能为空", trigger: "blur"}
        ],
        teamid: [
          {required: true, message: "所属团队不能为空", trigger: "blur"}
        ]
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
        this.loadAllOptions();
        this.loading = false;

      } else {
        const this_ = this;

        getFourtech(sheetid).then(response => {

          console.log("getFourtech response data is ", response.data);

          const contract = response.data;

          listContractdoc({contractid: contract.fourtechid}).then(response => {

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
              contract.sheetuseridImage = response.data.signpicName;

              let auditRecord = {sheettype: "四技合同", sheetid: contract.contractid};

              getSheetAuditRecord(auditRecord).then(response => {
                console.log("getSheetAuditRecord response data is ", response.data);
                contract.sheetAuditRecordList = response.data;

                this_.form = contract;

                this_.loadAllOptions();
              });
            });
          });







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
          this.hidden.saveBtn = false;
        }
        else if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.changeBtn = false;
          this.hidden.deleteBtn = false;
          this.hidden.submitBtn = false;
        }
      }
      else if (this.form.sheetstatus === this.SheetStatus.XiangMuShenPi) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        } else if (this.opcode.indexOf("audit") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm = false;
          this.hidden.confirmBtn = false;
        }
      }
      else if (this.form.sheetstatus === this.SheetStatus.BuMenShenPi) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        } else if (this.opcode.indexOf("audit") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm = false;
          this.hidden.confirmBtn = false;
        }
      }
      else if (this.form.sheetstatus === this.SheetStatus.ChuShenPi) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        } else if (this.opcode.indexOf("audit") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm = false;
          this.hidden.confirmBtn = false;
        }
      }
      else if (this.form.sheetstatus === this.SheetStatus.FenGuanSuoShenPi) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        } else if (this.opcode.indexOf("audit") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm = false;
          this.hidden.confirmBtn = false;
        }
      }
      else if (this.form.sheetstatus === this.SheetStatus.SuoZhangShenPi) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        }
        else if (this.opcode.indexOf("audit") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm = false;
          this.hidden.confirmBtn = false;
        }
      }
      else if (this.form.sheetstatus === this.SheetStatus.ShenPiWanCheng) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        }
      }
      else if (this.form.sheetstatus === this.SheetStatus.YiZuoFei) {
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

        }
      }
      else if (this.form.sheetstatus === this.SheetStatus.NoPass) {
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
        fourtechid: undefined,
        fourtechcode: undefined,
        fourtechname: undefined,

        fourtechtype: undefined,
        managerid: undefined,
        sheetuserid: undefined,
        sheettime: undefined,
        organizationid: undefined,
        teamid: undefined,
        fourtechmoney: undefined,
        daxie: undefined,
        coperationunit: undefined,
        begindate: undefined,

        enddate: undefined,

        passtime: undefined,
        iftemplate:true,

        sheetstatus: this.SheetStatus.XinJianZhong,
        sheetstatuslinktext: "新建中",
        sheetuseridlinktext: undefined,

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

    changeFourtechtypeValue(value) {
      console.log("changeFourtechtypeValue value is " + value);
      if (value) {
        this.form.fourtechtype = value;
      }
    },

    loadAllOptions() {
      let listOptions = [];
      listDept().then(response => {
        console.log(response);

        response.data.forEach(function (item) {
          const dept = {value: item.deptName, id: item.deptId};
          listOptions.push(dept);
        });
        this.deptList = listOptions;
        this.deptOptions = listOptions;

        listOptions = [];
        listTeam().then(response => {
          console.log(response);
          response.rows.forEach(function (item) {
            const team = {value: item.teamname, id: item.teamid};
            listOptions.push(team);
          });
          this.teamList = listOptions;
          this.teamOptions = listOptions;

          listOptions = [];
          listUser().then(response => {
            console.log("listUser is ", response);
            response.rows.forEach(function (item) {
              //console.log("item is ", item);
              const user = {value: item.realName, id: item.userId, hotKey: item.hotKey};
              //console.log(user);
              listOptions.push(user);
            });
            this.userList = listOptions;
            this.userOptions = listOptions;

            console.log("this.form is ", this.form);
            this.configTemplateStatus();

            this.loading = false;

          });
        });
      });
    },

    clearManagerValue() {


    },

    changeManagerValue(value) {

      if (value) {

        this.form.projectmanagerid = value;
      } else {
        this.form.projectmanagerid = undefined;
      }

    },

    filterManagerOptions(v) {

      console.log("filterManagerOptions value is " + v);

      if (v) {
        this.userOptions = this.userList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          const py = item.hotKey;
          var hh = -1;
          if (py !== undefined && py !== null) {
            hh = py.indexOf(val);
          }

          if (item.value.indexOf(val) !== -1 || hh !== -1) return true

        });
      } else {
        this.userOptions = this.userList;
      }
    },
    clearDeptValue() {

    },
    changeDeptValue(value) {
      if (value) {
        this.form.organizationid = value;
      } else {
        this.form.organizationid = undefined;
      }
    },

    filterDeptOptions(v) {
      console.log("filter value is " + v);
      if (v) {
        this.deptOptions = this.deptList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.deptOptions = this.deptList;
      }
    },

    clearTeamValue() {


    },

    changeTeamValue(value) {

      if (value) {

        this.form.teamid = value;
      } else {
        this.form.teamid = undefined;
      }
    },

    filterTeamOptions(v) {

      console.log("filter value is " + v);

      if (v) {
        this.teamOptions = this.teamList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.teamOptions = this.teamList;
      }
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

    changeIftemplate(value) {

      console.log("changeIftemplate is ", value);

    },


    printContractdoc() {
      this.msgError("开发中，打印合同正文。");
    },

    clickContractdoc() {
      this.msgError("开发中，浏览合同正文。");
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
    saveForm: function () {

      const this_ = this;

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("saveForm   is ", this.form);

          if (this_.opcode === "add" || this_.opcode === "query") {
            // 处理掉添加， 为了更新或修改。

            if (this_.form.fourtechid === undefined) {
              addFourtech(this.form).then(response => {
                if (response.data === 0) {
                  this_.msgError("保存合同失败");
                  this_.form.fourtechid = undefined;
                } else {
                  this.msgSuccess("保存合同成功");
                  this.form.fourtechid = response.data;
                  this.opcode = "query";
                  this.configTemplateStatus();
                }

              });
            } else {
              updateFourtech(this.form).then(result => {
                this.msgSuccess("保存合同成功");
              });

            }
          }
        }
      });
    },

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
                this.closeForm();
              });
            } else {
              updateSheet(this.form).then(result => {
                this.msgSuccess("提交审核成功");
                this.closeForm();
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
        deleteSheet(sheetid).then((result ) => {
          console.log("result is " , result);
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
            console.log("audit opcode is " + this_.opcode + " result is " , result);
            if (result.code === 200) {
              this_.closeForm();
              this_.msgSuccess(result.msg);
            }
            else {
              this_.msgSuccess(result.msg);
            }
            this_.loading = false;

          });

        });
      }
      else if (result === 2) {

        const note = this_.form.confirmNote;
        console.log("confirmNote is ", note);
        if (note !== null && note !== undefined && note.trim() !== '' ) {
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
