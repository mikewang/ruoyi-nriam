package com.ruoyi.project.service;

import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.project.mapper.BasDocMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BasDocService {
    private static final Logger log = LoggerFactory.getLogger(BasDocService.class);

    @Resource
    private BasDocMapper mapper;

    @Transactional
    public Integer insertBasDoc(BasDoc record) {
        Integer rows =  mapper.insertBasDoc(record);
        log.debug("insertBasDoc rows is " + rows.toString() + " docid is " + record.getDocid().toString());
        Integer docid = rows > 0 ? record.getDocid() : rows;

        return docid;
    }

    public Integer updateBasDoc(BasDoc record) {
        return mapper.updateBasDoc(record);
    }

    public Integer deleteBasDocById(Integer id) {
        return mapper.deleteBasDocById(id);
    }

    public Integer deleteBasDocByIds(List ids) {
        return mapper.deleteBasDocByIds(ids);
    }

    public List<BasDoc> selectBasDocListByIds(List ids) {
        return mapper.selectBasDocListByIds(ids);
    }

    public List<BasDoc> selectBasDocList(BasDoc record) {
        return mapper.selectBasDocList(record);
    }
}