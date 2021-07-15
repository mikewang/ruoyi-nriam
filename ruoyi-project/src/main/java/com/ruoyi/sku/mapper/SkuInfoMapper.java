package com.ruoyi.sku.mapper;

import com.ruoyi.sku.domain.SkuInfo;
import java.util.List;

public interface SkuInfoMapper {

    List<SkuInfo> selectSkuInfo(SkuInfo record);
    List<SkuInfo> selectSkuListByPhotoSizeValue(SkuInfo record);

    List<SkuInfo> batchSelectSkuList(SkuInfo record);
    List<SkuInfo> batchSelectSkuListByPhotoSizeValue(SkuInfo record);

    List<SkuInfo> exportSkuList(SkuInfo record);
    List<SkuInfo> exportSkuListByPhotoSizeValue(SkuInfo record);


    List<SkuInfo> exportSkuListWithFilesByPhotoSizeValue(SkuInfo record);

    int insertSkuInfo(SkuInfo record);
    SkuInfo selectSkuInfoById(Long skuId);
    int updateSkuInfo(SkuInfo record);
    int deleteSkuInfoBySkuIds(List<Long> skuIds);
    int queryIfDuplicate(SkuInfo record);

    int deleteByPrimaryKey(Long skuId);

}