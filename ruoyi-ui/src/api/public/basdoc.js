// 查询 文件列表
import request from "@/utils/request";

export function listBasDoc(query) {
  return request({
    url: '/bas/doc/list',
    method: 'get',
    params: query
  })
}

// 文件下载
export function downloadBasDoc(query) {
  return request({
    url: '/bas/doc/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}


// 文件上传
export function uploadBasDoc(type, data) {
  return request({
    headers: {'Content-Type':'multipart/form-data'},
    url: '/bas/doc/upload/' + type,
    method: 'post',
    data: data
  })
}
