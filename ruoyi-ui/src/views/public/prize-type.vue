<template>
<div>

  <el-select v-model="filterId" placeholder="请选择获奖类型" style="display:block;"
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
import {listData} from "@/api/system/dict/data";

export default {
  name: "PrizeType",
  props:['selectedPrizeType'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 传入的参数
      filterId: this.selectedPrizeType===undefined ? undefined : this.selectedPrizeType.toString(),
      // 数据源
      options: [],
      list: []
    };
  },
  created() {
    console.log("created on ", this.filterId);
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      const dictType = "获奖类型";
      console.log("加载 " + dictType + " 组件 " + this.selectedPrizeType);

      listData({"dictType": dictType}).then(response => {
        console.log("获取字典数据:" + dictType, response.rows);
        const listOptions = [];
        response.rows.sort(function (a, b) {
          return a.dictValue < b.dictValue
        }).forEach(function (item) {
          const adict = {value: item.dictLabel, id: item.dictValue};
          listOptions.push(adict);
        });
        this.list = listOptions;
        this.options = listOptions;

        this.loading = false;
      });

    },

    clearValue() {
      this.$emit('changePrizeType',{id:undefined, value:undefined});
    },

    changeValue(value) {

      if (value) {

        let adict = {id:value, value:undefined};

        for (let i=0; i< this.list.length; i++) {
          let item = this.list[i];
          if (item.id === value) {
            adict.value = item.value;
            break;
          }
        }

        this.$emit('changePrizeType',adict);

      } else {
        this.$emit('changePrizeType',{id:undefined, value:undefined});
      }

    },

    filterOptions(v) {

      console.log("filter value is " + v);

      if (v) {
        this.options = this.list.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.options = this.list;
      }
    }

  }
}
</script>

<style scoped>

</style>
