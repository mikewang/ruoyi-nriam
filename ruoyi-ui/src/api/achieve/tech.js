import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询农机新产品列表
export function listTech(query) {
  return request({
    url: '/achieve/tech/list',
    method: 'get',
    params: query
  })
}


// 查询农机新产品详细
export function getTech(techid) {
  return request({
    url: '/achieve/tech/' + praseStrEmpty(techid),
    method: 'get'
  })
}



// 新增农机新产品
export function addTech(data) {
  return request({
    url: '/achieve/tech',
    method: 'post',
    data: data
  })
}

// 修改农机新产品
export function updateTech(data) {
  return request({
    url: '/achieve/tech',
    method: 'put',
    data: data
  })
}

// 删除农机新产品
export function deleteTech(techids) {
  return request({
    url: '/achieve/tech/' + techids,
    method: 'delete'
  })
}
// 审核农机新产品
export function confirmTech(data) {
  return request({
    url: '/achieve/tech/confirm',
    method: 'put',
    data: data
  })
}

// 查询农机新产品 审核详细
export function getTechConfirm(techid,status) {
  return request({
    url: '/achieve/tech/confirm/' + praseStrEmpty(techid) + '/' + status,
    method: 'get'
  })
}



