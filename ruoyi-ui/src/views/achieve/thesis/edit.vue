<template>
  <div class="app-container">

    <el-row :gutter="20">
      <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="160px" :key="timer"
      >
        <template>
          <el-tag size="medium" type="info">论文信息</el-tag>
          <el-row>
            <el-col :span="8">
              <el-form-item label="论文题目" prop="thesisname">
                <el-input v-bind:readonly="readonly.basic" v-model="form.thesisname" placeholder="请输入论文题目"
                          :show-overflow-tooltip="true"/>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="所属团队" prop="teamid">
                <!-- 所属团队组件-->
                <team-data :readonly="readonly.basic" :selected-team-id="form.teamid" :join-team-user-id="undefined" @changeTeamId="selectTeamId"></team-data>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="期刊级别" prop="publishbooklevel">
                <dict-data :readonly="readonly.basic" :dict-type-name="DictTypeNamePublishbooklevel"
                           :selected-dict-value="form.publishbooklevel" :data-options="undefined"
                           @changeDictValue="changeFormDictType" :key="form.publishbooklevel"></dict-data>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="期刊名称" prop="publishbookname">
                <el-input v-bind:readonly="readonly.basic" v-model="form.publishbookname" placeholder="请输入期刊名称"/>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="发表年度" prop="year">
                <el-date-picker v-bind:readonly="readonly.basic" v-model="form.year" type="year" placeholder="请选择日期"
                                format="yyyy"
                                value-format="yyyy"
                                style="display:block;"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="卷数" prop="issue">
                <el-input v-bind:readonly="readonly.basic" v-model="form.issue" placeholder="请输入"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="期数" prop="period">
                <el-input v-bind:readonly="readonly.basic" v-model="form.period" placeholder="请输入"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="起止页码" prop="page">
                <el-input v-bind:readonly="readonly.basic" v-model="form.page" placeholder="请输入"/>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="论文级别" prop="thesislevel">
                <dict-data :readonly="readonly.basic" :dict-type-name="DictTypeNameThesislevel"
                           :selected-dict-value="form.thesislevel" :data-options="thesislevelOptions"
                           @changeDictValue="changeFormDictType"></dict-data>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item label="DOI" prop="doi">
                <el-input v-bind:readonly="readonly.basic" v-model="form.doi" placeholder=""/>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="作者列表" prop="authorList">
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
                  <el-table-column type="index" width="50" align="center"/>
                  <el-table-column label="所属单位" align="center" prop="ifourunit" :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                      <span v-if="scope.row.ifourunit === 1">本所</span>
                      <span v-else>外单位</span>
                    </template>
                  </el-table-column>
                  <el-table-column label="单位名称" align="center" prop="unitname"/>
                  <el-table-column label="人员" align="center" prop="personname" width="100"/>
                  <el-table-column label="通讯作者" align="center" prop="ifreporter" width="100">
                    <template slot-scope="scope">
                      <span v-if="scope.row.ifreporter">是</span>
                      <span v-else>否</span>
                    </template>
                  </el-table-column>
                  <el-table-column
                    label="操作"
                    align="center"
                    width="160"
                    class-name="small-padding fixed-width"
                    v-if="!readonly.basic"
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
              <el-form-item label="论文全文" prop="basicfileList1">
                <bas-doc :basdoc="basDocThesis" :readonly="readonly.basic" @changeFileList="changeBasicfileList1" :key="basDocThesis.relatedid" ></bas-doc>
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
              <el-form-item label="状态" prop="statuslinktext">
                <el-input v-model="form.statuslinktext" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="信息输入人员" prop="createuseridlinktext">
                <el-input v-model="form.createuseridlinktext" disabled/>
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
                <el-input v-model="form.confirmNote" placeholder="请输入意见" type="textarea"
                          v-bind:readonly="this.readonly.confirm"/>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

      </el-form>


      <el-row>
        <el-col :span="24" align="center">
          <el-button v-if="hidden.submitBtn === false" type="success" @click="submitForm">确 定</el-button>
          <el-button v-if="hidden.changeBtn === false" type="primary" @click="changeForm">修改信息</el-button>
          <el-button v-if="hidden.deleteBtn === false" type="danger" @click="deleteForm">删除</el-button>

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
                <el-radio-group v-model="authorForm.ifourunit" @change="changeAuthorIfourunit"
                                v-bind:readonly="this.readonly.ifourunit">
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
            <el-form-item label="人员" prop="userid">
              <user-data v-if="authorForm.ifourunit == 1" :selected-user-id="authorForm.userid" :user-list="teamMemberList" @changeUserData="changeAuthorPersonname" :key="timer" ></user-data>
              <el-input v-else v-model="authorForm.personname"  placeholder="请输入人员名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="通讯作者" prop="ifreporter">
              <el-checkbox v-model="authorForm.ifreporter" @change="changeAuthorIfreporter" >是</el-checkbox>
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
import {listTeamMember} from "@/api/project/team";
import {getThesis, addThesis, uniqueThesis, updateThesis, deleteThesis,confirmThesis, getThesisConfirm} from "@/api/achieve/thesis";

import TeamData from "@/views/public/team-data";
import DictData from "@/views/public/dict-data";
import UserData from "@/views/public/user-data";
import BasDoc from "@/views/public/bas-doc";

export default {
  name: "achieve_thesis_edit",
  components: {"team-data": TeamData, "dict-data": DictData, "user-data": UserData, "bas-doc": BasDoc},
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
      // 数据字典  期刊级别
      DictTypeNamePublishbooklevel: "期刊级别",

      DictTypeNameThesislevel: "论文级别",
      thesislevelOptions: [{dictLabel: "A", dictValue:  "A"}, {dictLabel:  "B", dictValue:  "B"}, {dictLabel:  "C", dictValue:  "C"}, {dictLabel: "D",dictValue: "D"}, {dictLabel: "E", dictValue: "E"}],

      basDocThesis: {relatedid: -2, attachtotype: "学术论文", doctype: "论文全文"},

      // 数据字典
      teamMemberList: [],

      AchieveStatus: {DaiQueRen: 36, BuTongGuo: 38, ZhengChang: 37, YiShanChu: 39},

      basicfileList1: [],

      // 日期范围
      // 查询参数
      // 表单参数
      form: {docList:[], authorList:[]},
      timer: '',
      // 表单校验
      rules: {
        thesisname: [
          {required: true, message: "论文题目不能为空", trigger: "blur"}, {
            required: true,
            trigger: "change",
            validator: this.validateThesisname
          }
        ],
        publishbooklevel: [
          {required: true, message: "期刊级别不能为空", trigger: "blur"}
        ],
        publishbookname: [
          {required: true, message: "期刊名称不能为空", trigger: "blur"}
        ],
        period: [
          {required: true, message: "期数不能为空", trigger: "blur"}
        ],
        page: [
          {required: true, message: "起止页码不能为空", trigger: "blur"}
        ],
        thesislevel: [
          {required: true, message: "论文级别不能为空", trigger: "blur"}
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
        userid: [
          {required: true, message: "不能为空", trigger: "blur"}
        ]
      }
    };

  },

  mounted() {
    console.log("mounted this.$route.meta is ", this.$route.meta);
  },

  beforeCreate() {
    console.log(" beforeCreate this.$route.meta is ", this.$route.meta);
    const thesisid = this.$route.params && this.$route.params.thesisid;

  },
  created() {
    console.log(" created this.$route.meta is ", this.$route.meta);
    this.resetTemplateStatus();
    console.log(" created this.$route.params is ", this.$route.params);

    let thesisid = this.$route.params && this.$route.params.thesisid;
    if (thesisid === undefined || Number(thesisid) === 0) {
      thesisid = undefined;
    } else {

    }

    this.opcode = this.$route.meta.opcode;
    this.applyid = this.$route.params.applyid;

    this.getData(thesisid);

  },
  methods: {
    /** 查询项目信息 */
    getData(thesisid) {

      const this_ = this;

      this.loading = true;
      console.log("loading is begin, thesisid is ", thesisid);

      if (thesisid === undefined) {
        this.reset();
        this.configTemplateStatus();
        this.loading = false;
      } else {
        getThesis(thesisid).then(response => {
          console.log("getData response is ", response.data);

          const data = response.data;
          data.year = data.year.toString();
          data.publishbooklevel = data.publishbooklevel.toString();
          data.thesislevel = data.thesislevel.toString();

          this_.form = data;

          if (this.applyid !== undefined) {
            this_.form.applyid = this.applyid;
          }
          console.log("this.form is ", this_.form);

          this_.configTemplateStatus();

          this_.basDocThesis.relatedid = this_.form.thesisid;

          // 获取 审核结果信息。
          if (this.form.status === this.AchieveStatus.BuTongGuo) {
            getThesisConfirm(thesisid,this.AchieveStatus.BuTongGuo).then(response => {
              console.log("getThesisConfirm is ", response);
              this.form.confirmResult = response.data.applystatus;
              this.form.confirmNote =  response.data.auditopinion;

              this.timer = Date.now().toString();
              this.loading = false;
            });
          }
          else {
            this.timer = Date.now().toString();
            this.loading = false;
          }

        });
      }
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
      console.log("configTemplateStatus is ", this.form.status);

      if (this.form.status === this.AchieveStatus.DaiQueRen) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("add") !== -1) {
          this.readonly.basic = false;
          this.hidden.submitBtn = false;
        } else if (this.opcode.indexOf("query") !== -1) {
          this.hidden.changeBtn = false;
          this.hidden.deleteBtn = false;
        }
        else if (this.opcode.indexOf("confirm") !== -1) {
          this.readonly.confirm = false;
          this.hidden.confirm = false;
          this.hidden.submitBtn = false;
        }
      } else if (this.form.status === this.AchieveStatus.BuTongGuo) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {
          this.readonly.confirm = true;
          this.hidden.confirm = false;

          this.hidden.changeBtn = false;
          this.hidden.deleteBtn = false;
        }
      } else if (this.form.status === this.AchieveStatus.ZhengChang) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {

        }
      } else if (this.form.status === this.AchieveStatus.YiShanChu) {
        console.log("this.opcode is ", this.opcode);

      } else {

      }
    },

    getThesisname(query) {
      return new Promise((resolve, reject) => {
        let res = uniqueThesis(query);
        resolve(res);
      });
    },

    async validateThesisname(rule, value, callback) {
      if (!value) {
        callback(new Error("论文名称不能为空"));
      } else {
        if (this.form.thesisname === undefined) {
          callback();
        } else {
          let query = {thesisname: value, thesisid: this.form.thesisid};
          let res = await this.getThesisname(query);
          console.log(res);
          if (res.data > 0) {
            callback(new Error("论文名称重复"));
          } else {
            callback();
          }
        }
      }
    },

    // 表单重置
    reset() {
      this.form = {
        thesisid: undefined,
        thesisname: undefined,

        patentcode: undefined,

        patenttype: undefined,
        passtime: undefined,
        ourunitorder: undefined,

        teamid: undefined,

        authorList: [],
        docList: [],

        memo: undefined,

        createuserid: undefined,
        status: this.AchieveStatus.DaiQueRen,

        // 审核结果
        confirmResult: undefined,
        confirmNote: undefined
      };

      this.resetForm("form");
    },

    // 组件方法
    changeFormDictType(dict) {

      if (dict.type === this.DictTypeNamePublishbooklevel) {
        console.log("changeFormDictType is ",dict);
        if (dict) {
          this.form.publishbooklevel = dict.id;
        } else {
          this.form.publishbooklevel = undefined;
        }
      }
      else if (dict.type === this.DictTypeNameThesislevel) {
        console.log("changeFormDictType is ", dict);
        if (dict) {
          this.form.thesislevel = dict.id;
        } else {
          this.form.thesislevel = undefined;
        }
      }
      else {
        console.error("changeFormDictType  意外 is ", dict);
      }
    },

    // 组件方法
    selectTeamId(value) {
      console.log("handleSelectTeam is ", value);
      if (value) {
        this.form.teamid = value.id;
      } else {
        this.form.teamid = undefined;
      }
    },

    changeAuthorIfourunit() {
      if (this.authorForm.ifourunit === 1) {
        this.authorForm.unitname = "农业部南京农业机械化研究所";

      } else {
        this.authorForm.unitname = "";
        this.authorForm.userid = -1;
        this.authorForm.personname = "";
      }
    },

    changeAuthorPersonname(user) {
        console.log("changeAuthorPersonname user is " + user);
      if (user) {
        this.authorForm.userid = user.userId;
        this.authorForm.personname = user.realName;
      } else {
        this.authorForm.userid = undefined;
        this.authorForm.personname = undefined;
      }
    },

    changeAuthorIfreporter(value) {
      console.log("changeAuthorIfreporter is ", value);
      this.authorForm.ifreporter = value;
    },


    /* 论文全文 */

    changeBasicfileList1(filelist) {
      if (this.form.docList == null) {
        this.form.docList = [];
      }
      console.log("论文全文 is ", filelist.length, this.form.docList.length);

      let doctype = "论文全文";

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

      const thesisid = this.form.thesisid;

      this.$confirm('是否确认删除"' + this.form.patentname + '"的成果?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        deleteThesis(thesisid);
      }).then(() => {
        this.msgSuccess("删除成功");
        this.closeForm();
      });
    },


    /*提交 审核 按钮*/
    submitForm: function () {

      const this_ = this;
      console.log("submit form is ", this.form);

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          this_.form.operateCode = 2; // 提交审核代码 提交给后端。

          if (this_.form.authorList === undefined || this_.form.authorList.length === 0) {
            this.msgError("专利人列表为空");
            return;
          }

          if (this_.form.docList === undefined || this_.form.docList.length === 0) {
            this.msgError("请上传论文全文");
            return;
          }

          if (this_.opcode === "add") {
            // 处理掉添加 专利人，为了更新或修改。
            this_.form.authorList.forEach(function (item) {
              if (item.authorid < 0) {
                item.authorid = undefined;
              }
            });

            if (this_.form.thesisid === undefined) {
              addThesis(this.form).then(response => {
                if (response.data === 0) {
                  this_.msgError("提交审核失败");
                  this_.form.thesisid = undefined;
                } else {
                  this.msgSuccess("提交审核成功");
                  this.form.thesisid = response.data;

                }
              }).then(() => {

                this.closeForm();
              });
            } else {
              updateThesis(this.form).then(response => {

              }).then(()=> {
                this.msgSuccess("提交审核成功");
                this.closeForm();
              });

            }
          }
          else if (this.opcode === "confirm") {
            console.log("confirmResult is " + this.form.confirmResult);

            if (this_.form.confirmResult == undefined) {
              this.msgError("请选择审核结果");
              return;
            }

            this_.form.confirmUserid = this_.$store.getters.userId;
            const result = this_.form.confirmResult;

            if (result === 1) {
              this_.$confirm('是否确认专利审核 通过?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              }).then(function () {
                confirmThesis(this_.form).then(response => {
                  this_.msgSuccess("审核完成");
                  this_.closeForm();
                });
              }).catch(() => {
                this.$message({
                  type: 'info',
                  message: '已取消'
                });
              });
            }
            else if (result === 2) {

              const note = this_.form.confirmNote;
              console.log("confirmNote is ", note);
              if (note !== null && note !== undefined && note.trim() !== '' ) {
                this_.$confirm('是否确认专利审核 不通过?', "警告", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning"
                }).then(function () {
                  confirmThesis(this_.form).then(response => {
                    this_.msgSuccess("审核完成");
                    this_.closeForm();
                  });
                }).catch(() => {
                  this.$message({
                    type: 'info',
                    message: '已取消'
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
          }


        }
      });
    },

    /** 编辑模式按钮 */
    changeForm() {
      this.form.status = this.AchieveStatus.DaiQueRen;
      this.opcode = "add";
      this.configTemplateStatus();
    },

    /* 主持的项目 子 form */

    // 多选框选中数据
    handleAuthorSelectionChange(selection) {
      // this.ids = selection.map(item => item.teamid);
      // this.single = selection.length != 1;
      // this.multiple = !selection.length;
    },


    getTeamMember(teamid) {
      const queryParams = {
        pageNum: 1,
        pageSize: 30,
        teamid: teamid
      };

      listTeamMember(queryParams).then(response => {
          this.teamMemberList = response.rows;
          this.timer = Date.now().toString();
        }
      );

    },

    handleAuthorAdd() {
      if (this.form.teamid === undefined) {
        this.msgError("所属团队没有选择");
        return;
      }

      this.resetAuthorForm();

      this.authorFormOpen = true;
      this.authorFormTitle = "添加专利人";

      if (this.form.teamid !== undefined) {
        this.getTeamMember(this.form.teamid);
      }
      else {
        console.error(" 所属团队没有选", this.form);
      }
    },

    handleAuthorUpdate(row) {
      this.authorFormOpen = true;
      this.authorFormTitle = "编辑专利人";
      const author = {
        authorid: row.authorid,
        ifourunit: row.ifourunit,
        unitname: row.unitname,
        userid: row.userid,
        personname: row.personname,
        ifreporter: row.ifreporter
      };

      this.authorForm = author;

      if (this.form.teamid !== undefined) {
        this.getTeamMember(this.form.teamid);
      }
    },


    /**  删除按钮操作 */
    handleAuthorDelete(row) {
      const this_ = this;
      const personname = row.personname
      this.$confirm('是否确认删除"' + personname + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        const index = this_.form.authorList.indexOf(row);
        this_.form.authorList.splice(index, 1);
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

          if (this_.authorForm.authorid !== undefined) {

            this_.form.authorList.forEach(function (item) {
              if (item.authorid == this_.authorForm.authorid) {
                item.ifourunit = this_.authorForm.ifourunit;
                item.unitname = this_.authorForm.unitname;
                item.userid = this_.authorForm.userid;
                item.personname = this_.authorForm.personname;
                item.ifreporter = this_.authorForm.ifreporter;
              }
            });
            this.msgSuccess("修改成功");
          } else {
            let authorid = 0;
            this_.form.authorList.forEach(function (item) {
              if (item.authorid !== undefined && item.authorid < authorid) {
                authorid = item.authorid;
              }
            });
            authorid = authorid - 1;
            const author2 = {
              authorid: authorid,
              ifourunit: this_.authorForm.ifourunit,
              unitname: this_.authorForm.unitname,
              userid : this_.authorForm.userid,
              personname: this_.authorForm.personname,
              ifreporter: this_.authorForm.ifreporter
            };
            console.log("添加专利人", author2);
            this_.form.authorList.push(author2);
            this_.msgSuccess("添加成功");
          }
          console.log("submit authorForm is ", this.authorForm);
          this_.authorFormOpen = false;
          this_.resetAuthorForm();
        }
      });
    },

    // 表单重置
    resetAuthorForm() {
      this.authorForm = {
        authorid: undefined,
        ifourunit: 1,
        unitname: "农业部南京农业机械化研究所",
        userid: undefined,
        personname: undefined
      };
      this.resetForm("authorForm");
    },

  }
}
</script>

<style scoped>

</style>
