<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" :rules="queryRules" ref="queryForm" :inline="true" v-show="showSearch"
               label-width="168px">
        <el-form-item label="筛选条件: 年度" prop="projectyear">
          <el-date-picker type="year" value-format="yyyy" v-model="queryParams.projectyear" clearable/>
        </el-form-item>
        <el-form-item label="所属团队" prop="teamid">
          <!-- 所属团队组件-->
          <team-data :selected-team-id="queryParams.teamid" :join-team-user-id="undefined"
                     @changeTeamId="selectTeamId"></team-data>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span="2.5">
          <el-button
            type="primary"
            icon="el-icon-check"
            size="mini"
            :disabled="multiple"
            @click="handleConfirm"
            v-hasPermi="['performance:perfundconfirm:list']"
          >确认到账
          </el-button>
        </el-col>

        <el-col :span="2.5">
          <el-button
            type="warning"
            icon="el-icon-download"
            size="mini"
            @click="handleExport"
            v-hasPermi="['performance:perfundconfirm:list']"
          >导出Excel
          </el-button>
        </el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="performanceList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column label="所属团队" align="center" prop="teamname" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="年度" align="center" prop="projectyear" width="60"/>
        <el-table-column label="项目名称" align="center" prop="projectname"/>
        <el-table-column label="项目编号" align="center" prop="projectcode" width="180"/>
        <el-table-column label="项目经费编号" align="center" prop="subjectcode"/>
        <el-table-column label="负责人" align="center" prop="projectManagerIDLinkText" width="100">

        </el-table-column>

        <el-table-column label="总经费（元）" align="center" prop="projectfunds" width="100">

        </el-table-column>

        <el-table-column label="到账可支配经费（元）" align="center" width="200">
          <template slot-scope="scope">
            <span v-if="scope.row.fundreport.status==='已确认'">
               <el-input  readonly v-model="scope.row.fundreport.fund" placeholder="0.00"></el-input>
            </span>
            <span v-else>
            <el-input  @focus="changeFundreport(scope.row)" v-model="scope.row.fundreport.fund" placeholder="0.00"></el-input>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="fundreport.status" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.fundreport.status==='已确认'">
               {{scope.row.fundreport.status}}
            </span>
            <span v-else style="color: red">
                {{scope.row.fundreport.status}}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-check"
              @click="handleConfirm(scope.row)"
              v-hasPermi="['performance:perfundconfirm:list']"
            >确认到账
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
    <el-row>
      <br/>
      <br/>
      <br/>
    </el-row>

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="400px" append-to-body>
      <template>
        <el-form ref="form" :model="form" :rules="rules" :key="timer">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="" prop="fund">
                <el-input ref="fundinput" type="number" v-model="form.fund" placeholder="0.00"/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </template>
      <template>
        <el-row>
          <el-col :span="24" align="center">
            <el-button type="success" @click="saveForm">确 定</el-button>
            <el-button @click="closeForm">取 消</el-button>
          </el-col>
        </el-row>

      </template>

    </el-dialog>


  </div>
</template>

<script>
import {
  listFundconfirm,updateFundreport,addFundreport,confirmFundreport
} from "@/api/performance/teamperformance";
import BasDoc from "../../public/bas-doc"
import TeamData from "../../public/team-data";
import {delContract, exportContract} from "@/api/logis/contract";



export default {
  name: "perfund_confirm_index",
  components: {"bas-doc": BasDoc, "team-data": TeamData},
  data() {
    return {
      // 各个组件的只读和隐藏属性控制
      readonly: {},
      hidden: {},
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
      performanceList: [],
      performanceListOption: [],

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userid: undefined,
        teamid: undefined,
        teamidlinktext: undefined,
        projectyear: undefined

      },
      queryRules: {
        projectyear: [
          {required: true, message: "年度不能为空", trigger: "blur"}
        ]
      },
      //

      // 表单参数
      form: {},


      timer: '',
      // 表单校验
      rules: {
        fund: [
          {required: true, message: "不能为空", trigger: "blur"}
        ]
      }


    };
  },
  watch: {},
  created() {

    this.loading = false;
    this.queryParams.userid = this.$store.getters.userId;

  },
  methods: {
    /** 查询列表 */

    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listFundconfirm(this.queryParams).then(response => {
        console.log("response data is ", response);
        this.total = response.total;
        this.performanceList = response.rows;
        this.performanceListOption = [];

        this.loading = false;
      });
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

    changeFundreport(row){

      console.log("row is ", row);
      this.open = true;

      this.title = "修改到账可支配经费";
      this.form = {fund: row.fundreport.fund, fundid:row.fundreport.fundid, projectid:row.projectid, year:row.projectyear};

      let dom = this.$refs.fundinput;

      if (dom !== undefined) {
        dom.focus();
      }
      console.log("this.$refs.fundInput is ", dom);

    },


    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.$refs["queryForm"].validate(valid => {
        if (valid) {
          this.getList();
        }
      });

    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      console.log("selection is ", selection);
      this.ids = selection.map(item => item.fundreport.fundid);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    // 组件方法
    selectTeamId(value) {
      console.log("selectTeamId is ", value);
      this.queryParams.teamid = value.id;
      this.queryParams.teamidlinktext = value.value;

    },



    /** 保存按钮 */
    saveForm() {

      this.$refs["form"].validate(
        valid => {
          if (valid) {
            const this_ = this;
            console.log("保存的数据为", this_.form);
            if (this_.form.fundid === undefined|| this_.form.fundid === null) {
              addFundreport(this_.form).then(response => {
                this_.msgSuccess("保存成功");
                this_.open = false;
                this_.getList();
              });
            }
            else {
              updateFundreport(this_.form).then(response => {
                this_.msgSuccess("修改成功");
                this_.open = false;
                this_.getList();
              });

            }

          }
        }
      );
    },


    /** 关闭按钮 */
    closeForm() {
      this.open = false;
    },

    handleConfirm(row) {
      let fundids = [];

      const this_ = this;
      console.log("handleConfirm is", this.ids, "row is ", row);

      if (row.fundreport) {
        console.log("handleConfirm fundreport is", row.fundreport);

        if (row.fundreport.fundid !== null) {
          fundids = [row.fundreport.fundid]
        }
      }
      else {
        console.log("handleConfirm 2  is", this.ids);

        for (let i=0 ; i < this.ids.length; i++) {
          let fundid = this.ids[i];
          console.log("fundid is ", fundid);
          if (fundid !== null) {
            fundids.push(fundid);
          }
        }
      }

      if (fundids.length > 0) {
        this.$confirm('是否确认已核对了编号为"' + fundids + '"的经费到账情况?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          confirmFundreport(fundids).then(() => {
            this_.msgSuccess("确认成功");
            this_.getList();

          });
        }).catch(() => {

        });
      }
      else {
        this.msgError("确认对账无效");
      }
    },

    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams;
      this.$confirm('是否确认导出所有合同数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return exportContract(queryParams);
      }).then(response => {
        this.download(response.msg);
      })
    },

  }

};
</script>


<style scoped>

</style>
