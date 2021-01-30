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
        <el-row  v-if="form.jointype===1">
          <el-col :span="24">
            <el-form-item label="项目参加单位列表" prop="memo">
              <el-row :gutter="10" class="mb8">
                <el-col :span="1.5">
                  <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="mini"
                    @click="handleJoinOrganizationAdd"
                    v-hasPermi="['project:zaiyan:add']"
                  >新增
                  </el-button>
                </el-col>
                <el-col :span="1.5">
                  <el-button
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
              <el-table :data="projectJoinOrganizationList" @selection-change="handleJoinOrganizationSelectionChange" style="display:block;">
                <el-table-column type="selection" width="50" align="center"/>
                <el-table-column label="子项目名称" align="center" prop="subjectname"  :show-overflow-tooltip="true"/>
                <el-table-column label="参加单位" align="center" prop="organizationname" />
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
        <el-row  v-if="form.jointype===2">
          <el-col :span="8">
            <el-form-item label="参与项目名称" prop="uplevelproject_subjectname">
              <el-input v-model="uplevelproject.subjectname" placeholder="请输入项目名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="主持单位" prop="uplevelproject_manageorganization">
              <el-input v-model="uplevelproject.manageorganization" placeholder="请输入项目编号"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="负责人" prop="uplevelproject_manager">
              <el-input v-model="uplevelproject.manager" placeholder="请输入项目经费编号"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="经费（元）" prop="uplevelproject_funds">
              <el-input v-model="uplevelproject.funds" placeholder="0.00"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col :span="24">
            <el-form-item label="项目成员" prop="projectmemberList">
              <template>
                <el-checkbox-group v-model="projectmemberList"
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

          <el-col :span="8">
            <el-form-item label="项目申报书" prop="contractFile">
              <el-upload action="#" :http-request="requestUpload1"  :before-remove="beforeRemove1" :on-remove="handleUploadRemove1"
                         :file-list="form.docList1" :before-upload="beforeUpload1" v-hasPermi="['project:zaiyan:edit']">
                <el-button size="small">上传文件<i class="el-icon-upload el-icon--right"></i></el-button>
                     </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目合同" prop="contractFile">

            </el-form-item>
          </el-col>
          <el-col :span="8">

          <el-form-item label="实施方案" prop="contractFile">

            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">

          <el-form-item label="项目批复文件" prop="contractFile">


            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" align="center">
            <el-button  type="success" @click="saveForm">暂 存</el-button>
          <el-button type="primary" @click="submitForm">提交审核</el-button>
          <el-button @click="close">取 消</el-button>
          </el-col>
        </el-row>

      </el-form>

    </el-row>


  </div>
</template>

<script>
import {listTeam} from "@/api/project/team";
import {listData} from "@/api/system/dict/data";
import {listDept} from "@/api/system/dept";
import {listUser} from "@/api/system/user";
import {getProject, addProject,updateProject, checkProject, uploadFile, downloadFile, listProjectjoinorganization, getUplevelProject, listProjectmember,listProjectdoc} from "@/api/project/zaiyan";
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
      addProjectmemberId:undefined,
      projectmemberOptions: [],
      projectmemberList: [],
      ProjectStatus: {XinJianZhong: 48, DaiQueRen: 40},
      projectJoinOrganizationList: [],
      uplevelproject: {},
      // 日期范围
      // 查询参数
      // 表单参数
      form: {
      },
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
          {required: true, message: "项目经费编号不能为空", trigger: "blur"}, {trigger: "change", validator: this.validateSubjectcode}
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
      }
    };

  },



  beforeCreate() {
    const projectid = this.$route.params && this.$route.params.projectid;
    if (Number(projectid) === 0) {
      this.$route.meta.title = "新增项目";
    }
    else {
      this.$route.meta.title = "编辑项目";
    }
  },
  created() {

    const projectid = this.$route.params && this.$route.params.projectid;
    if (Number(projectid) === 0) {

    }
    else {
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
        this.loading = false;
      }
      else {
        let projectid = this.form.projectid;
        getProject(projectid.toString()).then(response => {
          this.form = response.data;
          this.loading = false;

        });

        listProjectjoinorganization({projectid: this.form.projectid}).then(response => {
          let rows = response.data;
          console.log("listProjectjoinorganization is ", rows);
          this.projectJoinOrganizationList = rows;
        });

        getUplevelProject(projectid).then(response => {
          this.uplevelproject = response.data;
          console.log("this.uplevelproject is ", this.uplevelproject);

        });

        listProjectmember({projectid: this.form.projectid}).then(response => {
          const this_ = this;
          let rows = response.data;
          console.log("listProjectmember is ", rows);
          this_.projectmemberOptions = rows;
          this_.projectmemberList = [];
          this_.projectmemberOptions.forEach(function (obj) {
            this_.projectmemberList.push(obj.userid);
          });
        });

        listProjectdoc({projectid: this.form.projectid}).then(response => {
          const this_ = this;
          let rows = response.data;
          console.log("listProjectdoc is ", rows);

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
      }
      else {
        if (this.form.subjectcode === undefined) {
          callback();
        }
        else {
          let query = {subjectcode : value, projectid: this.form.projectid};
          let res = await this.getSubjectcode(query);
          console.log(res);
          if (res.data > 0) {
            callback(new Error("项目经费编号重复"));
          }
          else {
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

        //项目申报书
        docList1:[]
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
        const  queryString = v.toLowerCase();
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

          for (let i=0; i < this.projectmemberList.length; i++){
          let item = this.projectmemberList[i];
          if (value === item) {
            added = true;
            break;
          }
        }

        if (added) {
          this.$message.info("项目成员已存在。");
        }
        else {
          this.projectmemberList.push(value);

          for (let i=0; i < this.projectmemberOptions.length; i++){
            let item = this.projectmemberOptions[i];
            if (value === item.userid) {
              added = true;
              break;
            }
          }

          if (added) {
            this.$message.info("项目成员已选择。");
          }
          else {
            var realName = "";
            for (let i=0; i < this.userList.length; i++){
              let item = this.userList[i];
              if (value === item.id) {
                realName = item.value;
                break;
              }
            }
            const  member = {userid:value,realName:realName}
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


    /* 主持的项目 */

    // 多选框选中数据
    handleJoinOrganizationSelectionChange(selection) {
      this.ids = selection.map(item => item.teamid);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },



    handleJoinOrganizationAdd() {

    },


    handleJoinOrganizationUpdate(row) {


    },


    /**  删除按钮操作 */
    handleJoinOrganizationDelete(row) {
      // const teamIds = row.teamid || this.ids;
      // console.log("teamIds is ", teamIds);
      // this.$confirm('是否确认删除团队编号为"' + teamIds + '"的数据项?', "警告", {
      //   confirmButtonText: "确定",
      //   cancelButtonText: "取消",
      //   type: "warning"
      // }).then(function () {
      //   return deleteTeam(teamIds);
      // }).then(() => {
      //   this.getList();
      //   this.msgSuccess("删除成功");
      // })
    },






    /* 项目书上传 */
    beforeUpload1(file) {

      let x = true
      let fileList = this.form.docList1;

      console.log("fileList is ", fileList);
      for (let i=0; i< fileList.length; i++) {
        let item = fileList[i];
        if (item.name === file.name) {
          console.log("file existed now ,", file.name);
          x = false;
          break;
        }
      }
      return x;

    },

    requestUpload1: function (params) {
      let file = params.file;
      console.log(file);
      let formData = new FormData();
      formData.append('file', file);
      uploadFile(formData).then(response => {
        console.log("response", response.name);
        console.log("response", response.url);
        console.log(this.form.docList1);
        this.form.docList1.push({name: response.name, url: response.url});
      });
    },

    beforeRemove1(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeRemove1 index=" + index, file.name);
      return true;
    //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleUploadRemove1(file) {

      let today = new Date();

      let index = this.form.docList1.indexOf(file);
      if (index !== -1) {
        this.form.docList1.splice(index,1);
      }

      console.log("handleUploadRemove index=" + index, file.name, today.toDateString());
      console.log("this.form.docList1 is ", this.form.docList1);
      return;
    },

    beforeUpload(file) {

      this.form.docList1.forEach(
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
      this.$router.push({ path: "/project/zaiyan" });
    },


    /** 提交按钮 */
    saveForm: function () {

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          if (this.form.projectid !== undefined) {
            updateProject(this.form).then(response => {
              this.msgSuccess("修改成功");
              getProject(this.form.projectid);

            });
          } else {

            this.form.status = this.ProjectStatus.XinJianZhong;

            addProject(this.form).then(response => {

              if (response.data === 0 ) {
                this.msgError("暂存失败");
                this.form.projectid = undefined;
              }
              else {
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
    }

  }
}
</script>

<style scoped>

</style>
