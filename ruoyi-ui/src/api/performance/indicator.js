import request from "@/utils/request";
import {praseStrEmpty} from "@/utils/ruoyi";

export function listIndicatorProject(query) {
  return request({
    url: '/performance/indicatorproject/list',
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
    url: '/performance/indicatorproject',
    method: 'post',
    data: data
  })
}


// 修改
export function updateIndicatorProject(data) {
  return request({
    url: '/performance/indicatorproject',
    method: 'put',
    data: data
  })
}

export function deleteIndicatorProject(projectIds) {
  return request({
    url: '/performance/indicatorproject/' + projectIds,
    method: 'delete'
  })
}


// 查询经费详细
export function getIndicatorFund(fundId) {
  return request({
    url: '/performance/indicatorfund/' + praseStrEmpty(fundId),
    method: 'get'
  })
}

// 修改
export function updateIndicatorFund(data) {
  return request({
    url: '/performance/indicatorfund',
    method: 'put',
    data: data
  })
}
