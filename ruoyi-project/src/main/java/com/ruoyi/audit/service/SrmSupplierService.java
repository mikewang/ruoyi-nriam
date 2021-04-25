package com.ruoyi.audit.service;


import com.ruoyi.api.mapper.AppClientinfoMapper;
import com.ruoyi.audit.domain.SrmSupplier;
import com.ruoyi.audit.mapper.*;
import com.ruoyi.audit.service.AudApplyService;
import com.ruoyi.common.core.domain.model.BasDoc;
import com.ruoyi.project.domain.DocFile;
import com.ruoyi.project.mapper.BasDocMapper;
import com.ruoyi.project.service.AudProjectService;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysDictDataMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.mapper.SysUserRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SrmSupplierService {

    private static final Logger log = LoggerFactory.getLogger(AudApplyService.class);

    @Resource
    AudSheetMapper audSheetMapper;

    @Resource
    AudBudgetpayMapper audBudgetpayMapper;

    @Resource
    AudSheetauditrecordMapper audSheetauditrecordMapper;


    @Resource
    SrmSupplierMapper supplierinfoMapper;

    @Resource
    BasUsedmaxserialnumberMapper usedmaxserialnumberMapper;

    @Resource
    private AppClientinfoMapper clientinfoMapper;

    @Resource
    private AudMessageMapper messageMapper;

    @Resource
    private SysDeptMapper deptMapper;

    @Resource
    private SysUserRoleMapper userRoleMapper;

    @Resource
    private SysDictDataMapper dictDataMapper;

    @Resource
    private BasDocMapper basDocMapper;

    @Resource
    private SysUserMapper userMapper;


    @Resource
    private AudProjectService projectService;


    @Transactional
    public Integer addSupplierinfo(SrmSupplier supplier) {

        Integer result = supplierinfoMapper.insertSupplierInfo(supplier);

        BasDoc doc = new BasDoc();
        doc.setDocid(supplier.getOrgImgId());
        doc.setRelatedid(supplier.getSupplierid());
        doc.setDoctype("营业执照");
        doc.setAttachtotype("协作单位");
        basDocMapper.updateBasDocAttachToType(doc);
        log.debug("supplier is " + supplier.toString());

        if (supplier.getDocList() != null) {
            for (DocFile map : supplier.getDocList()) {
                log.debug("getDocList map is " + map.toString());
                Integer docid = map.getUrl();
                doc = new BasDoc();
                doc.setDocid(docid);
                doc.setRelatedid(supplier.getSupplierid());
                doc.setDoctype("其它附件");
                doc.setAttachtotype("协作单位");
                basDocMapper.updateBasDocAttachToType(doc);
            }
        }

        return result;
    }

    @Transactional
    public Integer updateSupplierinfo(SrmSupplier supplier) {
        log.debug("supplier is " + supplier.toString());

        Integer result = supplierinfoMapper.updateSupplierInfo(supplier);

        BasDoc doc = new BasDoc();
        doc.setRelatedid(supplier.getSupplierid());
        doc.setAttachtotype("协作单位");

        Set<Integer> sets = new HashSet<>();
        for( DocFile file : supplier.getDocList()){
            sets.add(file.getUrl());
        }

        sets.add(supplier.getOrgImgId());


        Set<Integer> sets2 = new HashSet<>();
        for (BasDoc record : basDocMapper.selectBasDocList(doc))
        {
            if (sets.contains(record.getDocid()) == false)
            {
                sets2.add(record.getDocid());
            }
        }

        List<Integer> ids = new ArrayList<Integer>();
        for(Integer id : sets2) {
            ids.add(id);
        }

        log.debug("deleteBasDocByIds is " + ids.toString());
        if (ids.size() > 0) {
            result =  basDocMapper.deleteBasDocByIds(ids);
        }

        doc.setDoctype("营业执照");
        doc.setDocid(supplier.getOrgImgId());

        result =  basDocMapper.updateBasDocAttachToType(doc);

        if (supplier.getDocList() != null && supplier.getDocList().size() > 0) {
            for (DocFile map : supplier.getDocList()) {
                Integer docid = (Integer) map.getUrl();
                doc = new BasDoc();
                doc.setDocid(docid);
                doc.setRelatedid(supplier.getSupplierid());
                doc.setDoctype("其它附件");
                doc.setAttachtotype("协作单位");
                result = basDocMapper.updateBasDocAttachToType(doc);
            }
        }

        return result;
    }

    @Transactional
    public Integer updateSupplierInfoIfAuditedByIds(List<Integer> ids) {
        log.debug("audit pass supplier ids is " + ids.toString());

        Integer result = supplierinfoMapper.updateSupplierInfoIfAuditedByIds(ids);

        return result;
    }

    @Transactional
    public Integer deleteSupplierinfo(List<Integer> ids) {
        log.debug("supplier ids is " + ids.toString());

        Integer result = supplierinfoMapper.deleteSupplierInfoByIds(ids);

        return result;
    }

    public List<SrmSupplier> selectSupplierInfoList(SrmSupplier query) {

        log.debug("selectSupplierInfoList  query is " + query.toString());

        List<SrmSupplier> list = supplierinfoMapper.selectSupplierInfoList(query);

        //   log.debug("request selectSupplierInfoList  list is " + list.toString());

        return list;
    }

    public SrmSupplier selectSupplierInfoById(Integer supplierid) {

        SrmSupplier s = supplierinfoMapper.selectSupplierInfoById(supplierid);

        return s;
    }

    public SrmSupplier selectSupplierInfoByOrganizationcode(String organizationcode) {

        SrmSupplier s = supplierinfoMapper.selectSupplierInfoByOrganizationcode(organizationcode);

        return s;
    }


}
