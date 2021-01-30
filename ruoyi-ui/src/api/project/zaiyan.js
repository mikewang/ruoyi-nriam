import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询项目列表
export function listProject(query) {
  return request({
    url: '/project/zaiyan/list',
    method: 'get',
    params: query
  })
}

// 查询项目详细
export function getProject(projectId) {
  return request({
    url: '/project/zaiyan/' + praseStrEmpty(projectId),
    method: 'get'
  })
}

// 校验项目详细
export function checkProject(query) {
  return request({
    url: '/project/zaiyan/check',
    method: 'get',
    params: query
  })
}

// 新增项目
export function addProject(data) {
  return request({
    url: '/project/zaiyan',
    method: 'post',
    data: data
  })
}

// 修改项目
export function updateProject(data) {
  return request({
    url: '/project/zaiyan',
    method: 'put',
    data: data
  })
}

// 删除项目
export function deleteProject(projectIds) {
  return request({
    url: '/project/zaiyan/' + projectIds,
    method: 'delete'
  })
}

// 文件上传
export function uploadFile(data) {
  return request({
    headers: {'Content-Type':'multipart/form-data'},
    url: '/project/zaiyan/upload',
    method: 'post',
    data: data
  })
}

// 文件下载
export function downloadFile(query) {
  return request({
    url: '/project/zaiyan/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
