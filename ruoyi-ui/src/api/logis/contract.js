import request from '@/utils/request'
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询合同
export function listContract(query) {
  return request({
    url: '/logis/contract/list',
    method: 'get',
    params: query
  })
}

// 查询合同详细
export function getContract(contractId) {
  return request({
    url: '/logis/contract/' + contractId,
    method: 'get'
  })
}

// 新增合同
export function addContract(data) {
  return request({
    url: '/logis/contract',
    method: 'post',
    data: data
  })
}

// 修改合同配置
export function updateContract(data) {
  return request({
    url: '/logis/contract',
    method: 'put',
    data: data
  })
}

// 删除合同配置
export function delContract(contractId) {
  return request({
    url: '/logis/contract/' + contractId,
    method: 'delete'
  })
}

// 清理合同缓存
export function clearCache() {
  return request({
    url: '/logis/contract/clearCache',
    method: 'delete'
  })
}

// 导出合同
export function exportContract(query) {
  return request({
    url: '/logis/contract/export',
    method: 'get',
    params: query
  })
}

// 文件上传
export function uploadContractFile(data) {
  return request({
    headers: {'Content-Type':'multipart/form-data'},
    url: '/logis/contract/upload',
    method: 'post',
    data: data
  })
}

// 文件下载
export function downloadContractFile(query) {
  return request({
    url: '/logis/contract/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
