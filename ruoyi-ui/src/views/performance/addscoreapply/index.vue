<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--查询数据-->
      <el-form :model="queryParams" :rules="queryRules" ref="queryForm" :inline="true" v-show="showSearch"
               label-width="168px">
        <el-form-item label="筛选条件: 年度" prop="year">
          <el-date-picker type="year" value-format="yyyy" v-model="queryParams.year" clearable/>
        </el-form-item>
        <el-form-item label="所属团队" prop="teamid">
          <!-- 所属团队组件-->
          <team-data :selected-team-id="queryParams.teamid" :join-team-user-id="queryParams.userid"
                     @changeTeamId="selectTeamId"></team-data>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">
        <el-col :span=12> 团队: {{ this.queryParams.teamidlinktext }}</el-col>
        <el-col :span=4> 年度: {{ this.queryParams.performanceyear }}</el-col>
        <el-col :span=4> 分数: {{ this.performancePointsSum }}</el-col>
        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="performanceList" :span-method="onSpanMethod">
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="团队名称" align="center" prop="teamidlinktext" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="年度" align="center" prop="year" width="100"/>
        <el-table-column label="一级指标" align="center" prop="level1name"/>
        <el-table-column label="二级指标" align="center" prop="level2name" width="180"/>
        <el-table-column label="统计指标" align="center" prop="level3name"/>
        <el-table-column label="统计指标" align="center" prop="applyscores" width="100"/>
        <el-table-column label="申请人" align="center" prop="applyuseridlinktext" width="100"/>
        <el-table-column label="申请时间" align="center" prop="applytime" width="100">
        </el-table-column>
        <el-table-column label="状态" align="center" prop="applystatus" width="100">
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.row)"
              v-hasPermi="['performance:addscoreapply:list']"
            >查看
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
    </el-row>
    <el-row>
      <br/>
      <br/>
      <br/>
    </el-row>
    <el-row :gutter="20">
      <!--父级数据-->
      <el-col :span="6" :xs="24">
        <div class="head-container">
          <h4>绩效考核指标</h4>
        </div>
        <div class="head-container">
          <el-tree
            :data="indicatorTreeOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            default-expand-all
            @node-click="handleNodeClick"
          />
        </div>
      </el-col>

      <!--子级数据-->
      <el-col :span="18" :xs="24">
        <!--查询数据-->

        <el-table v-loading="loading" :data="indicatorOptions" :key="timer">
          <el-table-column label="指标类型" align="center" prop="indicatortype" width="120">
          </el-table-column>
          <el-table-column label="一级指标" align="center" prop="level1name">
          </el-table-column>
          <el-table-column label="二级指标" align="center" prop="level2name">
          </el-table-column>
          <el-table-column label="统计指标" align="center" prop="indicatorname">
          </el-table-column>
          <el-table-column label="单项分值" align="center" prop="score" width="80">
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button v-if="scope.row.ifselfapply"
                         size="mini"
                         type="text"
                         icon="el-icon-plus"
                         @click="handleAddScoreApply(scope.row)"
                         v-hasPermi="['performance:addscoreapply:list']"
              >申请加分
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>

    <!-- 添加或修改对话框 -->
    <el-dialog v-if="open" :title="title" :visible.sync="open" width="800px" append-to-body>
      <template>
        <el-form ref="form" :model="form" :rules="rules" label-width="100px" :key="timer">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="所属团队" prop="teamid">
                <el-input readonly v-model="form.teamidlinktext" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="年度" prop="year">
                <el-input readonly v-model="form.year" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="一级指标" prop="level1name">
                <el-input readonly v-model="form.level1name" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="二级指标" prop="level2name">
                <el-input readonly v-model="form.level2name" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="统计指标" prop="level3name">
                <el-input readonly v-model="form.level3name" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请分数" prop="applyscores">
                <el-input type="number" v-model="form.applyscores" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="内容描述" prop="applycontent">
                <el-input type="textarea" v-model="form.applycontent" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="证明材料" prop="fileList">
                <bas-doc @changeFileList="changeBasDocFileList" :basdoc="form.basdoc"
                         :editable="form.docEditable"></bas-doc>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="申请时间" prop="applytime">
                <el-input readonly v-model="form.applytime" placeholder=""/>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请人" prop="applyuseridlinktext">
                <el-input readonly v-model="form.applyuseridlinktext" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="状态" prop="applystatus">
                <el-input readonly v-model="form.applystatus" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </template>
      <template>
        <el-row>
          <el-col :span="24" align="center">
            <el-button v-if="hidden.deleteBtn === false" type="danger" @click="deleteForm">删 除</el-button>
            <el-button v-if="hidden.changeBtn === false" type="primary" @click="changeForm">修 改</el-button>
            <el-button v-if="hidden.saveBtn === false" type="success" @click="saveForm">确 定</el-button>
            <el-button @click="closeForm">取 消</el-button>
          </el-col>
        </el-row>

      </template>

    </el-dialog>


  </div>
</template>

<script>
import {
  addAddscoreapply,
  deleteAddscoreapply,
  listAddscoreapply,
  updateAddscoreapply
} from "@/api/performance/teamperformance";
import BasDoc from "../../public/bas-doc"
import TeamData from "../../public/team-data";
import {spanRow} from "@/api/performance/spanRow";
import {listIndicatorTree} from "@/api/performance/indicator";


export default {
  name: "addscoreapply_index",
  components: {"bas-doc": BasDoc, "team-data": TeamData},
  data() {
    return {
      // 各个组件的只读和隐藏属性控制
      readonly: {},
      hidden: {},
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
      // 表格数据
      performanceList: [],
      performanceListOption: [],
      performancePointsSum: 0,

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userid: undefined,
        teamid: undefined,
        teamidlinktext: undefined,
        year: undefined

      },
      queryRules: {
        year: [
          {required: true, message: "年度不能为空", trigger: "blur"}
        ],
        teamid: [
          {required: true, message: "所属团队不能为空", trigger: "blur"}
        ]
      },
      //

      // 表单参数
      form: {fileList: []},


      timer: '',
      // 表单校验
      rules: {
        year: [
          {required: true, message: "年度不能为空", trigger: "blur"}
        ],
        teamid: [
          {required: true, message: "所属团队不能为空", trigger: "blur"}
        ],
        applyscores: [
          {required: true, message: "申请分数不能为空", trigger: "blur"}
        ],
        applycontent: [
          {required: true, message: "内容描述不能为空", trigger: "blur"}
        ]
      },

      // 父级树选项
      indicatorTreeOptions: undefined,
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表格数据
      indicatorList: [],
      indicatorOptions: [],
      clickedNodeData: undefined,

    };
  },
  watch: {},
  created() {

    this.loading = false;
    this.hidden = {confirmBtn: true};
    this.queryParams.userid = this.$store.getters.userId;

    this.getIndicatorTree();

  },
  methods: {
    /** 查询列表 */

    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listAddscoreapply(this.queryParams).then(response => {
        console.log("response data is ", response);
        this.total = response.total;
        this.performanceList = response.rows;
        this.performanceListOption = [];

        if (this.clickedNodeData !== undefined) {
          this.handleNodeClick(this.clickedNodeData);
        }

        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {};
      this.resetForm("form");
    },

    onSpanMethod({row, column, rowIndex, columnIndex}) {
      return spanRow(
        {row, column, rowIndex, columnIndex},
        this.performanceList,
        this.performanceListOption
      )
    },


    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.page = 1;
      this.$refs["queryForm"].validate(valid => {
        if (valid) {
          this.getList();
        }
      });

    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },


    // 组件方法
    selectTeamId(value) {
      console.log("selectTeamId is ", value);
      this.queryParams.teamid = value.id;
      this.queryParams.teamidlinktext = value.value;

    },


    handleView(row) {
      console.log("加分申请信息", row);

      this.title = "加分申请信息";
      this.form = row;
      //加载文档
      this.form.fileList = [];
      const kid = this.form.applyid;
      const query = {attachtotype: "加分申请", relatedid: kid, doctype: "证明材料"};
      this.form.basdoc = query;

      if (this.form.applyuserid === this.$store.getters.userId) {
        if (this.form.applystatus === "待审核" || this.form.applystatus === "审核不通过") {
          this.form.docEditable = true;

          this.hidden = {saveBtn: true, changeBtn: false, deleteBtn: false};
        } else {
          this.form.docEditable = false;

          this.hidden = {saveBtn: true, changeBtn: true, deleteBtn: true};
        }
      } else {
        // 非本人 ，只读
        this.form.docEditable = false;

        this.hidden = {saveBtn: true, changeBtn: true, deleteBtn: true};
      }

      this.open = true;

    },

    changeBasDocFileList(fileList) {
      console.log("changeBasDocFileList is ", fileList);
      this.form.fileList = fileList;

    },

    /** 删除按钮 */
    deleteForm() {
      const this_ = this;
      this_.$confirm('是否确认删除 加分申请?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        deleteAddscoreapply(this_.form.applyid).then(() => {
          this_.msgSuccess("删除成功");
          this_.open = false;
          this_.getList();

        });
      });
    },


    /** 修改按钮 */
    changeForm() {
      this.$refs["form"].validate(
        valid => {
          if (valid) {
            const this_ = this;
            if (this_.form.fileList.length === 0) {
              this_.msgError("保存失败，没有上传证明材料");
            } else {
              const docFileList = [];
              for (let i = 0; i < this_.form.fileList.length; i++) {
                let item = this_.form.fileList[i];
                docFileList.push({docid: item.url});
              }

              this_.form.fileList = docFileList;

              console.log("保存的数据为", this_.form);

              updateAddscoreapply(this_.form).then(response => {
                this_.msgSuccess("保存成功");
                this_.open = false;
                this_.getList();
              });
            }
          }
        }
      );
    },


    /** 保存按钮 */
    saveForm() {
      this.$refs["form"].validate(
        valid => {
          if (valid) {
            const this_ = this;
            if (this_.form.fileList.length === 0) {
              this_.msgError("保存失败，没有上传证明材料");
            } else {

              const docFileList = [];
              for (let i = 0; i < this_.form.fileList.length; i++) {
                let item = this_.form.fileList[i];
                docFileList.push({docid: item.url});
              }

              this_.form.fileList = docFileList;

              console.log("保存的数据为", this_.form);
              addAddscoreapply(this_.form).then(response => {
                this_.msgSuccess("保存成功");
                this_.open = false;
                this_.getList();
              });
            }
          }
        }
      );
    },


    /** 关闭按钮 */
    closeForm() {
      this.open = false;
    },


    getIndicatorTree() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listIndicatorTree().then(response => {
        console.log("response is ", response);
        this.indicatorList = response.data;

        this.indicatorTreeOptions = [];
        const rows = response.data;
        for (let i = 0; i < rows.length; i++) {
          let row = rows[i];
          console.log("row is " + row);

          let leve1list = [];
          for (let j = 0; j < row.childList.length; j++) {
            let leve1item = row.childList[j];
            // console.log("level1 is " + leve1item.indicatorname);
            let level2list = [];
            for (let k = 0; k < leve1item.childList.length; k++) {
              let level2item = leve1item.childList[k];
              // console.log("level2 is " + level2item.indicatorname);
              level2list.push({id: level2item.indicatorid, label: level2item.indicatorname, level: 2});
            }

            let leve1dict = {id: leve1item.indicatorid, label: leve1item.indicatorname, children: level2list, level: 1};
            leve1list.push(leve1dict);
          }

          let tree = {id: i + 1, label: row.type, children: leve1list, level: 0};
          this.indicatorTreeOptions.push(tree);
        }


        if (this.clickedNodeData !== undefined) {
          this.handleNodeClick(this.clickedNodeData);
        }

        this.loading = false;
      });
    },
    // 筛选节点
    filterNode(value, data) {

    },
    // 节点单击事件
    handleNodeClick(data) {

      console.log("data.level is ", data.level, data.id, data.label, "indicatorList count is ", this.indicatorList.length);

      this.clickedNodeData = data;
      this.indicatorOptions = [];
      const this_ = this;

      if (data.level === 2) {

        this.indicatorList.forEach(function (item) {
          item.childList.forEach(function (level1item) {
            //console.log("level1item is ", level1item);
            level1item.childList.forEach(function (level2item) {
              //console.log("level2item is ", level2item);
              if (level2item.indicatorid === data.id) {
                level2item.childList.forEach(function (k) {

                  k.indicatortype = item.type;
                  k.level1id = level1item.indicatorid;
                  k.level1name = level1item.indicatorname;
                  k.level2id = level2item.indicatorid;
                  k.level2name = level2item.indicatorname;
                  console.log("show item is ", k);
                  this_.indicatorOptions.push(k);
                });

                this_.indicatorOptions.sort(function (a, b) {
                  return a.ordernumber - b.ordernumber
                });
              }
            });

          });

        });

      } else if (data.level === 1) {

        this.indicatorList.forEach(function (item) {
          item.childList.forEach(function (level1item) {

            if (level1item.indicatorid === data.id) {
              level1item.childList.forEach(function (level2item) {
                if (level2item.childList === null || level2item.childList.length == 0) {
                  const k = level2item;
                  k.indicatortype = item.type;
                  k.level1id = level1item.indicatorid;
                  k.level1name = level1item.indicatorname;
                  k.level2id = level2item.indicatorid;
                  k.level2name = level2item.indicatorname;
                  this_.indicatorOptions.push(k);
                } else {
                  level2item.childList.forEach(function (k) {
                    // console.log(k);
                    k.indicatortype = item.type;
                    k.level1id = level1item.indicatorid;
                    k.level1name = level1item.indicatorname;
                    k.level2id = level2item.indicatorid;
                    k.level2name = level2item.indicatorname;
                    this_.indicatorOptions.push(k);
                  });

                  this_.indicatorOptions.sort(function (a, b) {
                    return a.ordernumber - b.ordernumber
                  });
                }
              });

            }
          });

        });

      } else if (data.level === 0) {
        this.indicatorList.forEach(function (item) {
          console.log("data is ", data, item);
          if (item.type === data.label) {
            item.childList.forEach(function (level1item) {
              level1item.childList.forEach(function (level2item) {
                if (level2item.childList === null || level2item.childList.length == 0) {
                  const k = level2item;
                  k.indicatortype = item.type;
                  k.level1id = level1item.indicatorid;
                  k.level1name = level1item.indicatorname;
                  k.level2id = level2item.indicatorid;
                  k.level2name = level2item.indicatorname;
                  this_.indicatorOptions.push(k);
                } else {
                  level2item.childList.forEach(function (k) {
                    // console.log(k);
                    k.indicatortype = item.type;
                    k.level1id = level1item.indicatorid;
                    k.level1name = level1item.indicatorname;
                    k.level2id = level2item.indicatorid;
                    k.level2name = level2item.indicatorname;
                    this_.indicatorOptions.push(k);
                  });

                  this_.indicatorOptions.sort(function (a, b) {
                    if (a.level1name === b.level1name) {
                      if (b.level2name === a.level2name) {
                        return a.ordernumber - b.ordernumber;
                      }
                      return b.level2name - a.level2name;
                    }
                    return b.level1name - a.level1name;
                  });
                }
              });
            });
          }
        });
      }
    },

    handleAddScoreApply(row) {

      console.log("新增加分申请 is ", row);
      this.title = "新增加分申请"
      this.form = {
        teamid: this.queryParams.teamid,
        teamidlinktext: this.queryParams.teamidlinktext,
        year: this.queryParams.year,
        level1id: row.level1id,
        level1name: row.level1name,
        level2id: row.level2id,
        level2name: row.level2name,
        level3id: row.indicatorid,
        level3name: row.indicatorname,
        applyscores: row.score,
        fileList: [],
        applytime: Date.now().toString(),
        applyuseridlinktext: this.$store.getters.realName,
        applyuserid: this.$store.getters.userId,
        applystatus: "待审核"
      };

      const query = {attachtotype: "加分申请", relatedid: undefined, doctype: "证明材料"};
      this.form.basdoc = query;
      this.form.docEditable = true;

      this.hidden = {saveBtn: false, changeBtn: true, deleteBtn: true};

      this.open = true;
    }
  }

};
</script>


<style scoped>

</style>
