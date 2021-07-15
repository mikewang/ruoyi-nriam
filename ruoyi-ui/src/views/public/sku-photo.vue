<template>
<div>
<!--  <el-upload action="#" :http-request="requestUpload" :before-remove="beforeRemove"-->
<!--             :on-remove="handleOnRemove" :on-preview="handleOnPreview"-->
<!--             :file-list="fileList" :before-upload="beforeUpload">-->
<!--    <el-button v-if="this.readonly === false" size="small">上传文件<i class="el-icon-upload el-icon&#45;&#45;right"></i></el-button>-->
<!--  </el-upload>-->
  <el-upload
    class="avatar-uploader"
    style="width:500px;hight:300px"
    accept="image/png,image/jpg,image/jpeg,image/tif"
    action="#"
    :show-file-list="false"
    :auto-upload="false"
    :on-change="changeFile">
    <img v-if="fileitem.photoText" :src="fileitem.photoText" class="avatar">
    <el-button v-else slot="trigger" size="small" type="primary">选取图片 {{fileitem.photoSizeLabel}}</el-button>
<!--    <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>-->
  </el-upload>
</div>
</template>

<script>

export default {
  name: "SkuPhoto",
  components: {},
  props:['item'],
  data() {
    return {
// 遮罩层
      loading: true,
// 传入的参数
      fileitem: this.item
    };
  },
  created() {

    this.loading = false;
  },

  methods: {
    /** 查询列表 */

// 上传。

    changeFile(file, fileList) {
      const this_ = this;

      //this.imageUrl = URL.createObjectURL(file.raw);
      const reader = new FileReader();
      reader.readAsDataURL(file.raw);
      reader.onload = function (e) {
        // 这个就是base64编码了
        // console.log("这个就是base64编码 is ", this.result);
        this_.fileitem.photoText = this.result;
      }
    },

    requestUpload: function (params) {
      let file = params.file;
      console.log(file);
    },

    beforeRemove(file) {
      if (this.readonly === false) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      }
      else {
        return false;
      }
    },

    handleOnPreview(file) {

    },

    beforeUpload(file) {
      let x = true
      return x;
    }

  }

}

</script>

<style scoped>

</style>
