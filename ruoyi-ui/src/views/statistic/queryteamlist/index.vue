<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--用户数据-->
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="团队名称" prop="teamname">
          <el-input
            v-model="queryParams.teamname"
            placeholder="请输入团队名称"
            clearable
            size="small"
            style="width: 240px"
            @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">

        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['project:team:list']"
          >导出
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="teamList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column label="团队名称" align="center" prop="teamname" width="300"/>
        <el-table-column label="团队负责人" align="center" prop="teamleaderidlinktext" width="120">
          <template slot-scope="scope">
            <el-link type="primary">{{ scope.row.teamleaderidlinktext }}</el-link>
          </template>

        </el-table-column>
        <el-table-column label="团队成员" align="left" prop="members">
          <template slot-scope="scope">
            <template v-for="item in scope.row.memberList" >
              <el-tag type="info">{{item.teamroleName}} </el-tag>
              <el-link type="primary" v-for="user in item.userList" style="padding: 8px"> {{user.realName}} </el-link>
              <br/>
            </template>
          </template>
        </el-table-column>
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
              icon="el-icon-view"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['project:team:list']"
            >查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination
        v-show="total>0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
    </el-row>


  </div>
</template>

<script>
import {addTeam, listTeam, updateTeam} from "@/api/project/team";
import {listUser} from "@/api/system/user";

export default {
  name: "pmteam",
  // components: {  },
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
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      teamList: null,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      teamroleListOptions: [],
      userList: [],
      userOptions: [],
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        teamname: undefined
      },
      // 表单参数
      form: {},
      timer: '',
      // 表单校验
      rules: {}
    };
  },
  watch: {

    form() {

    }

  },
  created() {

    this.getList();

  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listTeam(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.teamList = response.rows;
          console.log("response rows is ", this.teamList);
          this.total = response.total;
          this.loading = false;
        }
      );
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
        teamleaderidlinktext: undefined,
        createuseridlinktext: undefined,
        memberList: [],
        checkedIdList: [],
        teamAddUsername: undefined,
        teamAddUserid: undefined,
        teamAddTeamroleName: undefined
      };

      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
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

    queryUserListSearch(queryString, cb) {

      var queryParams = {
        pageNum: 1,
        pageSize: 30,
        realName: queryString
      };
      listUser(queryParams).then(response => {
          var userListOptions = [];
          const userList = response.rows;
          userList.forEach(function (user) {
            var item = {"value": user.realName, "userid": user.userId};
            userListOptions.push(item);
          });
          cb(userListOptions);
        }
      );

      // var userListOptions = this.userListOptions;
      // var results = queryString ? userListOptions.filter(this.createFilter(queryString)) : userListOptions;
      // // 调用 callback 返回建议列表的数据
      // cb(results);
    },

    handleSelectTeamLeader(user) {
      let leader = {"userid": user["userid"], "realName": user["value"]};
      this.form.teamleaderid = leader.userid;
      this.form.teamleaderidlinktext = leader.realName;
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
};
</script>

<style scoped>


</style>
