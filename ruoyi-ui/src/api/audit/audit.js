// 查询专利列表
import request from "@/utils/request";
import {praseStrEmpty} from "@/utils/ruoyi";

export function listAuditTijiaoren(query) {
  return request({
    url: '/audit/tijiaoren/list',
    method: 'get',
    params: query
  })
}

// 查询外拨单，之 提交人。
export function getSheet(sheetid) {
  return request({
    url: '/audit/' + praseStrEmpty(sheetid),
    method: 'get'
  })
}

// 查询外拨单的协作单位信息
export function getSheetBudgetpay(sheetid) {
  return request({
    url: '/audit/budgetpay/' + praseStrEmpty(sheetid),
    method: 'get'
  })
}

// 查询协作单位列表
export function getSheetSupplierById(supplierid) {
  return request({
    url: '/audit/supplier/'  + praseStrEmpty(supplierid),
    method: 'get'
  })
}

// 删除协作单位
export function deleteSheetSupplier(supplierids) {
  return request({
    url: '/audit/supplier/'  + praseStrEmpty(supplierids),
    method: 'delete'
  })
}


export function getSheetSupplier(query) {
  return request({
    url: '/audit/supplier/filter',
    method: 'get',
    params: query
  })
}


// 文件上传
export function uploadFile(data) {
  return request({
    headers: {'Content-Type':'multipart/form-data'},
    url: '/audit/supplier/upload',
    method: 'post',
    data: data
  })
}

export function listSheetSupplier(query) {
  return request({
    url: '/audit/supplier/list',
    method: 'get',
    params: query
  })
}


// 查询外拨单的协作单位的历史记录
export function getSheetBudgetpayRecord(query) {
  return request({
    url: '/audit/budgetpay/record',
    method: 'get',
    params: query
  })
}


// 查询外拨单的审批记录
export function getSheetAuditRecord(query) {
  return request({
    url: '/audit/audit/record',
    method: 'get',
    params: query
  })
}

// 拨付单，之 提交人。
export function addSheet(data) {
  return request({
    url: '/audit/tijiaoren',
    method: 'post',
    data: data
  })
}

// 删除外拨单，之 提交人。
export function deleteSheet(sheetid) {
  return request({
    url: '/audit/' + praseStrEmpty(sheetid),
    method: 'delete'
  })
}

// 审核
export function confirmSheet(data) {
  return request({
    url: '/audit/confirm',
    method: 'put',
    data: data
  })
}

// 修改
export function updateSheet(data) {
  return request({
    url: '/audit/change',
    method: 'put',
    data: data
  })
}


export function confirmAuditSheet(data, opcode) {
  return request({
    url: '/audit/' + opcode,
    method: 'put',
    data: data
  })
}


//  查询项目负责人审批的拨付单列表
export function listAudit3Sheet(query) {
  return request({
    url: '/audit/audit3/list',
    method: 'get',
    params: query
  })
}

//  查询部门审批的拨付单列表
export function listAudit4Sheet(query) {
  return request({
    url: '/audit/audit4/list',
    method: 'get',
    params: query
  })
}

//  查询分管处的审批的拨付单列表
export function listAudit5Sheet(query) {
  return request({
    url: '/audit/audit5/list',
    method: 'get',
    params: query
  })
}

//  查询分管所长审批的拨付单列表
export function listAudit6Sheet(query) {
  return request({
    url: '/audit/audit6/list',
    method: 'get',
    params: query
  })
}


//  查询所长审批的拨付单列表
export function listAudit7Sheet(query) {
  return request({
    url: '/audit/audit7/list',
    method: 'get',
    params: query
  })
}

export function addSheetSupplier(data) {
  return request({
    url: '/audit/supplier',
    method: 'post',
    data: data
  })
}

export function editSheetSupplier(data) {
  return request({
    url: '/audit/supplier',
    method: 'put',
    data: data
  })
}

//  查询我的审批记录 列表
export function listAuditMyrecord(query) {
  return request({
    url: '/audit/myrecord/list',
    method: 'get',
    params: query
  })
}
