package com.ruoyi.sku.mapper;

import com.ruoyi.sku.domain.SkuInfo;
import java.util.List;

public interface SkuInfoMapper {

    List<SkuInfo> selectSkuInfo(SkuInfo record);
    int insertSkuInfo(SkuInfo record);
    SkuInfo selectSkuInfoById(Long skuId);
    int updateSkuInfo(SkuInfo record);


    int deleteByPrimaryKey(Long skuId);

}