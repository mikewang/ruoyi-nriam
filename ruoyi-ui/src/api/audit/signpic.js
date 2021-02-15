import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询用户签名图片列表
export function listSignPic(query) {
  return request({
    url: '/audit/signpic/list',
    method: 'get',
    params: query
  })
}

// 文件上传
export function uploadSignPicFile(data) {
  return request({
    headers: {'Content-Type':'multipart/form-data'},
    url: '/audit/signpic/upload',
    method: 'post',
    data: data
  })
}


// 删除签名图片
export function delSignpic(userIds) {
  return request({
    url: '/audit/signpic/' + userIds,
    method: 'delete'
  })
}

// 获取签名图片
export function getSignpic(userid) {
  return request({
    url: '/audit/signpic/' + praseStrEmpty(userid) ,
    method: 'get'
  })
}
