package com.ruoyi.sheet.mapper;

import com.ruoyi.sheet.domain.SrmSupplierinfo;
import java.util.List;

public interface SrmSupplierinfoMapper {

    List<SrmSupplierinfo> selectSupplierInfoList(SrmSupplierinfo record);

    SrmSupplierinfo selectSupplierInfoById(Integer supplierid);

    SrmSupplierinfo selectSupplierInfoByOrganizationcode(String organizationcode);

    int insertSupplierInfo(SrmSupplierinfo record);

    int updateSupplierInfo(SrmSupplierinfo record);

    int deleteSupplierInfoByIds(List<Integer> ids);

//    int deleteByPrimaryKey(Integer supplierid);


//
//    List<SrmSupplierinfo> selectAll();
//
//
//    int updateByPrimaryKey(SrmSupplierinfo record);
}