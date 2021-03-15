<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--考核指标分数设置-->
      <el-col :span="4" :xs="24">
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
      <el-col :span="20" :xs="24">

        <!--动态切换显隐，组件-->
        <component :is="showComponent"></component>

      </el-col>
    </el-row>
  </div>


</template>

<script>

import IndicatorProject from "./indicator-project";
import IndicatorFund from "./indicator-fund";
import IndicatorPrize from "./indicator-prize";
import IndicatorPatent from "./indicator-patent";

import IndicatorThesis from "./indicator-thesis";
import IndicatorStandard from "./indicator-standard";
import IndicatorArticle from "./indicator-article";
import IndicatorSoftware from "./indicator-software";
import IndicatorProduct from "./indicator-product";
import IndicatorAppraisal from "./indicator-appraisal";
import IndicatorOrderrate from "./indicator-orderrate";
import IndicatorTech from "./indicator-tech";
import IndicatorIncomedevelop from "./indicator-incomedevelop";
import IndicatorIncometransfer from "./indicator-incometransfer";


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
    "indicator-fund": IndicatorFund,
    "indicator-prize": IndicatorPrize,
    "indicator-patent": IndicatorPatent,

    "indicator-thesis": IndicatorThesis,
    "indicator-standard": IndicatorStandard,
    "indicator-article": IndicatorArticle,
    "indicator-software": IndicatorSoftware,
    "indicator-product": IndicatorProduct,
    "indicator-appraisal": IndicatorAppraisal,
    "indicator-orderrate": IndicatorOrderrate,
    "indicator-tech": IndicatorTech,
    "indicator-incomedevelop": IndicatorIncomedevelop,
    "indicator-incometransfer": IndicatorIncometransfer

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
      else if (data.label === "获奖") {
        this.showComponent = "indicator-prize";
      }
      else if (data.label === "专利") {
        this.showComponent = "indicator-patent";
      }
      else if (data.label === "论文") {
        this.showComponent = "indicator-thesis";
      }
      else if (data.label === "标准") {
        this.showComponent = "indicator-standard";
      }
      else if (data.label === "著作") {
        this.showComponent = "indicator-article";
      }
      else if (data.label === "软件著作权") {
        this.showComponent = "indicator-software";
      }
      else if (data.label === "农机新产品") {
        this.showComponent = "indicator-product";
      }
      else if (data.label === "鉴定（评价）成果") {
        this.showComponent = "indicator-appraisal";
      }
      else if (data.label === "完成人排序系数") {
        this.showComponent = "indicator-orderrate";
      }
      else if (data.label === "农业部主推技术") {
        this.showComponent = "indicator-tech";
      }
      else if (data.label === "科技产业开发收入") {
        this.showComponent = "indicator-incomedevelop";
      }
      else if (data.label === "科技转让收入") {
        this.showComponent = "indicator-incometransfer";
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
