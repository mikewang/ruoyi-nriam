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
            type="danger"
            icon="el-icon-delete"
            size="mini"
            :disabled="multiple"
            @click="handleDelete"
            v-hasPermi="['project:team:remove']"
          >删除</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['project:team:export']"
          >导出</el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="teamList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="团队名称" align="center" prop="teamname" width="300" />
        <el-table-column label="团队负责人" align="center" prop="teamLeaderRealName" width="120" />
        <el-table-column label="团队成员" align="left" prop="members" > </el-table-column>
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
              v-hasPermi="['project:team:edit']"
            >编辑</el-button>
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

    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px" :key="timer">
        <el-row>
          <el-col :span="24">
            <el-form-item label="团队名称" prop="teamname">
              <el-input v-model="form.teamname" placeholder="请输入团队名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="团队负责人" prop="teamLeaderRealName">
              <el-input v-model="form.teamLeaderRealName" placeholder="请输入团队负责人名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建人员" prop="createUserRealName">
              <el-input v-model="form.createUserRealName" placeholder="请输入创建人员名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="团队成员" >
              <template>
              <div v-for="(item, index) in formMemberList" :key="index">
                <el-tag>{{ item.teamroleName }} {{ formCheckedIdList[index] }}</el-tag>
<!--                <el-tag>{{item.userList}} </el-tag>-->
<!--                <el-label v-for="data in item.userList" name="data.realName"  >{{data.userid}}{{data.realName}} </el-label>-->
                <el-checkbox-group v-model="formCheckedIdList[index]" @change="handleCheckedIdListChange(item.teamrole,index)" :key="timer">
                  <el-checkbox  v-for="data in item.userList" :label="data.userid" :key="data.userid"  @change="handleCheckedUseridChange(index,data.userid)" >{{data.realName}}</el-checkbox>
                </el-checkbox-group>
              </div>
              </template>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="添加成员" >
              <template>
                <el-autocomplete  class="input-with-select"
                                  v-model="form.teamAddUsername"
                                  :fetch-suggestions="queryUserListSearch"
                                  placeholder="请输入人员名称"
                                  @select="handleSelectUser"
                >


                </el-autocomplete>
              </template>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="团队角色" >
              <template>
                <el-select v-model="form.teamAddTeamroleName" placeholder="请选择" @change="changeTeamAddTeamrole" :key="timer">
                  <el-option
                    v-for="item in this.teamroleListOptions"
                    :key="item.dictValue"
                    :label="item.dictLabel"
                    :value="item.dictLabel">
                  </el-option>
                </el-select>
              </template>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listTeam, addTeam, updateTeam } from "@/api/project/team";
import {getToken } from "@/utils/auth";
import {getUser, listUser} from "@/api/system/user";
import {listData} from "@/api/system/dict/data";

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
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        teamname: undefined
      },
      formMemberList :[],
      formCheckedIdList :[],
      // 表单参数
      form: {
      },
      timer:'',
      // 表单校验
      rules: {
        teamname: [
          { required: true, message: "团队名称不能为空", trigger: "blur" }
        ],
        teamLeaderRealName: [
          { required: true, message: "团队负责人不能为空", trigger: "blur" }
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
  watch: {

    form() {

    },

    formCheckedIdList(newValue, oldValue) {
      console.log("formCheckedIdList oldValue", oldValue);
      console.log("formCheckedIdList newValue", newValue);

    }

  },
  created() {
    this.getList();
    listData({"dictType": "团队角色"}).then(response => {
      console.log(response);
      this.teamroleListOptions = response.rows.sort(function (a,b) {
       return a.dictValue < b.dictValue

      });

    });
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      listTeam(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.teamList = response.rows;
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
        teamLeaderRealName: undefined,
        createUserRealName: undefined,
        memberList: [],
        checkedIdList: [],
        teamAddUsername:undefined,
        teamAddUserid:undefined,
        teamAddTeamroleName: undefined
      };
      this.formCheckedIdList =[];
      this.formMemberList = [];

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
      this.ids = selection.map(item => item.userId);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加团队";
    },

    /**  删除按钮操作 */
    handleDelete(row) {

    },

    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有团队数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return exportUser(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    },

    handleUpdate(row) {
      this.reset();

      this.form = row;

      console.log("this.formCheckedIdList is ", this.formCheckedIdList);

      this.formMemberList = row.memberList;
      this.formCheckedIdList = [];

      for (let i = 0; i < this.formMemberList.length; i++) {
        let checkArr = []
        let item = this.formMemberList[i].userList
        if (item.length === 0) {
          this.formCheckedIdList.push([])
        } else {
          for (let j = 0; j < item.length; j++) {
            // checkArr.push(item[j].userid)
          }
          this.formCheckedIdList.push(checkArr)
        }
      }
      console.log("checkedIdList is ", this.formCheckedIdList);

      this.form.teamAddTeamroleName = this.teamroleListOptions[0].dictLabel;
      console.log("teamroleListOption default is ", this.teamroleListOptions[0].dictLabel);

      this.open = true;
      this.title = "编辑团队";

    },


    handleCheckedIdListChange (value,index) {
      console.log("change is ", value,index);
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

      this.timer=new Date().getTime();

      console.log(this.formCheckedIdList);
    },

    handleCheckedUseridChange(index,userid) {

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
            var item = {"value":user.realName, "userid" : user.userId};
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

    handleSelectUser(user) {

      console.log(user, this.form.teamAddTeamroleName);
      var index = 0;
      let cc = this.formMemberList.length;

      for (let i=0; i< cc; i++){
        let item = this.formMemberList[i];
        index = i;
        console.log("formMember is ", item.teamrole, item.teamroleName);
        if (item.teamroleName === this.form.teamAddTeamroleName){

          let arr = this.formMemberList[i].userList;
          console.log("handleSelectUser", arr);
          this.formMemberList[i].userList.push({"userid":user["userid"],"realName":user["value"]});
          this.formCheckedIdList[i].push(user["userid"]);
          index = cc;
          break;
        }
      }

      this.form.teamAddUserid = user["userid"];
      this.form.teamAddUsername = user["value"];

      if (index !== cc ) {

        let teamrole = '';

        for (let item in this.teamroleListOptions) {
          if (item.dictLabel === this.form.teamAddTeamroleName) {
            teamrole = item.dictValue;
          }
        }

        let add_user = {"userid":user["userid"],"realName":user["value"]};
        let formMember = {"teamrole":teamrole,"teamroleName":this.form.teamAddTeamroleName,"userList":[add_user]};

        console.log("this step now?", this.formMemberList);
        this.formMemberList.push(formMember);
        this.formCheckedIdList.push([user["userid"]]);
        console.log("this step now?", formMember);
      }



    },

    changeTeamAddTeamrole: function (){
      console.log("changeTeamAddTeamrole is working.");
      this.timer =  new Date().getTime();
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
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
