<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="168px">
        <el-form-item label="专利名称" prop="patentname">
          <el-input v-model="queryParams.patentname"></el-input>
        </el-form-item>
        <el-form-item label="专利号" prop="patentcode">
          <el-input v-model="queryParams.patentcode"></el-input>
        </el-form-item>
        <el-form-item label="专利类型" prop="patenttype">
          <dict-data :dict-type-name="DictTypeNamePatentType"
                     :selected-dict-value="queryParams.patenttype" :data-options="patenttypeOptions"
                     @changeDictValue="changeFormDictType" ></dict-data>
        </el-form-item>
        <el-form-item label="年份">
          <el-date-picker
            v-model="dateRange"
            size="small"
            value-format="yyyy"
            format="yyyy"
            type="monthrange"
            range-separator="-"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          ></el-date-picker>
        </el-form-item>
<!--        <el-form-item label="年份" prop="passtime">-->
<!--          <el-date-picker-->
<!--            type="year"-->
<!--            format="yyyy"-->
<!--            value-format="yyyy"-->
<!--            v-model="queryParams.passtime"-->
<!--            placeholder="请输入"-->
<!--            clearable-->
<!--            size="small"-->
<!--            style="display: block"-->
<!--          />-->
<!--        </el-form-item>-->
        <el-form-item label="所属团队" prop="teamid">
          <!-- 所属团队组件-->
          <team-data :selected-team-id="queryParams.teamid" :join-team-user-id="undefined" @changeTeamId="selectTeamId"></team-data>
        </el-form-item>
        <el-form-item label="专利人" prop="authors">
          <el-input v-model="queryParams.authors"></el-input>
        </el-form-item>
        <el-form-item label="本所排名" prop="ourunitorder">
          <dict-data  :dict-type-name="DictTypeNameOurunitOrder"
                     :selected-dict-value="queryParams.ourunitorder" :data-options="ourunitorderOptions"
                     @changeDictValue="changeFormDictType"></dict-data>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">

        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="achieveList" @selection-change="handleSelectionChange">
        <el-table-column type="index" width="50" align="center" :index="indexMethod"/>
        <el-table-column label="专利名称" align="center" prop="patentname" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="专利号" align="center" prop="patentcode" width="200"/>

        <el-table-column label="专利类型" align="center" prop="patenttype" width="100"/>
        <el-table-column label="专利授权日期" align="center" prop="passtime" width="100"/>

        <el-table-column label="专利人" align="center" prop="authors" />

        <el-table-column label="所属团队" align="center" prop="teamidlinktext" width="100"/>
        <el-table-column label="状态" align="center" prop="statuslinktext" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.statusColor === -1" style="color:red"
                  :key="Math.random()">{{ scope.row.statuslinktext }}</span>
            <span v-else-if="scope.row.statusColor === 1" style="color:green"
                  :key="Math.random()">{{ scope.row.statuslinktext }}</span>
            <span v-else :key="Math.random()">{{ scope.row.statuslinktext }}</span>
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
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['achieve:patent:list']"
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
import {listPatent} from "@/api/achieve/patent";
import TeamData from "@/views/public/team-data";
import DictData from "@/views/public/dict-data";

export default {
  // 专利
  name: "patent",
  components: {"team-data": TeamData, "dict-data": DictData},
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
      achieveList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      DictTypeNamePatentType: "专利类型",
      // 数据字典  专利类型
      patenttypeOptions: [{dictLabel: "发明", dictValue: "发明"}, {dictLabel: "实用新型", dictValue: "实用新型"}, {dictLabel: "外观设计",dictValue: "外观设计"}, {dictLabel: "国际专利", dictValue: "国际专利"}],

      DictTypeNameOurunitOrder: "本所排名",
      ourunitorderOptions: [{dictLabel: 1, dictValue: 1}, {dictLabel: 2, dictValue: 2}, {dictLabel: 3, dictValue: 3}, {dictLabel: 4,dictValue: 4}, {dictLabel: 5, dictValue: 5}],

      AchieveStatus: {DaiQueRen: 36, BuTongGuo: 38, ZhengChang: 37, YiShanChu: 39},
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        teamid: undefined,
        passtime: "2021"
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
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true;
      console.log("queryParams is " , this.queryParams);
      listPatent(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          console.log("response is ", response);
          this.achieveList = response.rows;
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
    indexMethod(index) {
      return (index + 1) + this.queryParams.pageSize*(this.queryParams.pageNum-1);
    },

    /** 新增按钮操作 */
    handleAdd() {
      this.$router.push({path: '/achieve/patent'});
    },

    handleUpdate(row) {
      const patentid = row.patentid
      console.log("edit patent id is ", patentid);
      this.$router.push({path: '/achieve/patent/' + patentid});
    },

    // 组件方法
    // 组件方法
    changeFormDictType(dict) {

      if (dict.type === this.DictTypeNamePatentType) {
        console.log("changeFormDictType is ",dict);
        if (dict) {
          this.queryParams.patenttype = dict.id;
        } else {
          this.queryParams.patenttype = undefined;
        }
      }
      else if (dict.type === this.DictTypeNameOurunitOrder) {
        console.log("changeFormDictType is ", dict);
        if (dict) {
          this.queryParams.ourunitorder = dict.id;
        } else {
          this.queryParams.ourunitorder = undefined;
        }
      }
      else {
        console.error("changeFormDictType  意外 is ", dict);
      }
    },

    selectTeamId(value) {
      console.log("handleSelectTeam is ", value);
      this.queryParams.teamid = value.id;
      this.queryParams.teamidlinktext = value.value;
      this.getList();
    }
  }
};
</script>

<style scoped>

</style>
