import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询农机新产品列表
export function listProduct(query) {
  return request({
    url: '/achieve/product/list',
    method: 'get',
    params: query
  })
}


// 查询农机新产品详细
export function getProduct(productid) {
  return request({
    url: '/achieve/product/' + praseStrEmpty(productid),
    method: 'get'
  })
}



// 新增农机新产品
export function addProduct(data) {
  return request({
    url: '/achieve/product',
    method: 'post',
    data: data
  })
}

// 修改农机新产品
export function updateProduct(data) {
  return request({
    url: '/achieve/product',
    method: 'put',
    data: data
  })
}

// 删除农机新产品
export function deleteProduct(productids) {
  return request({
    url: '/achieve/product/' + productids,
    method: 'delete'
  })
}
// 审核农机新产品
export function confirmProduct(data) {
  return request({
    url: '/achieve/product/confirm',
    method: 'put',
    data: data
  })
}

// 查询农机新产品 审核详细
export function getProductConfirm(productid,status) {
  return request({
    url: '/achieve/product/confirm/' + praseStrEmpty(productid) + '/' + status,
    method: 'get'
  })
}



