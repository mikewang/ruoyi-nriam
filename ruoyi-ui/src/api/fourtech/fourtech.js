import request from "@/utils/request";
import {praseStrEmpty} from "@/utils/ruoyi";

export function listTijiaorenFourtech(query) {
  return request({
    url: '/fourtech/tijiaoren/list',
    method: 'get',
    params: query
  })
}

// 四技合同，之 提交。
export function addFourtech(data) {
  return request({
    url: '/fourtech/tijiaoren',
    method: 'post',
    data: data
  })
}


// 修改
export function updateFourtech(data) {
  return request({
    url: '/fourtech/tijiaoren',
    method: 'put',
    data: data
  })
}

// 查询四技合同 合同，之 提交人。
export function getFourtech(fourtechid) {
  return request({
    url: '/fourtech/' + praseStrEmpty(fourtechid),
    method: 'get'
  })
}



// 文件下载
export function downloadTemplateDoc(query) {
  return request({
    url: '/fourtech/template/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}


// 文件上传
export function uploadFile(data) {
  return request({
    headers: {'Content-Type':'multipart/form-data'},
    url: '/fourtech/doc/upload',
    method: 'post',
    data: data
  })
}


// 查询合同文件列表
export function listContractdoc(fourtechid) {
  return request({
    url: '/fourtech/doc/list/' + praseStrEmpty(fourtechid),
    method: 'get'
  })
}

export function submitFourtech(data) {
  return request({
    url: '/fourtech/submit',
    method: 'put',
    data: data
  })
}

export function confirmAuditFourtech(data, opcode) {
  return request({
    url: '/fourtech/' + opcode,
    method: 'put',
    data: data
  })
}

export function listAudit2Fourtech(query) {
  return request({
    url: '/fourtech/audit2/list',
    method: 'get',
    params: query
  })
}


export function listAudit3Fourtech(query) {
  return request({
    url: '/fourtech/audit3/list',
    method: 'get',
    params: query
  })
}


export function listAudit4Fourtech(query) {
  return request({
    url: '/fourtech/audit4/list',
    method: 'get',
    params: query
  })
}


export function listAudit5Fourtech(query) {
  return request({
    url: '/fourtech/audit5/list',
    method: 'get',
    params: query
  })
}


export function listAudit7Fourtech(query) {
  return request({
    url: '/fourtech/audit7/list',
    method: 'get',
    params: query
  })
}

export function listMyAuditFourtech(query) {
  return request({
    url: '/fourtech/myaudit/list',
    method: 'get',
    params: query
  })
}

export function listFourtech_ShenPiWanCheng(query) {
  return request({
    url: '/fourtech/ShenPiWanCheng/list',
    method: 'get',
    params: query
  })
}

export function updaeCodefourtech(data) {
  return request({
    url: '/fourtech/codefourtech/',
    method: 'put',
    data: data
  })
}


export function queryFourtech(query) {
  return request({
    url: '/fourtech/query/list',
    method: 'get',
    params: query
  })
}
