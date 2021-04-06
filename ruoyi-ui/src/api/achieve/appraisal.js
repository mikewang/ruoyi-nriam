import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询鉴定（评价）成果列表
export function listAppraisal(query) {
  return request({
    url: '/achieve/appraisal/list',
    method: 'get',
    params: query
  })
}


// 查询鉴定（评价）成果详细
export function getAppraisal(appraisalid) {
  return request({
    url: '/achieve/appraisal/' + praseStrEmpty(appraisalid),
    method: 'get'
  })
}



// 新增鉴定（评价）成果
export function addAppraisal(data) {
  return request({
    url: '/achieve/appraisal',
    method: 'post',
    data: data
  })
}

// 修改鉴定（评价）成果
export function updateAppraisal(data) {
  return request({
    url: '/achieve/appraisal',
    method: 'put',
    data: data
  })
}

// 删除鉴定（评价）成果
export function deleteAppraisal(appraisalids) {
  return request({
    url: '/achieve/appraisal/' + appraisalids,
    method: 'delete'
  })
}
// 审核鉴定（评价）成果
export function confirmAppraisal(data) {
  return request({
    url: '/achieve/appraisal/confirm',
    method: 'put',
    data: data
  })
}

// 查询鉴定（评价）成果 审核详细
export function getAppraisalConfirm(appraisalid,status) {
  return request({
    url: '/achieve/appraisal/confirm/' + praseStrEmpty(appraisalid) + '/' + status,
    method: 'get'
  })
}



