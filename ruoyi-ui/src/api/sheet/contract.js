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


export function confirmAuditContract(data, opcode) {
  return request({
    url: '/contract/' + opcode,
    method: 'put',
    data: data
  })
}


export function listAudit3Contract(query) {
  return request({
    url: '/contract/audit3/list',
    method: 'get',
    params: query
  })
}


export function listAudit4Contract(query) {
  return request({
    url: '/contract/audit4/list',
    method: 'get',
    params: query
  })
}


export function listAudit5Contract(query) {
  return request({
    url: '/contract/audit5/list',
    method: 'get',
    params: query
  })
}


export function listAudit6Contract(query) {
  return request({
    url: '/contract/audit6/list',
    method: 'get',
    params: query
  })
}


export function listAudit7Contract(query) {
  return request({
    url: '/contract/audit7/list',
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

export function submitContract(data) {
  return request({
    url: '/contract/submit',
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

// 查询合同拨付单列表。
export function listContractPaysheet(contractid) {
  return request({
    url: '/contract/paysheetlist/' + praseStrEmpty(contractid),
    method: 'get'
  })
}


// 查询合同拨付单一个。
export function getContractPaysheet(sheetid) {
  return request({
    url: '/contract/paysheet/' + praseStrEmpty(sheetid),
    method: 'get'
  })
}

export function getContractConfirmNote(audittype) {

  if (audittype === "audit3") {
    return "根据项目实施方案和经费预算安排，特委托乙方完成本合同任务。乙方具备承担合同任务的资质和能力，协议经费量与工作量匹配，协议内容真实。特申请按本合同办理。";
  }
  else   if (audittype === "audit4") {
    return "合同文本已阅，内容真实，合同委托乙方的任务，符合本项目实施方案和经费预算要求，乙方资质能力符合相关要求。同意按本合同办理。";
  }
  if (audittype === "audit5") {
    return "合同文本已阅，合同内容符合项目实施方案和经费预算要求，乙方资质能力符合相关要求。同意按本合同办理。";
  }
  if (audittype === "audit6") {
    return "同意按本合同办理。";
  }
  if (audittype === "audit7") {
    return "同意按本合同办理。";
  }

}
