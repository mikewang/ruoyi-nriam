import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询软件著作权列表
export function listSoftware(query) {
  return request({
    url: '/achieve/software/list',
    method: 'get',
    params: query
  })
}


// 查询软件著作权详细
export function getSoftware(softwareid) {
  return request({
    url: '/achieve/software/' + praseStrEmpty(softwareid),
    method: 'get'
  })
}



// 新增软件著作权
export function addSoftware(data) {
  return request({
    url: '/achieve/software',
    method: 'post',
    data: data
  })
}

// 修改软件著作权
export function updateSoftware(data) {
  return request({
    url: '/achieve/software',
    method: 'put',
    data: data
  })
}

// 删除软件著作权
export function deleteSoftware(softwareids) {
  return request({
    url: '/achieve/software/' + softwareids,
    method: 'delete'
  })
}
// 审核软件著作权
export function confirmSoftware(data) {
  return request({
    url: '/achieve/software/confirm',
    method: 'put',
    data: data
  })
}

// 查询软件著作权 审核详细
export function getSoftwareConfirm(softwareid,status) {
  return request({
    url: '/achieve/software/confirm/' + praseStrEmpty(softwareid) + '/' + status,
    method: 'get'
  })
}



