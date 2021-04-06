import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询标准列表
export function listStandard(query) {
  return request({
    url: '/achieve/standard/list',
    method: 'get',
    params: query
  })
}


// 查询标准详细
export function getStandard(standardid) {
  return request({
    url: '/achieve/standard/' + praseStrEmpty(standardid),
    method: 'get'
  })
}



// 新增标准
export function addStandard(data) {
  return request({
    url: '/achieve/standard',
    method: 'post',
    data: data
  })
}

// 修改标准
export function updateStandard(data) {
  return request({
    url: '/achieve/standard',
    method: 'put',
    data: data
  })
}

// 删除标准
export function deleteStandard(standardids) {
  return request({
    url: '/achieve/standard/' + standardids,
    method: 'delete'
  })
}
// 审核标准
export function confirmStandard(data) {
  return request({
    url: '/achieve/standard/confirm',
    method: 'put',
    data: data
  })
}

// 查询标准 审核详细
export function getStandardConfirm(standardid,status) {
  return request({
    url: '/achieve/standard/confirm/' + praseStrEmpty(standardid) + '/' + status,
    method: 'get'
  })
}



