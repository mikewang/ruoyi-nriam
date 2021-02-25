import request from "@/utils/request";

export function listTijiaorenContract(query) {
  return request({
    url: '/contract/tijiaoren/list',
    method: 'get',
    params: query
  })
}
