import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询项目列表
export function listProject(query) {
  return request({
    url: '/project/project/list',
    method: 'get',
    params: query
  })
}

// 查询项目列表
export function listToconfirm(query) {
  return request({
    url: '/project/toconfirm/list',
    method: 'get',
    params: query
  })
}

export function listToacceptanceconfirm(query) {
  return request({
    url: '/project/toacceptanceconfirm/list',
    method: 'get',
    params: query
  })
}


// 查询项目列表
export function listToaddacceptance(query) {
  return request({
    url: '/project/toaddacceptance/list',
    method: 'get',
    params: query
  })
}

// 查询项目列表
export function listFinished(query) {
  return request({
    url: '/project/finished/list',
    method: 'get',
    params: query
  })
}

// 查询项目详细
export function getProject(projectId) {
  return request({
    url: '/project/' + praseStrEmpty(projectId),
    method: 'get'
  })
}

// 校验项目详细
export function uniqueProject(query) {
  return request({
    url: '/project/unique',
    method: 'get',
    params: query
  })
}

// 新增项目
export function addProject(data) {
  return request({
    url: '/project',
    method: 'post',
    data: data
  })
}

// 修改项目
export function updateProject(data) {
  return request({
    url: '/project',
    method: 'put',
    data: data
  })
}

// 删除项目
export function deleteProject(projectIds) {
  return request({
    url: '/project/' + projectIds,
    method: 'delete'
  })
}

// 文件上传
export function uploadFile(data) {
  return request({
    headers: {'Content-Type':'multipart/form-data'},
    url: '/project/upload',
    method: 'post',
    data: data
  })
}

// 文件下载
export function downloadFile(query) {
  return request({
    url: '/project/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}

// 查询项目列表
export function listProjectjoinorganization(query) {
  return request({
    url: '/project/joinorg/list',
    method: 'get',
    params: query
  })
}
// 查询项目列表
export function listUplevelproject(query) {
  return request({
    url: '/project/uplevel/list',
    method: 'get',
    params: query
  })
}

// 查询项目详细
export function getUplevelProject(projectId) {
  return request({
    url: '/project/uplevel/' + praseStrEmpty(projectId),
    method: 'get'
  })
}

// 查询项目列表
export function listProjectmember(query) {
  return request({
    url: '/project/member/list',
    method: 'get',
    params: query
  })
}

// 查询项目列表
export function listProjectdoc(query) {
  return request({
    url: '/project/doc/list',
    method: 'get',
    params: query
  })
}


// 查询项目 审核详细
export function getProjectConfirm(projectId,status) {
  return request({
    url: '/project/confirm/' + praseStrEmpty(projectId) + '/' + status,
    method: 'get'
  })
}


// 新建审核项目
export function confirmProject(data) {
  return request({
    url: '/project/confirm',
    method: 'put',
    data: data
  })
}

// 验收审核 项目
export function acceptanceconfirmProject(data) {
  return request({
    url: '/project/acceptanceconfirm',
    method: 'put',
    data: data
  })
}


// 新建项目验收信息
export function addProjectacceptance(data) {
  return request({
    url: '/project/acceptance',
    method: 'post',
    data: data
  })
}


// 查询项目验收信息
export function getProjectacceptance(projectId) {
  return request({
    url: '/project/acceptance/' + praseStrEmpty(projectId),
    method: 'get'
  })
}


// 退回到新建中
export function xinjianzhongProject(data) {
  return request({
    url: '/project/xinjianzhong',
    method: 'put',
    data: data
  })
}

