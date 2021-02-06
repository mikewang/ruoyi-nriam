
/* 验收申请书 */
import {downloadFile, uploadFile} from "@/api/project/project";

export function beforeUpload(file, fileList, docType) {

  let x = true

  console.log("fileList is ", fileList);
  for (let i = 0; i < fileList.length; i++) {
    let item = fileList[i];
    if (item.name === file.name) {
      console.log("file existed now ,", file.name);
      x = false;
      break;
    }
  }
  return x;
}


export function requestUpload(params, fileList, docType, projectdocList) {
  let file = params.file;
  console.log(file);
  let formData = new FormData();
  formData.append('file', file);
  uploadFile(formData).then(response => {
    console.log("response.name is ", response.name);
    console.log("response.url is ", response.url);
    fileList.push({name: response.name, url: response.url});
    console.log(docType, "fileList is ", fileList);
    projectdocList.push({docid: response.url, doctype: docType});

  });
}

export function  beforeRemove(file, fileList) {
  let index = fileList.indexOf(file);
  console.log("beforeRemove index=" + index, file.name);
  return true;
  //  return this.$confirm(`确定移除 ${ file.name }？`);
}

export function handleUploadRemove(file, fileList, docType, projectdocList) {

  let today = new Date();

  let docid = file.url;

  let index = fileList.indexOf(file);
  if (index !== -1) {
    fileList.splice(index, 1);
  }
  console.log("handleUploadRemove index=" + index, file.name, today.toDateString());
  console.log(docType, "fileList is ", fileList);

  let index2 = -1;
  for (let i = 0; i < projectdocList.length; i++) {
    let item = projectdocList[i];
    if (item.docid === docid) {
      index2 = i;
      break;
    }
  }

  if (index2 !== -1) {
    projectdocList.splice(index2, 1);
  }
  console.log(docType, "projectdocList is ", projectdocList);

  return;
}


export function handleUploadReview(file) {
  downloadFile({"file": file.url}).then(response => {
    var fileURL = window.URL.createObjectURL(new Blob([response]));
    var fileLink = document.createElement('a');

    console.log("response.data is ", response);

    fileLink.href = fileURL;
    fileLink.setAttribute('download', file.name);
    document.body.appendChild(fileLink);

    fileLink.click();
    URL.revokeObjectURL(fileURL);

  }).catch(console.error);

}
