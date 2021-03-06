import request from '@/utils/request'
/*导入的是 export default 组件，名称为service, 它在一个request.js文件中必须唯一。 */
// import { praseStrEmpty } from "@/utils/ruoyi";

// 查询代办事宜的消息
export function listMessage(query) {
  return request({
    url: '/audit/message/list',
    method: 'get',
    params: query
  })
}


// 设置已读代办事宜的消息
export function readMessage(ids) {
  return request({
    url: '/audit/message/'+ ids,
    method: 'delete'
  })
}

