package com.ruoyi.audit.mapper;

import com.ruoyi.audit.domain.AudSheetauditrecord;
import java.util.List;

public interface AudSheetauditrecordMapper {

    List<AudSheetauditrecord>  selectSheetauditRecord(AudSheetauditrecord sheet);

    int insertAudSheetauditrecord(AudSheetauditrecord record);

    List<AudSheetauditrecord> selectMyauditrecord(Integer audituserid);

    List<AudSheetauditrecord> selectFourtechMyauditrecord(Integer audituserid);


//    AudSheetauditrecord selectByPrimaryKey(Integer auditid);
//
//
//    List<AudSheetauditrecord> selectAll();
//
//
//    int deleteByPrimaryKey(Integer auditid);
//
//    int updateByPrimaryKey(AudSheetauditrecord record);
}