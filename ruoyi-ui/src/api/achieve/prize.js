import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询获奖列表
export function listPrize(query) {
  return request({
    url: '/achieve/prize/list',
    method: 'get',
    params: query
  })
}


// 查询获奖详细
export function getPrize(prizeid) {
  return request({
    url: '/achieve/prize/' + praseStrEmpty(prizeid),
    method: 'get'
  })
}



// 新增获奖
export function addPrize(data) {
  return request({
    url: '/achieve/prize',
    method: 'post',
    data: data
  })
}

// 修改获奖
export function updatePrize(data) {
  return request({
    url: '/achieve/prize',
    method: 'put',
    data: data
  })
}

// 删除获奖
export function deletePrize(prizeids) {
  return request({
    url: '/achieve/prize/' + prizeids,
    method: 'delete'
  })
}
// 审核获奖
export function confirmPrize(data) {
  return request({
    url: '/achieve/prize/confirm',
    method: 'put',
    data: data
  })
}

// 查询获奖 审核详细
export function getPrizeConfirm(prizeid,status) {
  return request({
    url: '/achieve/prize/confirm/' + praseStrEmpty(prizeid) + '/' + status,
    method: 'get'
  })
}



