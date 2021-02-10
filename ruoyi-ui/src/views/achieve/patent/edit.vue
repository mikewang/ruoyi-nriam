<template>
  <div class="app-container">

    <el-row :gutter="20">
      <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="160px" :key="timer"
      >
        <template>
          <el-tag size="medium" type="info">专利信息</el-tag>
          <el-row>
            <el-col :span="8">
              <el-form-item label="专利名称" prop="patentname">
                <el-input v-bind:readonly="readonly.basic" v-model="form.patentname" placeholder="请输入专利名称"
                          :show-overflow-tooltip="true"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="专利号" prop="patentcode">
                <el-input v-bind:readonly="readonly.basic" v-model="form.patentcode" placeholder="请输入专利号"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="专利类型" prop="patenttype">
                <el-select v-bind:readonly="readonly.basic" v-model="form.patenttype" placeholder="请选择"
                           style="display:block;" clearable
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
                <el-select v-bind:readonly="readonly.basic" v-model="form.ourunitorder" placeholder="请选择"
                           style="display:block;" clearable
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
                <el-select v-bind:readonly="readonly.basic" v-model="form.teamname" placeholder="请选择项目所属团队"
                           style="display:block;"
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
                  <el-table-column label="所属单位" align="center" prop="ifourunit" :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                      <span v-if="scope.row.ifourunit === 1">本所</span>
                      <span v-else>外单位</span>
                    </template>

                  </el-table-column>
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
                <el-upload v-bind:disabled="readonly.basic" action="#" :http-request="requestUpload1"
                           :before-remove="beforeRemove1"
                           :on-remove="handleUploadRemove1" :on-preview="handleReview"
                           :file-list="basicfileList1" :before-upload="beforeUpload1"
                >
                  <el-button size="small" v-if="readonly.basic == false" v-hasPermi="['project:project:edit']">上传文件<i
                    class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item label="其他附件" prop="basicfileList2">
                <el-upload v-bind:disabled="readonly.basic" action="#" :http-request="requestUpload2"
                           :before-remove="beforeRemove2"
                           :on-remove="handleUploadRemove2" :on-preview="handleReview"
                           :file-list="basicfileList2" :before-upload="beforeUpload2"
                >
                  <el-button v-if="readonly.basic == false" size="small" v-hasPermi="['project:project:edit']">上传文件<i
                    class="el-icon-upload el-icon--right"></i>
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
            <el-form-item label="人员" prop="personname">
              <el-select v-if="authorForm.ifourunit == 1"  v-model="authorForm.personname" placeholder="请选择人员名称" style="display:block;"
                         clearable @clear="clearAuthorPersonname" @change="changeAuthorPersonname"
                         filterable :filter-method="filterAuthorPersonnameOptions" :show-overflow-tooltip="true">
                <el-option
                  v-for="item in teamMemberOptions"
                  :key="item.id"
                  :label="item.value"
                  :value="item.id"/>
              </el-select>
              <el-autocomplete v-if="0 == 1" class="input-with-select"
                               v-model="authorForm.personname"
                               :fetch-suggestions="queryAuthorPersonListSearch"
                               placeholder="请输入人员名称"
                               clearable
                               size="small"
                               style="width: 240px"
                               @select="handleSelectAuthorPerson"
              >
              </el-autocomplete>
              <el-input v-if="authorForm.ifourunit == 0" v-model="authorForm.personname"
                        placeholder="请输入人员名称"></el-input>
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
import {listTeam, listTeamMember} from "@/api/project/team";
import {listUser} from "@/api/system/user";
import {getPatent, addPatent, updatePatent, deletePatent, confirmPatent, uniquePatent} from "@/api/achieve/patent";
import {listBasDoc,requestUpload, beforeRemove, beforeUpload, handleUploadRemove, handleUploadReview} from "@/api/achieve/basdoc";

export default {
  name: "EditPatent",
  data() {
    return {
      // 各个组件的只读和隐藏属性控制
      readonly: {},
      hidden: {},
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
      patenttypeOptions: [{value: "发明", label: "发明"}, {value: "实用新型", label: "实用新型"}, {value: "外观设计",label: "外观设计"}, {value: "国际专利", label: "国际专利"}],
      ourunitorderOptions: [{value: 1, label: 1}, {value: 2, label: 2}, {value: 3, label: 3}, {value: 4,label: 4}, {value: 5, label: 5}],
      // 数据字典
      teamOptions: [],
      teamList: [],

      userOptions: [],
      userList: [],

      teamMemberOptions: [],
      teamMemberList: [],

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
    console.log("mounted this.$route.meta is ", this.$route.meta);
  },

  beforeCreate() {
    console.log(" beforeCreate this.$route.meta is ", this.$route.meta);
    const patentid = this.$route.params && this.$route.params.patentid;

  },
  created() {
    console.log(" created this.$route.meta is ", this.$route.meta);
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

          listBasDoc({relatedid: this_.form.patentid, attachtotype: "专利"}).then(response => {

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

    getPatentcode(query) {
      return new Promise((resolve, reject) => {
        let res = uniquePatent(query);
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
        status: this.AchieveStatus.DaiQueRen,

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
      this.form.teamid = undefined;
      this.teamMemberList = [];
      this.teamMemberOptions = [];
    },

    changeTeamValue(value) {

      if (value) {
        this.form.teamid = value;
        this.teamMemberList = [];
        this.teamMemberOptions = [];

        const queryParams = {
          pageNum: 1,
          pageSize: 30,
          teamid: this.form.teamid
        };

        listTeamMember(queryParams).then(response => {
            const teamMemberOptions = [];
            const teamMemberList = response.rows;
            teamMemberList.forEach(function (member) {
              const item = {"value": member.realName, "id": member.userid, "hotKey": member.hotKey};
              teamMemberOptions.push(item);
            });
            this.teamMemberList = teamMemberOptions;
            this.teamMemberOptions = teamMemberOptions;
          }
        );

      } else {
        this.form.teamid = undefined;
        this.teamMemberList = [];
        this.teamMemberOptions = [];
      }
    },

    filterTeamOptions(queryString) {
      console.log("filter value is " + queryString);
      this.teamOptions = queryString ? this.teamList.filter(this.createFilter(queryString)) : this.teamList;
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

    clearAuthorPersonname() {

    },
    changeAuthorPersonname(value,id) {
        console.log("id is " + id , "value is " + value);
    },

    filterAuthorPersonnameOptions(queryString) {
      console.log("filter value is " + queryString);
      console.log("teamMemberList is " , this.teamMemberList);

      let options = [];
      if (this.teamMemberList.length > 0) {
        options = this.teamMemberList;
        this.teamMemberOptions = queryString ? options.filter(this.createFilter(queryString)) : options;
      }
     else if (this.userList.length > 0) {
        options = this.userList;
        this.teamMemberOptions = queryString ? options.filter(this.createFilter(queryString)) : options;
      }
     else  {
        // 在线查询。
        const queryParams = {
          pageNum: 1,
          pageSize: 30,
          teamid: this.form.teamid,
          realName: queryString
        };

        listTeamMember(queryParams).then(response => {
            const teamMemberOptions = [];
            const teamMemberList = response.rows;
            teamMemberList.forEach(function (member) {
              const item = {"value": member.realName, "id": member.userid, "hotKey": member.hotKey};
              var x = true;
              for (let i = 0; i < teamMemberOptions.length; i++) {
                let opt = teamMemberOptions[i];
                if (opt.value === member.realName) {
                  x = false;
                  break;
                }
              }
              if (x) {
                teamMemberOptions.push(item);
              }
            });
            this.teamMemberOptions = teamMemberOptions;
          }
        );
      }
    },


    createFilter(v) {
      return (item) => {
        //  console.log("item is ", item.hotKey);
        const queryString = v.toLowerCase();
        const typename = item.value;
        const ll = typename.indexOf(queryString);
        const py = item.hotKey;
        let hh = -1;
        if (py !== undefined && py !== null) {
          hh = py.indexOf(queryString);
        }
        //console.log("type is " + typename, queryString, ll);

        return (ll >= 0 || hh >= 0);
      };
    },

    queryAuthorPersonListSearch(queryString, cb) {
      // 调用 callback 返回建议列表的数据
      var options = [];
      if (this.teamMemberList.length > 0) {
        options = this.teamMemberList;
        const results = queryString ? options.filter(this.createFilter(queryString)) : options;
        // 调用 callback 返回建议列表的数据
        cb(results);
      } else if (this.userList.length > 0) {
        options = this.userList;
        const results = queryString ? options.filter(this.createFilter(queryString)) : options;
        // 调用 callback 返回建议列表的数据
        cb(results);
      } else {
        // 在线查询。
        const queryParams = {
          pageNum: 1,
          pageSize: 30,
          teamid: this.form.teamid,
          realName: queryString
        };

        listTeamMember(queryParams).then(response => {
            const teamMemberListOptions = [];
            const teamMemberList = response.rows;
            teamMemberList.forEach(function (member) {
              const item = {"value": member.realName, "id": member.userid, "hotKey": member.hotKey};
              var x = true;
              for (let i = 0; i < teamMemberListOptions.length; i++) {
                let opt = teamMemberListOptions[i];
                if (opt.value === member.realName) {
                  x = false;
                  break;
                }
              }
              if (x) {
                teamMemberListOptions.push(item);
              }
            });
            cb(teamMemberListOptions);
          }
        );

      }
    },

    handleSelectAuthorPerson(member) {
      console.log("handleSelectAuthorPerson is " + member["value"] + " userid is " +  member["id"]);
      this.authorForm.personname = member["value"];
      this.authorForm.userid = member["id"];
    },

    filterDocList(doctype) {
      const doclist = [];

      if (doctype !== "") {
        for (let i = 0; i < this.form.docList.length; i++) {
          let item = this.form.docList[i];
          if (item.doctype === doctype) {
            doclist.push({"name": item.docname, "url": item.docid});
          }
        }
      }
      return doclist;
    },


    /* 专利证书 */
    beforeUpload1(file) {

      return beforeUpload(file, this.basicfileList1, "专利证书");

    },

    requestUpload1: function (params) {
      requestUpload(2, params, this.basicfileList1, "专利证书", this.form.docList);
    },

    beforeRemove1(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeRemove1 index=" + index, file.name);
      return true;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleUploadRemove1(file) {

      handleUploadRemove(file, this.basicfileList1, "专利证书", this.form.docList);

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

    /* 其它附件 */
    beforeUpload2(file) {
      return beforeUpload(file, this.basicfileList2, "其它附件");
    },

    requestUpload2: function (params) {
      requestUpload(2, params, this.basicfileList2, "其它附件", this.form.docList);
    },

    beforeRemove2(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeRemove2 index=" + index, file.name);
      return true;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleUploadRemove2(file) {
      handleUploadRemove(file, this.basicfileList2, "其它附件", this.form.docList);
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


    /*提交 审核 按钮*/
    submitForm: function () {

      const this_ = this;

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          this_.form.operateCode = 2; // 提交审核代码 提交给后端。

          if (this_.opcode === "add") {
            // 处理掉添加 专利人，为了更新或修改。
            this_.form.authorList.forEach(function (item) {
              if (item.authorid < 0) {
                item.authorid = undefined;
              }
            });

            if (this_.form.patentid === undefined) {
              addPatent(this.form).then(response => {
                if (response.data === 0) {
                  this_.msgError("提交审核失败");
                  this_.form.patentid = undefined;
                } else {
                  this.msgSuccess("提交审核成功");
                  this.form.patentid = response.data;

                }
              }).then(() => {

                this.closeForm();
              });
            } else {
              updatePatent(this.form).then(response => {

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
                return confirmPatent(this_.form);
              }).then(() => {
                this_.closeForm();
                this_.msgSuccess("审核通过，完成");
              });
            }
            else if (result === 2) {

              var note = this_.form.confirmNote;
              console.log("confirmNote is ", note);
              if (note !== null && note !== undefined && note.trim() !== '' ) {
                this_.$confirm('是否确认专利审核 不通过?', "警告", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning"
                }).then(function () {
                  return  confirmPatent(this_.form);
                }).then(() => {
                  this_.closeForm();
                  this_.msgSuccess("新核不通过 完成");
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
          const teamMemberOptions = [];
          const teamMemberList = response.rows;
          teamMemberList.forEach(function (member) {
            const item = {"value": member.realName, "id": member.userid, "hotKey": member.hotKey};
            teamMemberOptions.push(item);
          });
          this.teamMemberList = teamMemberOptions;
          this.teamMemberOptions = teamMemberOptions;
        }
      );

    },

    handleAuthorAdd() {
      this.resetAuthorForm();
      this.authorFormOpen = true;
      this.authorFormTitle = "添加专利人";

      if (this.form.teamid !== undefined) {
        this.getTeamMember(this.form.teamid);
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
        personname: row.personname
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
              personname: this_.authorForm.personname
            };
            this_.form.authorList.push(author2);
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
        userid: -1,
        personname: undefined
      };
      this.resetForm("authorForm");
    },

  }
}
</script>

<style scoped>

</style>
