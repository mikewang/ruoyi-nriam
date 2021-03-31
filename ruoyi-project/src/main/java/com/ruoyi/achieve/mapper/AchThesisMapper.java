package com.ruoyi.achieve.mapper;

import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.domain.AchThesis;
import java.util.List;

public interface AchThesisMapper {

    List<AchThesis> selectAchThesis(AchThesis record);

    AchThesis selectAchThesisById(Integer thesisid);

    int queryIfDuplicate(AchThesis record);
    int insertAchThesis(AchThesis record);

    int updateAchThesis(AchThesis record);

    int updateStatusAchThesis(AchThesis record);

    int deleteByPrimaryKey(Integer thesisid);

    List<AchThesis> selectAll();

}