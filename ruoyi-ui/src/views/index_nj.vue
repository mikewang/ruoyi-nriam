<template>
   <div class="app-container home">

   </div>
</template>

<script>

import {  listMessage,readMessage } from "@/api/audit/message";


export default {
  name: "index",
  components: {},
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
      // 用户表格数据
      messageList: null,
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        messagetime: undefined
      },
      // 表单校验
      rules: {

      }
    };
  },
  watch: {

  },
  created() {
   // this.getList();
  },
  methods: {
    /** 查询合同列表 */
    getList() {
      this.loading = true;
      listMessage(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.messageList = response.rows;
          this.total = response.total;
          this.loading = false;
        }
      );
    },

    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.messageid);
      this.single = selection.length != 1;
      this.multiple = !selection.length;
    },

    /** 设置为已读按钮操作 */
    handleUpdate(row) {
      const messageids = row.messageid || this.ids;
      this.$confirm('是否确认设置已读编号为"' + messageids + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function() {
        return readMessage(messageids);
      }).then(() => {
        this.getList();
        this.msgSuccess("操作成功");
      })
    },

    /** 查看消息来源按钮操作 */
    handleLink(row) {
      this.msgSuccess("跳转到原文的链接");
    }

  }
};
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }
  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }
  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

