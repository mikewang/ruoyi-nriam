package com.ruoyi.sku.mapper;

import com.ruoyi.sku.domain.SkuFile;
import java.util.List;

public interface SkuFileMapper {
    int insertSkuFile(SkuFile record);
    int updateSkuFile(SkuFile record);
    int updateSkuFileSkuId(SkuFile record);

    SkuFile selectSkuFileById(Long fileId);
    List<SkuFile> selectSkuFile(SkuFile record);
    int deleteSkuFileById(Long fileId);
}