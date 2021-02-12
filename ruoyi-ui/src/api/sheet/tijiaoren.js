// 查询专利列表
import request from "@/utils/request";

export function listTijiaoren(query) {
  return request({
    url: '/sheet/tijiaoren/list',
    method: 'get',
    params: query
  })
}
