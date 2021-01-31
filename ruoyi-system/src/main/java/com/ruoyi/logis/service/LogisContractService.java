package com.ruoyi.logis.service;

import com.ruoyi.common.annotation.DataScope;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.logis.domain.LogisContract;
import com.ruoyi.logis.mapper.LogisContractMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LogisContractService {
    private static final Logger log = LoggerFactory.getLogger(LogisContractService.class);

    @Resource
    private LogisContractMapper logisContractMapper;


    @DataScope(deptAlias = "d", userAlias = "u")
    public List<LogisContract> selectLogisContractList(LogisContract logisContract) {
        List<LogisContract> contractList = logisContractMapper.selectLogisContractList(logisContract);

        for(LogisContract contract : contractList)
        {
            configFileList(contract);
        }

        return contractList;
    }

    public LogisContract selectLogisContractById(Long contractId) {
        LogisContract contract =  logisContractMapper.selectLogisContractById(contractId);

        configFileList(contract);

        log.debug("contract.fileList.size() is " + contract.fileList.size());

        return  contract;

    }

    @Transactional
    public int insertLogisContract(LogisContract logisContract) {
        // 新增信息
        int rows = logisContractMapper.insertLogisContract(logisContract);
//        // 新增用户岗位关联
//        insertUserPost(user);
//        // 新增用户与角色管理
//        insertUserRole(user);
        return rows;
    }

    @Transactional
    public int updateLogisContract(LogisContract logisContract) {
        // 更新合同信息

        Integer[] myList = new Integer[logisContract.fileList.size()];

        for (int i=0; i < logisContract.fileList.size(); i++) {
            HashMap item = logisContract.fileList.get(i);
            if  (item.get("id") == null) {
                String url = item.get("url").toString();
                String name = item.get("name").toString();

                String RelativePath = StringUtils.trimend(url,name);
                RelativePath = StringUtils.trimend(RelativePath, "/");

                BasDoc doc = new BasDoc();
                doc.setDocname(name);
                doc.setRelativepath(RelativePath);

//                int docId = basDocMapper.insert(doc);
//
//                myList[i] = doc.getDocid();

                log.debug("new item file upload. ** " + String.valueOf(doc.getDocid()) + " ** ="  + url );
            }
            else {
                log.debug("update item file upload." + item.get("name") + item.get("url"));
                myList[i] = (Integer) item.get("id");
            }
        }
        String contractFile = StringUtils.joinWith(",",myList);

        log.debug("contractFile is " + contractFile);

        logisContract.setContractFile(contractFile);

        int rows = logisContractMapper.updateLogisContract(logisContract);

        return rows;
    }

    private int configFileList(LogisContract contract) {

        if (contract.getContractFile() == null || contract.getContractFile().isEmpty()) {

            contract.fileList = new ArrayList<HashMap>();
        }
        else {
            List<HashMap> fileList = new ArrayList<HashMap>();

            List<Integer> docIdList = new ArrayList<Integer>();

            for (String  item : contract.getContractFile().split(",")) {
                log.debug("item is " + item);
                docIdList.add(Integer.parseInt(item));
            }

//            List<BasDoc> basDocList = basDocMapper.selectByIds(docIdList);
//
//            for(BasDoc doc : basDocList) {
//                HashMap fileMap = new HashMap();
//                fileMap.put("id",doc.getDocid());
//                fileMap.put("name",doc.getDocname());
//                fileMap.put("url",doc.getUrl());
//                fileList.add(fileMap);
//            }
//
//            log.debug(fileList.toString());
//
//            contract.fileList = fileList;

        }


        return  1;
    }

}
