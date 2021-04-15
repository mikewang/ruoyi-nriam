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
import {queryNormalProject, queryAftersetup} from "@/api/project/project";

export default {
  name: "ProjectData",
  components: {},
  props:['selectedProjectData', 'readonly', 'selectedOption'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 传入的参数
      filterProjectid: this.selectedProjectData === undefined ? undefined :this.selectedProjectData.projectid,
      selectedProjectName: undefined,
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

      this.projectDataOptions = [];
      this.projectDataList = [];

      if (this.filterProjectid !== undefined) {

      }
      console.log("加载 项目组件 begin ");
      console.log("加载 项目组件 " , this.selectedProjectData);
      const this_ = this;

      if (this.selectedOption === undefined || this.selectedOption === "normal") {
        queryNormalProject(this.selectedProjectData).then(response => {
          console.log(" 项目组件  is ",this.selectedProjectData, response.data);
          this_.projectDataList = response.data;
          const listOptions = [];
          this_.projectDataList.sort(function (a, b) {
            return b.projectname.charCodeAt(0) - a.projectname.charCodeAt(0)
          }).forEach(function (item) {
            const adict = {value: item.projectname, id: item.projectid};
            listOptions.push(adict);
            if (item.projectid === this_.filterProjectid) {

              this_.selectedProjectName = item.projectname;
              this_.$emit('changeProjectData',item);

              console.log("当前项目名称 $emit is ", item.projectid, item.projectname);
            }
          });
          this_.projectDataOptions = listOptions;

          if (this_.selectedProjectName === undefined) {
            queryNormalProject({}).then(response => {
              console.log(" 项目组件  is full ", response.data);
              response.data.sort(function (a, b) {
                return b.projectid - a.projectid
              }).forEach( function (item) {
                // console.log(" 项目 projectid is ",this_.filterProjectid, item.projectid, item.projectname);

                if (item.projectid === this_.filterProjectid) {
                  console.log("当前项目名称  is ", item.projectid, item.projectname);
                  this_.selectedProjectName = item.projectname;
                  this_.$emit('changeProjectData',item);
                }
              });
              this_.loading = false;
            });
          }
          else {
            this_.loading = false;
          }

        });
      }
      else if (this.selectedOption === "aftersetup") {
        queryAftersetup(this.selectedProjectData).then(response => {
          console.log(" 项目组件 aftersetup  is ",this.selectedProjectData, response.data);
          this_.projectDataList = response.data;
          const listOptions = [];
          this_.projectDataList.sort(function (a, b) {
            return b.projectname.charCodeAt(0) - a.projectname.charCodeAt(0)
          }).forEach(function (item) {
            const adict = {value: item.projectname, id: item.projectid};
            listOptions.push(adict);
            if (item.projectid === this_.filterProjectid) {

              this_.selectedProjectName = item.projectname;

              this_.$emit('changeProjectData',item);

              console.log("当前项目名称 aftersetup $emit is ", item.projectid, item.projectname);
            }
          });
          this_.projectDataOptions = listOptions;

          this_.loading = false;

        });

      }


    },

    clearProjectDataValue() {
      this.$emit('changeProjectData',undefined);
    },

    changeProjectDataValue(value) {

      console.log("changeProjectDataValue is " , value);
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
        const adict = {value: item.projectname, id: item.projectid};
        listOptions.push(adict);
      });
      this.projectDataOptions = listOptions;
    }

  }
}
</script>

<style scoped>

</style>
