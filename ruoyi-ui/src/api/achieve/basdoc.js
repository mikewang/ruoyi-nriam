// 查询专利列表
import request from "@/utils/request";

export function listBasDoc(query) {
  return request({
    url: '/bas/doc/list',
    method: 'get',
    params: query
  })
}
