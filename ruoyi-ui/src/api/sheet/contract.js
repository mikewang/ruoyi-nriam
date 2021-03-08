import request from "@/utils/request";
import {downloadFile} from "@/api/project/project";
import {praseStrEmpty} from "@/utils/ruoyi";

export function listTijiaorenContract(query) {
  return request({
    url: '/contract/tijiaoren/list',
    method: 'get',
    params: query
  })
}

export function listXiangmuContract(query) {
  return request({
    url: '/contract/xiangmu/list',
    method: 'get',
    params: query
  })
}

// 查询合同，之 提交人。
export function getContract(contractid) {
  return request({
    url: '/contract/' + praseStrEmpty(contractid),
    method: 'get'
  })
}


// 合同，之 提交人。
export function addContract(data) {
  return request({
    url: '/contract/tijiaoren',
    method: 'post',
    data: data
  })
}

export function updateContract(data) {
  return request({
    url: '/contract/tijiaoren',
    method: 'put',
    data: data
  })
}



// 文件下载
export function downloadTemplateDoc(query) {
  return request({
    url: '/contract/template/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}


// 文件上传
export function uploadFile(data) {
  return request({
    headers: {'Content-Type':'multipart/form-data'},
    url: '/contract/doc/upload',
    method: 'post',
    data: data
  })
}


// 查询项目列表
export function listContractdoc(query) {
  return request({
    url: '/contract/doc/list',
    method: 'get',
    params: query
  })
}

export function confirmContract(data) {
  return request({
    url: '/contract/confirm',
    method: 'put',
    data: data
  })
}

export function deleteContract(contractid) {
  return request({
    url: '/contract/' + praseStrEmpty(contractid),
    method: 'delete'
  })
}

// 查询合同，之 提交人。
export function getContractPaysheet(contractid) {
  return request({
    url: '/contract/paysheet/' + praseStrEmpty(contractid),
    method: 'get'
  })
}
