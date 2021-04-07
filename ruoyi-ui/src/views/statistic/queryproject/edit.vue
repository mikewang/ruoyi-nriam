<template>
  <div class="app-container">

    <el-row :gutter="20">
      <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="160px" :key="timer"
      >
        <template>
          <el-tag size="medium" type="info">项目信息</el-tag>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目名称" prop="projectname">
                <el-input v-bind:readonly="readonly.basic" v-model="form.projectname" placeholder="请输入项目名称"
                          :show-overflow-tooltip="true"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目编号" prop="projectcode">
                <el-input v-bind:readonly="readonly.basic" v-model="form.projectcode" placeholder="请输入项目编号"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目经费编号" prop="subjectcode">
                <el-input v-bind:readonly="readonly.basic" v-model="form.subjectcode" placeholder="请输入项目经费编号"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目总经费（元）" prop="projectfunds">
                <el-input type="number" v-bind:readonly="readonly.basic" v-model="form.projectfunds"
                          placeholder="请输入项目总经费（元）"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="财政拨款（元）" prop="financefunds">
                <el-input type="number" v-bind:readonly="readonly.basic" v-model="form.financefunds"
                          placeholder="请输入财政拨款（元）"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="可支配经费（元）" prop="canusefunds">
                <el-input type="number" v-bind:readonly="readonly.basic" v-model="form.canusefunds"
                          placeholder="请输入可支配经费（元）"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目所属部门" prop="organizationid">
                <dept-data :key="timer" :readonly="readonly.basic" :selected-dept-id="form.organizationid"
                           @changeDeptId="changeFormDeptId"></dept-data>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目开始日期" prop="projectbegindate">
                <el-date-picker v-bind:readonly="readonly.basic" v-model="form.projectbegindate" type="date"
                                placeholder="请选择日期"
                                value-format="yyyy-MM-dd"
                                style="display:block;"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目结束日期" prop="projectenddate">
                <el-date-picker v-bind:readonly="readonly.basic" v-model="form.projectenddate" type="date"
                                placeholder="请选择日期" value-format="yyyy-MM-dd"
                                style="display:block;"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目类型" prop="projecttype">
                <dict-data :readonly="readonly.basic" :dict-type-name="DictTypeNameProjectType"
                           :selected-dict-value="form.projecttype"
                           @changeDictValue="changeFormProjectTypeValue"></dict-data>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="项目所属团队" prop="teamid">
                <team-data :readonly="readonly.basic" :selected-team-id="form.teamid"
                           @changeTeamId="changeFormTeamValue"></team-data>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目负责人" prop="projectmanagerid">
                <user-data :readonly="readonly.basic" :selected-user-id="form.projectmanagerid"
                           @changeUserData="changeFormManagerValue"></user-data>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="8">

              <el-form-item label="主持/参与" prop="jointype">
                <dict-data :readonly="readonly.basic" :dict-type-name="DictTypeNameJoinType"
                           :selected-dict-value="form.jointype" @changeDictValue="changeJoinTypeValue"></dict-data>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="备注" prop="memo">
                <el-input v-bind:readonly="readonly.basic" v-model="form.memo" placeholder="" type="textarea"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="form.jointype === 1" :key="form.jointype">
            <el-col :span="24">
              <el-form-item label="项目参加单位列表" prop="JoinorganizationList">
                <el-row :gutter="10" class="mb8" v-bind:hidden="readonly.basic">
                  <el-col :span="1.5">
                    <el-button plain
                               type="primary"
                               icon="el-icon-plus"
                               size="mini"
                               @click="handleJoinorganizationAdd"
                               v-hasPermi="['project:project:list']"
                    >新增
                    </el-button>
                  </el-col>
                  <el-col :span="1.5">
                    <el-button plain
                               v-if="1===0"
                               type="danger"
                               icon="el-icon-delete"
                               size="mini"
                               :disabled="multiple"
                               @click="handleJoinorganizationDelete"
                               v-hasPermi="['project:project:list']"
                    >删除
                    </el-button>
                  </el-col>
                </el-row>
                <el-table :data="form.projectjoinorganizationlist"
                          @selection-change="handleJoinorganizationSelectionChange"
                          style="display:block;">
                  <el-table-column type="index" width="50" align="center"/>
                  <el-table-column label="子项目名称" align="center" prop="subjectname" :show-overflow-tooltip="true"/>
                  <el-table-column label="参加单位" align="center" prop="organizationname"/>
                  <el-table-column label="负责人" align="center" prop="manager" width="100"/>
                  <el-table-column label="经费（元）" align="center" prop="funds" width="100"/>
                  <el-table-column
                    label="操作"
                    align="center"
                    width="160"
                    class-name="small-padding fixed-width" v-if="!readonly.basic"
                  >
                    <template slot-scope="scope">
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-edit"
                        @click="handleJoinorganizationUpdate(scope.row)"
                        v-hasPermi="['project:project:list']"
                        v-if="!readonly.basic"
                      >编辑
                      </el-button>
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-delete"
                        @click="handleJoinorganizationDelete(scope.row)"
                        v-hasPermi="['project:project:remove']"
                        v-if="!readonly.basic"
                      >删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row v-if="form.jointype === 2" :key="form.jointype">

            <el-col :span="8">
              <el-form-item label="参与项目名称" prop="uplevelsubjectname">
                <el-input v-bind:readonly="readonly.basic" v-model="form.uplevelproject.subjectname"
                          placeholder="请输入项目名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="主持单位" prop="uplevelmanageorganization">
                <el-input v-bind:readonly="readonly.basic" v-model="form.uplevelproject.manageorganization"
                          placeholder="请输入项目编号"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="负责人" prop="uplevelmanager">
                <el-input v-bind:readonly="readonly.basic" v-model="form.uplevelproject.manager"
                          placeholder="请输入项目经费编号"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="经费（元）" prop="uplevelfunds">
                <el-input v-bind:readonly="readonly.basic" v-model="form.uplevelproject.funds" placeholder="0.00"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-divider></el-divider>
          <el-row>
            <el-col :span="24">
              <el-form-item label="项目成员" prop="projectmemberUserIdList">
                <template>
                  <el-checkbox-group v-bind:disabled="readonly.basic" v-model="form.projectmemberUserIdList"
                                     @change="handleProjectmemberListChange" :key="timer">
                    <el-checkbox v-for="data in projectmemberOptions" :label="data.userId" :key="data.userId">
                      {{ data.realName }}
                    </el-checkbox>
                  </el-checkbox-group>
                </template>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="!readonly.basic">
            <el-col :span="8">
              <el-form-item label="添加项目成员">
                <template>
                  <user-data :selected-user-id="undefined" @changeUserData="changeFormProjectMemberValue"></user-data>
                </template>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item label="项目申报书" prop="basicdocList1">
                <project-doc :doc-type="DocTypeXiangmuShenbaoShu" :doc-list="basicdocList1" :readonly="readonly.basic"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item label="项目合同" prop="basicdocList2">
                <project-doc :doc-type="DocTypeXiangmuHetong" :doc-list="basicdocList2" :readonly="readonly.basic"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item label="实施方案" prop="basicdocList3">
                <project-doc :doc-type="DocTypeShishiFangan" :doc-list="basicdocList3" :readonly="readonly.basic"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item label="项目批复文件" prop="basicdocList4">
                <project-doc :doc-type="DocTypeXiangmuPifu" :doc-list="basicdocList4" :readonly="readonly.basic"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目状态" prop="statuslinktext">
                <el-input v-model="form.statuslinktext" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="信息输入人员" prop="createuseridlinktext">
                <el-input v-model="form.createuseridlinktext" disabled/>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template>
          <el-divider v-if="hidden.acceptance === false">项目验收信息</el-divider>
          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="项目验收意见" prop="acceptanceOpinion">
                <el-input v-bind:readonly="readonly.acceptance" v-model="form.acceptance.opinion" placeholder=""
                          type="textarea"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="验收申请书" prop="acceptdocList1">
                <project-doc :doc-type="DocTypeYanshouShenqingshu" :doc-list="acceptdocList1" :readonly="readonly.acceptance"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="验收证书" prop="acceptdocList2">
                <project-doc :doc-type="DocTypeYanshouZhengshu" :doc-list="acceptdocList2" :readonly="readonly.acceptance"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="技术总结" prop="acceptdocList3">
                <project-doc :doc-type="DocTypeJishuZongjie" :doc-list="acceptdocList3" :readonly="readonly.acceptance"
                             @changeFileList="changeBasicDocList"></project-doc>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="工作总结" prop="acceptdocList4">
                <project-doc :doc-type="DocTypeGongzuoZongjie" :doc-list="acceptdocList4" :readonly="readonly.acceptance"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="查新报告" prop="acceptdocList5">
                <project-doc :doc-type="DocTypeChaxinBaogao" :doc-list="acceptdocList5" :readonly="readonly.acceptance"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="审计报告" prop="acceptdocList6">
                <project-doc :doc-type="DocTypeShenjiBaogao" :doc-list="acceptdocList6" :readonly="readonly.acceptance"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="检测报告" prop="acceptdocList7">
                <project-doc :doc-type="DocTypeJiancheBaogao" :doc-list="acceptdocList7" :readonly="readonly.acceptance"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="其它附件" prop="acceptdocList8">
                <project-doc :doc-type="DocTypeQitaFujian" :doc-list="acceptdocList8" :readonly="readonly.acceptance"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="用户证明" prop="acceptdocList9">
                <project-doc :doc-type="DocTypeYonghuZhengmin" :doc-list="acceptdocList9" :readonly="readonly.acceptance"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="成果照片视频" prop="acceptdocList10">
                <project-doc :doc-type="DocTypeChengguoZhaopian" :doc-list="acceptdocList10" :readonly="readonly.acceptance"
                             @changeFileList="changeBasicDocList"></project-doc>

              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance === false" label="备注" prop="acceptanceMemo">
                <el-input v-bind:readonly="readonly.acceptance" v-model="form.acceptance.memo" placeholder=""
                          type="textarea"/>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template>
          <el-divider v-if="this.opcode.indexOf('acceptconfirm') !== -1 && this.hidden.confirm === false">审核验收项目信息
          </el-divider>
          <el-divider v-else-if="this.opcode.indexOf('confirm') !== -1 && this.hidden.confirm === false">审核新建项目信息
          </el-divider>
          <el-row v-if="this.hidden.confirm == false">
            <el-col :span="8">
              <el-form-item v-if="this.opcode === 'confirm'" label="更正项目经费编号" prop="confirmSubjectcode">
                <el-input v-model="form.confirmSubjectcode"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="this.hidden.confirm == false">
            <el-col :span="12">
              <el-form-item label="审核结果" prop="confirmResult">
                <template>
                  <el-radio-group v-model="form.confirmResult" v-bind:readonly="this.readonly.confirm">
                    <el-radio :label="1">通过</el-radio>
                    <el-radio :label="2">不通过</el-radio>
                  </el-radio-group>
                </template>
              </el-form-item>
            </el-col>
            <el-col :span="8" v-if="this.opcode.indexOf('acceptconfirm') !== -1">
              <el-form-item label="" prop="ifacceptancefull">
                <el-checkbox v-model="form.ifacceptancefull" v-bind:readonly="this.readonly.confirm">验收材料已齐全
                </el-checkbox>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="this.hidden.confirm == false">
            <el-col :span="16">
              <el-form-item label="意见" prop="confirmNote">
                <el-input v-model="form.confirmNote" placeholder="请输入意见" type="textarea"
                          v-bind:readonly="this.readonly.confirm"/>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

      </el-form>


      <el-row>
        <el-col :span="24" align="center">
          <el-button v-if="hidden.saveBtn === false" type="success" @click="saveForm">暂 存</el-button>
          <el-button v-if="hidden.changeBtn === false" type="primary" @click="changeForm">修改项目信息</el-button>
          <el-button v-if="hidden.deleteBtn === false" type="danger" @click="deleteForm">删除项目</el-button>
          <el-button v-if="hidden.submitBtn === false" type="primary" @click="submitForm">提交审核</el-button>
          <el-button v-if="hidden.changeAcceptanceBtn === false" type="warning" @click="changeAcceptanceForm">修改验收信息
          </el-button>
          <el-button v-if="hidden.returnBtn === false" type="warning" @click="returnForm">退回新建</el-button>
          <el-button v-if="hidden.confirmBtn === false" type="primary" @click="confirmForm">确 认</el-button>
          <el-button v-if="hidden.addAcceptanceBtn === false" type="primary" @click="addAcceptanceForm">补充验收材料
          </el-button>
          <el-button @click="closeForm">取 消</el-button>
        </el-col>
      </el-row>


    </el-row>


    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="JoinorganizationTitle" :visible.sync="JoinorganizationOpen" width="600px" append-to-body>
      <el-form ref="joinForm" :model="joinForm" :rules="joinRules" label-width="100px" :key="timer">
        <el-row>
          <el-col :span="24">
            <el-form-item label="子项目名称" prop="subjectname">
              <el-input v-model="joinForm.subjectname" placeholder="请输入子项目名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="参加单位" prop="organizationname">
              <el-input v-model="joinForm.organizationname" placeholder="请输入参加单位名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="负责人名称" prop="manager">
              <el-input v-model="joinForm.manager" placeholder="请输入负责人名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="经费（元）" prop="funds">
              <el-input v-model="joinForm.funds" placeholder="0.00"/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitJoinForm">确 定</el-button>
        <el-button @click="cancelJoinForm">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {listUser} from "@/api/system/user";
import {
  acceptanceconfirmProject,
  addProject,
  addProjectacceptance,
  confirmProject,
  getProject,
  getProjectacceptance,
  getProjectConfirm,
  getUplevelProject,
  listProjectdoc,
  listProjectjoinorganization,
  listProjectmember,
  uniqueProject,
  updateProject,
  xinjianzhongProject
} from "@/api/project/project";


import {isNumber} from "@/utils/validate.js";
import TeamData from "@/views/public/team-data";
import DeptData from "@/views/public/dept-data";
import DictData from "@/views/public/dict-data";
import UserData from "@/views/public/user-data";
import ProjectDoc from "@/views/public/project-doc";

export default {
  name: "EditInfo",
  components: {ProjectDoc, UserData, "dept-data": DeptData, "team-data": TeamData, "dict-data": DictData},
  data() {
    return {
      // 各个组件的只读和隐藏属性控制
      readonly: {},
      hidden: {},
      opcode: undefined,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 数据字典
      DictTypeNameProjectType: "项目类型",
      // 数据字典
      DictTypeNameJoinType: "主持参与",


      projectmemberOptions: [],

      ProjectStatus: {
        XinJianZhong: 48,
        DaiQueRen: 40,
        BuTongGuo: 44,
        ZaiYan: 41,
        JieTiDaiQueRen: 45,
        JietiBuTongGuo: 46,
        YiJieTi: 42,
        YiShanChu: 43
      },

      // 数据字典
      DocTypeXiangmuShenbaoShu: "项目申报书",
      DocTypeXiangmuHetong: "项目合同",
      DocTypeShishiFangan: "实施方案",
      DocTypeXiangmuPifu: "项目批复文件",

      DocTypeYanshouShenqingshu: "验收申请书",
      DocTypeYanshouZhengshu: "验收证书",
      DocTypeJishuZongjie: "技术总结",
      DocTypeGongzuoZongjie: "工作总结",

      DocTypeChaxinBaogao: "查新报告",
      DocTypeShenjiBaogao: "审计报告",
      DocTypeJiancheBaogao: "检测报告",

      DocTypeQitaFujian: "其它附件",
      DocTypeYonghuZhengmin: "用户证明",
      DocTypeChengguoZhaopian: "成果照片视频",

      basicdocList1: [],
      basicdocList2: [],
      basicdocList3: [],
      basicdocList4: [],

      acceptdocList1: [],
      acceptdocList2: [],
      acceptdocList3: [],
      acceptdocList4: [],
      acceptdocList5: [],
      acceptdocList6: [],
      acceptdocList7: [],
      acceptdocList8: [],
      acceptdocList9: [],
      acceptdocList10: [],


      // 日期范围
      // 查询参数
      // 表单参数
      form: {},
      timer: '',
      timerkey: '',
      // 表单校验
      rules: {
        projectname: [
          {required: true, message: "项目名称不能为空", trigger: "blur"}
        ],
        projectcode: [
          {required: true, message: "项目编号不能为空", trigger: "blur"}
        ],
        subjectcode: [
          {required: true, message: "项目经费编号不能为空", trigger: "blur"}, {
            required: true,
            trigger: "change",
            validator: this.validateSubjectcode
          }
        ],
        projectfunds: [
          {required: true, message: "项目总经费不能为空", trigger: "blur"}, {trigger: "blur", validator: isNumber}
        ],
        financefunds: [
          {required: true, message: "财政拨款不能为空", trigger: "blur"}, {trigger: "blur", validator: isNumber}
        ],
        canusefunds: [
          {required: true, message: "可支配经费不能为空", trigger: "blur"}, {trigger: "blur", validator: isNumber}
        ],
        teamid: [
          {required: true, message: "项目所属团队不能为空", trigger: "blur"}
        ],
        projectmanagerid: [
          {required: true, message: "项目负责人不能为空", trigger: "blur"}
        ],
        jointype: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        confirmSubjectcode: [
          {required: true, message: "更正的项目经费编号不能为空", trigger: "blur"}, {
            required: true,
            trigger: "change",
            validator: this.validateSubjectcode
          }
        ]
      },

      JoinorganizationTitle: "",
      JoinorganizationOpen: false,
      joinForm: {},
      joinRules: {
        subjectname: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        organizationname: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        manager: [
          {required: true, message: "不能为空", trigger: "blur"}
        ],
        funds: [
          {required: true, message: "不能为空", trigger: "blur"}, {trigger: "blur", validator: isNumber}
        ]
      }
    };

  },

  mounted() {
    console.log("mounted this.$route.meta is ", this.$route.meta);
  },

  beforeCreate() {
    console.log(" beforeCreate this.$route.meta is ", this.$route.meta);
    const projectid = this.$route.params && this.$route.params.projectid;
    // 新增之后，再加编辑项目，会默认将标题 设置为 新增项目，不添加新页面的话，这个问题暂时不能解决，所以先注释掉，一律为 编辑项目
    // if (Number(projectid) === 0) {
    //   this.$route.meta.title = "新增项目";
    // } else {
    //   this.$route.meta.title = "编辑项目";
    // }
  },
  created() {
    console.log(" created this.$route.meta is ", this.$route.meta);
    this.resetTemplateStatus();
    var projectid = this.$route.params && this.$route.params.projectid;
    if (projectid === undefined || Number(projectid) === 0) {
      projectid = undefined;
    } else {

    }

    this.opcode = this.$route.meta.opcode;

    this.getData(projectid);

  },
  methods: {
    /** 查询项目信息 */
    getData(projectid) {

      const this_ = this;

      this.loading = true;
      console.log("loading is begin, projectid is ", projectid);

      if (projectid === undefined) {
        console.log("add a new project now.");
        this.reset(projectid);
        console.log("add a new project now.2");
        this.configTemplateStatus();
        this.loading = false;
      } else {
        getProject(projectid.toString()).then(response => {
          console.log("getProject this.form is ", response.data);

          const data = response.data;

          data.uplevelproject = {"subjectname": null};
          data.acceptance = {projectid: projectid, opinion: null, memo: null};

          data.confirmSubjectcode = data.subjectcode;

          this.form = data;
          this.form.ifacceptancefull = true;

          this.configTemplateStatus();

          // 获取 审核结果信息。
          if (this.form.status === this.ProjectStatus.BuTongGuo) {
            getProjectConfirm(projectid, this.ProjectStatus.BuTongGuo).then(response => {
              console.log("getProjectConfirm is ", response);
              this.form.confirmResult = response.data.applystatus;
              this.form.confirmNote = response.data.auditopinion;
            });
          } else if (this.form.status === this.ProjectStatus.JietiBuTongGuo) {
            getProjectConfirm(projectid, this.ProjectStatus.JietiBuTongGuo).then(response => {
              console.log("getProjectAcceptanceConfirm is ", response);
              this.form.confirmResult = response.data.applystatus;
              this.form.confirmNote = response.data.auditopinion;
            });
          }

          getProjectacceptance(projectid).then(response => {
            console.log("this.form.acceptance is ", response.data);
            if (response.data !== null) {
              this_.form.acceptance = response.data;
            } else {
              this_.form.acceptance = {projectid: projectid, opinion: null, memo: null};
            }
          });

          listProjectjoinorganization({projectid: this.form.projectid}).then(response => {
            if (response.data !== null) {
              this_.form.projectjoinorganizationlist = response.data;
            } else {

            }
            console.log("listProjectjoinorganization is ", response.data);

            getUplevelProject(projectid).then(response => {
              if (response.data !== null) {
                console.log("this.form.uplevelproject is ", response.data);
                this_.form.uplevelproject = response.data;
              } else {
                this_.form.uplevelproject = {subjectname: null};
              }

              console.log("this.form.uplevelproject is ", this_.form.uplevelproject);
              listProjectmember({projectid: this_.form.projectid}).then(response => {

                let rows = response.data;
                this_.projectmemberOptions = rows;
                console.log("projectmemberOptions is ", this_.projectmemberOptions);

                this_.form.projectmemberUserIdList = [];
                this_.projectmemberOptions.forEach(function (obj) {
                  this_.form.projectmemberUserIdList.push(obj.userId);
                });

                console.log("this_.form.projectmemberUserIdList is ", rows);
                listProjectdoc({projectid: this.form.projectid}).then(response => {
                  const this_ = this;
                  let rows = response.data;
                  console.log("listProjectdoc is ", rows);
                  this.form.projectdocList = rows;

                  this.basicdocList1 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "项目申报书"
                  });

                  this_.timer = Date.now().toString();

                  this.basicdocList2 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "项目合同"
                  });

                  this.basicdocList3 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "实施方案"
                  });

                  this.basicdocList4 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "项目批复文件"
                  });

                  this.acceptdocList1 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "验收申请书"
                  });
                  this.acceptdocList2 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "验收证书"
                  });

                  this.acceptdocList3 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "技术总结"
                  });

                  this.acceptdocList4 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "工作总结"
                  });

                  this.acceptdocList5 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "查新报告"
                  });

                  this.acceptdocList6 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "审计报告"
                  });

                  this.acceptdocList7 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "检测报告"
                  });
                  this.acceptdocList8 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "其它附件"
                  });

                  this.acceptdocList9 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "用户证明"
                  });
                  this.acceptdocList10 = this.form.projectdocList.filter(function (item) {
                    return item.doctype === "成果照片视频"
                  });
                });

                this.timer = Date.now().toString();
                this.loading = false;
              });

            });

          });
        });
      }

      const listOptions = [];
      listUser().then(response => {
        console.log("listUser is ", response);
        response.rows.forEach(function (item) {
          //console.log("item is ", item);
          const user = {value: item.realName, id: item.userId, hotKey: item.hotKey};
          //console.log(user);
          listOptions.push(user);
        });
        this.userList = listOptions;
        this.userOptions = listOptions;
      });
    },

    resetTemplateStatus() {
      // 初始化各个组件的状态。
      this.readonly = {basic: true, acceptance: true, confirm: true};
      this.hidden = {
        basic: false,
        acceptance: true,
        confirm: true,
        saveBtn: true,
        changeBtn: true,
        deleteBtn: true,
        submitBtn: true,
        changeAcceptanceBtn: true,
        returnBtn: true,
        confirmBtn: true,
        addAcceptanceBtn: true
      };
    },

    configTemplateStatus() {
      this.resetTemplateStatus();
      console.log("configTemplateStatus is ", this.form.status);

      if (this.form.status === this.ProjectStatus.XinJianZhong) {

        this.readonly.basic = false;
        this.hidden.saveBtn = false;
        this.hidden.submitBtn = false;
        this.hidden.deleteBtn = (this.form.projectid === undefined || this.form.projectid === 0)

      } else if (this.form.status === this.ProjectStatus.DaiQueRen) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {
          // this.hidden.changeBtn = false;
          // this.hidden.deleteBtn = false;
          // this.hidden.returnBtn = false;
        } else if (this.opcode.indexOf("confirm") !== -1) {
          this.readonly.confirm = false;
          this.hidden.confirm = false;
          this.hidden.confirmBtn = false;
        }

      } else if (this.form.status === this.ProjectStatus.BuTongGuo) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {
          this.readonly.confirm = true;
          this.hidden.confirm = false;

          this.hidden.changeBtn = false;
          this.hidden.deleteBtn = false;
        }
      } else if (this.form.status === this.ProjectStatus.ZaiYan) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.returnBtn = false;
          this.hidden.acceptance = false;
        } else if (this.opcode.indexOf("toaccept") !== -1) {
          this.hidden.acceptance = false;
          this.readonly.acceptance = false;
          this.hidden.confirmBtn = false;

        }
      } else if (this.form.status === this.ProjectStatus.JieTiDaiQueRen) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;

          this.hidden.changeAcceptanceBtn = false;

        } else if (this.opcode.indexOf("acceptconfirm") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirm = false;
          this.readonly.confirm = false;
          this.hidden.confirmBtn = false;

        }
      } else if (this.form.status === this.ProjectStatus.JietiBuTongGuo) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {
          this.readonly.confirm = true;
          this.hidden.confirm = false;

          this.hidden.acceptance = false;

          this.hidden.changeBtn = false;
          this.hidden.deleteBtn = false;
          this.hidden.returnBtn = false;
          this.hidden.changeAcceptanceBtn = false;
        }
      } else if (this.form.status === this.ProjectStatus.YiJieTi) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode.indexOf("query") !== -1) {
          this.hidden.acceptance = false;
        } else if (this.opcode.indexOf("addacceptance") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.confirmBtn = false;
          this.readonly.acceptance = false;
        } else if (this.opcode.indexOf("acceptance") !== -1) {
          this.hidden.acceptance = false;
          this.hidden.addAcceptanceBtn = false;
        }
      } else {
        this.readonly.basic = true;
        this.hidden.acceptance = false;
      }
    },

    getSubjectcode(query) {
      return new Promise((resolve, reject) => {
        let res = uniqueProject(query);
        resolve(res);
      });
    },

    async validateSubjectcode(rule, value, callback) {
      if (!value) {
        callback(new Error("项目经费编号不能为空"));
      } else {
        if (this.form.subjectcode === undefined) {
          callback();
        } else {
          let query = {subjectcode: value, projectid: this.form.projectid};
          let res = await this.getSubjectcode(query);
          console.log(res);
          if (res.data > 0) {
            callback(new Error("项目经费编号重复"));
          } else {
            callback();
          }
        }

      }
    },

    // 表单重置
    reset(prjId) {
      this.form = {
        projectid: undefined,
        projectname: undefined,
        projectcode: undefined,
        subjectcode: undefined,

        projectfunds: undefined,
        financefunds: undefined,
        canusefunds: undefined,

        projectbegindate: undefined,
        projectenddate: undefined,
        projecttype: undefined,

        projectmanagerid: undefined,
        organizationid: undefined,
        teamid: undefined,
        memo: undefined,

        jointype: undefined,
        createuserid: undefined,
        status: this.ProjectStatus.XinJianZhong,

        projectTypeLinkText: undefined,
        dictionaryOrderNum: undefined,
        ProjectManagerIDLinkText: undefined,
        organizationIDLinkText: undefined,
        statusLinkText: undefined,
        teamname: undefined,

        projectjoinorganizationlist: [],
        uplevelproject: {subjectname: null},
        projectmemberUserIdList: [],
        projectdocList: [],
        acceptance: {projectid: prjId, opinion: null, memo: null},

        // 审核结果
        confirmSubjectcode: undefined,
        confirmResult: undefined,
        confirmNote: undefined,
        ifacceptancefull: true
      };

      this.resetForm("form");
    },


    // 组件方法
    changeFormDeptId(dept) {
      this.form.organizationid = dept.deptId;

    },


    changeFormProjectTypeValue(value) {

      if (value) {
        this.form.projecttype = value.id.toString();
      } else {
        this.form.projecttype = undefined;
      }

    },

    changeFormTeamValue(value) {

      if (value) {

        this.form.teamid = value.id;
      } else {
        this.form.teamid = undefined;
      }
    },

    changeFormManagerValue(value) {

      if (value) {

        this.form.projectmanagerid = value.userId;
      } else {
        this.form.projectmanagerid = undefined;
      }

    },

    changeJoinTypeValue(value) {

      if (value === undefined) {
        this.form.jointype = undefined;
      } else {
        if (value.id === "1") {
          this.form.jointype = 1;
        } else if (value.id === "2") {
          this.form.jointype = 2;
        } else {

          console.log("changeJoinTypeValue is 意外 ", value);
        }

      }

      console.log("changeJoinTypeValue value is ", value.id, "this.form.jointype is ", this.form.jointype);

      return;

      if (value !== undefined) {
        this.form.jointype = value.id;

        if (this.form.jointype === 1) {
          this.hidden.uplevelproject = true;
        } else {
          this.hidden.uplevelproject = false;
        }
        console.log("changeJoinTypeValue is ", value.id, "this.form.jointype is ", this.form.jointype);

      } else {
        this.form.jointype = undefined;
        this.hidden.uplevelproject = true;
        console.log("changeJoinTypeValue is undefined ", value.id, "this.form.jointype is ", this.form.jointype);
      }


      this.timerkey = Date.now().toString();

    },


    handleProjectmemberListChange(value) {
      // 刷新dialog的组件，否则不渲染。
      this.timer = new Date().getTime();
      console.log("handleProjectmemberListChange", value);
    },


    changeFormProjectMemberValue(user) {
      if (user) {

        console.log("changeFormProjectMemberValue is ", user);
        console.log("projectmemberUserIdList is ", this.form.projectmemberUserIdList);

        let added = false;

        for (let i = 0; i < this.form.projectmemberUserIdList.length; i++) {
          let memberuserid = this.form.projectmemberUserIdList[i];

          if (user.userId === memberuserid) {
            added = true;
            break;
          }
        }

        if (added) {
          this.$message.error("项目成员 " + user.realName + " 已存在。");
        } else {
          this.form.projectmemberUserIdList.push(user.userId);

          for (let i = 0; i < this.projectmemberOptions.length; i++) {
            let item = this.projectmemberOptions[i];
            if (user.userId === item.userId) {
              added = true;
              break;
            }
          }

          if (added) {
            this.$message.error("项目成员已选择。");
          } else {
            const member = {userId: user.userId, realName: user.realName}
            this.projectmemberOptions.push(member);
          }

        }

      } else {

      }
    },

    changeBasicDocList(fileList, docType, operate, docid) {

      console.log("changeBasicDocList is ", docType, "fileList is ", fileList);
      /* 项目申报书 */
      if (docType === this.DocTypeXiangmuShenbaoShu) {
        this.basicdocList1 = fileList;
      }

      /* 项目合同 */

      if (docType === this.DocTypeXiangmuShenbaoShu) {
        this.basicdocList2 = fileList;
      }

      /* 实施方案 */

      if (docType === this.DocTypeShishiFangan) {
        this.basicdocList3 = fileList;
      }

      /* 项目批复文件 */

      if (docType === this.DocTypeXiangmuPifu) {
        this.basicdocList4 = fileList;
      }

      /* 验收申请书 */

      if (docType === this.DocTypeYanshouShenqingshu) {
        this.acceptdocList1 = fileList;
      }
      /* 验收证书 */

      if (docType === this.DocTypeYanshouZhengshu) {
        this.acceptdocList2 = fileList;
      }

      /* 技术总结 */

      if (docType === this.DocTypeJishuZongjie) {
        this.acceptdocList3 = fileList;
      }
      /* 工作总结 */

      if (docType === this.DocTypeGongzuoZongjie) {
        this.acceptdocList4 = fileList;
      }

      /* 查新报告 */

      if (docType === this.DocTypeChaxinBaogao) {
        this.acceptdocList5 = fileList;
      }
      /* 审计报告 */

      if (docType === this.DocTypeShenjiBaogao) {
        this.acceptdocList6 = fileList;
      }
      /* 检测报告 */
      if (docType === this.DocTypeJiancheBaogao) {
        this.acceptdocList7 = fileList;
      }
      /* 其它附件 */

      if (docType === this.DocTypeQitaFujian) {
        this.acceptdocList8 = fileList;
      }
      /* 用户证明 */

      if (docType === this.DocTypeYonghuZhengmin) {
        this.acceptdocList9 = fileList;
      }
      /* 成果照片视频 */

      if (docType === this.DocTypeChengguoZhaopian) {
        this.acceptdocList10 = fileList;
      }

      if (operate === "add") {

        this.form.projectdocList.push({docid: docid, doctype: docType});

      } else if (operate === "remove") {
        for (let i = 0; i < this.form.projectdocList.length; i++) {
          let doc = this.form.projectdocList[i];
          if (doc.docid === docid) {
            this.form.projectdocList.splice(i, 1);
            break;
          }
        }

      }

      console.log("this.basdocList is ", this.form.projectdocList);
    },


    /** 关闭按钮 */
    closeForm() {

      // this.reset();
      this.$router.go(-1)// 返回
      //关闭子页面

      this.$store.state.tagsView.visitedViews.splice(this.$store.state.tagsView.visitedViews.findIndex(item => item.path === this.$route.path), 1)
      this.$router.push(this.$store.state.tagsView.visitedViews[this.$store.state.tagsView.visitedViews.length - 1].path)
    },

    /** 删除按钮 */
    deleteForm() {

      this.$confirm('是否确认删除"' + this.form.projectname + '"的项目?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {

      }).then(() => {
        this.msgSuccess("删除成功");
        this.closeForm();
      });
    },

    /** 暂存 按钮 */
    saveForm: function () {

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          this.form.operateCode = 1; // 暂存代码 提交给后端。

          // 处理掉添加的参与单位，为了更新或修改。
          this.form.projectjoinorganizationlist.forEach(function (item) {
            if (item.joid < 0) {
              item.joid = undefined;
            }
          });

          if (this.form.projectid === undefined) {
            addProject(this.form).then(response => {
              if (response.data === 0) {
                this.msgError("暂存失败");
                this.form.projectid = undefined;
              } else {
                this.msgSuccess("暂存成功");
                this.form.projectid = response.data;
              }
            });
          } else {
            updateProject(this.form).then(response => {
              this.msgSuccess("修改成功");
              getProject(this.form.projectid);
            });
          }
        }
      });
    },

    /*提交 审核 按钮*/
    submitForm: function () {

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          this.form.operateCode = 2; // 提交审核代码 提交给后端。

          // 处理掉添加的参与单位，为了更新或修改。
          this.form.projectjoinorganizationlist.forEach(function (item) {
            if (item.joid < 0) {
              item.joid = undefined;
            }
          });

          if (this.form.projectid === undefined) {
            addProject(this.form).then(response => {
              if (response.data === 0) {
                this.msgError("提交审核失败");
                this.form.projectid = undefined;
              } else {
                this.msgSuccess("提交审核成功");
                this.form.projectid = response.data;
                this.closeForm();
              }
            });
          } else {
            updateProject(this.form).then(response => {
              this.msgSuccess("提交审核成功");
              this.closeForm();
            });

          }
        }
      });
    },

    /** 编辑模式按钮 */
    changeForm() {
      this.form.status = this.ProjectStatus.XinJianZhong;
      this.configTemplateStatus();

    },

    /** 修改验收信息模式按钮 */
    changeAcceptanceForm() {
      console.log(" 修改验收信息 ", this.form.status);

      this.form.status = this.ProjectStatus.ZaiYan;
      this.opcode = "toaccept";
      this.configTemplateStatus();
    },

    /** 退回到新建中 模式按钮 */
    returnForm() {
      const this_ = this;
      this_.$confirm('是否确认将项目退回"新建中"状态?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        // 发送到后台。
        xinjianzhongProject(this_.form);
      }).then(() => {
        this_.closeForm();
        this_.msgSuccess("退回成功");
      });
    },

    /**  验收申请确认 和 新建审核确认 模式按钮 */
    confirmForm() {

      const this_ = this;

      if (this.form.status === this.ProjectStatus.ZaiYan) {
        this_.$confirm('是否确认提交项目 验收信息?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          this_.form.acceptance.projectdocList = this_.form.projectdocList;
          this_.form.acceptance.status = this_.form.status;
          this_.form.acceptance.projectname = this_.form.projectname;
          return addProjectacceptance(this_.form.acceptance);
        }).then(() => {
          this_.closeForm();
          this_.msgSuccess("验收信息提交成功");
        });
      } else if (this.form.status === this.ProjectStatus.DaiQueRen) {
        this.$refs["form"].validate(
          valid => {
            if (valid) {
              console.log("confirmResult is " + this.form.confirmResult);

              if (this_.form.confirmResult == undefined) {
                this.msgError("请选择审核结果");
                return;
              }

              this_.form.confirmUserid = this_.$store.getters.userId;
              const result = this_.form.confirmResult;

              if (result === 1) {
                this_.$confirm('是否确认项目新建审核 通过?', "警告", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning"
                }).then(function () {
                  return confirmProject(this_.form);
                }).then(() => {
                  this_.closeForm();
                  this_.msgSuccess("新建审核成功");
                });
              } else if (result === 2) {

                const note = this_.form.confirmNote;
                console.log("confirmNote is ", note);
                if (note !== null && note !== undefined && note.trim() !== '') {
                  this_.$confirm('是否确认项目新建审核 不通过?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                  }).then(function () {
                    return confirmProject(this_.form);
                  }).then(() => {
                    this_.closeForm();
                    this_.msgSuccess("新建审核不通过 完成");
                  });
                } else {
                  this_.$confirm('您选择的结果为 “不通过”, 请输入意见！', "警告", {
                    confirmButtonText: "确定",
                    type: "warning"
                  }).then(function () {

                  });
                }
              }
            }
          }
        );
      } else if (this.form.status === this.ProjectStatus.JieTiDaiQueRen) {
        this.$refs["form"].validate(
          valid => {
            if (valid) {
              console.log("confirmResult is " + this.form.confirmResult);

              if (this_.form.confirmResult == undefined) {
                this.msgError("请选择审核结果");
                return;
              }

              this_.form.confirmUserid = this_.$store.getters.userId;
              const result = this_.form.confirmResult;

              if (result === 1) {
                this_.$confirm('是否确认项目验收审核 通过?', "警告", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning"
                }).then(function () {
                  return acceptanceconfirmProject(this_.form);
                }).then(() => {
                  this_.closeForm();
                  this_.msgSuccess("项目验收审核成功");
                });
              } else if (result === 2) {

                var note = this_.form.confirmNote;
                console.log("confirmNote is ", note);
                if (note !== null && note !== undefined && note.trim() !== '') {
                  this_.$confirm('是否确认项目验收审核 不通过?', "警告", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                  }).then(function () {
                    return acceptanceconfirmProject(this_.form);
                  }).then(() => {
                    this_.closeForm();
                    this_.msgSuccess("验收审核不通过 完成");
                  });
                } else {
                  this_.$confirm('您选择的结果为 “不通过”, 请输入意见！', "警告", {
                    confirmButtonText: "确定",
                    type: "warning"
                  }).then(function () {

                  });
                }
              }
            }
          }
        );
      } else if (this.form.status === this.ProjectStatus.YiJieTi) {
        this_.$confirm('是否确认提交 补充验收材料?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          this_.form.acceptance.projectdocList = this_.form.projectdocList;
          this_.form.acceptance.status = this_.form.status;
          this_.form.acceptance.projectname = this_.form.projectname;
          return addProjectacceptance(this_.form.acceptance);
        }).then(() => {
          this_.closeForm();
          this_.msgSuccess("补充验收材料提交成功");
        });
      } else {


      }
    },

    /** 补充验收材料模式按钮 */
    addAcceptanceForm() {
      console.log(" 补充验收材料 ", this.form.status);

      this.form.status = this.ProjectStatus.YiJieTi;
      this.opcode = "addacceptance";
      this.configTemplateStatus();
    },


    /* 主持的项目 子 form */

    // 多选框选中数据
    handleJoinorganizationSelectionChange(selection) {
      // this.ids = selection.map(item => item.teamid);
      // this.single = selection.length != 1;
      // this.multiple = !selection.length;
    },


    handleJoinorganizationAdd() {
      this.resetJoinForm();
      this.JoinorganizationOpen = true;
      this.JoinorganizationTitle = "添加项目参加单位";

    },


    handleJoinorganizationUpdate(row) {
      this.JoinorganizationOpen = true;
      this.JoinorganizationTitle = "编辑项目参加单位";
      const joinorg = {
        joid: row.joid,
        subjectname: row.subjectname,
        organizationname: row.organizationname,
        manager: row.manager,
        funds: row.funds
      };

      this.joinForm = joinorg;
    },


    /**  删除按钮操作 */
    handleJoinorganizationDelete(row) {
      const this_ = this;
      const subjectname = row.subjectname
      this.$confirm('是否确认删除"' + subjectname + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        const index = this_.form.projectjoinorganizationlist.indexOf(row);
        this_.form.projectjoinorganizationlist.splice(index, 1);
      }).then(() => {
        this.msgSuccess("删除成功");
      });
    },

    cancelJoinForm: function () {
      this.JoinorganizationOpen = false;
      this.resetJoinForm();
    },

    submitJoinForm: function () {
      const this_ = this;
      this_.$refs["joinForm"].validate(valid => {
        if (valid) {

          if (this_.joinForm.joid !== undefined) {

            this_.form.projectjoinorganizationlist.forEach(function (item) {
              if (item.joid == this_.joinForm.joid) {
                item.subjectname = this_.joinForm.subjectname;
                item.organizationname = this_.joinForm.organizationname;
                item.manager = this_.joinForm.manager;
                item.funds = this_.joinForm.funds;
              }
            });
            this.msgSuccess("修改成功");
          } else {
            let joid = 0;
            this_.form.projectjoinorganizationlist.forEach(function (item) {
              if (item.joid !== undefined && item.joid < joid) {
                joid = item.joid;
              }
            });
            joid = joid - 1;
            const joinorg = {
              joid: joid,
              subjectname: this_.joinForm.subjectname,
              organizationname: this_.joinForm.organizationname,
              manager: this_.joinForm.manager,
              funds: this_.joinForm.funds
            };
            this_.form.projectjoinorganizationlist.push(joinorg);
            this.msgSuccess("添加成功");
          }
          console.log("submit joinForm is ", this.joinForm);
          this_.JoinorganizationOpen = false;
          this.resetJoinForm();
        }
      });
    },

    // 表单重置
    resetJoinForm() {
      this.joinForm = {
        joid: undefined,
        subjectname: undefined,
        organizationname: undefined,
        manager: undefined,
        funds: undefined
      };
      this.resetForm("joinForm");
    },

  }
}
</script>

<style scoped>

</style>
