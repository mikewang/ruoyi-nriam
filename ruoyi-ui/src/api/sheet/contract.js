import request from "@/utils/request";

export function listTijiaorenContract(query) {
  return request({
    url: '/contract/tijiaoren/list',
    method: 'get',
    params: query
  })
}
// 合同，之 提交人。
export function addContract(data) {
  return request({
    url: '/contract/tijiaoren',
    method: 'post',
    data: data
  })
}

export function updateContract(data) {
  return request({
    url: '/contract/tijiaoren',
    method: 'put',
    data: data
  })
}
