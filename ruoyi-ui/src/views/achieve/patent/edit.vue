<template>
  <div class="app-container">

    <el-row :gutter="20">
      <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="160px" :key="timer"
      >
        <template>
          <el-tag size="medium" type="info">专利信息</el-tag>
          <el-row >
            <el-col :span="8">
              <el-form-item label="专利名称" prop="patentname">
                <el-input v-bind:readonly="readonly.basic" v-model="form.patentname" placeholder="请输入专利名称" :show-overflow-tooltip="true" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="专利号" prop="patentcode">
                <el-input v-bind:readonly="readonly.basic" v-model="form.patentcode" placeholder="请输入专利号"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="专利类型" prop="patenttype">
                  <el-select v-bind:readonly="readonly.basic" v-model="form.patenttype" placeholder="请选择" style="display:block;" clearable
                           @change="changePatenttypeValue">
                  <el-option
                    v-for="item in patenttypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"/>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="授权日期" prop="passtime">
                <el-date-picker v-bind:readonly="readonly.basic" v-model="form.passtime" type="date" placeholder="请选择日期"
                                value-format="yyyy-MM-dd"
                                style="display:block;"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="本所排名" prop="ourunitorder">
                <el-select v-bind:readonly="readonly.basic" v-model="form.ourunitorder" placeholder="请选择" style="display:block;" clearable
                           @change="changeOurunitorderValue">
                  <el-option
                    v-for="item in ourunitorderOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="所属团队" prop="teamid">
                <el-select v-bind:readonly="readonly.basic" v-model="form.teamname" placeholder="请选择项目所属团队" style="display:block;"
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
            <el-col :span="24">
              <el-form-item label="专利人" prop="authorList">
                <el-row :gutter="10" class="mb8" v-bind:hidden="readonly.basic">
                  <el-col :span="1.5">
                    <el-button plain
                               type="primary"
                               icon="el-icon-plus"
                               size="mini"
                               @click="handleAuthorAdd"
                               v-hasPermi="['achieve:patent:list']"
                    >新增
                    </el-button>
                  </el-col>
                  <el-col :span="1.5">
                    <el-button plain
                               v-if="1===0"
                               type="danger"
                               icon="el-icon-delete"
                               size="mini"
                               :disabled="multiple"
                               @click="handleAuthorDelete"
                               v-hasPermi="['achieve:patent:list']"
                    >删除
                    </el-button>
                  </el-col>
                </el-row>
                <el-table :data="form.authorList"
                          @selection-change="handleAuthorSelectionChange"
                          style="display:block;">
                  <el-table-column type="selection" width="50" align="center"/>
                  <el-table-column label="所属单位" align="center" prop="ifourunit" :show-overflow-tooltip="true"/>
                  <el-table-column label="单位名称" align="center" prop="unitname"/>
                  <el-table-column label="人员" align="center" prop="personname" width="100"/>
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
                        @click="handleAuthorUpdate(scope.row)"
                        v-hasPermi="['achieve:patent:list']"
                      >编辑
                      </el-button>
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-delete"
                        @click="handleAuthorDelete(scope.row)"
                        v-hasPermi="['achieve:patent:list']"
                      >删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>

            <el-col :span="16">
              <el-form-item label="专利证书" prop="basicfileList1">
                <el-upload v-bind:disabled="readonly.basic" action="#" :http-request="requestUpload1" :before-remove="beforeRemove1"
                           :on-remove="handleUploadRemove1" :on-preview="handleReview"
                           :file-list="basicfileList1" :before-upload="beforeUpload1"
                           >
                  <el-button size="small" v-if="readonly.basic == false" v-hasPermi="['project:project:edit']">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item label="其他附件" prop="basicfileList2">
                <el-upload v-bind:disabled="readonly.basic" action="#" :http-request="requestUpload2" :before-remove="beforeRemove2"
                           :on-remove="handleUploadRemove2" :on-preview="handleReview"
                           :file-list="basicfileList2" :before-upload="beforeUpload2"
                           >
                  <el-button v-if="readonly.basic == false" size="small" v-hasPermi="['project:project:edit']">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item label="备注" prop="memo">
                <el-input v-bind:readonly="readonly.basic" v-model="form.memo" placeholder="" type="textarea"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="专利状态" prop="statuslinktext">
                <el-input v-model="form.achieveStatus" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="信息输入人员" prop="createuseridlinktext">
                <el-input v-model="form.createuseridlinktext"  disabled/>
              </el-form-item>
            </el-col>
          </el-row>
        </template>


        <template>
          <el-tag v-if="this.hidden.confirm === false" size="medium">审核信息</el-tag>
          <el-row v-if="this.hidden.confirm == false">
            <el-col :span="12">
              <el-form-item label="审核结果" prop="confirmResult">
                <template>
                  <el-radio-group v-model="form.confirmResult" v-bind:readonly="this.readonly.confirm">
                    <el-radio :label="1">通过</el-radio>
                    <el-radio :label="2">不通过</el-radio>
                  </el-radio-group>
                </template>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="this.hidden.confirm == false">
            <el-col :span="16">
              <el-form-item label="意见" prop="confirmNote">
                <el-input v-model="form.confirmNote" placeholder="请输入意见" type="textarea" v-bind:readonly="this.readonly.confirm"/>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

      </el-form>


        <el-row >
          <el-col :span="24" align="center">
            <el-button v-if="hidden.saveBtn === false" type="success" @click="saveForm">暂 存</el-button>
            <el-button v-if="hidden.changeBtn === false" type="primary" @click="changeForm">修改项目信息</el-button>
            <el-button v-if="hidden.deleteBtn === false" type="danger" @click="deleteForm">删除项目</el-button>
            <el-button v-if="hidden.submitBtn === false" type="primary" @click="submitForm">提交审核</el-button>
            <el-button v-if="hidden.changeAcceptanceBtn === false" type="warning" @click="changeAcceptanceForm">修改验收信息</el-button>
            <el-button v-if="hidden.returnBtn === false" type="warning" @click="returnForm">退回新建</el-button>
            <el-button v-if="hidden.confirmBtn === false" type="primary" @click="confirmForm">确 认</el-button>
            <el-button v-if="hidden.addAcceptanceBtn === false" type="primary" @click="addAcceptanceForm">补充验收材料</el-button>
            <el-button @click="closeForm">取 消</el-button>
          </el-col>
        </el-row>


    </el-row>


    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="authorFormTitle" :visible.sync="authorFormOpen" width="600px" append-to-body>
      <el-form ref="authorForm" :model="authorForm" :rules="authorRules" label-width="100px" :key="timer">
        <el-row>
          <el-col :span="24">
            <el-form-item label="所属单位" prop="ifourunit">
              <template>
                <el-radio-group v-model="authorForm.ifourunit"  @change="changeAuthorIfourunit" v-bind:readonly="this.readonly.ifourunit">
                  <el-radio :label="1">本所</el-radio>
                  <el-radio :label="0">外单位</el-radio>
                </el-radio-group>
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="单位名称" prop="unitname">
              <el-input v-model="authorForm.unitname" placeholder="请输入单位名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="人员" prop="personname">
              <el-autocomplete class="input-with-select"
                               v-model="authorForm.personname"
                               :fetch-suggestions="queryAuthorPersonListSearch"
                               placeholder="请输入人员名称"
                               clearable
                               size="small"
                               style="width: 240px"
                               @select="handleSelectAuthorPerson"
              >
              </el-autocomplete>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAuthorForm">确 定</el-button>
        <el-button @click="cancelAuthorForm">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listTeam} from "@/api/project/team";
import {listData} from "@/api/system/dict/data";
import {listDept} from "@/api/system/dept";
import {listUser} from "@/api/system/user";
import {
  addProject,
  uniqueProject,
  downloadFile,
  getProject,
  getUplevelProject,
  listProjectdoc,
  listProjectjoinorganization,
  listProjectmember,
  updateProject,
  uploadFile,
  confirmProject,
  getProjectConfirm,
  getProjectacceptance,
  addProjectacceptance,
  xinjianzhongProject,
  acceptanceconfirmProject
} from "@/api/project/project";
import {listAchieve,getPatent, uniquePatent} from "@/api/achieve/patent";
import {beforeUpload, beforeRemove, handleUploadRemove, handleUploadReview} from "@/api/project/projectdoc";
import {listBasDoc} from "@/api/achieve/basdoc";
export default {
  name: "EditPatent",
  data() {
    return {
      // 各个组件的只读和隐藏属性控制
      readonly: {},
      hidden:{},
      opcode: undefined,
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
      patenttypeOptions: [{value: "发明", label: "发明"}, {value: "实用新型", label: "实用新型"},{value: "外观设计", label: "外观设计"},{value: "国际专利", label: "国际专利"}],
      ourunitorderOptions:[{value: 1, label: 1},{value: 2, label: 2},{value: 3, label: 3},{value: 4, label: 4},{value: 5, label: 5}],
      // 数据字典
      teamOptions: [],
      teamList: [],

      userOptions: [],
      userList: [],

      AchieveStatus: {DaiQueRen: 36, BuTongGuo: 38, ZhengChang: 37, YiShanChu: 39},

      basicfileList1: [],
      basicfileList2: [],
      // 日期范围
      // 查询参数
      // 表单参数
      form: {},
      timer: '',
      // 表单校验
      rules: {
        patentname: [
          {required: true, message: "专利名称不能为空", trigger: "blur"}
        ],
        patentcode: [
          {required: true, message: "专利号不能为空", trigger: "blur"}, {
            required: true,
            trigger: "change",
            validator: this.validatePatentcode
          }
        ],
        patenttype: [
          {required: true, message: "专利类型不能为空", trigger: "blur"}
        ],
        passtime: [
          {required: true, message: "授权日期不能为空", trigger: "blur"}
        ],
        ourunitorder: [
          {required: true, message: "本所排名不能为空", trigger: "blur"}
        ],
        teamid: [
          {required: true, message: "所属团队不能为空", trigger: "blur"}
        ]
      },

      authorFormTitle: "",
      authorFormOpen: false,
      authorForm: {},
      authorRules: {
        ifourunit: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        unitname: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        personname: [
          {required: true, message: "不能为空", trigger: "blur"}
        ]
      }
    };

  },

  mounted() {
    console.log("mounted this.$route.meta is ",   this.$route.meta);
  },

  beforeCreate() {
    console.log(" beforeCreate this.$route.meta is ",   this.$route.meta);
    const patentid = this.$route.params && this.$route.params.patentid;

  },
  created() {
    console.log(" created this.$route.meta is ",   this.$route.meta);
    this.resetTemplateStatus();
    var patentid = this.$route.params && this.$route.params.patentid;
    if (patentid === undefined || Number(patentid) === 0) {
      patentid = undefined;
    } else {

    }

    this.opcode = this.$route.meta.opcode;

    this.getData(patentid);

  },
  methods: {
    /** 查询项目信息 */
    getData(patentid) {

      const this_ = this;

      this.loading = true;
      console.log("loading is begin, patentid is ", patentid);

      if (patentid === undefined) {
        this.reset();
        this.configTemplateStatus();
        this.loading = false;
      } else {
        getPatent(patentid).then(response => {
          console.log("this.form is ", response.data);

          const data = response.data;

          this_.form = data;

          this_.configTemplateStatus();

          listBasDoc({relatedid: this_.form.patentid, attachtotype:"专利"}).then(response => {

            let rows = response.data;
            console.log("listBasDoc is ", rows);
            this.form.docList = rows;
            this.basicfileList1 = this.filterDocList("专利证书");
            console.log("专利证书 is ", this.basicfileList1);
            this.basicfileList2 = this.filterDocList("其它附件");
            console.log("其它附件 is ", this.basicfileList2);

          });

          this.loading = false;

          // 获取 审核结果信息。
          // if (this.form.status === this.ProjectStatus.BuTongGuo) {
          //   getProjectConfirm(projectid,this.ProjectStatus.BuTongGuo).then(response => {
          //     console.log("getProjectConfirm is ", response);
          //     this.form.confirmResult = response.data.applystatus;
          //     this.form.confirmNote =  response.data.auditopinion;
          //   });
          // }

        });
      }

      var listOptions = [];
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
        });
      });

    },

    resetTemplateStatus() {
      // 初始化各个组件的状态。
      this.readonly = {basic: true,acceptance:true, confirm: true};
      this.hidden = {basic:false,acceptance:true,confirm:true, saveBtn:true,changeBtn:true,deleteBtn:true,submitBtn:true,changeAcceptanceBtn:true,returnBtn:true,confirmBtn:true,addAcceptanceBtn:true };
    },

    configTemplateStatus() {
      this.resetTemplateStatus();
      console.log("configTemplateStatus is ", this.form.status);
     if (this.form.achieveStatus === this.AchieveStatus.DaiQueRen) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("add") !== -1) {
          this.readonly.basic = false;
        }
        else if (this.opcode.indexOf("query") !== -1) {
          this.readonly.confirm = false;
          this.hidden.confirm = false;
          this.hidden.confirmBtn = false;
        }
        else if (this.opcode.indexOf("confirm") !== -1) {
          this.readonly.confirm = false;
          this.hidden.confirm = false;
          this.hidden.confirmBtn = false;
        }

      }
      else if (this.form.achieveStatus === this.AchieveStatus.BuTongGuo) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {
          this.readonly.confirm = true;
          this.hidden.confirm = false;

          this.hidden.changeBtn = false;
          this.hidden.deleteBtn = false;
        }
      }
      else if (this.form.achieveStatus === this.AchieveStatus.ZhengChang) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.returnBtn = false;
          this.hidden.acceptance = false;
        }
        else if (this.opcode.indexOf("toaccept") !== -1) {
          this.hidden.acceptance = false;
          this.readonly.acceptance = false;
          this.hidden.confirmBtn = false;

        }
      }
      else if (this.form.achieveStatus === this.AchieveStatus.YiShanChu) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

          this.hidden.changeAcceptanceBtn = false;

        }
        else if (this.opcode.indexOf("acceptconfirm") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm =false;
          this.hidden.confirmBtn = false;

        }
      }
      else {
        this.readonly.basic = true;
        this.hidden.acceptance = false;
      }
    },

    getPatentcode(query) {
      return new Promise((resolve, reject) => {
        let res = uniqueProject(query);
        resolve(res);
      });
    },

    async validatePatentcode(rule, value, callback) {
      if (!value) {
        callback(new Error("专利号不能为空"));
      } else {
        if (this.form.subjectcode === undefined) {
          callback();
        } else {
          let query = {subjectcode: value, projectid: this.form.projectid};
          let res = await this.getPatentcode(query);
          console.log(res);
          if (res.data > 0) {
            callback(new Error("专利号重复"));
          } else {
            callback();
          }
        }

      }
    },

    // 表单重置
    reset() {
      this.form = {
        patentid: undefined,
        patentname: undefined,
        patentcode: undefined,

        patenttype: undefined,
        passtime: undefined,
        ourunitorder: undefined,

        teamid: undefined,

        authorList: [],
        docList: [],

        memo: undefined,

        createuserid: undefined,
        achieveStatus: this.AchieveStatus.DaiQueRen,

        statusLinkText: undefined,
        teamname: undefined,


        // 审核结果
        confirmResult: undefined,
        confirmNote: undefined
      };

      this.resetForm("form");
    },


    changePatenttypeValue(value) {

      if (value) {
        this.form.patenttype = value;
      } else {
        this.form.patenttype = undefined;
      }

    },

    changeOurunitorderValue(value) {

      if (value) {
        this.form.ourunitorder = value;
      } else {
        this.form.ourunitorder = undefined;
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

    changeAuthorIfourunit() {
    if (this.authorForm.ifourunit === 1) {
      this.authorForm.unitname= "农业部南京农业机械化研究所";
    }
    else {
      this.authorForm.unitname= "";
    }

    },


    createFilter(v) {
      return (item) => {
        //  console.log("item is ", item.hotKey);
        const queryString = v.toLowerCase();
        var typename = item.value;

        var ll = typename.indexOf(queryString);

        var py = item.hotKey;

        var hh = -1;
        if (py !== undefined && py !== null) {
          hh = py.indexOf(queryString);
        }
        //console.log("type is " + typename, queryString, ll);

        return (ll >= 0 || hh >= 0);
      };
    },

    queryAuthorPersonListSearch(queryString, cb) {

      const queryParams = {
        pageNum: 1,
        pageSize: 30,
        teamname: queryString
      };
      listTeam(queryParams).then(response => {
          const teamListOptions = [];
          const teamList = response.rows;
          teamList.forEach(function (team) {
            const item = {"value": team.teamname, "teamid": team.teamid};
            teamListOptions.push(item);
          });
          cb(teamListOptions);
        }
      );
    },

    handleSelectAuthorPerson(team) {
      console.log("handleSelectTeam is " + team["value"]);
      this.queryParams.teamid = team["teamid"];
      this.queryParams.teamname = team["value"];
    },




    filterDocList(doctype) {
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
    beforeUpload1(file) {

      return beforeUpload(file, this.basicfileList2, "项目申报书");

    },

    requestUpload1: function (params) {
      requestUpload(params, this.basicfileList1, "项目申报书", this.form.projectdocList);
    },

    beforeRemove1(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeRemove1 index=" + index, file.name);
      return true;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleUploadRemove1(file) {

      handleUploadRemove(file, this.basicfileList1,"项目申报书", this.form.projectdocList );

      return;
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
    beforeUpload2(file) {
      return beforeUpload(file, this.basicfileList2, "项目合同");
    },

    requestUpload2: function (params) {
      requestUpload(params, this.basicfileList2, "项目合同", this.form.projectdocList);
    },

    beforeRemove2(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeRemove1 index=" + index, file.name);
      return true;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleUploadRemove2(file) {
      handleUploadRemove(file, this.basicfileList2,"项目合同", this.form.projectdocList );
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

      this.$confirm('是否确认删除"' + this.form.projectname + '"的项目?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {

      }).then(() => {
        this.msgSuccess("删除成功");
        this.closeForm();
      });
    },

    /** 暂存 按钮 */
    saveForm: function () {

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          this.form.operateCode = 1; // 暂存代码 提交给后端。

          // 处理掉添加的参与单位，为了更新或修改。
          this.form.projectJoinOrganizationList.forEach(function (item) {
            if (item.joid < 0) {
              item.joid = undefined;
            }
          });

          if (this.form.projectid === undefined) {
            addProject(this.form).then(response => {
              if (response.data === 0) {
                this.msgError("暂存失败");
                this.form.projectid = undefined;
              } else {
                this.msgSuccess("暂存成功");
                this.form.projectid = response.data;
              }
            });
          } else {
            updateProject(this.form).then(response => {
              this.msgSuccess("修改成功");
              getProject(this.form.projectid);
            });
          }
        }
      });
    },

    /*提交 审核 按钮*/
    submitForm: function () {

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          this.form.operateCode = 2; // 提交审核代码 提交给后端。

          // 处理掉添加的参与单位，为了更新或修改。
          this.form.projectJoinOrganizationList.forEach(function (item) {
            if (item.joid < 0) {
              item.joid = undefined;
            }
          });

          if (this.form.projectid === undefined) {
            addProject(this.form).then(response => {
              if (response.data === 0) {
                this.msgError("提交审核失败");
                this.form.projectid = undefined;
              } else {
                this.msgSuccess("提交审核成功");
                this.form.projectid = response.data;
                this.closeForm();
              }
            });
          } else {
            updateProject(this.form).then(response => {
              this.msgSuccess("提交审核成功");
              this.closeForm();
            });

          }
        }
      });
    },

    /** 编辑模式按钮 */
    changeForm() {
      this.form.status = this.ProjectStatus.XinJianZhong;
      this.configTemplateStatus();

    },

    /** 修改验收信息模式按钮 */
    changeAcceptanceForm() {
      console.log(" 修改验收信息 ",this.form.status);

      this.form.status = this.ProjectStatus.ZaiYan;
      this.opcode = "toaccept";
      this.configTemplateStatus();
    },

    /** 退回到新建中 模式按钮 */
    returnForm() {
      const this_ = this;
      this_.$confirm('是否确认将项目退回"新建中"状态?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        // 发送到后台。
        xinjianzhongProject(this_.form);
      }).then(() => {
        this_.closeForm();
        this_.msgSuccess("退回成功");
      });
    },

    /**  验收申请确认 和 新建审核确认 模式按钮 */
    confirmForm() {

      const this_ = this;

      if (this.form.status === this.ProjectStatus.ZaiYan) {
        this_.$confirm('是否确认提交项目 验收信息?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          this_.form.acceptance.projectdocList = this_.form.projectdocList;
          this_.form.acceptance.status = this_.form.status;
          this_.form.acceptance.projectname = this_.form.projectname;
          return addProjectacceptance(this_.form.acceptance);
        }).then(() => {
          this_.closeForm();
          this_.msgSuccess("验收信息提交成功");
        });
      }
      else if (this.form.status === this.ProjectStatus.DaiQueRen){
        this.$refs["form"].validate(
          valid => {
            if (valid) {
              console.log("confirmResult is " + this.form.confirmResult);

              if (this_.form.confirmResult == undefined) {
                this.msgError("请选择审核结果");
                return;
              }

              this_.form.confirmUserid = this_.$store.getters.userId;
              const result = this_.form.confirmResult;

              if (result === 1) {
                this_.$confirm('是否确认项目新建审核 通过?', "警告", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning"
                }).then(function () {
                  return confirmProject(this_.form);
                  }).then(() => {
                    this_.closeForm();
                    this_.msgSuccess("新建审核成功");
                  });
              }
              else if (result === 2) {

                var note = this_.form.confirmNote;
                console.log("confirmNote is ", note);
                if (note !== null && note !== undefined && note.trim() !== '' ) {
                  this_.$confirm('是否确认项目新建审核 不通过?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                  }).then(function () {
                   return  confirmProject(this_.form);
                  }).then(() => {
                    this_.closeForm();
                    this_.msgSuccess("新建审核不通过 完成");
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
            }
          }
        );
      }
      else if (this.form.status === this.ProjectStatus.JieTiDaiQueRen){
        this.$refs["form"].validate(
          valid => {
            if (valid) {
              console.log("confirmResult is " + this.form.confirmResult);

              if (this_.form.confirmResult == undefined) {
                this.msgError("请选择审核结果");
                return;
              }

              this_.form.confirmUserid = this_.$store.getters.userId;
              const result = this_.form.confirmResult;

              if (result === 1) {
                this_.$confirm('是否确认项目验收审核 通过?', "警告", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning"
                }).then(function () {
                  return acceptanceconfirmProject(this_.form);
                }).then(() => {
                  this_.closeForm();
                  this_.msgSuccess("项目验收审核成功");
                });
              }
              else if (result === 2) {

                var note = this_.form.confirmNote;
                console.log("confirmNote is ", note);
                if (note !== null && note !== undefined && note.trim() !== '' ) {
                  this_.$confirm('是否确认项目验收审核 不通过?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                  }).then(function () {
                    return  acceptanceconfirmProject(this_.form);
                  }).then(() => {
                    this_.closeForm();
                    this_.msgSuccess("验收审核不通过 完成");
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
            }
          }
        );
      }
      else if (this.form.status === this.ProjectStatus.YiJieTi) {
        this_.$confirm('是否确认提交 补充验收材料?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          this_.form.acceptance.projectdocList = this_.form.projectdocList;
          this_.form.acceptance.status = this_.form.status;
          this_.form.acceptance.projectname = this_.form.projectname;
          return addProjectacceptance(this_.form.acceptance);
        }).then(() => {
          this_.closeForm();
          this_.msgSuccess("补充验收材料提交成功");
        });
      }
      else {


      }
    },



    /* 主持的项目 子 form */

    // 多选框选中数据
    handleAuthorSelectionChange(selection) {
      // this.ids = selection.map(item => item.teamid);
      // this.single = selection.length != 1;
      // this.multiple = !selection.length;
    },


    handleAuthorAdd() {
      this.resetAuthorForm();
      this.authorFormOpen = true;
      this.authorFormTitle = "添加专利人";

    },


    handleAuthorUpdate(row) {
      this.authorFormOpen = true;
      this.authorFormTitle = "编辑专利人";
      const author = {
        authorid: row.authorid,
        ifourunit: row.ifourunit,
        unitname: row.unitname,
        personname: row.personname
      };

      this.authorForm = author;
    },


    /**  删除按钮操作 */
    handleAuthorDelete(row) {
      const this_ = this;
      const subjectname = row.subjectname
      this.$confirm('是否确认删除"' + subjectname + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        const index = this_.form.projectJoinOrganizationList.indexOf(row);
        this_.form.projectJoinOrganizationList.splice(index, 1);
      }).then(() => {
        this.msgSuccess("删除成功");
      });
    },

    cancelAuthorForm: function () {
      this.authorFormOpen = false;
      this.resetAuthorForm();
    },

    submitAuthorForm: function () {
      const this_ = this;
      this_.$refs["authorForm"].validate(valid => {
        if (valid) {

          if (this_.authorForm.joid !== undefined) {

            this_.form.projectJoinOrganizationList.forEach(function (item) {
              if (item.joid == this_.authorForm.joid) {
                item.subjectname = this_.authorForm.subjectname;
                item.organizationname = this_.authorForm.organizationname;
                item.manager = this_.authorForm.manager;
                item.funds = this_.authorForm.funds;
              }
            });
            this.msgSuccess("修改成功");
          } else {
            let joid = 0;
            this_.form.projectJoinOrganizationList.forEach(function (item) {
              if (item.joid !== undefined && item.joid < joid) {
                joid = item.joid;
              }
            });
            joid = joid - 1;
            const joinorg = {
              joid: joid,
              subjectname: this_.authorForm.subjectname,
              organizationname: this_.authorForm.organizationname,
              manager: this_.authorForm.manager,
              funds: this_.authorForm.funds
            };
            this_.form.projectJoinOrganizationList.push(joinorg);
            this.msgSuccess("添加成功");
          }
          console.log("submit authorForm is ", this.authorForm);
          this_.authorFormOpen = false;
          this.resetAuthorForm();
        }
      });
    },

    // 表单重置
    resetAuthorForm() {
      this.authorForm = {
        authorid: undefined,
        ifourunit: 1,
        unitname: "农业部南京农业机械化研究所",
        personname: undefined
      };
      this.resetForm("authorForm");
    },

  }
}
</script>

<style scoped>

</style>
