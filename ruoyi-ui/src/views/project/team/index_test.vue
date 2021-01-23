<template>
  <div id="Demo">
    <el-form ref="form" label-width="100px">
      <el-form-item label="设备名称">
        <div
          class="check-group"
          v-for="(item, index) in memberList"
          :key="index_test">
          <el-tag>{{ item.menu }}</el-tag>
          <el-checkbox-group
            v-model="checkedUseridList[index_test]"
            @change="handleChange(item.id)">
            {{ checkedUseridList[index_test] }}
            <el-checkbox
              v-for="data in item.childMenu"
              :label="data.id"
              :key="data.menu">
              {{ data.menu }}
            </el-checkbox>
          </el-checkbox-group>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
export default {
  name: "pmteam2",
  data() {
    return {
      checkedUseridList: [], //随意修改后的checked项（即要传到后台的变更数据）
      memberList: [   // 所有数据
        {
          id: '1',
          menu: '设备1',
          childMenu: [{
            id: '1-1',
            menu: '暖通一'
          }, {
            id: '1-2',
            menu: '照明一'
          }, {
            id: '1-3',
            menu: '取暖一'
          }, {
            id: '1-4',
            menu: '应急一'
          }
          ]
        },
        {
          id: '2',
          menu: '设备2',
          childMenu: [
            {
              id: '2-1',
              menu: '暖通二'
            }, {
              id: '2-2',
              menu: '照明二'
            }, {
              id: '2-3',
              menu: '取暖二'
            }, {
              id: '2-4',
              menu: '应急二'
            }
          ]
        }
      ],
      checkMember: [  //模拟后台获取的数据（各el-checkbox-group默认checked项）
        {
          id: '1',
          menu: '设备1',
          childMenu: [
            {
              id: '1-1',
              menu: '暖通一'
            }, {
              id: '1-2',
              menu: '照明一'
            }
          ]
        }, {
          id: '2',
          menu: '设备2',
          childMenu: []
        }
      ]
    };
  },
  methods: {
    handleChange() {
      console.log("checkedUseridList", this.checkedUseridList);

      // this.checkMemberArr = []
      // let arr = this.checkedUseridList
      // for (let i = 0; i < arr.length; i++) {
      //   let equipment = arr[i]
      //   if (equipment.length > 0) {
      //     let obj = {
      //       id: this.memberList[i].id,
      //       equips: []
      //     }
      //     for (let j = 0; j < equipment.length; j++) {
      //       obj.equips.push(equipment[j])
      //     }
      //     this.checkMemberArr.push(obj)
      //   }
      // }
      // console.log("checkMemberArr", this.checkMemberArr);
      console.log("checkedUseridList", this.checkedUseridList);
    }
  },
  created() {
// 初始化默认选中状态
    for (let i = 0; i < this.memberList.length; i++) {
      let checkArr = []
      let item = this.memberList[i].childMenu
      if (item.length === 0) {
        this.checkedUseridList.push([])
      } else {
        for (let j = 0; j < item.length; j++) {
          checkArr.push(item[j].id)
        }
        this.checkedUseridList.push(checkArr)
      }
    }
    console.log(this.checkedUseridList);
  }
};

</script>
