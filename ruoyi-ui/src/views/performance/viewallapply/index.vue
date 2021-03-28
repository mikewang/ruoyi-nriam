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
          <team-data :selected-team-id="queryParams.teamid" :join-team-user-id="undefined"
                     @changeTeamId="selectTeamId"></team-data>
        </el-form-item>
        <el-form-item label="一级指标" prop="level1id">
          <el-select v-model="queryParams.level1id" placeholder="请选择" style="display:block;"
                     clearable @clear="clearLevel1Value" @change="changeLevel1Value"
                     filterable :filter-method="filterLevel1Options">
            <el-option
              v-for="item in indicatorLevel1Options"
              :key="item.id"
              :label="item.value"
              :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="二级指标" prop="level2id">
          <el-select v-model="queryParams.level2id" placeholder="请选择" style="display:block;"
                     clearable @clear="clearLevel2Value" @change="changeLevel2Value"
                     filterable :filter-method="filterLevel2Options">
            <el-option
              v-for="item in indicatorLevel2Options"
              :key="item.id"
              :label="item.value"
              :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="统计指标" prop="level3id">
          <el-select v-model="queryParams.level3id" placeholder="请选择" style="display:block;"
                     clearable @clear="clearLevel3Value" @change="changeLevel3Value"
                     filterable :filter-method="filterLevel3Options">
            <el-option
              v-for="item in indicatorLevel3Options"
              :key="item.id"
              :label="item.value"
              :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>

      <el-row :gutter="10" class="mb8">

        <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
      </el-row>

      <el-table v-loading="loading" :data="performanceList">
        <el-table-column type="index" width="50" align="center"/>
        <el-table-column label="团队名称" align="center" prop="teamidlinktext" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="年度" align="center" prop="year" width="100"/>
        <el-table-column label="一级指标" align="center" prop="level1name"/>
        <el-table-column label="二级指标" align="center" prop="level2name" width="180"/>
        <el-table-column label="统计指标" align="center" prop="level3name"/>
        <el-table-column label="加分分数" align="center" prop="applyscores" width="100"/>
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
              icon="el-icon-check"
              @click="handleView(scope.row)"
              v-hasPermi="['performance:viewallapply:list']"
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
                <el-input readonly type="textarea" v-model="form.applycontent" placeholder=""/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="证明材料" prop="fileList">
                <bas-doc @changeFileList="changeBasDocFileList" :basdoc="form.basdoc"
                         :editable="false"></bas-doc>
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
          <el-row :gutter="20">
            <el-col :span="24" align="center">
              <el-form-item label="审批结果" prop="auditResult">
                <el-radio-group v-model="form.auditResult">
                  <el-radio :label="1">通过</el-radio>
                  <el-radio :label="2">不通过</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24" align="center">
              <el-form-item label="审批意见" prop="auditopinion">
                <el-input v-model="form.auditopinion" placeholder="请输入意见" type="textarea"/>
              </el-form-item>
            </el-col>
          </el-row>

        </el-form>
      </template>

      <template>

      </template>

      <template>
        <el-row>
          <el-col :span="24" align="center">
            <el-button @click="closeForm">取 消</el-button>
          </el-col>
        </el-row>

      </template>

    </el-dialog>


  </div>
</template>

<script>
import {confirmAddscoreapply, listViewAllapply} from "@/api/performance/teamperformance";
import BasDoc from "../../public/bas-doc"
import TeamData from "../../public/team-data";
import {praseStrEmpty} from "@/utils/ruoyi";
import {listIndicatorTree} from "@/api/performance/indicator";


export default {
  name: "viewallapply_index",
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
      // 父级树选项
      indicatorTreeOptions: undefined,
      indicatorLevel1Options: [],
      indicatorLevel1List: [],

      indicatorLevel2Options: [],
      indicatorLevel2List: [],

      indicatorLevel3Options: [],
      indicatorLevel3List: [],

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
        year: undefined,
        level1id: undefined,
        level2id: undefined,
        level3id: undefined
      },
      queryRules: {},
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
      }

    };
  },
  watch: {},
  created() {

    this.loading = false;
    this.hidden = {confirmBtn: false};
    this.queryParams.userid = this.$store.getters.userId;

    this.getIndicatorTree();

  },
  methods: {
    /** 查询列表 */

    getIndicatorTree() {

      const this_ = this;

      listIndicatorTree().then(response => {
        console.log("response is ", response);
        this_.indicatorList = response.data;

        this_.indicatorTreeOptions = [];
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
              let level3list = [];
              for (let l =0; l < level2item.childList.length; l++) {
                let level3item = level2item.childList[l];
                level3list.push({id: level3item.indicatorid, label: level3item.indicatorname, level: 3});
              }
              level2list.push({id: level2item.indicatorid, label: level2item.indicatorname,children: level3list, level: 2});
            }

            let leve1dict = {id: leve1item.indicatorid, label: leve1item.indicatorname, children: level2list, level: 1};
            leve1list.push(leve1dict);
          }

          let tree = {id: i + 1, label: row.type, children: leve1list, level: 0};
          this_.indicatorTreeOptions.push(tree);
        }

        this_.indicatorLevel1List = [];
        this_.indicatorLevel1Options = [];

        this_.indicatorTreeOptions.forEach(function (tree) {
          if ("定量积分评价" === tree.label) {
            tree.children.forEach(function (level1) {
              this_.indicatorLevel1List.push({id: level1.id, value: level1.label});
              this_.indicatorLevel1Options.push({id: level1.id, value: level1.label});
            });
          }
        });
      });
    },


    clearLevel1Value() {
      this.queryParams.level1id = undefined;
      this.queryParams.level2id = undefined;
      this.queryParams.level3id = undefined;
    },

    changeLevel1Value(value) {

      if (value) {

        const this_ = this;

        let updateitem = {id: value, value: undefined};

        for (let i = 0; i < this.indicatorLevel1List.length; i++) {
          let item = this.indicatorLevel1List[i];
          if (item.id === value) {
            updateitem.value = item.value;
            break;
          }
        }
        console.log("changeLevel1Value updateitem is ", updateitem);

        this_.queryParams.level1id = value;
        this_.queryParams.level2id = undefined;
        this_.queryParams.level3id = undefined;


        this_.indicatorTreeOptions.forEach(function (tree) {
          if ("定量积分评价" === tree.label) {
            tree.children.forEach(function (level1) {

              if (this_.queryParams.level1id === level1.id) {

                console.log("level 3 level1.id is ", level1.label);
                this_.indicatorLevel2List = [];
                this_.indicatorLevel2Options = [];

                level1.children.forEach(function (level2) {
                  this_.indicatorLevel2List.push({id: level2.id, value: level2.label});
                  this_.indicatorLevel2Options.push({id: level2.id, value: level2.label});
                });
              }
            });
          }
        });

      } else {

      }

    },

    filterLevel1Options(v) {
      console.log("filter value is " + v);
    },


    clearLevel2Value() {
      this.queryParams.level2id = undefined;
      this.queryParams.level3id = undefined;
    },

    changeLevel2Value(value) {

      if (value) {
        const this_ = this;

        let projectType = {id: value, value: undefined};

        for (let i = 0; i < this.indicatorLevel2List.length; i++) {
          let item = this.indicatorLevel2List[i];
          if (item.id === value) {
            projectType.value = item.value;
            break;
          }
        }

        this_.queryParams.level2id = value;
        this_.queryParams.level3id = undefined;

        this_.indicatorTreeOptions.forEach(function (tree) {
          if ("定量积分评价" === tree.label) {
            tree.children.forEach(function (level1) {

              if (this_.queryParams.level1id === level1.id) {

                level1.children.forEach(function (level2) {

                  if (this_.queryParams.level2id === level2.id) {

                    this_.indicatorLevel3List = [];
                    this_.indicatorLevel3Options = [];

                    level2.children.forEach(function (level3) {
                      this_.indicatorLevel3List.push({id: level3.id, value: level3.label});
                      this_.indicatorLevel3Options.push({id: level3.id, value: level3.label});
                    });
                  }
                });
              }
            });
          }
        });

      } else {

      }

    },

    filterLevel2Options(v) {

      console.log("filter value is " + v);

      if (v) {
        this.indicatorLevel2Options = this.indicatorLevel2List.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.indicatorLevel2Options = this.indicatorLevel2List;
      }
    },

    clearLevel3Value() {
      this.queryParams.level3id = undefined;
    },

    changeLevel3Value(value) {

      if (value) {

        let projectType = {id: value, value: undefined};

        for (let i = 0; i < this.indicatorLevel2List.length; i++) {
          let item = this.indicatorLevel2List[i];
          if (item.id === value) {
            projectType.value = item.value;
            break;
          }
        }

        this.queryParams.level3id = value;

      } else {

      }

    },

    filterLevel3Options(v) {

      console.log("filter value is " + v);

      if (v) {
        this.indicatorLevel2Options = this.indicatorLevel2List.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.indicatorLevel2Options = this.indicatorLevel2List;
      }
    },


    getList() {
      this.loading = true;
      console.log("queryParams is ", this.queryParams);
      listViewAllapply(this.queryParams).then(response => {
        console.log("response data is ", response);
        this.total = response.total;
        this.performanceList = response.rows;
        this.performanceListOption = [];

        this.loading = false;
      });
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
      console.log("查看所有加分申请", row);

      this.title = "查看所有加分申请";
      this.form = row;
      if (this.form.applystatus === '审核通过') {
        this.form.auditResult = 1
      } else {
        this.form.auditResult = 2
      }
      //加载文档
      this.form.fileList = [];
      const kid = this.form.applyid;
      const query = {attachtotype: "加分申请", relatedid: kid, doctype: "证明材料"};
      this.form.basdoc = query;
      this.form.lastApplyscores = this.form.applyscores;

      this.open = true;

    },

    // 组件方法
    changeBasDocFileList(fileList) {
      console.log("changeBasDocFileList is ", fileList);
      this.form.fileList = fileList;

    },

    /** 保存按钮 */
    confirmForm() {
      this.$refs["form"].validate(
        valid => {
          if (valid) {
            const this_ = this;
            if (this_.form.fileList.length === 0) {
              this_.msgError("保存失败，没有上传证明材料");
            } else if (this_.form.auditResult === undefined) {
              this_.msgError("保存失败，请选择审批结果");
            } else if (this_.form.auditResult === "2" && praseStrEmpty(this_.form.auditopinion) === "") {
              this_.msgError("保存失败，您选择的结果为“不通过”，请输入意见！");
            } else if (this_.form.lastApplyscores !== this_.form.applyscores && praseStrEmpty(this_.form.auditopinion) === "") {
              this_.msgError("保存失败，您设置的分数与原申请分数不同，请输入意见！");
            } else {

              confirmAddscoreapply(this_.form).then(response => {
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
    }


  }

};
</script>


<style scoped>

</style>
