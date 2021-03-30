<template>
<div>
  <el-select  v-if="readonly===false || readonly === undefined"  v-model="filterUserId" placeholder="" style="display:block;"
             clearable @clear="clearUserDataValue" @change="changeUserDataValue"
             filterable :filter-method="filterUserDataOptions" >
    <el-option
      v-for="item in userDataOptions"
      :key="item.id"
      :label="item.value"
      :value="item.id"/>
  </el-select>

  <el-input v-else  v-model="selectedRealName"  readonly  placeholder=""></el-input>
</div>
</template>

<script>
import {listUser} from "@/api/system/user";

export default {
  name: "UserData",
  components: {},
  props:['selectedUserId','readonly', 'userList'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 传入的参数
      // filterId 必须 Integer类型，否则 因为类型不匹配而不显示名称，只显示字符串。
      filterUserId: this.selectedUserId === undefined ? undefined : this.selectedUserId,
      selectedRealName: undefined,

      // 数据源
      userDataOptions: [],
      userDataList: []
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
      console.log("加载 用户组件 " + this.selectedUserId);
      const this_ = this;

      if (this_.userList === undefined) {

        listUser(this.selectedUserId).then(response => {
          console.log(response.rows);

          this_.userDataList = response.rows;

          this_.filterUserDataOptions(null);

          this_.loading = false;
        });
      }
      else {

        this_.userDataList = this_.userList;

        this_.filterUserDataOptions(null);

        this_.loading = false;
      }



    },

    clearUserDataValue() {
      this.$emit('changeUserData',undefined);
    },

    changeUserDataValue(value) {

      if (value) {

        let userData = undefined;

        for (let i=0; i< this.userDataList.length; i++) {
          let item = this.userDataList[i];
          if (item.userId === value) {
            userData = item;
            this.filterUserId = item.userId;
            break;
          }
        }

        this.$emit('changeUserData',userData);

      } else {
        this.$emit('changeUserData',undefined);
      }

    },

    filterUserDataOptions(v) {

      console.log("filter value is " + v);
      let rows = this.userDataList;

      if (v) {
        rows = this.userDataList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          const py = item.hotKey;
          let hh = -1;
          if (py !== undefined && py !== null) {
            hh = py.indexOf(val);
          }
          if (item.realName.indexOf(val) !== -1 || hh !== -1) return true

        });

      } else {

      }

      const this_ = this;

      const listOptions = [];
      rows.sort(function (a, b) {
        return b.realName.charCodeAt(0) - a.realName.charCodeAt(0)
      }).forEach(function (item) {
        const adict = {value: item.realName, id: item.userId};

        if (this_.selectedUserId === item.userId) {
          this_.selectedRealName = item.realName;
        }

        listOptions.push(adict);
      });

      this.userDataOptions = listOptions;
    }

  }
}
</script>

<style scoped>

</style>
