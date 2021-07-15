<template >
  <div>
    <el-row :key="timer" >
    <el-upload
      ref="upload"
      class="avatar-uploader"
      action="#"
      accept="image/png,image/gif,image/jpg,image/jpeg,image/tif"
      :show-file-list="false"
      :auto-upload="false"
      :on-change="handleChange"
      :on-exceed="handleExceed"
      :before-upload="handleBeforeUpload"
      :on-preview="handlePictureCardPreview"
      :on-success="handleSuccess"
      :on-progress="handleProgress"
      :on-error="handleError"
      :on-remove="handleRemove">
      <el-image v-if="this.skuFile.url" :src="this.skuFile.url" class="avatar">
        <div slot="placeholder" class="image-slot">
          加载中<span class="dot">...</span>
        </div>
      </el-image>
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
    </el-row>

<!--    <el-row :key="timer">{{this.timer}}, {{this.skuFile.url}} ,</el-row>-->

  </div>
<!--  <div>-->
<!--    <el-upload-->
<!--      ref="upload"-->
<!--      action="#"-->
<!--      accept="image/png,image/gif,image/jpg,image/jpeg,image/tif"-->
<!--      list-type="picture-card"-->
<!--      :auto-upload="false"-->
<!--      :on-preview="handlePictureCardPreview"-->
<!--      :on-remove="handleRemove">-->
<!--      <i v-if="imageUrl" class="el-icon-plus"></i>-->
<!--    </el-upload>-->
<!--  </div>-->
</template>

<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 2px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 800px;
  height: 800px;
  line-height: 800px;
  text-align: center;
}
.avatar {
  width: 800px;
  height: 800px;
  display: block;
}
</style>

<script>
import {uploadSkuFile} from "@/api/sku/sku";

export default {
  name: "SkuFile",
  components: {},
  props: ['skuFile'],
  data() {
    return {
// 遮罩层
      loading: true,
// 传入的参数
// 数据源
      fileId:'',
      photofile: {},
      timer: ''
    };
  },
  created() {
    this.getList();
  },

  methods: {
    getList() {

      if (this.skuFile.fileName != null) {
        console.log("加载图片 is ", this.skuFile.fileName, "url is " + this.skuFile.url);

        this.timer = (new Date()).toString();
      }
      else {

        this.timer = '';
      }

    },

    handleChange(file,fileList) {
      // console.log('handleChange', file);
      this.skuFile.url = URL.createObjectURL(file.raw);
      this.photofile = file.raw;

      this.uploadFile();
    },

// 上传文件之前的钩子
    handleBeforeUpload(file) {
      console.log('handleBeforeUpload', file)
      if (!(file.type === 'image/png' || file.type === 'image/gif' || file.type === 'image/jpg' || file.type === 'image/jpeg' || file.type === 'image/tif'|| file.type === 'image/ai'|| file.type === 'image/psd')) {
        this.msgError("请上传格式为image/png, image/gif, image/jpg, image/jpeg, image/tif,image/ai, image/psd的图片");
        return false;
      }
      let size = file.size / 1024 / 1024 / 2
      if (size > 2) {

      }
    },
    // 文件超出个数限制时的钩子
    handleExceed(files, fileList) {

    },
    // 文件列表移除文件时的钩子
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    // 点击文件列表中已上传的文件时的钩子
    handlePictureCardPreview(file) {
      return;
    },

    handleSuccess(response, file, fileList) {

      console.log(response);

      this.msgSuccess("上传成功");
    },
    handleProgress(event, file, fileList){
      console.log("正在上传。。。。");
    },
    handleError(err, file, fileList) {
      this.skuFile.url = "";
      this.timer = (new Date()).toString();
      this.msgError(err.toString());
    },

    uploadFile() {

      let formData = new FormData();
      formData.append('file', this.photofile);

      uploadSkuFile(this.skuFile.photoSizeValue, formData).then(response => {
        console.log("response.relativepath is ", response.relativepath);
        console.log("response.fileName is ", response.fileName);
        console.log("response.fileId is ", response.fileId);
        this.msgSuccess(response.fileName + "上传成功");

        this.$emit('changePhotoFile', this.photofile, this.skuFile.photoSizeValue, "add", response.fileId);

        this.timer = (new Date()).toString();

      });
    },

  }

}

</script>

<style scoped>

</style>
