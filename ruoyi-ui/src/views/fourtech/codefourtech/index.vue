<template>
  <div class="app-container">
    <el-row :gutter="20">

    </el-row>
    <!--查询数据-->
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="年度" prop="sheettime">
        <el-date-picker
          type="year"
          format="yyyy"
          value-format="yyyy"
          v-model="queryParams.sheettime"
          placeholder="请输入"
          clearable
          size="small"
          style="width: 240px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="15">
        <el-tag>已审批通过的四技合同列表</el-tag>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getContractList"></right-toolbar>
    </el-row>
    <el-row :gutter="20">
      <el-table v-loading="loading" :data="contractList" stripe>

        <el-table-column type="index" width="50" align="center"/>
        <el-table-column
          label="合同编号"
          align="center"
          width="160"
        >
          <template slot-scope="scope">
            <el-link @click="handleCodeFourtech(scope.row)" type="success" v-if="scope.row.fourtechcode === null||scope.row.fourtechcode === '' ">修改</el-link>
            <el-link @click="handleCodeFourtech(scope.row)" type="success" v-else>{{scope.row.fourtechcode}}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="合同类型" align="center" prop="fourtechtype" :show-overflow-tooltip="true" width="150">
        </el-table-column>
        <el-table-column label="经办人" align="center" prop="sheetuseridlinktext" width="150"/>

        <el-table-column label="提交时间" align="center" prop="sheettime" width="100"/>
        <el-table-column label="项目名称" align="center" prop="fourtechname"/>

        <el-table-column label="部门名称" align="center" prop="organizationidlinktext" width="100"/>
        <el-table-column label="合同金额" align="center" prop="fourtechmoney" width="100"/>
        <el-table-column label="乙方单位" align="center" prop="coperationunit" width="100"/>
        <el-table-column label="合同状态" align="center" prop="sheetstatuslinktext" width="150">
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="160"
          class-name="small-padding fixed-width"
        >
          <template slot-scope="scope">
            <el-button size="mini"
                       type="text"
                       icon="el-icon-edit"
                       @click="handleAuditFourtech(scope.row)"
                       v-hasPermi="['fourtech:codefourtech:list']"
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
        @pagination="getContractList"
      />

    </el-row>

    <!-- 添加或修改菜单对话框 -->
    <template>
    <el-dialog :title="title" :visible.async="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="合同编号" prop="fourtechcode">
              <el-input v-model="form.fourtechcode" placeholder="请输入合同编号" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    </template>
  </div>

</template>

<script>
import {listFourtech_ShenPiWanCheng, updaeCodefourtech} from "@/api/fourtech/fourtech";

export default {
  name: "sheet_ShenPiWanCheng_index",
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
      contractList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      // 科技成果类型
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sheettime: undefined
      },

      SheetStatus: {
        NoPass: 2,
        XiangMuShenPi: 3,
        BuMenShenPi: 4,
        ChuShenPi: 5,
        FenGuanSuoShenPi: 6,
        SuoZhangShenPi: 7,
        ShenPiWanCheng: 8,
        YiZuoFei: 9,
        XinJianZhong: 17,
        YiQianDing: 30,
        FuKuanWanCheng: 31,
        ShenQingZuoFei: 34,
        ChuShen: 47
      },

      // 表单参数
      form: {},
      timer: '',
      // 表单校验
      rules: {
        fourtechcode: [
          {required: true, message: "合同编号不能为空", trigger: "blur"}
        ]
      }
    };
  },
  created() {
    this.getContractList();
  },
  methods: {

    getContractList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listFourtech_ShenPiWanCheng(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          console.log("response2 is ", response);

          let sheetList = [];
          let rows = response.rows;
          for (let i = 0; i < rows.length; i++) {
            let row = rows[i];
            sheetList.push(row);
          }

          this.contractList = sheetList;
          this.total = response.total;
          this.loading = false;
        }
      );
    },


    // 表单重置
    reset() {
      this.form = {
        fourtechcode: undefined,
        fourtechid: undefined
      };
      this.resetForm("form");
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      console.log("year is ", this.queryParams.projectyear);
      this.getContractList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },

    handleCodeFourtech(row) {
      this.reset();
      console.log("CodeFourtech row is  ", row);
      this.title = "请修改合同编号";
      this.form.fourtechcode = row.fourtechcode;
      this.form.fourtechid = row.fourtechid;
      this.form.fourtechname = row.fourtechname;
      this.form.sheetuserid = row.sheetuserid;
      this.open = true;
    },

    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          updaeCodefourtech(this.form).then(response =>{
            this.open = false;
            this.getContractList();
          });

        }
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    handleAuditFourtech(row) {
      console.log("update row is  ", row);
      const path = '/fourtech/tijiaoren/' + row.fourtechid;
      console.log("path is " + path);
      this.$router.push({path: path});
    }
  }
}
</script>

<style scoped>

</style>
