<template>
  <div class="app-container">
    <el-row>
      <h3 style="alignment:center" >科技转让收入</h3>
    </el-row>
    <el-row>
      <el-form :model="indicatorfund1" ref="indicatorfund1Form" :inline="true" label-width="100px">
        <el-col :span="12">
          <el-form-item label="每 " label-width="200px">
            <el-input type="number" v-model="indicatorfund1.permoney" style="alignment: right"><i slot="suffix">万元</i></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item  label="分值" label-width="100px">
            <el-input  type="number"  v-model="indicatorfund1.points"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item>
            <el-button type="success" icon="el-icon-check" size="mini" @click="saveIndicatorfund1">设置</el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>

    <el-row>
      <!--国家科技计划经费 关联的考核指标：一级指标  二级指标  [lab_Type]-->
      <indicator-relation :indicatortype="relationType"></indicator-relation>
    </el-row>

  </div>
</template>

<script>
import {getIndicatorFund, updateIndicatorFund} from "@/api/performance/indicator";

import IndicatorRelation from "./indicator-relation";

export default {
  name: "indicator-incomedevelop",
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
      indicatorfund1: {indicatorfundid:1,permoney:undefined,points: undefined},

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
      relationType: "科技转让收入"
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;

      getIndicatorFund(3).then(response => {

        this.indicatorfund1 = response.data;

        // 最后结束刷新。
        this.loading = false;
      });

    },

    saveIndicatorfund1: function () {
      let this_ = this;
      this.$confirm('是否确认更新"' + "科技转让收入" + '"?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        console.log("this.indicatorfund1 is ", this_.indicatorfund1);
        updateIndicatorFund(this_.indicatorfund1).then(response => {
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
