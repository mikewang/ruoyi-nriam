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


export function listViewTeam(query) {
  return request({
    url: '/performance/viewteam/list',
    method: 'get',
    params: query
  })
}


export function confirmVerifyTeamRequest(data) {
  return request({
    url:  '/performance/viewteam/confirmrequest',
    method: 'put',
    data: data
  })
}

export function listIndicatorTree() {
  return request({
    url: '/performance/indicatortable',
    method: 'get'
  })
}




export function listAddscoreapply(query) {
  return request({
    url: '/performance/addscoreapply/list',
    method: 'get',
    params: query
  })
}


// 新建
export function addAddscoreapply(data) {
  return request({
    url:  '/performance/addscoreapply',
    method: 'post',
    data: data
  })
}

export function updateAddscoreapply(data) {
  return request({
    url:  '/performance/addscoreapply',
    method: 'put',
    data: data
  })
}

export function deleteAddscoreapply(applyid) {
  return request({
    url: '/performance/addscoreapply/' + applyid,
    method: 'delete'
  })
}


export function listApplylisttoconfirm(query) {
  return request({
    url: '/performance/applylisttoconfirm/list',
    method: 'get',
    params: query
  })
}

export function confirmAddscoreapply(data) {
  return request({
    url:  '/performance/applylisttoconfirm',
    method: 'put',
    data: data
  })
}



export function listFundreport(query) {
  return request({
    url: '/performance/perfundreport/list',
    method: 'get',
    params: query
  })
}

export function addFundreport(data) {
  return request({
    url:  '/performance/perfundreport',
    method: 'post',
    data: data
  })
}

export function updateFundreport(data) {
  return request({
    url:  '/performance/perfundreport',
    method: 'put',
    data: data
  })
}


export function listFundconfirm(query) {
  return request({
    url: '/performance/perfundconfirm/list',
    method: 'get',
    params: query
  })
}

export function confirmFundreport(ids) {
  return request({
    url:  '/performance/perfundconfirm/' + ids,
    method: 'put'
  })
}
