// 查询专利列表
import request from "@/utils/request";

export function listToconfirm(query) {
  return request({
    url: '/achieve/toconfirm/list',
    method: 'get',
    params: query
  })
}

export function listAchievetype(query) {
  return request({
    url: '/achieve/toconfirm/achievetype/list',
    method: 'get',
    params: query
  })
}
