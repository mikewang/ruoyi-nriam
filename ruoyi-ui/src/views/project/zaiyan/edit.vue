<template>
  <div class="app-container">

    <el-row :gutter="20">
      <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="160px" :key="timer">
        <el-row>
          <el-col :span="8">
            <el-form-item label="项目名称" prop="projectname">
              <el-input v-model="form.projectname" placeholder="请输入项目名称" :show-overflow-tooltip="true"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目编号" prop="projectcode">
              <el-input v-model="form.projectcode" placeholder="请输入项目编号"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目经费编号" prop="subjectcode">
              <el-input v-model="form.subjectcode" placeholder="请输入项目经费编号"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="项目总经费（元）" prop="projectfunds">
              <el-input v-model="form.projectfunds" placeholder="请输入项目总经费（元）"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="财政拨款（元）" prop="financefunds">
              <el-input v-model="form.financefunds" placeholder="请输入财政拨款（元）"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="可支配经费（元）" prop="canusefunds">
              <el-input v-model="form.canusefunds" placeholder="请输入可支配经费（元）"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="项目所属部门" prop="organizationid">
              <el-select v-model="form.organizationIDLinkText" placeholder="请选择项目所属部门" style="display:block;"
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
            <el-form-item label="项目开始日期" prop="projectbegindate">
              <el-date-picker v-model="form.projectbegindate" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd"
                              style="display:block;"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目结束日期" prop="projectenddate">
              <el-date-picker v-model="form.projectenddate" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd"
                              style="display:block;"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="项目类型" prop="projecttype">
              <el-select v-model="form.projectTypeLinkText" placeholder="请选择项目类型" style="display:block;"
                         clearable @clear="clearProjectTypeValue" @change="changeProjectTypeValue"
                         filterable :filter-method="filterProjectTypeOptions">
                <el-option
                  v-for="item in projectTypeOptions"
                  :key="item.id"
                  :label="item.value"
                  :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="项目所属团队" prop="teamid">
              <el-select v-model="form.teamname" placeholder="请选择项目所属团队" style="display:block;"
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
          <el-col :span="8">
            <el-form-item label="项目负责人" prop="projectmanagerid">
              <el-select v-model="form.projectmanagerid" placeholder="请选择项目负责人" style="display:block;"
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

        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="主持/参与" prop="jointype">

              <el-select v-model="form.jointype" placeholder="请选择" style="display:block;" clearable
                         @clear="clearJointypeValue" @change="changeJointypeValue">
                <el-option
                  v-for="item in jointypeOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="16">
            <el-form-item label="备注" prop="memo">
              <el-input v-model="form.memo" placeholder="" type="textarea"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="form.jointype===1">
          <el-col :span="24">
            <el-form-item label="项目参加单位列表" prop="joinOrganizationList">
              <el-row :gutter="10" class="mb8">
                <el-col :span="1.5">
                  <el-button plain
                             type="primary"
                             icon="el-icon-plus"
                             size="mini"
                             @click="handleJoinOrganizationAdd"
                             v-hasPermi="['project:zaiyan:add']"
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
                             @click="handleJoinOrganizationDelete"
                             v-hasPermi="['project:zaiyan:remove']"
                  >删除
                  </el-button>
                </el-col>
                <right-toolbar @queryTable="getList"></right-toolbar>
              </el-row>
              <el-table :data="form.projectJoinOrganizationList"
                        @selection-change="handleJoinOrganizationSelectionChange"
                        style="display:block;">
                <el-table-column type="selection" width="50" align="center"/>
                <el-table-column label="子项目名称" align="center" prop="subjectname" :show-overflow-tooltip="true"/>
                <el-table-column label="参加单位" align="center" prop="organizationname"/>
                <el-table-column label="负责人" align="center" prop="manager" width="100"/>
                <el-table-column label="经费（元）" align="center" prop="funds" width="100"/>
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
                      @click="handleJoinOrganizationUpdate(scope.row)"
                    >编辑
                    </el-button>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="handleJoinOrganizationDelete(scope.row)"
                      v-hasPermi="['project:zaiyan:remove']"
                    >删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row v-if="form.jointype===2">
          <el-col :span="8">
            <el-form-item label="参与项目名称" prop="uplevelsubjectname">
              <el-input v-model="form.uplevelproject.subjectname" placeholder="请输入项目名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="主持单位" prop="uplevelmanageorganization">
              <el-input v-model="form.uplevelproject.manageorganization" placeholder="请输入项目编号"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="负责人" prop="uplevelmanager">
              <el-input v-model="form.uplevelproject.manager" placeholder="请输入项目经费编号"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="经费（元）" prop="uplevelfunds">
              <el-input v-model="form.uplevelproject.funds" placeholder="0.00"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="项目成员" prop="projectmemberList">
              <template>
                <el-checkbox-group v-model="form.projectmemberList"
                                   @change="handleProjectmemberListChange" :key="timer">
                  <el-checkbox v-for="data in projectmemberOptions" :label="data.userid" :key="data.userid">
                    {{ data.realName }}
                  </el-checkbox>
                </el-checkbox-group>

                <el-select v-model="addProjectmemberId" placeholder="请添加成员"
                           clearable @clear="clearProjectMemberValue" @change="changeProjectMemberValue"
                           filterable :filter-method="filterProjectMemberOptions">
                  <el-option
                    v-for="item in userOptions"
                    :key="item.id"
                    :label="item.value"
                    :value="item.id"/>
                </el-select>
              </template>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>

          <el-col :span="12">
            <el-form-item label="项目申报书" prop="sbfileList">
              <el-upload action="#" :http-request="requestSBUpload" :before-remove="beforeSBRemove"
                         :on-remove="handleSBUploadRemove"
                         :file-list="sbfileList" :before-upload="beforeSBUpload" v-hasPermi="['project:zaiyan:edit']">
                <el-button size="small">上传文件<i class="el-icon-upload el-icon--right"></i></el-button>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目合同" prop="contractFile">

            </el-form-item>
          </el-col>

        </el-row>

        <el-row>
          <el-col :span="12">

            <el-form-item label="实施方案" prop="contractFile">

            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目批复文件" prop="contractFile">

            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" align="center">
            <el-button type="success" @click="saveForm">暂 存</el-button>
            <el-button type="primary" @click="submitForm">提交审核</el-button>
            <el-button @click="close">取 消</el-button>
          </el-col>
        </el-row>

      </el-form>

    </el-row>


    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="joinOrganizationTitle" :visible.sync="joinOrganizationOpen" width="600px" append-to-body>
      <el-form ref="joinForm" :model="joinForm" :rules="joinRules" label-width="100px" :key="timer">
        <el-row>
          <el-col :span="24">
            <el-form-item label="子项目名称" prop="subjectname">
              <el-input v-model="joinForm.subjectname" placeholder="请输入子项目名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="参加单位" prop="organizationname">
              <el-input v-model="joinForm.organizationname" placeholder="请输入参加单位名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="负责人名称" prop="manager">
              <el-input v-model="joinForm.manager" placeholder="请输入负责人名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="经费（元）" prop="funds">
              <el-input v-model="joinForm.funds" placeholder="0.00"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitJoinForm">确 定</el-button>
        <el-button @click="cancelJoinForm">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {addTeam, listTeam, updateTeam} from "@/api/project/team";
import {listData} from "@/api/system/dict/data";
import {listDept} from "@/api/system/dept";
import {listUser} from "@/api/system/user";
import {
  addProject,
  checkProject,
  getProject,
  getUplevelProject,
  listProjectdoc,
  listProjectjoinorganization,
  listProjectmember,
  updateProject,
  uploadFile
} from "@/api/project/zaiyan";
import {isNumber} from "@/utils/validate.js"

export default {
  name: "EditInfo",
  data() {
    return {
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
      // 数据字典
      projectTypeOptions: [],
      projectTypeList: [],
      // 数据字典
      teamOptions: [],
      teamList: [],

      // 数据字典
      deptOptions: [],
      deptList: [],

      userOptions: [],
      userList: [],

      jointypeOptions: [{value: 1, label: "主持"}, {value: 2, label: "参与"}],

      addProjectmemberId: undefined,
      projectmemberOptions: [],

      ProjectStatus: {XinJianZhong: 48, DaiQueRen: 40},

      sbfileList: [],
      htfileList: [],
      fafileList: [],
      pffileList: [],

      // 日期范围
      // 查询参数
      // 表单参数
      form: {},
      timer: '',
      // 表单校验
      rules: {
        projectname: [
          {required: true, message: "项目名称不能为空", trigger: "blur"}
        ],
        projectcode: [
          {required: true, message: "项目编号不能为空", trigger: "blur"}
        ],
        subjectcode: [
          {required: true, message: "项目经费编号不能为空", trigger: "blur"}, {
            required: true,
            trigger: "change",
            validator: this.validateSubjectcode
          }
        ],
        projectfunds: [
          {required: true, message: "项目总经费不能为空", trigger: "blur"}, {trigger: "blur", validator: isNumber}
        ],
        financefunds: [
          {required: true, message: "财政拨款不能为空", trigger: "blur"}, {trigger: "blur", validator: isNumber}
        ],
        canusefunds: [
          {required: true, message: "可支配经费不能为空", trigger: "blur"}, {trigger: "blur", validator: isNumber}
        ]
      },

      joinOrganizationTitle: "",
      joinOrganizationOpen: false,
      joinForm: {},
      joinRules: {
        subjectname: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        organizationname: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        manager: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        funds: [
          {required: true, message: "不能为空", trigger: "blur"}, {trigger: "blur", validator: isNumber}
        ]
      }

    };

  },


  beforeCreate() {
    const projectid = this.$route.params && this.$route.params.projectid;
    if (Number(projectid) === 0) {
      this.$route.meta.title = "新增项目";
    } else {
      this.$route.meta.title = "编辑项目";
    }
  },
  created() {

    const projectid = this.$route.params && this.$route.params.projectid;
    if (Number(projectid) === 0) {

    } else {
      this.form.projectid = projectid;
      this.form.projectname = "项目id = " + projectid;
    }

    this.getList();

  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      console.log("loading is begin.");

      if (this.form.projectid == undefined) {
        this.reset();
        this.loading = false;
      } else {
        let projectid = this.form.projectid;
        getProject(projectid.toString()).then(response => {
          this.form = response.data;

          listProjectjoinorganization({projectid: this.form.projectid}).then(response => {
            if (response.data !== null) {
              this.form.projectJoinOrganizationList = response.data;
            }
            else {

            }
            console.log("listProjectjoinorganization is ", response.data);
          });

          getUplevelProject(projectid).then(response => {
            if (response.data !== null) {
              this.form.uplevelproject = response.data;
            }
            else {
              this.form.uplevelproject = {subjectname:null};
            }

            console.log("this.form.uplevelproject is ", this.form.uplevelproject);

          });

          listProjectmember({projectid: this.form.projectid}).then(response => {
            const this_ = this;
            let rows = response.data;
            console.log("listProjectmember is ", rows);
            this_.projectmemberOptions = rows;
            this_.form.projectmemberList = [];
            this_.projectmemberOptions.forEach(function (obj) {
              this_.form.projectmemberList.push(obj.userid);
            });
          });

          listProjectdoc({projectid: this.form.projectid}).then(response => {
            const this_ = this;
            let rows = response.data;
            console.log("listProjectdoc is ", rows);
            this.form.projectdocList = rows;
            this.sbfileList = this.filterProjectdoc("项目申报书");
            console.log("项目申报书 is ", this.sbfileList);
          });

          this.loading = false;

        });


      }


      listData({"dictType": "项目类型"}).then(response => {
        console.log(response);

        var listOptions = [];
        response.rows.sort(function (a, b) {
          return a.dictValue < b.dictValue
        }).forEach(function (item) {
          const projecttype = {value: item.dictLabel, id: item.dictValue};
          listOptions.push(projecttype);
        });

        this.projectTypeList = listOptions;
        this.projectTypeOptions = listOptions;

        listOptions = [];

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
              console.log(response);
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
        });
      });

    },

    getSubjectcode(query) {
      return new Promise((resolve, reject) => {
        let res = checkProject(query);
        resolve(res);
      });
    },

    async validateSubjectcode(rule, value, callback) {
      if (!value) {
        callback(new Error("项目经费编号不能为空"));
      } else {
        if (this.form.subjectcode === undefined) {
          callback();
        } else {
          let query = {subjectcode: value, projectid: this.form.projectid};
          let res = await this.getSubjectcode(query);
          console.log(res);
          if (res.data > 0) {
            callback(new Error("项目经费编号重复"));
          } else {
            callback();
          }
        }

      }
    },

    // 表单重置
    reset() {
      this.form = {
        projectid: undefined,
        projectname: undefined,
        projectcode: undefined,
        subjectcode: undefined,

        projectfunds: undefined,
        financefunds: undefined,
        canusefunds: undefined,

        projectbegindate: undefined,
        projectenddate: undefined,
        projecttype: undefined,

        projectmanagerid: undefined,
        organizationid: undefined,
        teamid: undefined,
        memo: undefined,

        jointype: undefined,
        createuserid: undefined,
        status: undefined,

        projectTypeLinkText: undefined,
        dictionaryOrderNum: undefined,
        ProjectManagerIDLinkText: undefined,
        organizationIDLinkText: undefined,
        statusLinkText: undefined,
        teamname: undefined,

        projectJoinOrganizationList: [],
        uplevelproject: {},
        projectmemberList: [],
        projectdocList: []
      };

      this.resetForm("form");
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

    clearProjectTypeValue() {
      this.form.projecttype = undefined;
      this.form.projectTypeLinkText = undefined;
    },

    changeProjectTypeValue(value) {

      if (value) {
        this.form.projecttype = value;
      } else {
        this.form.projecttype = undefined;
      }

    },

    filterProjectTypeOptions(v) {

      console.log("filter value is " + v);

      if (v) {
        this.projectTypeOptions = this.projectTypeList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.projectTypeOptions = this.projectTypeList;
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

    clearJointypeValue() {
      console.log("clear jointype");
    },

    changeJointypeValue(value) {

      if (value) {
        this.form.jointype = value;
      } else {
        this.form.jointype = undefined;
      }

    },

    filterJointypeValue() {
      console.log("filter jointype");
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

    clearProjectMemberValue() {

    },

    changeProjectMemberValue(value) {
      if (value) {

        var added = false;

        for (let i = 0; i < this.form.projectmemberList.length; i++) {
          let item = this.form.projectmemberList[i];
          if (value === item) {
            added = true;
            break;
          }
        }

        if (added) {
          this.$message.info("项目成员已存在。");
        } else {
          this.form.projectmemberList.push(value);

          for (let i = 0; i < this.projectmemberOptions.length; i++) {
            let item = this.projectmemberOptions[i];
            if (value === item.userid) {
              added = true;
              break;
            }
          }

          if (added) {
            this.$message.info("项目成员已选择。");
          } else {
            var realName = "";
            for (let i = 0; i < this.userList.length; i++) {
              let item = this.userList[i];
              if (value === item.id) {
                realName = item.value;
                break;
              }
            }
            const member = {userid: value, realName: realName}
            this.projectmemberOptions.push(member);
          }

        }

      } else {

      }
    },

    filterProjectMemberOptions(v) {

      console.log("filter value is " + v);

      if (v) {

        this.userOptions = this.userList.filter(this.createFilter(v));
      } else {
        this.userOptions = this.userList;
      }
    },

    handleProjectmemberListChange(value) {
      // 刷新dialog的组件，否则不渲染。
      this.timer = new Date().getTime();
      console.log("handleProjectmemberListChange", value);
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
    beforeSBUpload(file) {

      let x = true
      let fileList = this.sbfileList;

      console.log("fileList is ", fileList);
      for (let i = 0; i < fileList.length; i++) {
        let item = fileList[i];
        if (item.name === file.name) {
          console.log("file existed now ,", file.name);
          x = false;
          break;
        }
      }
      return x;

    },

    requestSBUpload: function (params) {
      let file = params.file;
      console.log(file);
      let formData = new FormData();
      formData.append('file', file);
      uploadFile(formData).then(response => {
        console.log("response.name is ", response.name);
        console.log("response.url is ", response.url);
        this.sbfileList.push({name: response.name, url: response.url});
        console.log("sbfileList is ", this.sbfileList);

        this.form.projectdocList.push({docid:response.url, doctype: "项目申报书"});

      });
    },

    beforeSBRemove(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeSBRemove index=" + index, file.name);
      return true;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleSBUploadRemove(file) {

      let today = new Date();

      let docid = file.url;

      let index = this.sbfileList.indexOf(file);
      if (index !== -1) {
        this.sbfileList.splice(index, 1);
      }
      let index2 = this.form.projectJoinOrganizationList.indexOf(function (item) {
        return item.docid === docid
      });

      if (index2 !== -1) {
        this.form.projectJoinOrganizationList.splice(index2, 1);
      }

      console.log("handleSBUploadRemove index=" + index, file.name, today.toDateString());
      console.log("this.sbfileList is ", this.sbfileList);
      return;
    },


    beforeUpload(file) {

      this.sbfileList.forEach(
        function (item) {
          if (item.name === file.name) {
            return false;
          }
        }
      );
      return true;
    },


    handleUploadError(error, file, fileList) {
    },
    handleUploadExceed(file, fileList) {
    },
    handleUploadSuccess(response, file, fileList) {
    },
    requestUpload: function (params) {
      let file = params.file;
      console.log(file);
      let formData = new FormData();
      formData.append('file', file);
      uploadFile(formData).then(response => {
        console.log("response", response.name);
        console.log("response", response.url);
        this.form.docList1 = this.form.docList1.push({name: response.name, url: response.url});
      });
    },
    handleUploadChange(file, fileList) {
      // this.form.fileList = fileList.slice(-10);
    },

    handleUploadPreview(file) {

      console.log("handleUploadPreview is ", file.url);

      downloadContractFile({"file": file.url}).then(response => {
        var fileURL = window.URL.createObjectURL(new Blob([response]));
        var fileLink = document.createElement('a');

        console.log("response.data is ", response);

        fileLink.href = fileURL;
        fileLink.setAttribute('download', file.name);
        document.body.appendChild(fileLink);

        fileLink.click();
        URL.revokeObjectURL(fileURL);

      }).catch(console.error);
    },


    /** 关闭按钮 */
    close() {
      this.reset();
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({path: "/project/zaiyan"});
    },


    /** 提交按钮 */
    saveForm: function () {

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);

          // 处理掉添加的参与单位，为了更新或修改。
          this.form.projectJoinOrganizationList.forEach(function (item) {
            if (item.joid < 0) {
              item.joid = undefined;
            }
          });

          if (this.form.projectid !== undefined) {
            updateProject(this.form).then(response => {
              this.msgSuccess("修改成功");
              getProject(this.form.projectid);

            });
          } else {

            this.form.status = this.ProjectStatus.XinJianZhong;

            addProject(this.form).then(response => {

              if (response.data === 0) {
                this.msgError("暂存失败");
                this.form.projectid = undefined;
              } else {
                this.msgSuccess("暂存成功");
                this.form.projectid = response.data;
              }

            });
          }
        }
      });
    },

    submitForm: function () {

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          if (this.form.projectid !== undefined) {
            // updateTeam(this.form).then(response => {
            //   this.msgSuccess("修改成功");
            //   this.open = false;
            //   this.getList();
            // });
          } else {
            this.form.status = this.ProjectStatus.DaiQueRen;

            // addTeam(this.form).then(response => {
            //   this.msgSuccess("新增成功");
            //   this.open = false;
            //   this.getList();
            // });
          }
        }
      });
    },


    /* 主持的项目 */

    // 多选框选中数据
    handleJoinOrganizationSelectionChange(selection) {
      // this.ids = selection.map(item => item.teamid);
      // this.single = selection.length != 1;
      // this.multiple = !selection.length;
    },


    handleJoinOrganizationAdd() {
      this.resetJoinForm();
      this.joinOrganizationOpen = true;
      this.joinOrganizationTitle = "添加项目参加单位";

    },


    handleJoinOrganizationUpdate(row) {
      this.joinOrganizationOpen = true;
      this.joinOrganizationTitle = "编辑项目参加单位";
      const joinorg = {joid: row.joid, subjectname: row.subjectname,organizationname: row.organizationname,manager: row.manager,funds: row.funds};

      this.joinForm = joinorg;
    },


    /**  删除按钮操作 */
    handleJoinOrganizationDelete(row) {
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
      })
    },

    cancelJoinForm: function () {
      this.joinOrganizationOpen = false;
      this.resetJoinForm();
    },

    submitJoinForm: function () {
      const this_ = this;
      this_.$refs["joinForm"].validate(valid => {
        if (valid) {

          if (this_.joinForm.joid !== undefined) {

            this_.form.projectJoinOrganizationList.forEach(function (item) {
              if (item.joid == this_.joinForm.joid) {
                item.subjectname = this_.joinForm.subjectname;
                item.organizationname = this_.joinForm.organizationname;
                item.manager = this_.joinForm.manager;
                item.funds = this_.joinForm.funds;
              }
            });
            this.msgSuccess("修改成功");
          } else {
            let joid = 0;
            this_.form.projectJoinOrganizationList.forEach(function (item) {
              if (item.joid < joid) {
                joid = item.joid;
              }
            });
            joid = joid - 1;
            const joinorg = {joid: joid, subjectname: this_.joinForm.subjectname,organizationname: this_.joinForm.organizationname,manager: this_.joinForm.manager,funds: this_.joinForm.funds};
            this_.form.projectJoinOrganizationList.push(joinorg);
            this.msgSuccess("添加成功");
          }
          console.log("submit joinForm is ", this.joinForm);
          this_.joinOrganizationOpen = false;
          this.resetJoinForm();
        }
      });
    },

    // 表单重置
    resetJoinForm() {
      this.joinForm = {
        joid: undefined,
        subjectname: undefined,
        organizationname: undefined,
        manager: undefined,
        funds: undefined
      };
      this.resetForm("joinForm");
    },

  }
}
</script>

<style scoped>

</style>
