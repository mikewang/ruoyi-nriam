import request from '@/utils/request'
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询项目
export function listProject(query) {
  return request({
    url: '/project/zaiyan/list',
    method: 'get',
    params: query
  })
}


// 新增项目
export function addProject(data) {
  return request({
    url: '/project/zaiyan',
    method: 'post',
    data: data
  })
}

// 修改项目
export function updateProject(data) {
  return request({
    url: '/project/zaiyan',
    method: 'put',
    data: data
  })
}

// 删除项目
export function deleteProject(projectIds) {
  return request({
    url: '/project/zaiyan/' + projectIds,
    method: 'delete'
  })
}