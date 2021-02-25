import request from "@/utils/request";

export function listTijiaorenFourtech(query) {
  return request({
    url: '/fourtech/tijiaoren/list',
    method: 'get',
    params: query
  })
}
