import request from '@/utils/request'
import {praseStrEmpty} from "@/utils/ruoyi";
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询列表
export function listSku(query) {
  return request({
    url: '/sku/sku/list',
    method: 'get',
    params: query
  })
}

export function getSku(skuId) {
  return request({
    url: '/sku/sku/' + skuId,
    method: 'get'
  })
}


// 新增sku
export function addSku(data) {
  return request({
    url: '/sku/sku',
    method: 'post',
    data: data
  })
}

// 修改sku
export function updateSku(data) {
  return request({
    url: '/sku/sku',
    method: 'put',
    data: data
  })
}

// 删除sku
export function deleteSku(ids) {
  return request({
    url: '/sku/sku/' + ids,
    method: 'delete'
  })
}

export function uniqueSku(query) {
  return request({
    url: '/sku/sku/unique',
    method: 'get',
    params: query
  })
}

export function exportSku(ids) {
  return request({
    url: '/sku/export/' + ids,
    responseType: 'blob',
    method: 'get'
  })
}

// 查询列表
export function listSkuPhoto(query) {
  return request({
    url: '/sku/photo/list',
    method: 'get',
    params: query
  })
}


// 新增skuPhoto
export function addSkuPhoto(data) {
  return request({
    url: '/sku/photo',
    method: 'post',
    data: data
  })
}

// 修改sku
export function updateSkuPhoto(data) {
  return request({
    url: '/sku/photo',
    method: 'put',
    data: data
  })
}

// 删除sku
export function deleteSkuPhoto(ids) {
  return request({
    url: '/sku/photo/' + ids,
    method: 'delete'
  })
}


