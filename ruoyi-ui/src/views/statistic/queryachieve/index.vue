<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--考核指标分数设置-->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-tree
            :data="achieveOptions"
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

import Article from "./article";
import Patent from "./patent";
import Thesis from "./thesis";
import Prize from "./prize";
import Standard from "./standard";
import Software from "./software";
import Product from "./product";
import Tech from "./tech";
import Appraisal from "./appraisal";

export default {
  name: "index",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 部门树选项
      achieveOptions: [
        {
          label: '成果类型',
          children: [{label: '专利'}, {label: '获奖'}, {label: '论文'}, {label: '标准'}, {label: '著作'}, {label: '软件著作权'}, {label: '农机新产品'}, {label: '鉴定（评价）成果'}]
        }],
      defaultProps: {
        children: "children",
        label: "label"
      },
      showComponent : "achieve-patent"

    };
  },
  components: {
    "achieve-patent": Patent,
    "achieve-thesis": Thesis,
    "achieve-prize": Prize,
    "achieve-article": Article,
    "achieve-standard": Standard,
    "achieve-software": Software,
    "achieve-product": Product,
    "achieve-tech": Tech,
    "achieve-appraisal": Appraisal
  },

  created() {

  },
  methods: {

    // 节点单击事件
    handleNodeClick(data) {
      console.log("data is ", data);
      if (data.label === "专利") {
        this.showComponent = "achieve-patent";
      }
      else if (data.label === "获奖") {
        this.showComponent = "achieve-prize";
      }

      else if (data.label === "论文") {
        this.showComponent = "achieve-thesis";
      }
      else if (data.label === "标准") {
        this.showComponent = "achieve-standard";
      }
      else if (data.label === "著作") {

        this.showComponent = "achieve-article";
      }
      else if (data.label === "软件著作权") {
        this.showComponent = "achieve-software";
      }
      else if (data.label === "农机新产品") {
        this.showComponent = "achieve-product";
      }
      else if (data.label === "鉴定（评价）成果") {
        this.showComponent = "achieve-appraisal";
      }
      else if (data.label === "农业部主推技术") {
        this.showComponent = "achieve-tech";
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
