<template>
  <div class="app-container">

    <el-row :gutter="20">
      <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="160px" :key="timer">
        <el-row>
          <el-col :span="8">
            <el-form-item label="项目名称" prop="projectname">
              <el-input v-model="form.projectname" placeholder="请输入项目名称"/>
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
                         filterable :filter-method="filterDeptOptions">
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
                    @click="handleAdd"
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
                    @click="handleDelete"
                    v-hasPermi="['project:zaiyan:remove']"
                  >删除
                  </el-button>
                </el-col>
                <right-toolbar @queryTable="getList"></right-toolbar>
              </el-row>
              <el-table :data="projectList" @selection-change="handleSelectionChange" style="display:block;">
                <el-table-column type="selection" width="50" align="center"/>
                <el-table-column label="子项目名称" align="center" prop="projectname"  :show-overflow-tooltip="true"/>
                <el-table-column label="参加单位" align="center" prop="projectcode" />
                <el-table-column label="负责人" align="center" prop="projectManagerIDLinkText" width="100"/>
                <el-table-column label="经费（元）" align="center" prop="statusLinkText" width="100"/>
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
                      @click="handleUpdate(scope.row)"
                    >编辑
                    </el-button>
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-delete"
                      @click="handleDelete(scope.row)"
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
            <el-form-item label="参与项目名称" prop="projectname">
              <el-input v-model="form.projectname" placeholder="请输入项目名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="主持单位" prop="projectcode">
              <el-input v-model="form.projectcode" placeholder="请输入项目编号"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="负责人）" prop="subjectcode">
              <el-input v-model="form.subjectcode" placeholder="请输入项目经费编号"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="经费（元）" prop="subjectcode">
              <el-input v-model="form.subjectcode" placeholder="请输入项目经费编号"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row >
          <el-col :span="24">
            <el-form-item label="项目成员" prop="memo">
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
              <el-upload action="#" :http-request="requestUpload" :on-remove="handleUploadRemove"
                         :on-preview="handleUploadPreview"
                         :file-list="form.docList1" :before-upload="beforeUpload" v-hasPermi="['project:zaiyan:edit']">
                <el-button size="small">上传文件<i class="el-icon-upload el-icon--right"></i></el-button>
                     </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目合同" prop="contractFile">
              <el-upload action="#" :http-request="requestUpload" :on-remove="handleUploadRemove"
                         :on-preview="handleUploadPreview"
                         :file-list="form.docList2" :before-upload="beforeUpload" v-hasPermi="['project:zaiyan:edit']">
                <el-button size="small">上传文件<i class="el-icon-upload el-icon--right"></i></el-button>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="8">

          <el-form-item label="实施方案" prop="contractFile">
              <el-upload action="#" :http-request="requestUpload" :on-remove="handleUploadRemove"
                         :on-preview="handleUploadPreview"
                         :file-list="form.docList3" :before-upload="beforeUpload" v-hasPermi="['project:zaiyan:edit']">
                <el-button size="small">上传文件<i class="el-icon-upload el-icon--right"></i></el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">

          <el-form-item label="项目批复文件" prop="contractFile">
            <el-upload action="#" :http-request="requestUpload" :on-remove="handleUploadRemove"
                       :on-preview="handleUploadPreview"
                       :file-list="form.docList4" :before-upload="beforeUpload" v-hasPermi="['project:zaiyan:edit']">
              <el-button size="small">上传文件<i class="el-icon-upload el-icon--right"></i></el-button>
            </el-upload>

            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" align="center">
          <el-button type="primary" @click="submitForm">确 定</el-button>
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
import {uploadFile, downloadFile} from "@/api/project/zaiyan";

export default {
  name: "EditInfo",
  data() {
    return {
      // 遮罩层
      loading: false,
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
      // 日期范围
      // 查询参数
      // 表单参数
      form: {
        docList1:  [{name: 'food.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}, {name: 'food2.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}]
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
          {required: true, message: "项目经费编号不能为空", trigger: "blur"}
        ],
        projectfunds: [
          {required: true, message: "项目总经费不能为空", trigger: "blur"}
        ],
        financefunds: [
          {required: true, message: "财政拨款不能为空", trigger: "blur"}
        ],
        canusefunds: [
          {required: true, message: "可支配经费不能为空", trigger: "blur"}
        ],
        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur"
          }
        ]
      }
    };

  },
  beforeCreate() {
    const dictId = this.$route.params && this.$route.params.projectid;
    this.projectid = dictId;
    if (Number(dictId) === 0) {
      this.$route.meta.title = "新增项目";
    }

  },
  created() {

    this.getList();

  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      console.log("loading is begin.");
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

                  // 停止 转圈
                  this.loading = false;

                }
              );

            }
          );


        })

      });

    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        //项目申报书
        docList1:[],
        teamid: undefined,
        teamname: undefined,
        teamleaderid: undefined,
        memo: undefined,
        createuserid: undefined,
        teamLeaderRealName: undefined,
        createUserRealName: undefined,
        memberList: [],
        checkedIdList: [],
        teamAddUsername: undefined,
        teamAddUserid: undefined,
        teamAddTeamroleName: undefined
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
    handleUploadRemove(file) {

      console.log("handleUploadRemove ", file.name);

      this.form.docList1.forEach((item, index) => {
        if (item.name === file.name) {
          this.form.docList1.splice(index, 1);
        }
      });

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



    close() {
      this.$store.dispatch("tagsView/delView", this.$route);
      this.$router.push({ path: "/project/zaiyan" });
    },




    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      console.log("year is ", this.queryParams.projectyear);
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.teamid);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({path: '/zaiyan/projectInfo/0'});
      return;
      this.reset();
      this.form.createuserid = this.$store.getters.userId;
      this.form.createUserRealName = this.$store.getters.realName;
      this.open = true;
      this.title = "添加团队";
    },

    /**  删除按钮操作 */
    handleDelete(row) {
      const teamIds = row.teamid || this.ids;
      console.log("teamIds is ", teamIds);
      this.$confirm('是否确认删除团队编号为"' + teamIds + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return deleteTeam(teamIds);
      }).then(() => {
        this.getList();
        this.msgSuccess("删除成功");
      })
    },

    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有团队数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportUser(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    },

    handleUpdate(row) {
      this.reset();
      const teamid = row.teamid || this.ids

      this.form = row;
      this.form.checkedIdList = [];

      let memberList = this.form.memberList;

      let checkedIdList = this.form.checkedIdList;

      console.log("this.form.CheckedIdList is ", this.form.checkedIdList);
      console.log("this.form.memberList is ", memberList);


      for (let i = 0; i < memberList.length; i++) {
        let item = memberList[i].userList;
        if (item.length === 0) {
          checkedIdList.push([])
        } else {
          let checkArr = [];
          for (let j = 0; j < item.length; j++) {
            checkArr.push(item[j].userid);
          }
          checkedIdList.push(checkArr);
        }
      }


      console.log("checkedIdList is ", checkedIdList);

      this.form.teamAddTeamroleName = this.teamroleListOptions[0].dictLabel;
      console.log("teamroleListOption default is ", this.teamroleListOptions[0].dictLabel);

      this.open = true;
      this.title = "编辑团队";

    },


    handleCheckedIdListChange(value, index) {
      console.log("change is ", value, index);
      // this.checkedMemberList = []
      // let arr = this.checkedMemberList
      // for (let i = 0; i < arr.length; i ++) {
      //   let memGroup = arr[i]
      //   if (memGroup.length > 0) {
      //     let obj = {
      //       teamrole: this.memGroup[i].teamrole,
      //       members: []
      //     }
      //     for (let j = 0; j < memGroup.length; j++) {
      //       obj.members.push(memGroup[j])
      //     }
      //     this.checkedMemberList.push(obj)
      //   }
      // }

      // 刷新dialog的组件，否则不渲染。
      this.timer = new Date().getTime();
      console.log(this.form.checkedIdList);
    },

    handleCheckedUseridChange(index, userid) {

    },


    handleSelectUser(user) {

      let select_user = {userid: user.userid, realName: user[value]};

      console.log("handleSelectUser is ", select_user, this.form.teamAddTeamroleName);

      let memberList = this.form.memberList;
      let checkedIdList = this.form.checkedIdList;

      console.log(memberList);
      this.form.teamAddUserid = select_user.userid;
      this.form.teamAddUsername = select_user.realName;

      // checked userid 存在吗？
      let checked_userid_inserted = 0;
      for (let i = 0; i < checkedIdList.length; i++) {
        let item = checkedIdList[i];
        for (let j = 0; j < item.length; j++) {
          let userid = item[j];
          if (userid == select_user.userid) {
            checked_userid_inserted = 1;
            break;
          }

        }
        if (checked_userid_inserted === 1) {
          break;
        }
      }

      if (checked_userid_inserted === 1) {
        this.msgError(select_user.realName + " 选中重复，不能再次添加选中");
        return;
      }

      // role存在吗？
      let select_role_inserted = 0;
      for (let i = 0; i < memberList.length; i++) {
        let item = memberList[i];
        if (item["teamroleName"] === this.form.teamAddTeamroleName) {
          select_role_inserted = 1;
          break;
        }
      }

      if (select_role_inserted === 1) {
        for (let i = 0; i < memberList.length; i++) {
          let item = memberList[i];
          let select_user_merged = 0;
          for (let j = 0; j < item.userList.length; j++) {
            let userItem = item.userList[j];
            if (userItem.userid == select_user.userid) {
              select_user_merged = 1;
              break;
            }
          }
          if (select_user_merged === 0) {
            memberList[i].userList.push(select_user);
            checkedIdList[i].push(select_user.userid);
            this.msgSuccess(select_user.realName + " 添加并选中完成");
          } else {
            this.msgError(select_user.realName + " 重复，不能再次添加");
          }
        }
      } else {
        let teamrole = 0;
        for (let i = 0; i < this.teamroleListOptions.length; i++) {
          let item = this.teamroleListOptions[i];
          if (item.dictLabel === this.form.teamAddTeamroleName) {
            teamrole = Number(item.dictValue);
          }
        }

        let formMember = {
          "teamrole": teamrole,
          "teamroleName": this.form.teamAddTeamroleName,
          "userList": [select_user]
        };

        memberList.push(formMember);
        checkedIdList.push([select_user.userid]);
        this.msgSuccess(select_user.realName + " 添加之新角色并选中完成");
      }

    },

    changeTeamAddTeamrole: function () {
      console.log("changeTeamAddTeamrole is working.");
      this.timer = new Date().getTime();
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          if (this.form.teamid != undefined) {
            updateTeam(this.form).then(response => {
              this.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTeam(this.form).then(response => {
              this.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    }


  }
}
</script>

<style scoped>

</style>
