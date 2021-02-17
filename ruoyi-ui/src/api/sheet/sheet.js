// 查询专利列表
import request from "@/utils/request";
import {praseStrEmpty} from "@/utils/ruoyi";

export function listTijiaorenSheet(query) {
  return request({
    url: '/sheet/tijiaoren/list',
    method: 'get',
    params: query
  })
}

// 查询外拨单，之 提交人。
export function getSheet(sheetid) {
  return request({
    url: '/sheet/' + praseStrEmpty(sheetid),
    method: 'get'
  })
}

// 查询外拨单的协作单位信息
export function getSheetBudgetpay(sheetid) {
  return request({
    url: '/sheet/budgetpay/' + praseStrEmpty(sheetid),
    method: 'get'
  })
}

// 查询协作单位列表
export function listSheetSupplier(query) {
  return request({
    url: '/sheet/supplier/list',
    method: 'get',
    params: query
  })
}


// 查询外拨单的协作单位的历史记录
export function getSheetBudgetpayRecord(query) {
  return request({
    url: '/sheet/budgetpay/record',
    method: 'get',
    params: query
  })
}


// 查询外拨单的审批记录
export function getSheetAuditRecord(query) {
  return request({
    url: '/sheet/audit/record',
    method: 'get',
    params: query
  })
}

// 拨付单，之 提交人。
export function addSheet(data) {
  return request({
    url: '/sheet/tijiaoren',
    method: 'post',
    data: data
  })
}

// 删除外拨单，之 提交人。
export function deleteSheet(sheetid) {
  return request({
    url: '/sheet/' + praseStrEmpty(sheetid),
    method: 'delete'
  })
}

// 审核
export function confirmSheet(data) {
  return request({
    url: '/sheet/confirm',
    method: 'put',
    data: data
  })
}

// 修改
export function updateSheet(data) {
  return request({
    url: '/sheet/confirm',
    method: 'put',
    data: data
  })
}

