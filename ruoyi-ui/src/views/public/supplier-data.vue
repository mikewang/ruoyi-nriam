<template>
<div>
  <el-select  v-if="readonly===false || readonly === undefined"  v-model="filterSupplierid" placeholder="" style="display:block;"
             clearable @clear="clearSupplierDataValue" @change="changeSupplierDataValue"
             filterable :filter-method="filterSupplierDataOptions" >
    <el-option
      v-for="item in supplierDataOptions"
      :key="item.id"
      :label="item.value"
      :value="item.id"/>
  </el-select>
  <el-input v-else  v-model="selectedSupplierName"  readonly  placeholder=""></el-input>
</div>
</template>

<script>
import {getSheetSupplier} from "@/api/audit/audit";

export default {
  name: "SupplierData",
  components: {},
  props:['selectedSupplierData', 'readonly'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 传入的参数
      filterSupplierid: this.selectedSupplierData === undefined ? undefined :this.selectedSupplierData.supplierid,
      selectedSupplierName: undefined,
      // 数据源
      supplierDataOptions: [],
      supplierDataList: []
    };
  },
  created() {
    this.getList();
  },
  watch: {

  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;

      this.supplierDataOptions = [];
      this.supplierDataList = [];

      console.log("加载 Supplier组件 begin ");
      console.log("加载 Supplier组件 " , this.selectedSupplierData);
      const this_ = this;

      getSheetSupplier(this.selectedSupplierData).then(response => {
          console.log("  Supplier组件 is ",this.selectedSupplierData, response.data);
          this_.supplierDataList = response.data;
          const listOptions = [];
          this_.supplierDataList.sort(function (a, b) {
            return b.suppliername.charCodeAt(0) - a.suppliername.charCodeAt(0)
          }).forEach(function (item) {
            const adict = {value: item.suppliername, id: item.supplierid};
            listOptions.push(adict);
            if (item.supplierid === this_.filterSupplierid) {

              this_.selectedSupplierName = item.suppliername;

              this_.$emit('changeSupplierData',item);

              console.log("当前Supplier组件 $emit is ", item.supplierid, item.suppliername);
            }
          });
          this_.supplierDataOptions = listOptions;

          this_.loading = false;

        });

    },

    clearSupplierDataValue() {
      this.$emit('changeSupplierData',undefined);
    },

    changeSupplierDataValue(value) {

      console.log("changeSupplierDataValue is " , value);
      if (value) {

        let supplierData = undefined;

        for (let i=0; i< this.supplierDataList.length; i++) {
          let item = this.supplierDataList[i];
          if (item.supplierid === value) {
            supplierData = item;
            this.filterSupplierid = item.supplierid;
            break;
          }
        }

        this.$emit('changeSupplierData',supplierData);

      } else {
        this.$emit('changeSupplierData',undefined);
      }

    },

    filterSupplierDataOptions(v) {

      console.log("filter value is " + v);
      let rows = this.supplierDataList;

      if (v) {
        rows = this.supplierDataList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.suppliername.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });

      } else {

      }

      const listOptions = [];
      rows.sort(function (a, b) {
        return b.suppliername.charCodeAt(0) - a.suppliername.charCodeAt(0)
      }).forEach(function (item) {
        const adict = {value: item.suppliername, id: item.supplierid};
        listOptions.push(adict);
      });
      this.supplierDataOptions = listOptions;
    }

  }
}
</script>

<style scoped>

</style>
