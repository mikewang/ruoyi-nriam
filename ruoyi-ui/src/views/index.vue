<template>
   <div class="app-container home">
        <el-row :gutter="24">
          <!--用户数据-->
          <el-col :span="24" :xs="24">

            <el-row :gutter="10" class="mb8">
              <el-col :span="1.5">
                <el-button
                  type="primary"
                  icon="el-icon-plus"
                  size="mini"
                  @click="handleAdd"
                  v-hasPermi="['logis:contract:add']"
                >新增
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="success"
                  icon="el-icon-edit"
                  size="mini"
                  :disabled="single"
                  @click="handleUpdate"
                  v-hasPermi="['logis:contract:edit']"
                >修改
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  size="mini"
                  :disabled="multiple"
                  @click="handleDelete"
                  v-hasPermi="['logis:contract:remove']"
                >删除
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="warning"
                  icon="el-icon-download"
                  size="mini"
                  @click="handleExport"
                  v-hasPermi="['logis:contract:export']"
                >导出
                </el-button>
              </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
            </el-row>

            <el-table v-loading="loading" :data="messageList" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="50" align="center"/>
              <el-table-column label="信息ID" align="center" prop="messageid"/>
              <el-table-column label="标题" align="center" prop="messagetitle"/>
              <el-table-column label="内容" align="center" prop="messagecontent" :show-overflow-tooltip="true"/>
              <el-table-column label="创建时间" align="center" prop="messagetime" width="160">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.messagetime) }}</span>
                </template>
              </el-table-column>
              <el-table-column
                label="操作"
                align="center"
                width="160"
                class-name="small-padding fixed-width"
              >
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handleUpdate(scope.row)"
                    v-hasPermi="['logis:contract:edit']"
                  >修改
                  </el-button>
                  <el-button
                    v-if="scope.row.userId !== 1"
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleDelete(scope.row)"
                    v-hasPermi="['logis:contract:remove']"
                  >删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <pagination
              v-show="total>0"
              :total="total"
              :page.sync="queryParams.pageNum"
              :limit.sync="queryParams.pageSize"
              @pagination="getList"
            />
          </el-col>
        </el-row>
   </div>
</template>

<script>

import {  listMessage } from "@/api/audit/message";


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
    this.getList();
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

