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

import Patent from "./patent";



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
      showComponent : "patent"

    };
  },
  components: {
    "patent": Patent,

  },
  created() {

  },
  methods: {

    // 节点单击事件
    handleNodeClick(data) {
      console.log("data is ", data);
      if (data.label === "专利") {
        this.showComponent = "patent";
      }
      else if (data.label === "科研项目") {
        this.showComponent = "indicator-project";
      }
      else if (data.label === "科研经费") {
        this.showComponent = "indicator-fund";
      }
      else if (data.label === "获奖") {
        this.showComponent = "indicator-prize";
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
