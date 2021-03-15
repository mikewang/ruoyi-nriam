import request from "@/utils/request";

export function listVerifyTeam(query) {
  return request({
    url: '/performance/verifyteam/list',
    method: 'get',
    params: query
  })
}
