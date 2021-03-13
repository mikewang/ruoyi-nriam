<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--考核指标分数设置-->
      <el-col :span="6" :xs="24">
        <div class="head-container">
          <el-tree
            :data="indicatorOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>
      <!--指标数据-->
      <el-col :span="18" :xs="24">

        <!--动态切换显隐，组件-->
        <component :is="showComponent"></component>

      </el-col>
    </el-row>
  </div>


</template>

<script>

import IndicatorProject from "./indicator-project";
import IndicatorFund from "./indicator-fund";

export default {
  name: "index",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 部门树选项
      indicatorOptions: [{
        label: '科研投入',
        children: [{label: '科研项目'}, {label: '科研经费'}]
      },
        {
          label: '科研产出',
          children: [{label: '获奖'}, {label: '专利'}, {label: '论文'}, {label: '标准'}, {label: '著作'}, {label: '软件著作权'}, {label: '农机新产品'}, {label: '鉴定（评价）成果'}, {label: '完成人排序系数'}]
        },
        {
          label: '成果转化',
          children: [{label: '农业部主推技术'}, {label: '科技产业开发收入'}, {label: '科技转让收入'}]
        }],
      defaultProps: {
        children: "children",
        label: "label"
      },
      showComponent : "indicator-project"

    };
  },
  components: {
    "indicator-project": IndicatorProject,
    "indicator-fund": IndicatorFund
  },
  created() {

  },
  methods: {

    // 节点单击事件
    handleNodeClick(data) {
      console.log("data is ", data);

      if (data.label === "科研项目") {
        this.showComponent = "indicator-project";
      }
      else if (data.label === "科研经费") {
        this.showComponent = "indicator-fund";
      }
      else {
        this.showComponent = "";
      }

    },
  }
}
</script>

<style scoped>

</style>
