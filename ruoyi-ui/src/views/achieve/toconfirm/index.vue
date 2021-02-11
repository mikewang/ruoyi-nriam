<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
        <el-form-item label="成果类型" prop="applytype">
          <el-select v-model="queryParams.applytype" placeholder="请选择"
                     style="display:block;" clearable
                     @change="changeAchieveTypeValue">
            <el-option
              v-for="item in achieveTypeOptions"
              :key="item.link"
              :label="item.info"
              :value="item.info"/>
          </el-select>
        </el-form-item>
        <el-form-item label="所属团队" prop="teamid">
          <el-select v-model="queryParams.teamid" placeholder="请选择所属团队"
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
        <el-form-item label="名称" prop="name">
          <el-input v-model="queryParams.name" clearable/>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="applyList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column label="成果名称" align="center" prop="name" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="类型" align="center" prop="applytype" width="200"/>

        <el-table-column label="提交人" align="center" prop="realName"/>
        <el-table-column label="提交时间" align="center" prop="applytime" width="180"/>

        <el-table-column label="状态" align="center" prop="statuslinktext" width="100">
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
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['achieve:toconfirm:list']"
            >审核
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
import {listAchievetype, listToconfirm} from "@/api/achieve/toconfirm";
import {listTeam} from "@/api/project/team";


export default {
  name: "patent",
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
      // 表格数据
      applyList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      // 科技成果类型
      achieveTypeOptions: [],
      teamOptions: [],
      teamList: [],
      AchieveStatus: {DaiQueRen: 36, BuTongGuo: 38, ZhengChang: 37, YiShanChu: 39},
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        teamid: undefined,
        applytype:undefined,
        name: undefined
      },
      // 表单参数
      form: {},
      // 状态为在研的项目，申请审核 功能按钮是否显示？
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
    listTeam().then(response => {
      console.log(response);
      var listOptions = [];
      response.rows.forEach(function (item) {
        const team = {value: item.teamname, id: item.teamid};
        listOptions.push(team);
      });
      this.teamList = listOptions;
      this.teamOptions = listOptions;
    });
    listAchievetype().then(response => {
      console.log("listAchievetype is ", response);
      this.achieveTypeOptions = response.data;
    });
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listToconfirm(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          console.log("response is ", response);
          this.applyList = response.rows;
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
      this.form = {};

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
      this.ids = selection.map(item => item.patentid);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },


    changeAchieveTypeValue(value) {

      console.log("value is ", value);
      if (value) {
        this.queryParams.applytype = value;
      } else {
        this.queryParams.applytype = undefined;
      }

    },

    clearTeamValue() {
      this.queryParams.teamid = undefined;
    },

    changeTeamValue(value) {

      console.log("value is " + value);
      if (value) {
        this.queryParams.teamid = value;

      } else {
        this.queryParams.teamid = undefined;
      }
    },

    filterTeamOptions(queryString) {
      console.log("filter value is " + queryString);
      this.teamOptions = queryString ? this.teamList.filter(this.createFilter(queryString)) : this.teamList;
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

    /** 按钮操作 */

    handleUpdate(row) {
      console.log("update row is  ", row);

      for(let i = 0; i < this.achieveTypeOptions.length; i++) {
        let item = this.achieveTypeOptions[i];
        if (row.applytype == item.info) {
          const path = '/achieve/'+ item.link + '/confirm/' + row.relatedid + '/' + row.applyid;
          console.log("path is " + path);
          this.$router.push({path: path});
        }
      }



    },

  }
};
</script>

<style scoped>

</style>
