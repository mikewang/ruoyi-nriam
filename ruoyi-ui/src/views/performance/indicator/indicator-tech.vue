<template>
  <div class="app-container">
    <el-row>
      <h3 style="alignment:center" >农业部主推技术</h3>
    </el-row>
    <el-row>
      <el-form :model="indicatortech" ref="indicatortechForm" :inline="true" label-width="100px">
        <el-col :span="20">
          <el-form-item label="每1项“农业部主推技术”分值 " label-width="200px">
            <el-input type="number" v-model="indicatortech.points" style="alignment: right"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item>
            <el-button type="success" icon="el-icon-check" size="mini" @click="saveIndicator1">设置</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>

    <el-row>
      <!--关联的考核指标：一级指标  二级指标  [lab_Type]-->
      <indicator-relation :indicatortype="relationType"></indicator-relation>
    </el-row>


  </div>
</template>

<script>
import {getIndicatorTech, updateIndicatorTech} from "@/api/performance/indicator";

import IndicatorRelation from "./indicator-relation";

export default {
  name: "indicator-tech",
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
      indicatortech: {indicatortechid:1, points: undefined},

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
        userName: [
          {required: true, message: "用户名称不能为空", trigger: "blur"}
        ]
      },
      // 初始化 关联指标。
      relationType: "农业部主推技术"
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;

      getIndicatorTech(1).then(response => {

        this.indicatortech = response.data;

        // 最后结束刷新。
        this.loading = false;

      });

    },

    saveIndicator1: function () {
      let this_ = this;
      this.$confirm('是否确认更新"' + "农业部主推技术" + '"?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        console.log("this.indicatortech is ", this_.indicatortech);
        updateIndicatorTech(this_.indicatortech).then(response => {
          this_.getList();
          this_.msgSuccess("修改成功");
        });
      });
    }

  }
}
</script>

<style scoped>

</style>
