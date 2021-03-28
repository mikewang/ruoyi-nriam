<template>
<div>
  <el-select v-model="filterId" placeholder="请选择" style="display:block;"
             clearable @clear="clearValue" @change="changeValue"
             filterable :filter-method="filterOptions" >
    <el-option
      v-for="item in options"
      :key="item.id"
      :label="item.value"
      :value="item.id"/>
  </el-select>
</div>
</template>

<script>
import {listDept} from "@/api/system/dept";

export default {
  name: "DeptData",
  components: {},
  props:['selectedDeptId'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 传入的参数
      teamid: undefined,
      // filterId 必须 Integer类型，否则 因为类型不匹配而不显示名称，只显示字符串。
      filterId: this.selectedDeptId === undefined ? undefined : this.selectedDeptId,
      // 数据源
      options: [],
      list: []
    };
  },
  mounted() {

  },
  created() {
    console.log("created,", this.filterId);
    this.getList();
  },

  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      const dictType = "部门";
      console.log("加载 " + dictType + " 组件 " + this.selectedDeptId);
      listDept().then(response => {

        console.log("listDept is ", response.data);
        this.list =  response.data;
        this.filterOptions(null);

        this.loading = false;
      });

    },

    clearValue() {
      this.$emit('changeDeptId',undefined);
    },

    changeValue(value) {

      if (value) {

        let dept = undefined;

        for (let i=0; i< this.list.length; i++) {
          let item = this.list[i];
          if (item.deptId === value) {
            dept= item;
            break;
          }
        }

        this.$emit('changeDeptId',dept);

      } else {
        this.$emit('changeDeptId',undefined);
      }

    },

    filterOptions(v) {

      console.log("filter value is " + v);
      let rows = this.list;

      if (v) {
        rows = this.list.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          // const py = item.hotKey;
          let hh = -1;
          // if (py !== undefined && py !== null) {
          //   hh = py.indexOf(val);
          // }
          if (item.deptName.indexOf(val) !== -1 || hh !== -1) return true

        });

      } else {

      }

      const listOptions = [];
      // rows.sort(function (a, b) {
      //   return b.deptName.charCodeAt(0) - a.deptName.charCodeAt(0)
      // });
      rows.forEach(function (item) {
        const adict = {value: item.deptName, id: item.deptId};
        listOptions.push(adict);
      });

      this.options = listOptions;
    }

  }
}
</script>

<style scoped>

</style>
