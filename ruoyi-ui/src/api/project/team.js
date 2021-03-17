import request from '@/utils/request'
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询团队
export function listTeam(query) {
  return request({
    url: '/project/team/list',
    method: 'get',
    params: query
  })
}


// 新增团队
export function addTeam(data) {
  return request({
    url: '/project/team',
    method: 'post',
    data: data
  })
}

// 修改团队
export function updateTeam(data) {
  return request({
    url: '/project/team',
    method: 'put',
    data: data
  })
}

// 删除团队
export function deleteTeam(teamIds) {
  return request({
    url: '/project/team/' + teamIds,
    method: 'delete'
  })
}


// 查询团队成员
export function listTeamMember(query) {
  return request({
    url: '/project/team/member/list',
    method: 'get',
    params: query
  })
}

export function getJoinTeamlist(userid) {
  return request({
    url: '/project/team/joinTeamlist/' + userid,
    method: 'get'
  })
}

