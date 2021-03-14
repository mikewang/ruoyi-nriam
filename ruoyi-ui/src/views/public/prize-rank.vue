<template>
<div>

  <el-select v-model="filterId" placeholder="请选择获奖等级" style="display:block;"
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
  name: "PrizeRank",
  components: {},
  props:['selectedPrizeRank'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 传入的参数
      filterId: this.selectedPrizeRank===undefined ? undefined : this.selectedPrizeRank.toString(),
      // 数据源
      options: [],
      list: []
    };
  },
  created() {
    this.getList();
  },

  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      const dictType = "获奖等级";
      console.log("加载 " + dictType + " 组件 " + this.selectedPrizeRank);

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
      this.$emit('changePrizeRank',{id:undefined, value:undefined});
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

        this.$emit('changePrizeRank',adict);

      } else {
        this.$emit('changePrizeRank',{id:undefined, value:undefined});
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
