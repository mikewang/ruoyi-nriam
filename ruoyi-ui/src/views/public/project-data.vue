<template>
<div>
  <el-select  v-if="readonly===false || readonly === undefined"  v-model="filterProjectid" placeholder="" style="display:block;"
             clearable @clear="clearProjectDataValue" @change="changeProjectDataValue"
             filterable :filter-method="filterProjectDataOptions" >
    <el-option
      v-for="item in projectDataOptions"
      :key="item.id"
      :label="item.value"
      :value="item.id"/>
  </el-select>
  <el-input v-else  v-model="selectedProjectName"  readonly  placeholder=""></el-input>
</div>
</template>

<script>
import {listProject} from "@/api/project/project";

export default {
  name: "ProjectData",
  components: {},
  props:['selectedProjectid', ' ', 'readonly'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 传入的参数
      filterProjectid: this.selectedProjectid === undefined ? undefined : this.selectedProjectid.toString(),
      selectedProjectName: undefined,
      selectedProjectData: {},
      // 数据源
      projectDataOptions: [],
      projectDataList: []
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
      if (this.filterProjectid !== undefined) {
        this.selectedProjectData.projectid = this.filterProjectid;
      }
      if (this.selectedTeamid !== undefined) {
        this.selectedProjectData.teamid = this.selectedTeamid;
      }
      console.log("加载 项目组件 " , this.selectedProjectData.projectid, this.selectedProjectData.teamid );
      const this_ = this;

      listProject(this.selectedProjectData).then(response => {
        console.log(response.rows);

        this_.projectDataList = response.rows;

        const listOptions = [];
        this_.projectDataList.sort(function (a, b) {
          return b.projectname.charCodeAt(0) - a.projectname.charCodeAt(0)
        }).forEach(function (item) {
          const adict = {value: item.projectname, id: item.projectcode};
          listOptions.push(adict);
          if (item.projectid === this.filterProjectid) {
            this.selectedProjectName = item.projectname;
          }
        });

        this_.projectDataOptions = listOptions;

        this_.loading = false;
      });

    },

    clearProjectDataValue() {
      this.$emit('changeProjectData',undefined);
    },

    changeProjectDataValue(value) {

      if (value) {

        let projectData = undefined;

        for (let i=0; i< this.projectDataList.length; i++) {
          let item = this.projectDataList[i];
          if (item.projectid === value) {
            projectData = item;
            this.filterProjectid = item.projectid;
            break;
          }
        }

        this.$emit('changeProjectData',projectData);

      } else {
        this.$emit('changeProjectData',undefined);
      }

    },

    filterProjectDataOptions(v) {

      console.log("filter value is " + v);
      let rows = this.projectDataList;

      if (v) {
        rows = this.projectDataList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.projectname.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });

      } else {

      }

      const listOptions = [];
      rows.sort(function (a, b) {
        return b.projectname.charCodeAt(0) - a.projectname.charCodeAt(0)
      }).forEach(function (item) {
        const adict = {value: item.projectname, id: item.projectcode};
        listOptions.push(adict);
      });
      this.projectDataOptions = listOptions;
    }

  }
}
</script>

<style scoped>

</style>
