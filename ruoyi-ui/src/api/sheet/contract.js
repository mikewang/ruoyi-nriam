import request from "@/utils/request";
import {downloadFile} from "@/api/project/project";

export function listTijiaorenContract(query) {
  return request({
    url: '/contract/tijiaoren/list',
    method: 'get',
    params: query
  })
}
// 合同，之 提交人。
export function addContract(data) {
  return request({
    url: '/contract/tijiaoren',
    method: 'post',
    data: data
  })
}

export function updateContract(data) {
  return request({
    url: '/contract/tijiaoren',
    method: 'put',
    data: data
  })
}


// 文件下载
export function downloadTemplateDoc(query) {
  return request({
    url: '/contract/template/download',
    method: 'get',
    responseType: 'blob',
    params: query
  })
}
