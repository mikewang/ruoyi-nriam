<template>
  <div class="app-container">

    <el-row :gutter="20">
      <el-form v-loading="loading" ref="form" :model="form" :rules="rules" label-width="160px" :key="timer"
      >
        <template>
          <el-tag size="medium" type="info">项目信息</el-tag>
          <el-row >
            <el-col :span="8">
              <el-form-item label="项目名称" prop="projectname">
                <el-input v-bind:disabled="readonly.basic" v-model="form.projectname" placeholder="请输入项目名称" :show-overflow-tooltip="true" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目编号" prop="projectcode">
                <el-input v-bind:disabled="readonly.basic" v-model="form.projectcode" placeholder="请输入项目编号"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目经费编号" prop="subjectcode">
                <el-input v-bind:disabled="readonly.basic" v-model="form.subjectcode" placeholder="请输入项目经费编号"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目总经费（元）" prop="projectfunds">
                <el-input v-bind:disabled="readonly.basic" v-model="form.projectfunds" placeholder="请输入项目总经费（元）"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="财政拨款（元）" prop="financefunds">
                <el-input v-bind:disabled="readonly.basic" v-model="form.financefunds" placeholder="请输入财政拨款（元）"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="可支配经费（元）" prop="canusefunds">
                <el-input v-bind:disabled="readonly.basic" v-model="form.canusefunds" placeholder="请输入可支配经费（元）"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目所属部门" prop="organizationid">
                <el-select v-bind:disabled="readonly.basic" v-model="form.organizationIDLinkText" placeholder="请选择项目所属部门" style="display:block;"
                           clearable @clear="clearDeptValue" @change="changeDeptValue"
                           filterable :filter-method="filterDeptOptions" :show-overflow-tooltip="true">
                  <el-option
                    v-for="item in deptOptions"
                    :key="item.id"
                    :label="item.value"
                    :value="item.id"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目开始日期" prop="projectbegindate">
                <el-date-picker v-bind:disabled="readonly.basic" v-model="form.projectbegindate" type="date" placeholder="请选择日期"
                                value-format="yyyy-MM-dd"
                                style="display:block;"></el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目结束日期" prop="projectenddate">
                <el-date-picker v-bind:disabled="readonly.basic" v-model="form.projectenddate" type="date" placeholder="请选择日期" value-format="yyyy-MM-dd"
                                style="display:block;"></el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目类型" prop="projecttype">
                <el-select v-bind:disabled="readonly.basic" v-model="form.projectTypeLinkText" placeholder="请选择项目类型" style="display:block;"
                           clearable @clear="clearProjectTypeValue" @change="changeProjectTypeValue"
                           filterable :filter-method="filterProjectTypeOptions">
                  <el-option
                    v-for="item in projectTypeOptions"
                    :key="item.id"
                    :label="item.value"
                    :value="item.id"/>
                </el-select>
              </el-form-item>
            </el-col>

            <el-col :span="8">
              <el-form-item label="项目所属团队" prop="teamid">
                <el-select v-bind:disabled="readonly.basic" v-model="form.teamname" placeholder="请选择项目所属团队" style="display:block;"
                           clearable @clear="clearTeamValue" @change="changeTeamValue"
                           filterable :filter-method="filterTeamOptions">
                  <el-option
                    v-for="item in teamOptions"
                    :key="item.id"
                    :label="item.value"
                    :value="item.id"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="项目负责人" prop="projectmanagerid">
                <el-select v-bind:disabled="readonly.basic" v-model="form.projectmanagerid" placeholder="请选择项目负责人" style="display:block;"
                           clearable @clear="clearManagerValue" @change="changeManagerValue"
                           filterable :filter-method="filterManagerOptions">
                  <el-option
                    v-for="item in userOptions"
                    :key="item.id"
                    :label="item.value"
                    :value="item.id"/>
                </el-select>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="8">
              <el-form-item label="主持/参与" prop="jointype">

                <el-select v-bind:disabled="readonly.basic" v-model="form.jointype" placeholder="请选择" style="display:block;" clearable
                           @clear="clearJointypeValue" @change="changeJointypeValue">
                  <el-option
                    v-for="item in jointypeOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"/>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="备注" prop="memo">
                <el-input v-bind:disabled="readonly.basic" v-model="form.memo" placeholder="" type="textarea"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="form.jointype===1">
            <el-col :span="24">
              <el-form-item label="项目参加单位列表" prop="joinOrganizationList">
                <el-row :gutter="10" class="mb8" v-bind:hidden="readonly.basic">
                  <el-col :span="1.5">
                    <el-button plain
                               type="primary"
                               icon="el-icon-plus"
                               size="mini"
                               @click="handleJoinOrganizationAdd"
                               v-hasPermi="['project:zaiyan:add']"
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
                               @click="handleJoinOrganizationDelete"
                               v-hasPermi="['project:zaiyan:remove']"
                    >删除
                    </el-button>
                  </el-col>
                  <right-toolbar @queryTable="getList"></right-toolbar>
                </el-row>
                <el-table :data="form.projectJoinOrganizationList"
                          @selection-change="handleJoinOrganizationSelectionChange"
                          style="display:block;">
                  <el-table-column type="selection" width="50" align="center"/>
                  <el-table-column label="子项目名称" align="center" prop="subjectname" :show-overflow-tooltip="true"/>
                  <el-table-column label="参加单位" align="center" prop="organizationname"/>
                  <el-table-column label="负责人" align="center" prop="manager" width="100"/>
                  <el-table-column label="经费（元）" align="center" prop="funds" width="100"/>
                  <el-table-column
                    label="操作"
                    align="center"
                    width="160"
                    class-name="small-padding fixed-width"
                  >
                    <template slot-scope="scope">
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-edit"
                        @click="handleJoinOrganizationUpdate(scope.row)"
                      >编辑
                      </el-button>
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-delete"
                        @click="handleJoinOrganizationDelete(scope.row)"
                        v-hasPermi="['project:zaiyan:remove']"
                      >删除
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="form.jointype===2">
            <el-col :span="8">
              <el-form-item label="参与项目名称" prop="uplevelsubjectname">
                <el-input v-bind:disabled="readonly.basic" v-model="form.uplevelproject.subjectname" placeholder="请输入项目名称"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="主持单位" prop="uplevelmanageorganization">
                <el-input v-bind:disabled="readonly.basic" v-model="form.uplevelproject.manageorganization" placeholder="请输入项目编号"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="负责人" prop="uplevelmanager">
                <el-input v-bind:disabled="readonly.basic" v-model="form.uplevelproject.manager" placeholder="请输入项目经费编号"/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="经费（元）" prop="uplevelfunds">
                <el-input v-bind:disabled="readonly.basic" v-model="form.uplevelproject.funds" placeholder="0.00"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="24">
              <el-form-item label="项目成员" prop="projectmemberList">
                <template>
                  <el-checkbox-group v-bind:disabled="readonly.basic" v-model="form.projectmemberList"
                                     @change="handleProjectmemberListChange" :key="timer">
                    <el-checkbox v-for="data in projectmemberOptions" :label="data.userid" :key="data.userid">
                      {{ data.realName }}
                    </el-checkbox>
                  </el-checkbox-group>

                  <el-select v-if="readonly.basic == false" v-model="addProjectmemberId" placeholder="请添加成员"
                             clearable @clear="clearProjectMemberValue" @change="changeProjectMemberValue"
                             filterable :filter-method="filterProjectMemberOptions">
                    <el-option
                      v-for="item in userOptions"
                      :key="item.id"
                      :label="item.value"
                      :value="item.id"/>
                  </el-select>
                </template>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>

            <el-col :span="12">
              <el-form-item label="项目申报书" prop="sbfileList">
                <el-upload v-bind:disabled="readonly.basic" action="#" :http-request="requestSBUpload" :before-remove="beforeSBRemove"
                           :on-remove="handleSBUploadRemove" :on-preview="handleSBUploadReview"
                           :file-list="sbfileList" :before-upload="beforeSBUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button size="small" v-if="readonly.basic == false">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="项目合同" prop="htfileList">
                <el-upload v-bind:disabled="readonly.basic" action="#" :http-request="requestHTUpload" :before-remove="beforeHTRemove"
                           :on-remove="handleHTUploadRemove"
                           :file-list="htfileList" :before-upload="beforeHTUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button v-if="readonly.basic == false" size="small" >上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>

          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="实施方案" prop="fafileList">
                <el-upload v-bind:disabled="readonly.basic" action="#" :http-request="requestFAUpload" :before-remove="beforeFARemove"
                           :on-remove="handleFAUploadRemove"
                           :file-list="fafileList" :before-upload="beforeFAUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button  v-if="readonly.basic == false" size="small">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="项目批复文件" prop="pffileList">
                <el-upload v-bind:disabled="readonly.basic" action="#" :http-request="requestPFUpload" :before-remove="beforePFRemove"
                           :on-remove="handlePFUploadRemove"
                           :file-list="pffileList" :before-upload="beforePFUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button  v-if="readonly.basic == false" size="small">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="项目状态" prop="statusLinkText">
                <el-input v-model="form.statusLinkText" readonly disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="信息输入人员" prop="createUserIDLinkText">
                <el-input v-model="form.createUserIDLinkText" readonly disabled/>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template>
          <el-tag v-if="hidden.acceptance == false" size="medium" >项目验收信息</el-tag>
          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance == false" label="项目验收意见" prop="confirmNote" >
                <el-input v-model="form.confirmNote" placeholder="" type="textarea"/>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item v-if="hidden.acceptance == false" label="验收申请书" prop="fafileList">
                <el-upload action="#" :http-request="requestFAUpload" :before-remove="beforeFARemove"
                           :on-remove="handleFAUploadRemove"
                           :file-list="fafileList" :before-upload="beforeFAUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button size="small" v-if="readonly.acceptance == false">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-if="hidden.acceptance == false" label="验收证书" prop="pffileList">
                <el-upload action="#" :http-request="requestPFUpload" :before-remove="beforePFRemove"
                           :on-remove="handlePFUploadRemove"
                           :file-list="pffileList" :before-upload="beforePFUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button size="small" v-if="readonly.acceptance == false">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item v-if="hidden.acceptance == false" label="技术总结" prop="fafileList">
                <el-upload action="#" :http-request="requestFAUpload" :before-remove="beforeFARemove"
                           :on-remove="handleFAUploadRemove"
                           :file-list="fafileList" :before-upload="beforeFAUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button size="small" v-if="readonly.acceptance == false">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-if="hidden.acceptance == false" label="工作总结" prop="pffileList">
                <el-upload action="#" :http-request="requestPFUpload" :before-remove="beforePFRemove"
                           :on-remove="handlePFUploadRemove"
                           :file-list="pffileList" :before-upload="beforePFUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button size="small" v-if="readonly.acceptance == false">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item v-if="hidden.acceptance == false" label="查新报告" prop="fafileList">
                <el-upload action="#" :http-request="requestFAUpload" :before-remove="beforeFARemove"
                           :on-remove="handleFAUploadRemove"
                           :file-list="fafileList" :before-upload="beforeFAUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button size="small" v-if="readonly.acceptance == false">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-if="hidden.acceptance == false" label="审计报告" prop="pffileList">
                <el-upload action="#" :http-request="requestPFUpload" :before-remove="beforePFRemove"
                           :on-remove="handlePFUploadRemove"
                           :file-list="pffileList" :before-upload="beforePFUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button size="small" v-if="readonly.acceptance == false">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item v-if="hidden.acceptance == false" label="检测报告" prop="fafileList">
                <el-upload action="#" :http-request="requestFAUpload" :before-remove="beforeFARemove"
                           :on-remove="handleFAUploadRemove"
                           :file-list="fafileList" :before-upload="beforeFAUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button size="small" v-if="readonly.acceptance == false">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-if="hidden.acceptance == false" label="其它附件" prop="pffileList">
                <el-upload action="#" :http-request="requestPFUpload" :before-remove="beforePFRemove"
                           :on-remove="handlePFUploadRemove"
                           :file-list="pffileList" :before-upload="beforePFUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button size="small" v-if="readonly.acceptance == false">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item v-if="hidden.acceptance == false" label="用户证明" prop="fafileList">
                <el-upload action="#" :http-request="requestFAUpload" :before-remove="beforeFARemove"
                           :on-remove="handleFAUploadRemove"
                           :file-list="fafileList" :before-upload="beforeFAUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button size="small" v-if="readonly.acceptance == false">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item v-if="hidden.acceptance == false" label="成果照片视频" prop="pffileList">
                <el-upload action="#" :http-request="requestPFUpload" :before-remove="beforePFRemove"
                           :on-remove="handlePFUploadRemove"
                           :file-list="pffileList" :before-upload="beforePFUpload"
                           v-hasPermi="['project:project:edit']">
                  <el-button size="small" v-if="readonly.acceptance == false">上传文件<i class="el-icon-upload el-icon--right"></i>
                  </el-button>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="16">
              <el-form-item v-if="hidden.acceptance == false" label="备注" prop="confirmNote">
                <el-input v-model="form.confirmNote" placeholder="" type="textarea"/>
              </el-form-item>
            </el-col>
          </el-row>
        </template>

        <template>
          <el-tag v-if="this.opcode.indexOf('confirm') !== -1 && this.hidden.confirm == false" size="medium">项目新建审核信息</el-tag>
          <el-tag v-if="this.opcode.indexOf('acceptconfirm') !== -1 && this.hidden.confirm == false" size="medium">项目验收审核信息</el-tag>
          <el-row  v-if="this.hidden.confirm == false">
            <el-col :span="8">
              <el-form-item label="更正项目经费编号" prop="confirmSubjectcode">
                <el-input v-if="this.opcode === 'confirm'"  v-model="form.confirmSubjectcode"/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="this.hidden.confirm == false">
            <el-col :span="12">
              <el-form-item label="请选择审核结果" prop="confirmResult">
                <template>
                  <el-radio-group v-model="form.confirmResult">
                    <el-radio :label="1">通过</el-radio>
                    <el-radio :label="-1">不通过</el-radio>
                  </el-radio-group>
                </template>
              </el-form-item>
            </el-col>
            <el-col :span="8" v-if="this.opcode.indexOf('acceptConfirm') !== -1" >
              <el-form-item  label="" prop="confirmSubjectcode">
                <el-checkbox v-model="checked">验收材料已齐全</el-checkbox>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-if="this.hidden.confirm == false">
            <el-col :span="16">
              <el-form-item label="请输入意见" prop="confirmNote">
                <el-input v-model="form.confirmNote" placeholder="" type="textarea"/>
              </el-form-item>
            </el-col>
          </el-row>
        </template>


        <template>
          <el-row >
            <el-col :span="24" align="center">
              <el-button v-if="hidden.saveBtn !== true" type="success" @click="saveForm">暂 存</el-button>
              <el-button v-if="hidden.changeBtn !== true" type="primary" @click="changeForm">修改项目信息</el-button>
              <el-button v-if="hidden.deleteBtn !== true" type="danger" @click="deleteForm">删除</el-button>
              <el-button v-if="hidden.submitBtn !== true" type="primary" @click="submitForm">提交审核</el-button>
              <el-button v-if="hidden.changeAcceptanceBtn !== true" type="warning" @click="changeForm">修改验收信息</el-button>
              <el-button v-if="hidden.returnBtn !== true" type="warning" @click="returnForm">退回新建</el-button>
              <el-button v-if="hidden.confirmBtn !== true" type="primary" @click="confirmForm">确 认</el-button>
              <el-button @click="closeForm">取 消</el-button>
            </el-col>
          </el-row>
        </template>

      </el-form>


    </el-row>


    <!-- 添加或修改菜单对话框 -->
    <el-dialog :title="joinOrganizationTitle" :visible.sync="joinOrganizationOpen" width="600px" append-to-body>
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
import {listTeam} from "@/api/project/team";
import {listData} from "@/api/system/dict/data";
import {listDept} from "@/api/system/dept";
import {listUser} from "@/api/system/user";
import {
  addProject,
  checkProject,
  downloadFile,
  getProject,
  getUplevelProject,
  listProjectdoc,
  listProjectjoinorganization,
  listProjectmember,
  updateProject,
  uploadFile,
  confirmProject
} from "@/api/project/project";
import {isNumber} from "@/utils/validate.js"

export default {
  name: "EditInfo",
  data() {
    return {
      // 各个组件的只读和隐藏属性控制
      readonly: {},
      hidden:{},
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
      projectTypeOptions: [],
      projectTypeList: [],
      // 数据字典
      teamOptions: [],
      teamList: [],

      // 数据字典
      deptOptions: [],
      deptList: [],

      userOptions: [],
      userList: [],

      jointypeOptions: [{value: 1, label: "主持"}, {value: 2, label: "参与"}],

      addProjectmemberId: undefined,
      projectmemberOptions: [],

      ProjectStatus: {XinJianZhong: 48, DaiQueRen: 40,BuTongGuo:44, ZaiYan: 41, JieTiDaiQueRen: 45,JietiBuTongGuo:46, YiJieTi: 42, YiShanChu: 43},

      sbfileList: [],
      htfileList: [],
      fafileList: [],
      pffileList: [],

      yssqfileList: [],
      yszsfileList: [],
      jszjfileList: [],
      gzzjfileList: [],
      cxbgfileList: [],
      jsbgfileList: [],
      jcbgfileList: [],
      qtfjfileList: [],
      yhzmfileList: [],
      cgzpfileList: [],


      // 日期范围
      // 查询参数
      // 表单参数
      form: {},
      timer: '',
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

      joinOrganizationTitle: "",
      joinOrganizationOpen: false,
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
    console.log("mounted this.$route.meta is ",   this.$route.meta);
  },

  beforeCreate() {
    console.log(" beforeCreate this.$route.meta is ",   this.$route.meta);
    const projectid = this.$route.params && this.$route.params.projectid;
    // 新增之后，再加编辑项目，会默认将标题 设置为 新增项目，不添加新页面的话，这个问题暂时不能解决，所以先注释掉，一律为 编辑项目
    // if (Number(projectid) === 0) {
    //   this.$route.meta.title = "新增项目";
    // } else {
    //   this.$route.meta.title = "编辑项目";
    // }
  },
  created() {
    console.log(" created this.$route.meta is ",   this.$route.meta);
    const projectid = this.$route.params && this.$route.params.projectid;
    if (Number(projectid) === 0) {
    } else {
      this.form.projectid = projectid;
    }

    this.opcode = this.$route.meta.opcode;

    this.getList();

  },
  methods: {
    /** 查询用户列表 */
    getList() {

      const this_ = this;

      this.loading = true;
      console.log("loading is begin.");

      if (this.form.projectid == undefined) {
        this.reset();
        this.configTemplateStatus();
        this.loading = false;
      } else {
        let projectid = this.form.projectid;
        getProject(projectid.toString()).then(response => {
          console.log("this.form is ", response.data);

          const data = response.data;

          data.uplevelproject = {"subjectname": null};

          data.confirmSubjectcode = data.subjectcode;

          this.form = data;

          this.configTemplateStatus();

          listProjectjoinorganization({projectid: this.form.projectid}).then(response => {
            if (response.data !== null) {
              this_.form.projectJoinOrganizationList = response.data;
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

              console.log("this.form.uplevelproject is ", this.form.uplevelproject);
              listProjectmember({projectid: this.form.projectid}).then(response => {

                let rows = response.data;

                this_.projectmemberOptions = rows;
                this_.form.projectmemberList = [];
                this_.projectmemberOptions.forEach(function (obj) {
                  this_.form.projectmemberList.push(obj.userid);
                });

                console.log("this_.form.projectmemberList is ", rows);
                listProjectdoc({projectid: this.form.projectid}).then(response => {
                  const this_ = this;
                  let rows = response.data;
                  console.log("listProjectdoc is ", rows);
                  this.form.projectdocList = rows;
                  this.sbfileList = this.filterProjectdoc("项目申报书");
                  console.log("项目申报书 is ", this.sbfileList);
                  this.htfileList = this.filterProjectdoc("项目合同");
                  console.log("项目合同 is ", this.htfileList);
                  this.fafileList = this.filterProjectdoc("实施方案");
                  console.log("实施方案 is ", this.fafileList);
                  this.pffileList = this.filterProjectdoc("项目批复文件");
                  console.log("项目批复文件 is ", this.pffileList);
                });

                this.loading = false;
              });

            });


          });


        });


      }


      listData({"dictType": "项目类型"}).then(response => {
        console.log(response);

        var listOptions = [];
        response.rows.sort(function (a, b) {
          return a.dictValue < b.dictValue
        }).forEach(function (item) {
          const projecttype = {value: item.dictLabel, id: item.dictValue};
          listOptions.push(projecttype);
        });

        this.projectTypeList = listOptions;
        this.projectTypeOptions = listOptions;

        listOptions = [];

        listDept().then(response => {
          console.log(response);

          response.data.forEach(function (item) {
            const dept = {value: item.deptName, id: item.deptId};
            listOptions.push(dept);
          });
          this.deptList = listOptions;
          this.deptOptions = listOptions;

          listOptions = [];
          listTeam().then(response => {
            console.log(response);
            response.rows.forEach(function (item) {
              const team = {value: item.teamname, id: item.teamid};
              listOptions.push(team);
            });
            this.teamList = listOptions;
            this.teamOptions = listOptions;

            listOptions = [];
            listUser().then(response => {
              console.log(response);
              response.rows.forEach(function (item) {
                //console.log("item is ", item);
                const user = {value: item.realName, id: item.userId, hotKey: item.hotKey};
                //console.log(user);
                listOptions.push(user);
              });
              this.userList = listOptions;
              this.userOptions = listOptions;
            });
          });
        });
      });

    },

    resetTemplateStatus() {
      this.readonly = {basic: true,acceptance:true};
      this.hidden = {basic:false,acceptance:true,confirm:true, saveBtn:true,changeBtn:true,deleteBtn:true,submitBtn:true,changeAcceptanceBtn:true,returnBtn:true,confirmBtn:true };
    },

    configTemplateStatus() {
      this.resetTemplateStatus();
      console.log("configTemplateStatus is ", this.form.status);
      if (this.form.status === this.ProjectStatus.XinJianZhong) {

        this.readonly.basic = false;
        this.hidden.saveBtn = false;
        this.hidden.submitBtn = false;
        console.log("configTemplateStatus projectid is", this.form.projectid);
        this.hidden.deleteBtn = (this.form.projectid === 0 || this.form.projectid === undefined)

      }
      else if (this.form.status === this.ProjectStatus.DaiQueRen) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode === "query") {
          this.hidden.changeBtn = false;
          this.hidden.deleteBtn = false;
          this.hidden.returnBtn = false;
        }
        else if (this.opcode === "confirm") {
          this.hidden.confirmBtn = false;
        }

      }
      else if (this.form.status === this.ProjectStatus.ZaiYan) {
        console.log("this.opcode is ", this.opcode);
        if (this.opcode === "query") {
          this.hidden.acceptance = false;
          this.readonly.acceptance = false;
          this.hidden.confirmBtn = false;
        }
        else if (this.opcode === "toaccept") {
          this.hidden.confirmBtn = false;
        }

      }
      else {
        this.readonly.basic = true;
      }
    },

    getSubjectcode(query) {
      return new Promise((resolve, reject) => {
        let res = checkProject(query);
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
    reset() {
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

        projectJoinOrganizationList: [],
        uplevelproject: {subjectname: null},
        projectmemberList: [],
        projectdocList: [],

        // 审核结果
        confirmSubjectcode: undefined,
        confirmResult: undefined,
        confirmNote: undefined
      };

      this.resetForm("form");
    },


    clearDeptValue() {

    },
    changeDeptValue(value) {
      if (value) {
        this.form.organizationid = value;
      } else {
        this.form.organizationid = undefined;
      }
    },

    filterDeptOptions(v) {
      console.log("filter value is " + v);
      if (v) {
        this.deptOptions = this.deptList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.deptOptions = this.deptList;
      }
    },

    clearProjectTypeValue() {
      this.form.projecttype = undefined;
      this.form.projectTypeLinkText = undefined;
    },

    changeProjectTypeValue(value) {

      if (value) {
        this.form.projecttype = value;
      } else {
        this.form.projecttype = undefined;
      }

    },

    filterProjectTypeOptions(v) {

      console.log("filter value is " + v);

      if (v) {
        this.projectTypeOptions = this.projectTypeList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.projectTypeOptions = this.projectTypeList;
      }
    },

    clearTeamValue() {


    },

    changeTeamValue(value) {

      if (value) {

        this.form.teamid = value;
      } else {
        this.form.teamid = undefined;
      }
    },

    filterTeamOptions(v) {

      console.log("filter value is " + v);

      if (v) {
        this.teamOptions = this.teamList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          if (item.value.indexOf(val) !== -1) return true
          // if (item.szm.substring(0, 1).indexOf(val) !== -1) return true
          // if (item.szm.indexOf(val) !== -1) return true
        });
      } else {
        this.teamOptions = this.teamList;
      }
    },

    clearManagerValue() {


    },

    changeManagerValue(value) {

      if (value) {

        this.form.projectmanagerid = value;
      } else {
        this.form.projectmanagerid = undefined;
      }

    },

    filterManagerOptions(v) {

      console.log("filterManagerOptions value is " + v);

      if (v) {
        this.userOptions = this.userList.filter((item) => {
          // 如果直接包含输入值直接返回true
          const val = v.toLowerCase()
          const py = item.hotKey;
          var hh = -1;
          if (py !== undefined && py !== null) {
            hh = py.indexOf(val);
          }

          if (item.value.indexOf(val) !== -1 || hh !== -1) return true

        });
      } else {
        this.userOptions = this.userList;
      }
    },

    clearJointypeValue() {
      console.log("clear jointype");
    },

    changeJointypeValue(value) {

      if (value) {
        this.form.jointype = value;
      } else {
        this.form.jointype = undefined;
      }

    },

    filterJointypeValue() {
      console.log("filter jointype");
    },

    createFilter(v) {
      return (item) => {
        //  console.log("item is ", item.hotKey);
        const queryString = v.toLowerCase();
        var typename = item.value;

        var ll = typename.indexOf(queryString);

        var py = item.hotKey;

        var hh = -1;
        if (py !== undefined && py !== null) {
          hh = py.indexOf(queryString);
        }
        //console.log("type is " + typename, queryString, ll);

        return (ll >= 0 || hh >= 0);
      };
    },

    clearProjectMemberValue() {

    },

    changeProjectMemberValue(value) {
      if (value) {

        var added = false;

        for (let i = 0; i < this.form.projectmemberList.length; i++) {
          let item = this.form.projectmemberList[i];
          if (value === item) {
            added = true;
            break;
          }
        }

        if (added) {
          this.$message.info("项目成员已存在。");
        } else {
          this.form.projectmemberList.push(value);

          for (let i = 0; i < this.projectmemberOptions.length; i++) {
            let item = this.projectmemberOptions[i];
            if (value === item.userid) {
              added = true;
              break;
            }
          }

          if (added) {
            this.$message.info("项目成员已选择。");
          } else {
            var realName = "";
            for (let i = 0; i < this.userList.length; i++) {
              let item = this.userList[i];
              if (value === item.id) {
                realName = item.value;
                break;
              }
            }
            const member = {userid: value, realName: realName}
            this.projectmemberOptions.push(member);
          }

        }

      } else {

      }
    },

    filterProjectMemberOptions(v) {

      console.log("filter value is " + v);

      if (v) {

        this.userOptions = this.userList.filter(this.createFilter(v));
      } else {
        this.userOptions = this.userList;
      }
    },

    handleProjectmemberListChange(value) {
      // 刷新dialog的组件，否则不渲染。
      this.timer = new Date().getTime();
      console.log("handleProjectmemberListChange", value);
    },


    filterProjectdoc(doctype) {
      const doclist = [];

      if (doctype !== "") {
        for (let i = 0; i < this.form.projectdocList.length; i++) {
          let item = this.form.projectdocList[i];
          if (item.doctype === doctype) {
            doclist.push({"name": item.docname, "url": item.docid});
          }
        }
      }
      return doclist;
    },


    /* 项目申报书 */
    beforeSBUpload(file) {

      let x = true
      let fileList = this.sbfileList;

      console.log("fileList is ", fileList);
      for (let i = 0; i < fileList.length; i++) {
        let item = fileList[i];
        if (item.name === file.name) {
          console.log("file existed now ,", file.name);
          x = false;
          break;
        }
      }
      return x;

    },

    requestSBUpload: function (params) {
      let file = params.file;
      console.log(file);
      let formData = new FormData();
      formData.append('file', file);
      uploadFile(formData).then(response => {
        console.log("response.name is ", response.name);
        console.log("response.url is ", response.url);
        this.sbfileList.push({name: response.name, url: response.url});
        console.log("sbfileList is ", this.sbfileList);

        this.form.projectdocList.push({docid: response.url, doctype: "项目申报书"});

      });
    },

    beforeSBRemove(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeSBRemove index=" + index, file.name);
      return true;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleSBUploadRemove(file) {

      let today = new Date();

      let docid = file.url;

      let index = this.sbfileList.indexOf(file);
      if (index !== -1) {
        this.sbfileList.splice(index, 1);
      }
      console.log("handleSBUploadRemove index=" + index, file.name, today.toDateString());
      console.log("this.sbfileList is ", this.sbfileList);

      let index2 = -1;
      for (let i = 0; i < this.form.projectdocList.length; i++) {
        let item = this.form.projectdocList[i];
        if (item.docid === docid) {
          index2 = i;
          break;
        }
      }

      if (index2 !== -1) {
        this.form.projectdocList.splice(index2, 1);
      }
      console.log("projectdocList all items,", this.form.projectdocList);

      return;
    },

    handleSBUploadReview(file) {
      this.$confirm('是否确认下载"' + file.name + '"的文件?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        downloadFile({"file": file.url}).then(response => {
          var fileURL = window.URL.createObjectURL(new Blob([response]));
          var fileLink = document.createElement('a');

          console.log("response.data is ", response);

          fileLink.href = fileURL;
          fileLink.setAttribute('download', file.name);
          document.body.appendChild(fileLink);

          fileLink.click();
          URL.revokeObjectURL(fileURL);

        }).catch(console.error);
      }).then(() => {
        this.msgSuccess("下载开始");
      })
    },

    /* 项目合同 */
    beforeHTUpload(file) {

      let x = true
      let fileList = this.htfileList;

      console.log("fileList is ", fileList);
      for (let i = 0; i < fileList.length; i++) {
        let item = fileList[i];
        if (item.name === file.name) {
          console.log("file existed now ,", file.name);
          x = false;
          break;
        }
      }
      return x;

    },

    requestHTUpload: function (params) {
      let file = params.file;
      console.log(file);
      let formData = new FormData();
      formData.append('file', file);
      uploadFile(formData).then(response => {
        console.log("response.name is ", response.name);
        console.log("response.url is ", response.url);
        this.htfileList.push({name: response.name, url: response.url});
        console.log("htfileList is ", this.htfileList);

        this.form.projectdocList.push({docid: response.url, doctype: "项目合同"});

      });
    },

    beforeHTRemove(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeSBRemove index=" + index, file.name);
      return true;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleHTUploadRemove(file) {

      let today = new Date();

      let docid = file.url;

      let index = this.htfileList.indexOf(file);
      if (index !== -1) {
        this.htfileList.splice(index, 1);
      }
      console.log("handleSBUploadRemove index=" + index, file.name, today.toDateString());
      console.log("this.htfileList is ", this.htfileList);

      let index2 = -1;
      for (let i = 0; i < this.form.projectdocList.length; i++) {
        let item = this.form.projectdocList[i];
        if (item.docid === docid) {
          index2 = i;
          break;
        }
      }

      if (index2 !== -1) {
        this.form.projectdocList.splice(index2, 1);
      }
      console.log("projectdocList all items,", this.form.projectdocList);

      return;
    },

    /* 实施方案 */
    beforeFAUpload(file) {

      let x = true
      let fileList = this.fafileList;

      console.log("fileList is ", fileList);
      for (let i = 0; i < fileList.length; i++) {
        let item = fileList[i];
        if (item.name === file.name) {
          console.log("file existed now ,", file.name);
          x = false;
          break;
        }
      }
      return x;

    },

    requestFAUpload: function (params) {
      let file = params.file;
      console.log(file);
      let formData = new FormData();
      formData.append('file', file);
      uploadFile(formData).then(response => {
        console.log("response.name is ", response.name);
        console.log("response.url is ", response.url);
        this.fafileList.push({name: response.name, url: response.url});
        console.log("fafileList is ", this.fafileList);

        this.form.projectdocList.push({docid: response.url, doctype: "实施方案"});

      });
    },

    beforeFARemove(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforeFARemove index=" + index, file.name);
      return true;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handleFAUploadRemove(file) {

      let today = new Date();

      let docid = file.url;

      let index = this.fafileList.indexOf(file);
      if (index !== -1) {
        this.fafileList.splice(index, 1);
      }
      console.log("handleFAUploadRemove index=" + index, file.name, today.toDateString());
      console.log("this.fafileList is ", this.fafileList);

      let index2 = -1;
      for (let i = 0; i < this.form.projectdocList.length; i++) {
        let item = this.form.projectdocList[i];
        if (item.docid === docid) {
          index2 = i;
          break;
        }
      }

      if (index2 !== -1) {
        this.form.projectdocList.splice(index2, 1);
      }
      console.log("projectdocList all items,", this.form.projectdocList);

      return;
    },

    /* 项目批复文件 */
    beforePFUpload(file) {

      let x = true
      let fileList = this.pffileList;

      console.log("fileList is ", fileList);
      for (let i = 0; i < fileList.length; i++) {
        let item = fileList[i];
        if (item.name === file.name) {
          console.log("file existed now ,", file.name);
          x = false;
          break;
        }
      }
      return x;

    },

    requestPFUpload: function (params) {
      let file = params.file;
      console.log(file);
      let formData = new FormData();
      formData.append('file', file);
      uploadFile(formData).then(response => {
        console.log("response.name is ", response.name);
        console.log("response.url is ", response.url);
        this.pffileList.push({name: response.name, url: response.url});
        console.log("pffileList is ", this.pffileList);

        this.form.projectdocList.push({docid: response.url, doctype: "项目批复文件"});

      });
    },

    beforePFRemove(file, fileList) {
      let index = fileList.indexOf(file);
      console.log("beforePFRemove index=" + index, file.name);
      return true;
      //  return this.$confirm(`确定移除 ${ file.name }？`);
    },

    handlePFUploadRemove(file) {

      let today = new Date();

      let docid = file.url;

      let index = this.pffileList.indexOf(file);
      if (index !== -1) {
        this.pffileList.splice(index, 1);
      }
      console.log("handlePFUploadRemove index=" + index, file.name, today.toDateString());
      console.log("this.pffileList is ", this.pffileList);

      let index2 = -1;
      for (let i = 0; i < this.form.projectdocList.length; i++) {
        let item = this.form.projectdocList[i];
        if (item.docid === docid) {
          index2 = i;
          break;
        }
      }

      if (index2 !== -1) {
        this.form.projectdocList.splice(index2, 1);
      }
      console.log("projectdocList all items,", this.form.projectdocList);

      return;
    },

    /** 关闭按钮 */
    closeForm() {

      this.reset();
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

    /** 提交按钮 */
    saveForm: function () {

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          this.form.operateCode = 1; // 暂存代码 提交给后端。

          // 处理掉添加的参与单位，为了更新或修改。
          this.form.projectJoinOrganizationList.forEach(function (item) {
            if (item.joid < 0) {
              item.joid = undefined;
            }
          });

          if (this.form.projectid !== undefined) {
            updateProject(this.form).then(response => {
              this.msgSuccess("修改成功");
              getProject(this.form.projectid);

            });
          } else {

            addProject(this.form).then(response => {

              if (response.data === 0) {
                this.msgError("暂存失败");
                this.form.projectid = undefined;
              } else {
                this.msgSuccess("暂存成功");
                this.form.projectid = response.data;
              }

            });
          }
        }
      });
    },

    submitForm: function () {

      this.$refs["form"].validate(valid => {
        if (valid) {
          console.log("submit form is ", this.form);
          this.form.operateCode = 2; // 提交审核代码 提交给后端。

          // 处理掉添加的参与单位，为了更新或修改。
          this.form.projectJoinOrganizationList.forEach(function (item) {
            if (item.joid < 0) {
              item.joid = undefined;
            }
          });

          if (this.form.projectid !== undefined) {
            updateProject(this.form).then(response => {
              this.msgSuccess("提交审核成功");
              this.closeForm();

            });
          } else {

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
          }
        }
      });
    },

    /** 编辑模式按钮 */
    changeForm() {
      this.form.status = this.ProjectStatus.XinJianZhong
      this.configTemplateStatus();

    },

    /** 修改验收信息模式按钮 */
    verifyAcceptanceForm() {
      // Response.Redirect("FinishProject.aspx?mode=edit&kid=" + ProjectDetail1.lab_ProjectID.Text
      //   + "&from=" + Request["from"].ToString() + "&steam="
      //   + Request["steam"] + "&syear=" + Request["syear"]);
    },

    /** 退回到新建中 模式按钮 */
    returnForm() {
      this.$confirm('是否确认将"' + this.form.projectname + '"的项目退回"新建中"状态?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        // 发送到后台。

      }).then(() => {
        this.msgSuccess("退回成功");
        this.closeForm();

      });
    },

    /** 新建审核确认 模式按钮 */
    confirmForm() {

      const this_ = this;

      this.$refs["form"].validate(
        valid => {
          if (valid) {
            console.log("confirmResult is " + this.form.confirmResult);

            if (this_.form.confirmResult == undefined) {
              this.msgError("请选择审核结果");
              return;
            }

            const result = this_.form.confirmResult;

            if (result === 1) {
              this_.$confirm('是否确认审核"' + this.form.projectname + '"的项目 通过?', "警告", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
              }).then(function () {
                confirmProject(this_.form).then(response => {
                  this_.msgSuccess("新建审核成功");
                  this_.closeForm();
                });
              });
            }
            else if (result === -1) {

              var note = this_.form.confirmNote;
              console.log("confirmNote is ", note);
              if (note !== null && note !== undefined && note.trim() !== '' ) {
                this_.$confirm('是否确认审核"' + this.form.projectname + '"的项目 不通过?', "警告", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning"
                }).then(function () {
                  confirmProject(this_.form).then(response => {
                    this_.msgSuccess("新建审核不通过 完成");
                    this_.closeForm();
                  });
                });
              }
              else {
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
    },

    acceptanceForm() {

      const this_ = this;

      this.$refs["form"].validate(
        valid => {
          if (valid) {
            console.log("confirmResult is " + this.form.confirmResult);

            this_.$confirm('是否确认提交"' + this.form.projectname + '"的项目的验收信息?', "警告", {
              confirmButtonText: "确定",
              cancelButtonText: "取消",
              type: "warning"
            }).then(function () {
              confirmProject(this_.form).then(response => {
                this_.msgSuccess("验收信息提交成功");
                this_.closeForm();
              });
            });
          }
        }
      );
    },

    /* 主持的项目 子 form */

    // 多选框选中数据
    handleJoinOrganizationSelectionChange(selection) {
      // this.ids = selection.map(item => item.teamid);
      // this.single = selection.length != 1;
      // this.multiple = !selection.length;
    },


    handleJoinOrganizationAdd() {
      this.resetJoinForm();
      this.joinOrganizationOpen = true;
      this.joinOrganizationTitle = "添加项目参加单位";

    },


    handleJoinOrganizationUpdate(row) {
      this.joinOrganizationOpen = true;
      this.joinOrganizationTitle = "编辑项目参加单位";
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
    handleJoinOrganizationDelete(row) {
      const this_ = this;
      const subjectname = row.subjectname
      this.$confirm('是否确认删除"' + subjectname + '"的数据项?', "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(function () {
        const index = this_.form.projectJoinOrganizationList.indexOf(row);
        this_.form.projectJoinOrganizationList.splice(index, 1);
      }).then(() => {
        this.msgSuccess("删除成功");
      });
    },

    cancelJoinForm: function () {
      this.joinOrganizationOpen = false;
      this.resetJoinForm();
    },

    submitJoinForm: function () {
      const this_ = this;
      this_.$refs["joinForm"].validate(valid => {
        if (valid) {

          if (this_.joinForm.joid !== undefined) {

            this_.form.projectJoinOrganizationList.forEach(function (item) {
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
            this_.form.projectJoinOrganizationList.forEach(function (item) {
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
            this_.form.projectJoinOrganizationList.push(joinorg);
            this.msgSuccess("添加成功");
          }
          console.log("submit joinForm is ", this.joinForm);
          this_.joinOrganizationOpen = false;
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
