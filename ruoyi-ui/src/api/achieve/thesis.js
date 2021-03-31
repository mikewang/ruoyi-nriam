import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询论文列表
export function listThesis(query) {
  return request({
    url: '/achieve/thesis/list',
    method: 'get',
    params: query
  })
}


// 查询论文详细
export function getThesis(thesisid) {
  return request({
    url: '/achieve/thesis/' + praseStrEmpty(thesisid),
    method: 'get'
  })
}

// 校验论文名称是否重复
export function uniqueThesis(query) {
  return request({
    url: '/achieve/thesis/unique',
    method: 'get',
    params: query
  })
}


// 新增论文
export function addThesis(data) {
  return request({
    url: '/achieve/thesis',
    method: 'post',
    data: data
  })
}

// 修改论文
export function updateThesis(data) {
  return request({
    url: '/achieve/thesis',
    method: 'put',
    data: data
  })
}

// 删除论文
export function deleteThesis(thesisids) {
  return request({
    url: '/achieve/thesis/' + thesisids,
    method: 'delete'
  })
}
// 审核论文
export function confirmThesis(data) {
  return request({
    url: '/achieve/thesis/confirm',
    method: 'put',
    data: data
  })
}

// 查询论文 审核详细
export function getThesisConfirm(thesisid,status) {
  return request({
    url: '/achieve/thesis/confirm/' + praseStrEmpty(thesisid) + '/' + status,
    method: 'get'
  })
}



