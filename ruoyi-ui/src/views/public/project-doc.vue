<template>
<div>
  <el-upload action="#" :http-request="requestUpload" :before-remove="beforeRemove"
             :on-remove="handleOnRemove" :on-preview="handleOnPreview"
             v-bind:file-list="fileList" :before-upload="beforeUpload">
    <el-button v-if="readonly === false" size="small">上传文件<i class="el-icon-upload el-icon--right"></i></el-button>
  </el-upload>
</div>
</template>

<script>
import {downloadBasDoc, uploadBasDoc} from "@/api/public/basdoc";

export default {
  name: "ProjectDoc",
  components: {},
  props:['docType', 'docList','readonly'],
  data() {
    return {
// 遮罩层
      loading: true,
// 传入的参数
// 数据源
      fileList: [],
      timer:''
    };
  },
  created() {
     this.getList();
  },

  methods: {
    /** 查询列表 */
    getList() {
      console.log("加载文件组件" , this.docList);
      this.fileList = [];
      for (let i = 0; i < this.docList.length; i++) {
        let item = this.docList[i];
        this.fileList.push({name:item.docname, url:item.docid});
      }
      console.log("加载文件组件, 显示" , this.fileList);

      this.$emit('changeFileList',this.fileList, this.docType, undefined,undefined);
    },

// 上传。

    requestUpload: function (params) {
      let file = params.file;
      console.log(file);
      let formData = new FormData();
      formData.append('file', file);
      uploadBasDoc(1, formData).then(response => {
        console.log("response.name is ", response.name);
        console.log("response.url is ", response.url);

        this.fileList.push({name:response.name, url:response.url});

        this.$emit('changeFileList',this.fileList, this.docType,"add",response.url);

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
        this.$emit('changeFileList',this.fileList, this.docType,"remove",file.url);
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
          const fileURL = window.URL.createObjectURL(new Blob([response]));
          const fileLink = document.createElement('a');

          console.log("response.data is ", response);

          fileLink.href = fileURL;
          fileLink.setAttribute('download', file.name);
          document.body.appendChild(fileLink);

          fileLink.click();
          URL.revokeObjectURL(fileURL);
        }).catch(console.error);
      });
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
