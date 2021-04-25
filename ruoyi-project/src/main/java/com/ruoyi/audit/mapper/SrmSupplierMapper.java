package com.ruoyi.audit.mapper;

import com.ruoyi.audit.domain.SrmSupplier;
import java.util.List;

public interface SrmSupplierMapper {

    List<SrmSupplier> selectSupplierInfoList(SrmSupplier record);

    SrmSupplier selectSupplierInfoById(Integer supplierid);

    SrmSupplier selectSupplierInfoByOrganizationcode(String organizationcode);

    int insertSupplierInfo(SrmSupplier record);

    int updateSupplierInfo(SrmSupplier record);

    int updateSupplierInfoIfAuditedByIds(List<Integer> ids);
    int deleteSupplierInfoByIds(List<Integer> ids);


//    int deleteByPrimaryKey(Integer supplierid);


//
//    List<SrmSupplierinfo> selectAll();
//
//
//    int updateByPrimaryKey(SrmSupplierinfo record);
}