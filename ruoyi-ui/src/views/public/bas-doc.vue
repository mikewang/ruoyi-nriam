<template>
<div>
  <el-upload action="#" :http-request="requestUpload" :before-remove="beforeRemove"
             :on-remove="handleOnRemove" :on-preview="handleOnPreview"
             :file-list="fileList" :before-upload="beforeUpload">
    <el-button v-if="this.readonly === false" size="small">上传文件<i class="el-icon-upload el-icon--right"></i></el-button>
  </el-upload>

</div>
</template>

<script>
import {listBasDoc, downloadBasDoc, uploadBasDoc} from "@/api/public/basdoc";

export default {
  name: "BasDoc",
  components: {},
  props:['basdoc','readonly'],
  data() {
    return {
// 遮罩层
      loading: true,
// 传入的参数

// 数据源
      basDocList: [],
      fileList: []
    };
  },
  created() {
     this.getList();
  },

  methods: {
    /** 查询列表 */
    getList() {
      console.log("加载组件" , this.basdoc );
      this.fileList = [];
      if (this.basdoc.relatedid === undefined) {

      }
      else {
        this.loading = true;
        listBasDoc(this.basdoc).then(response => {
          const data = response.data;
          console.log("listBasDoc response.data is ", data.length);

          if (data) {
            this.basDocList = data;
            for (let i=0; i< data.length; i++) {
              let item = data[i];
              let file = {name:item.docname, url:item.docid};
              this.fileList.push(file);
            }
            if (data.length > 0) {
              console.log("load filelist is ", this.fileList);
              this.$emit('changeFileList',this.fileList);
            }
          }


          this.loading = false;
        });
      }

    },

// 上传。

    requestUpload: function (params) {
      let file = params.file;
      console.log(file);
      let formData = new FormData();
      formData.append('file', file);

      let filepath = 1;

      if (this.basdoc.attachtotype === "专利") {
          filepath = 2;
      }
      if (this.basdoc.attachtotype === "小额经费单") {
        filepath = 2;
      }

      uploadBasDoc(filepath, formData).then(response => {
        console.log("response.name is ", response.name);
        console.log("response.url is ", response.url);
        this.fileList.push({name: response.name, url: response.url});

        this.$emit('changeFileList',this.fileList);

      });


    },

    beforeRemove(file) {
      if (this.readonly === false) {
        return this.$confirm(`确定移除 ${ file.name }？`);
      }
      else {
        return false;
      }
    },

    handleOnRemove(file) {

      let index = this.fileList.indexOf(file);
      if (index !== -1) {
        this.fileList.splice(index, 1);
        this.$emit('changeFileList',this.fileList);
        console.log("remove filelist is ", this.fileList);
      }

    },

    handleOnPreview(file) {
      const this_ = this;
      this.$confirm('是否确认下载"' + file.name + '"的文件?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {

        downloadBasDoc({file: file.url}).then((response) => {
          this_.msgSuccess("下载开始");
          var fileURL = window.URL.createObjectURL(new Blob([response]));
          var fileLink = document.createElement('a');

          console.log("response.data is ", response);

          fileLink.href = fileURL;
          fileLink.setAttribute('download', file.name);
          document.body.appendChild(fileLink);

          fileLink.click();
          URL.revokeObjectURL(fileURL);
        }).catch(console.error);

      })
    },

    beforeUpload(file) {
      let x = true
      for (let i = 0; i < this.fileList.length; i++) {
        let item = this.fileList[i];
        if (item.name === file.name) {
          console.log("文件已存在 ,", file.name);
          x = false;
          break;
        }
      }
      return x;
    }

  }

}

</script>

<style scoped>

</style>
