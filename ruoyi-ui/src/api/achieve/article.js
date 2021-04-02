import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询著作列表
export function listArticle(query) {
  return request({
    url: '/achieve/article/list',
    method: 'get',
    params: query
  })
}


// 查询著作详细
export function getArticle(articleid) {
  return request({
    url: '/achieve/article/' + praseStrEmpty(articleid),
    method: 'get'
  })
}

// 校验著作号是否重复
export function uniqueArticle(query) {
  return request({
    url: '/achieve/article/unique',
    method: 'get',
    params: query
  })
}


// 新增著作
export function addArticle(data) {
  return request({
    url: '/achieve/article',
    method: 'post',
    data: data
  })
}

// 修改著作
export function updateArticle(data) {
  return request({
    url: '/achieve/article',
    method: 'put',
    data: data
  })
}

// 删除著作
export function deleteArticle(articleids) {
  return request({
    url: '/achieve/article/' + articleids,
    method: 'delete'
  })
}
// 审核著作
export function confirmArticle(data) {
  return request({
    url: '/achieve/article/confirm',
    method: 'put',
    data: data
  })
}

// 查询著作 审核详细
export function getArticleConfirm(articleid,status) {
  return request({
    url: '/achieve/article/confirm/' + praseStrEmpty(articleid) + '/' + status,
    method: 'get'
  })
}



