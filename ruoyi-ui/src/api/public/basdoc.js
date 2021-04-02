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


export function changeDocList(filelist, doctype, docList) {

  docList.forEach(function(item, index, arr) {
    console.log("清理前 index=" + index.toString(),"doctype is " , item.doctype, "docid", item.docid);
  });

  while (docList.filter(function (item) {
    return item.doctype === doctype
  }).length > 0) {
    docList.forEach(function(item, index, arr) {
      if(item.doctype == doctype) {
        arr.splice(index, 1);
      }
    });
  }

  if (docList.filter(function (item) {return item.doctype === doctype}).length >0 ) {
    docList.forEach(function(item, index, arr) {
      console.log("清理后 index=" + index.toString(),"doctype is " , item.doctype, "docid", item.docid);
    });
  }

  for (let j=0; j < filelist.length; j++) {
    let file = filelist[j];
    let doc = {docid: file.url, doctype: doctype};

    console.log("docList push is ",docList, file, doc);

    docList.push(doc);
  }

  docList.filter(function (item) {return item.doctype === doctype}).forEach(function(item, index, arr) {
    console.log("清理结束，index=" + index.toString(),"doctype is " , item.doctype, "docid", item.docid);
  });

}
