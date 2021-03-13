<template>
<div>

  <el-select v-model="filterProjectType" placeholder="请选择项目类型" style="display:block;"
             clearable @clear="clearProjectTypeValue" @change="changeProjectTypeValue"
             filterable :filter-method="filterProjectTypeOptions" >
    <el-option
      v-for="item in projectTypeOptions"
      :key="item.id"
      :label="item.value"
      :value="item.id"/>
  </el-select>
</div>
</template>

<script>
import {listData} from "@/api/system/dict/data";

export default {
  name: "ProjectType",
  components: {},
  props:['selectedProjectType'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 传入的参数
      filterProjectType: this.selectedProjectType.toString(),
      // 数据源
      projectTypeOptions: [],
      projectTypeList: []
    };
  },
  created() {
    this.getList();
  },
  watch: {
    'filterProjectType': function (val, oldVal) {
      console.log("filterProjectType is ", val, " from ", oldVal);
    }
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      console.log("加载 项目类型 组件 " + this.selectedProjectType);

      listData({"dictType": "项目类型"}).then(response => {
        console.log(response);
        const listOptions = [];
        response.rows.sort(function (a, b) {
          return a.dictValue < b.dictValue
        }).forEach(function (item) {
          const projecttype = {value: item.dictLabel, id: item.dictValue};
          listOptions.push(projecttype);
        });
        this.projectTypeList = listOptions;
        this.projectTypeOptions = listOptions;

        this.loading = false;
      });

    },

    clearProjectTypeValue() {
      this.$emit('changeProjectType',{id:undefined, value:undefined});
    },

    changeProjectTypeValue(value) {

      if (value) {

        let projectType = {id:value, value:undefined};

        for (let i=0; i< this.projectTypeList.length; i++) {
          let item = this.projectTypeList[i];
          if (item.id === value) {
            projectType.value = item.value;
            break;
          }
        }

        this.$emit('changeProjectType',projectType);

      } else {
        this.$emit('changeProjectType',{id:undefined, value:undefined});
      }

    },

    filterProjectTypeOptions(v) {

      console.log("filter value is " + v);

      if (v) {
        this.projectTypeOptions = this.projectTypeList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.projectTypeOptions = this.projectTypeList;
      }
    }

  }
}
</script>

<style scoped>

</style>
