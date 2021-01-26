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
            <el-form-item label="项目类型" prop="projecttype">
              <el-autocomplete class="input-with-select"
                               v-model="form.projectTypeLinkText"
                               :fetch-suggestions="queryProjectTypeListSearch"
                               placeholder="请输入项目类型"
                               clearable
                               @select="handleSelectProjectType"
              >
              </el-autocomplete>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目开始日期" prop="projectbegindate">
              <el-date-picker v-model="form.projectbegindate" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目结束日期" prop="projectenddate">
              <el-date-picker v-model="form.projectenddate" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="8">
            <el-form-item label="项目所属部门" prop="organizationid">
              <el-autocomplete class="input-with-select"
                               v-model="form.organizationIDLinkText"
                               :fetch-suggestions="queryDeptListSearch"
                               placeholder="请输入项目所属部门"
                               clearable
                               @select="handleSelectDept"
              >
              </el-autocomplete>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目所属团队" prop="teamid">
              <el-autocomplete class="input-with-select"
                               v-model="form.teamname"
                               :fetch-suggestions="queryTeamListSearch"
                               placeholder="请输入项目所属团队"
                               clearable
                               @select="handleSelectTeam"
              >
              </el-autocomplete>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="项目负责人" prop="projectmanagerid">
              <el-autocomplete class="input-with-select"
                               v-model="form.ProjectManagerIDLinkText"
                               :fetch-suggestions="queryManagerListSearch"
                               placeholder="请输入项目负责人"
                               clearable
                               @select="handleSelectManager"
              >
              </el-autocomplete>
            </el-form-item>
          </el-col>

        </el-row>

        <el-row>
          <el-col :span="24">
            <el-form-item label="备注" prop="memo">
              <el-input v-model="form.memo" placeholder="" type="textarea"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>

    </el-row>
  </div>
</template>

<script>
import {listTeam} from "@/api/project/team";
import {listData} from "@/api/system/dict/data";
import {listDept} from "@/api/system/dept";
import {listUser} from "@/api/system/user";

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
      projectTypeListOptions: [],
      // 数据字典
      deptListOptions: [],
      // 数据字典
      teamListOptions: [],
      userListOptions: [],
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
          const projecttype = {"value": item.dictLabel, "id": item.dictValue};
          listOptions.push(projecttype);
        });

        this.projectTypeListOptions = listOptions;

        listOptions = [];

        listDept().then(response => {
          console.log(response);

          response.data.forEach(function (item) {
            const dept = {"value": item.deptName, "id": item.deptId};
            listOptions.push(dept);
          });
          this.deptListOptions = listOptions;


          listOptions = [];
          listTeam().then(response => {
              console.log(response);
              response.rows.forEach(function (item) {
                const team = {"value": item.teamname, "id": item.teamid};
                listOptions.push(team);
              });
              this.teamListOptions = listOptions;

              listOptions = [];
              listUser().then(response => {
                  console.log(response);
                  response.rows.forEach(function (item) {
                    //console.log("item is ", item);
                    const user = {"value": item.realName, "id": item.userId, "hotKey": item.hotKey};
                    listOptions.push(user);
                  });
                  this.userListOptions = listOptions;

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


    createFilter(queryString) {
      return (item) => {
      //  console.log("item is ", item.hotKey);
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


    queryProjectTypeListSearch(queryString, cb) {

      // 调用 callback 返回建议列表的数据
      var options = this.projectTypeListOptions;

      var results = queryString ? options.filter(this.createFilter(queryString)) : options;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },

    handleSelectProjectType(projecttype) {
      console.log("handleSelectProjectType is " + projecttype["value"]);
      this.form.projecttype = projecttype["id"];
      this.form.projectTypeLinkText = projecttype["value"];
    },


    queryDeptListSearch(queryString, cb) {

      // 调用 callback 返回建议列表的数据
      var options = this.deptListOptions;

      var results = queryString ? options.filter(this.createFilter(queryString)) : options;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },

    handleSelectDept(item) {
      console.log("handleSelectDept is " + item["value"]);
      this.form.organizationid = item["id"];
      this.form.organizationIDLinkText = item["value"];
    },


    queryTeamListSearch(queryString, cb) {

      // 调用 callback 返回建议列表的数据
      var options = this.teamListOptions;

      var results = queryString ? options.filter(this.createFilter(queryString)) : options;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },

    handleSelectTeam(item) {
      console.log("handleSelectTeam is " + item["value"]);
      this.form.teamid = item["id"];
      this.form.teamname = item["value"];
    },


    queryManagerListSearch(queryString, cb) {

      // 调用 callback 返回建议列表的数据
      var options = this.userListOptions;

      var results = queryString ? options.filter(this.createFilter(queryString)) : options;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },

    handleSelectManager(item) {
      console.log("handleSelectManager is " + item["value"]);
      this.form.projectmanagerid = item["id"];
      this.form.ProjectManagerIDLinkText = item["value"];
    },

    projectbegindateSearch() {


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

      let select_user = {"userid": user["userid"], "realName": user["value"]};

      console.log("handleSelectUser is ", select_user, this.form.teamAddTeamroleName);

      let memberList = this.form.memberList;
      let checkedIdList = this.form.checkedIdList;

      console.log(memberList);
      this.form.teamAddUserid = select_user["userid"];
      this.form.teamAddUsername = select_user["realName"];

      // checked userid 存在吗？
      let checked_userid_inserted = 0;
      for (let i = 0; i < checkedIdList.length; i++) {
        let item = checkedIdList[i];
        for (let j = 0; j < item.length; j++) {
          let userid = item[j];
          if (userid == select_user["userid"]) {
            checked_userid_inserted = 1;
            break;
          }

        }
        if (checked_userid_inserted === 1) {
          break;
        }
      }

      if (checked_userid_inserted === 1) {
        this.msgError(select_user["realName"] + " 选中重复，不能再次添加选中");
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
            if (userItem["userid"] == select_user["userid"]) {
              select_user_merged = 1;
              break;
            }
          }
          if (select_user_merged === 0) {
            memberList[i].userList.push(select_user);
            checkedIdList[i].push(select_user["userid"]);
            this.msgSuccess(select_user["realName"] + " 添加并选中完成");
          } else {
            this.msgError(select_user["realName"] + " 重复，不能再次添加");
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
        checkedIdList.push([select_user["userid"]]);
        this.msgSuccess(select_user["realName"] + " 添加之新角色并选中完成");
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
