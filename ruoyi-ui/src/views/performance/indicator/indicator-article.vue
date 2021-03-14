<template>
  <div class="app-container">
    <el-row>
      <el-row>
        <h3 style="alignment:center" >著作</h3>
      </el-row>
      <el-table v-loading="loading" :data="patentList">
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="专利类型" align="left" prop="patenttype"/>
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
import {deleteIndicatorPrize, listIndicatorPatent, updateIndicatorPatent} from "@/api/performance/indicator";
import IndicatorRelation from "./indicator-relation";

export default {
  name: "indicator-article",
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
      patentList: [],
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
      relationType: "专利"

    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      listIndicatorPatent(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.patentList = response.rows;
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
        indicatorpatentid: undefined,
        patenttype: undefined,
        points: undefined
      };
      this.resetForm("form");
    },

    /** 修改按钮操作 */
    handleUpdate(row) {
      console.log("编辑专利信息", row);

      this.$confirm('是否确认修改 ' + row.patenttype + ' 分数为 ' +  row.points +' ?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return updateIndicatorPatent(row);
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
