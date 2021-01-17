import request from '@/utils/request'
import { praseStrEmpty } from "@/utils/ruoyi";

// 查询用户签名图片列表
export function listSignPic(query) {
  return request({
    url: '/system/signpic/list',
    method: 'get',
    params: query
  })
}
