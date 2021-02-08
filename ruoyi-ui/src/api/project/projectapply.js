import request from "@/utils/request";

// 查询项目列表
export function listProjectApply(query) {
  return request({
    url: '/project/apply/tijiaoren/list',
    method: 'get',
    params: query
  })
}
