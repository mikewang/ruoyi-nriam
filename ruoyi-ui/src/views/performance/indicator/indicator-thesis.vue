<template>
  <div class="app-container">
    <el-row>
      <el-row>
        <h3 style="alignment:center" >论文</h3>
      </el-row>
      <el-table v-loading="loading" :data="thesisList">
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="论文级别" align="left" prop="thesislevel"/>
        <el-table-column label="分数" align="center" prop="points" width="120">
          <template slot-scope="scope">
            <el-input v-model="scope.row.points"></el-input>
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
              icon="el-icon-check"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['performance:indicator:list']"
            >设置
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
    <el-row>
      <!--关联的考核指标：一级指标  二级指标  [lab_Type]-->
      <indicator-relation :indicatortype="relationType"></indicator-relation>

    </el-row>

  </div>

</template>

<script>
import {listIndicatorThesis, updateIndicatorThesis} from "@/api/performance/indicator";
import IndicatorRelation from "./indicator-relation";

export default {
  name: "indicator-thesis",
  components :{"indicator-relation": IndicatorRelation},
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
      // 项目表格数据
      thesisList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      // 表单校验
      rules: {
        points: [
          {required: true, message: "分数不能为空", trigger: "blur"}
        ]
      },

      // 初始化 关联指标。
      relationType: "学术论文"

    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      listIndicatorThesis(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.thesisList = response.rows;
          this.total = response.total;

        // 最后结束刷新。
        this.loading = false;
        }
      );
    },


    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.getList();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },

    // 表单重置
    reset() {
      this.form = {
        indicatorthesisid: undefined,
        thesislevel: undefined,
        points: undefined
      };
      this.resetForm("form");
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log("编辑学术论文", row);

      this.$confirm('是否确认修改 ' + row.thesislevel + ' 分数为 ' +  row.points +' ?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return updateIndicatorThesis(row);
      }).then(() => {
        this.getList();
        this.msgSuccess("修改成功");
      });

    }




  }
}
</script>

<style scoped>

</style>
