import request from "@/utils/request";
import {praseStrEmpty} from "@/utils/ruoyi";

export function listTijiaorenFourtech(query) {
  return request({
    url: '/fourtech/tijiaoren/list',
    method: 'get',
    params: query
  })
}

// 四技合同，之 提交。
export function addFourtech(data) {
  return request({
    url: '/fourtech/tijiaoren',
    method: 'post',
    data: data
  })
}


// 修改
export function updateFourtech(data) {
  return request({
    url: '/fourtech/tijiaoren',
    method: 'put',
    data: data
  })
}

// 查询四技合同 合同，之 提交人。
export function getFourtech(fourtechid) {
  return request({
    url: '/fourtech/' + praseStrEmpty(fourtechid),
    method: 'get'
  })
}



// 文件下载
export function downloadTemplateDoc(query) {
  return request({
    url: '/fourtech/template/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}


// 文件上传
export function uploadFile(data) {
  return request({
    headers: {'Content-Type':'multipart/form-data'},
    url: '/fourtech/doc/upload',
    method: 'post',
    data: data
  })
}


// 查询合同文件列表
export function listContractdoc(query) {
  return request({
    url: '/fourtech/doc/list',
    method: 'get',
    params: query
  })
}
