<template>

      <el-form :model="indicatorRelation" ref="indicatorRelationForm" :inline="true" label-width="100px">
        <el-col :span="12">
        <el-form-item label="关联的考核指标：一级指标" prop="level1id" label-width="200px">
          <el-select v-model="indicatorRelation.level1id" placeholder="请选择一级指标型" style="display:block;"
                     clearable @clear="clearRelationLevel1Value" @change="changeRelationLevel1Value"
                     filterable :filter-method="filterRelationLevel1Options">
            <el-option
              v-for="item in relationLevel1Options"
              :key="item.id"
              :label="item.value"
              :value="item.id"/>
          </el-select>
        </el-form-item>
        </el-col>
        <el-col :span="10">
        <el-form-item label="二级指标" prop="level2id">
          <el-select v-model="indicatorRelation.level2id" placeholder="请选择二级指标" style="display:block;"
                     clearable @clear="clearRelationLevel2Value" @change="changeRelationLevel2Value"
                     filterable :filter-method="filterRelationLevel2Options">
            <el-option
              v-for="item in relationLevel2Options"
              :key="item.id"
              :label="item.value"
              :value="item.id"/>
          </el-select>
        </el-form-item>
        </el-col>
        <el-col :span="2">
        <el-form-item>
          <el-button type="success" icon="el-icon-check" size="mini" @click="savePerRelation">设置</el-button>
        </el-form-item>
        </el-col>
      </el-form>

</template>

<script>
import {listIndicator,  listIndicatorRelation, updateIndicatorRelation} from "@/api/performance/indicator";

export default {
  name: "IndicatorRelation",
  components: {},
  props:['indicatortype'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 传入的参数
      filterIndicatortype: this.indicatortype,

      relationLevel1Options: [],
      relationLevel1List: [],
      relationLevel2Options: [],
      relationLevel2List: [],
      // 关联的考核指标：
      indicatorRelation: {
        indicatortype: this.indicatortype,
        level1id: undefined,
        level1name: undefined,
        level2id: undefined,
        level2name: undefined
      }

    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;

      this.loadIndicatorRelation();

    },

    loadIndicatorRelation() {
      let listOptions = [];
      let query = {indicatortype: '', indicatorlevel: 1};
      listIndicator(query).then(response => {
        console.log(response);
        response.data.forEach(function (item) {
          const relationLevel = {value: item.indicatorname, id: item.indicatorid};
          listOptions.push(relationLevel);
        });

        this.relationLevel1List = listOptions;
        this.relationLevel1Options = listOptions;

        let this_ = this;

        query = {indicatortype: this.filterIndicatortype};
        listIndicatorRelation(query).then(response => {
          console.log(response);
          let relations = response.data;
          if (relations.length > 0) {
            let relation = relations[0];
            //
            this_.relationLevel1List.forEach(function (item) {
              if (relation.level1id === item.id) {
                this_.indicatorRelation.level1id = item.id;

                listOptions = [];
                query = {indicatortype: '', indicatorlevel: 2, level1id: item.id};
                listIndicator(query).then(response => {
                  console.log("listIndicator  relationLevel2 is ", response);
                  response.data.forEach(function (item) {
                    const relationLevel = {value: item.indicatorname, id: item.indicatorid};
                    listOptions.push(relationLevel);
                  });

                  this_.relationLevel2List = listOptions;
                  this_.relationLevel2Options = listOptions;

                  this_.indicatorRelation.level2id = relation.level2id;

                  // for (let i=0; i <listOptions.length; i++) {
                  //
                  //   let item = listOptions[i];
                  //   if (relation.level2id == item.id) {
                  //     this_.indicatorRelation.level2id = item.id;
                  //     break;
                  //   }
                  //
                  // }


                });


              }
            });

          }

          // 最后结束刷新。
          this.loading = false;
        });

      });
    },

    savePerRelation: function () {
      let this_  = this;
      this.$confirm('是否确认修改关联的考核指标?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        return updateIndicatorRelation(this_.indicatorRelation);
      }).then(() => {
        this_.getList();
        this_.msgSuccess("修改成功");
      })
    },

    clearRelationLevel1Value() {


    },

    changeRelationLevel1Value(value) {

      if (value) {
        this.indicatorRelation.level1id = value;
        for (let i=0; i< this.relationLevel1List.length; i++) {
          let item = this.relationLevel1List[i];
          if (item.id === value) {
            this.indicatorRelation.level1name = item.value;
            break;
          }
        }
      } else {
        this.indicatorRelation.level1id = undefined;
        this.indicatorRelation.level1name = undefined;
      }

    },

    filterRelationLevel1Options(v) {

      console.log("filterRelationLevel1Options value is " + v);

      if (v) {
        this.relationLevel1Options = this.relationLevel1List.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          const py = item.hotKey;
          var hh = -1;
          if (py !== undefined && py !== null) {
            hh = py.indexOf(val);
          }

          if (item.value.indexOf(val) !== -1 || hh !== -1) return true

        });
      } else {
        this.relationLevel1Options = this.relationLevel1List;

      }
    },

    clearRelationLevel2Value() {


    },

    changeRelationLevel2Value(value) {

      if (value) {
        this.indicatorRelation.level2id = value;
        for (let i=0; i< this.relationLevel2List.length; i++) {
          let item = this.relationLevel2List[i];
          if (item.id === value) {
            this.indicatorRelation.level2name = item.value;
            break;
          }
        }
      } else {
        this.indicatorRelation.level2id = undefined;
        this.indicatorRelation.level2name = undefined;
      }

    },

    filterRelationLevel2Options(v) {

      console.log("filterRelationLevel2Options value is " + v);
      if (v) {
        this.relationLevel2Options = this.relationLevel2List.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          const py = item.hotKey;
          var hh = -1;
          if (py !== undefined && py !== null) {
            hh = py.indexOf(val);
          }

          if (item.value.indexOf(val) !== -1 || hh !== -1) return true

        });
      } else {
        this.relationLevel2Options = this.relationLevel2List;
      }
    }


  }
}
</script>

<style scoped>

</style>
