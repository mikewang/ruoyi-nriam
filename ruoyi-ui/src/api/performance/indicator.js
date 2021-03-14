import request from "@/utils/request";
import {praseStrEmpty} from "@/utils/ruoyi";

export function listIndicatorProject(query) {
  return request({
    url: '/performance/indicatorProject/list',
    method: 'get',
    params: query
  })
}


export function listIndicator(query) {
  return request({
    url: '/performance/indicator/list',
    method: 'get',
    params: query
  })
}


export function listIndicatorRelation(query) {
  return request({
    url: '/performance/indicatorRelation/list',
    method: 'get',
    params: query
  })
}

// 修改
export function updateIndicatorRelation(data) {
  return request({
    url: '/performance/indicatorRelation',
    method: 'put',
    data: data
  })
}

export function addIndicatorProject(data) {
  return request({
    url: '/performance/indicatorProject',
    method: 'post',
    data: data
  })
}


// 修改
export function updateIndicatorProject(data) {
  return request({
    url: '/performance/indicatorProject',
    method: 'put',
    data: data
  })
}

export function deleteIndicatorProject(projectIds) {
  return request({
    url: '/performance/indicatorProject/' + projectIds,
    method: 'delete'
  })
}


// 查询经费详细
export function getIndicatorFund(fundId) {
  return request({
    url: '/performance/indicatorFund/' + praseStrEmpty(fundId),
    method: 'get'
  })
}

// 修改
export function updateIndicatorFund(data) {
  return request({
    url: '/performance/indicatorFund',
    method: 'put',
    data: data
  })
}


// 获奖组件

export function listIndicatorPrize(query) {
  return request({
    url: '/performance/indicatorPrize/list',
    method: 'get',
    params: query
  })
}


export function addIndicatorPrize(data) {
  return request({
    url: '/performance/indicatorPrize',
    method: 'post',
    data: data
  })
}


// 修改
export function updateIndicatorPrize(data) {
  return request({
    url: '/performance/indicatorPrize',
    method: 'put',
    data: data
  })
}

export function deleteIndicatorPrize(prizeIds) {
  return request({
    url: '/performance/indicatorPrize/' + prizeIds,
    method: 'delete'
  })
}


// 专利

export function listIndicatorPatent(query) {
  return request({
    url: '/performance/indicatorPatent/list',
    method: 'get',
    params: query
  })
}

// 修改
export function updateIndicatorPatent(data) {
  return request({
    url: '/performance/indicatorPatent',
    method: 'put',
    data: data
  })
}
