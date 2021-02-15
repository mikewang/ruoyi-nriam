package com.ruoyi.sheet.mapper;

import com.ruoyi.sheet.domain.AudSheetauditrecord;
import java.util.List;

public interface AudSheetauditrecordMapper {

    List<AudSheetauditrecord>  selectSheetauditRecord(AudSheetauditrecord sheet);

    int deleteByPrimaryKey(Integer auditid);


    int insert(AudSheetauditrecord record);


    AudSheetauditrecord selectByPrimaryKey(Integer auditid);


    List<AudSheetauditrecord> selectAll();


    int updateByPrimaryKey(AudSheetauditrecord record);
}