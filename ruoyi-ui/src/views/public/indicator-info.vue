<template>
  <div>
  <el-form ref="form" :model="form" :rules="rules" label-width="100px" :key="timer">
    <el-row>
      <el-col :span="24">
        <el-form-item label="指标类型" prop="indicatortype">
          <el-select v-model="form.indicatortype" placeholder="请选择" style="display:block;"
                     clearable @clear="clearIndicatorTypeValue" @change="changeIndicatorTypeValue"
                     filterable :filter-method="filterIndicatorTypeOptions" >
            <el-option
              v-for="item in indicatorTypeOptions"
              :key="item"
              :label="item"
              :value="item"/>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="指标级别" prop="level">
          <el-select v-model="form.level" placeholder="请选择" style="display:block;"
                     clearable @clear="clearLevelValue" @change="changeLevelValue"
                     filterable :filter-method="filterLevelOptions" >
            <el-option
              v-for="item in indicatorLevelOptions"
              :key="item.id"
              :label="item.value"
              :value="item.id"/>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item v-if="this.hidden.level1id===false" label="所属一级指标" prop="level1id">
          <el-select v-model="form.level1id" placeholder="请选择" style="display:block;"
                     clearable @clear="clearLevel1Value" @change="changeLevel1Value"
                     filterable :filter-method="filterLevel1Options" >
            <el-option
              v-for="item in indicatorLevel1Options"
              :key="item.id"
              :label="item.value"
              :value="item.id"/>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item v-if="this.hidden.level2id===false" label="所属二级指标" prop="level2id">
          <el-select v-model="form.level2id" placeholder="请选择" style="display:block;"
                     clearable @clear="clearLevel2Value" @change="changeLevel2Value"
                     filterable :filter-method="filterLevel2Options" >
            <el-option
              v-for="item in indicatorLevel2Options"
              :key="item.id"
              :label="item.value"
              :value="item.id"/>
          </el-select>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item label="指标内容" prop="indicatorname">
          <el-input type="textarea" v-model="form.indicatorname" placeholder="请输入"/>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item v-if="this.hidden.score===false" label="单项分值" prop="score">
          <el-input  v-model="form.score" placeholder="请输入"/>
        </el-form-item>
      </el-col>
      <el-col :span="24">
        <el-form-item v-if="this.hidden.ifselfapply===false" label="是否自主申请" prop="ifselfapply">
          <el-input  v-model="form.ifselfapply" placeholder="请输入"/>
        </el-form-item>
      </el-col>
    </el-row>
  </el-form>
  <div slot="footer" class="dialog-footer" align="center">
    <el-button type="danger" @click="removeForm">删除</el-button>
    <el-button type="primary" @click="submitForm">确 定</el-button>
    <el-button @click="cancelForm">取 消</el-button>
  </div>
  </div>
</template>

<script>
import {listData} from "@/api/system/dict/data";

export default {
  name: "IndicatorInfo",
  components: {},
  props:['info','infoLevel', 'treeOptions'],

  data() {
    return {
      // 遮罩层
      loading: true,
      // 传入的参数
      form:{level:this.infoLevel, indicatorid:this.info.indicatorid, indicatortype:this.info.indicatortype,
        level1id: this.info.level1id.toString(), level2id: this.info.level2id.toString(), indicatorname:this.info.indicatorname,
         score:this.info.score, ifselfapply: this.info.ifselfapply},
      rules:{},
      timer: '',

      // 数据源
      indicatorTreeOptions: this.treeOptions,

      indicatorTypeOptions: [],
      indicatorTypeList: [],

      indicatorLevel1Options: [],
      indicatorLevel1List: [],

      indicatorLevel2Options: [],
      indicatorLevel2List: [],

      indicatorLevelOptions: [{id:1, value:"一级指标"},{id:2,value:"二级指标"},{id:3, value:"统计指标"}],
      indicatorLevelList: [],

      hidden:{level1id:false, level2id:false, score:false, ifselfapply:false},
      readonly:[]

    };
  },
  created() {
    // this.getList();

    console.log(" props info is ", this.info);

    this.configTemplate(this.form.level);

    this.loading = false;
  },
  watch: {

  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      console.log("加载 组件 " + this.form);

      listData({"dictType": "项目类型"}).then(response => {
        console.log(response.rows);
        const listOptions = [];
        response.rows.sort(function (a, b) {
          return a.dictValue - b.dictValue
        }).forEach(function (item) {
          const adict = {value: item.dictLabel, id: item.dictValue};
          listOptions.push(adict);
        });
        this.indicatorTypeList = listOptions;
        this.indicatorTypeOptions = listOptions;
        this.loading = false;
      });

    },


    getIndicatorTypes(){

      return ["定量积分评价","定性系数评价"];
    },

    configTemplate(level) {

      if (level === 1) {
        this.hidden.ifselfapply = true;
        this.hidden.score = true;
        this.hidden.level2id = true;
        this.hidden.level1id = true;
      }
      else if (level === 2) {
        this.hidden.ifselfapply = true;
        this.hidden.score = true;
        this.hidden.level2id = true;
        this.hidden.level1id = false;
      }
      else if (level === 3) {
        this.hidden.ifselfapply = false;
        this.hidden.score = false;
        this.hidden.level2id = false;
        this.hidden.level1id = false;
      }


      this.indicatorTypeOptions =[];
      this.indicatorLevel1List = [];
      this.indicatorLevel1Options = [];
      this.indicatorLevel2List = [];
      this.indicatorLevel2Options = [];

      console.log("indicatorTreeOptions is ", this.indicatorTreeOptions);

      const this_ = this;

      this.indicatorTreeOptions.forEach(function (tree) {
        this_.indicatorTypeOptions.push(tree.label);

        if (level ===1) {


        }
        else if (level === 2) {
          //
          if (this_.form.indicatortype === tree.label) {
            tree.children.forEach(function(level1) {

              // console.log(level1.id, level1.label);
              this_.indicatorLevel1List.push({id:level1.id, value:level1.label});
              this_.indicatorLevel1Options.push({id:level1.id, value:level1.label});

            });

          }
        }
        else if (level === 3) {
          if (this_.form.indicatortype === tree.label) {
            tree.children.forEach(function(level1) {
              this_.indicatorLevel1List.push({id:level1.id, value:level1.label});
              this_.indicatorLevel1Options.push({id:level1.id, value:level1.label});
              console.log("this_.form.level1name is ", this_.form.level1name);
              console.log("level1.id is ", level1.label);

              if (this_.info.level1name === level1.label) {
                level1.children.forEach(function(level2) {
                  this_.indicatorLevel2List.push({id: level2.id, value: level2.label});
                  this_.indicatorLevel2Options.push({id:level2.id, value:level2.label});
                });
              }

            });

          }
        }

      });

      this_.timer = Date.now().toString();
      console.log("this.form is ", this_.form);
      console.log("indicatorLevel1Options is ", this_.indicatorLevel1Options);
      console.log("indicatorLevel2Options is ", this_.indicatorLevel2Options);



    },

    clearIndicatorTypeValue() {

    },

    changeIndicatorTypeValue(value) {

      if (value) {

        let projectType = {id:value, value:undefined};

        for (let i=0; i< this.indicatorTypeList.length; i++) {
          let item = this.indicatorTypeList[i];
          if (item.id === value) {
            projectType.value = item.value;
            break;
          }
        }
      } else {

      }

    },

    filterIndicatorTypeOptions(v) {

      console.log("filter value is " + v);

      if (v) {
        this.indicatorTypeOptions = this.indicatorTypeList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.indicatorTypeOptions = this.indicatorTypeList;
      }
    },


    clearLevelValue() {

    },

    changeLevelValue(value) {

      if (value) {

        let projectType = {id:value, value:undefined};

        for (let i=0; i< this.indicatorTypeList.length; i++) {
          let item = this.indicatorTypeList[i];
          if (item.id === value) {
            projectType.value = item.value;
            break;
          }
        }
      } else {

      }

    },

    filterLevelOptions(v) {

      console.log("filter value is " + v);

      if (v) {
        this.indicatorTypeOptions = this.indicatorTypeList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.indicatorTypeOptions = this.indicatorTypeList;
      }
    },



    clearLevel1Value() {

    },

    changeLevel1Value(value) {

      if (value) {

        let projectType = {id:value, value:undefined};

        for (let i=0; i< this.indicatorLevel1List.length; i++) {
          let item = this.indicatorLevel1List[i];
          if (item.id === value) {
            projectType.value = item.value;
            break;
          }
        }
      } else {

      }

    },

    filterLevel1Options(v) {

      console.log("filter value is " + v);

      if (v) {
        this.indicatorLevel1Options = this.indicatorLevel1List.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.indicatorLevel1Options = this.indicatorLevel1List;
      }
    },


    clearLevel2Value() {

    },

    changeLevel2Value(value) {

      if (value) {

        let projectType = {id:value, value:undefined};

        for (let i=0; i< this.indicatorLevel2List.length; i++) {
          let item = this.indicatorLevel2List[i];
          if (item.id === value) {
            projectType.value = item.value;
            break;
          }
        }
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


    cancelForm: function () {
      this.open = false;

      this.$emit('changeIndicatorInfo',null);
    },

    submitForm: function () {
      const this_ = this;
      this_.$refs["form"].validate(valid => {
        if (valid) {
          this.$emit('changeIndicatorInfo',null);
        }
      });
    },
    removeForm: function () {

    }

  }
}
</script>

<style scoped>

</style>
