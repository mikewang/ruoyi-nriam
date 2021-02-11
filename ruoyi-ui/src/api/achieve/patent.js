import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询专利列表
export function listPatent(query) {
  return request({
    url: '/achieve/patent/list',
    method: 'get',
    params: query
  })
}


// 查询专利详细
export function getPatent(patentid) {
  return request({
    url: '/achieve/patent/' + praseStrEmpty(patentid),
    method: 'get'
  })
}

// 校验专利号是否重复
export function uniquePatent(query) {
  return request({
    url: '/achieve/patent/unique',
    method: 'get',
    params: query
  })
}


// 新增专利
export function addPatent(data) {
  return request({
    url: '/achieve/patent',
    method: 'post',
    data: data
  })
}

// 修改专利
export function updatePatent(data) {
  return request({
    url: '/achieve/patent',
    method: 'put',
    data: data
  })
}

// 删除专利
export function deletePatent(patentids) {
  return request({
    url: '/achieve/patent/' + patentids,
    method: 'delete'
  })
}
// 审核专利
export function confirmPatent(data) {
  return request({
    url: '/achieve/patent/confirm',
    method: 'put',
    data: data
  })
}

// 查询专利 审核详细
export function getPatentConfirm(patentid,status) {
  return request({
    url: '/achieve/patent/confirm/' + praseStrEmpty(patentid) + '/' + status,
    method: 'get'
  })
}



