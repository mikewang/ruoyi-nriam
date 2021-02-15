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


// 查询外拨单的协作单位信息
export function getSheetBudgetpayRecord(query) {
  return request({
    url: '/sheet/budgetpay/record',
    method: 'get',
    params: query
  })
}
