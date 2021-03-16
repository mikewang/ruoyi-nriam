import request from "@/utils/request";

export function listVerifyTeam(query) {
  return request({
    url: '/performance/verifyteam/list',
    method: 'get',
    params: query
  })
}

export function exportVerifyTeam(query) {
  return request({
    url: '/performance/verifyteam/export',
    method: 'get',
    params: query
  })
}

export function getVerifyTeamPointsSum(query) {
  return request({
    url: '/performance/verifyteam/pointssum',
    method: 'get',
    params: query
  })
}



export function getVerifyTeamConfirmrequest(query) {
  return request({
    url: '/performance/verifyteam/confirmrequest',
    method: 'get',
    params: query
  })
}


// 新建
export function addVerifyTeamConfirmrequest(data) {
  return request({
    url:  '/performance/verifyteam/confirmrequest',
    method: 'post',
    data: data
  })
}

export function updateVerifypoints(data) {
  return request({
    url:  '/performance/verifyteam/verifypoints',
    method: 'put',
    data: data
  })
}


export function getVerifypoints(query) {
  return request({
    url: '/performance/verifyteam/verifypoints',
    method: 'get',
    params: query
  })
}


export function deleteVerifyTeam(performanceid) {
  return request({
    url: '/performance/verifyteam/' + performanceid,
    method: 'delete'
  })
}
