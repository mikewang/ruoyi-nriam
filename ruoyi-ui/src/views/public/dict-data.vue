<template>
<div>
  <el-select v-if="readonly===false || readonly === undefined"  v-model="filterId" placeholder="请选择" style="display:block;"
             clearable @clear="clearValue" @change="changeValue"
             filterable :filter-method="filterOptions" >
    <el-option
      v-for="item in options"
      :key="item.id"
      :label="item.value"
      :value="item.id"/>
  </el-select>
  <el-input v-else  v-model="filterValue"  readonly  placeholder="dictType"></el-input>
</div>
</template>

<script>
import {listData} from "@/api/system/dict/data";

export default {
  name: "DictData",
  components: {},
  props:['dictTypeName','selectedDictValue', 'readonly', 'dataOptions'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 传入的参数
      dictType: this.dictTypeName,
      filterId: this.selectedDictValue === undefined ? undefined : this.selectedDictValue.toString(),
      filterValue:undefined,
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
      console.log("加载 " + this.dictType + " 组件 " + this.selectedDictValue);

      const this_ = this;

      if (this_.dataOptions === undefined) {
        listData({"dictType": this.dictType}).then(response => {
          console.log("获取字典数据:" + this.dictType, response.rows);
          const listOptions = [];
          response.rows.sort(function (a, b) {
            return a.dictLabel.charCodeAt(0) - b.dictLabel.charCodeAt(0)
          }).forEach(function (item) {
            const adict = {value: item.dictLabel, id: item.dictValue};
            // console.log("item.dictValue is ", item.dictValue, "this.filterId is ", this_.filterId);
            if (item.dictValue === this_.filterId) {
              this_.filterValue = item.dictLabel;
            }
            listOptions.push(adict);
          });

          this_.list = listOptions;
          this_.options = listOptions;

          this_.loading = false;
        });
      }
      else {
        const listOptions = [];
        this_.dataOptions.forEach(function (item) {
          const adict = {value: item.dictLabel, id: item.dictValue};
          if (item.dictValue === this_.filterId) {
            this_.filterValue = item.dictLabel;
          }
          listOptions.push(adict);
        });

        this_.list = listOptions;
        this_.options = listOptions;

        this_.loading = false;
      }

    },

    clearValue() {
      this.$emit('changeDictValue',{type:this.dictType, id:undefined, value:undefined});
    },

    changeValue(value) {

      if (value) {

        let adict = {type:this.dictType, id:value, value:undefined};

        for (let i=0; i< this.list.length; i++) {
          let item = this.list[i];
          if (item.id === value) {
            adict.value = item.value;
            break;
          }
        }

        this.$emit('changeDictValue',adict);

      } else {
        this.$emit('changeDictValue',{type:this.dictType, id:undefined, value:undefined});
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
