package com.ruoyi.achieve.mapper;

import com.ruoyi.achieve.domain.AchPatent;
import java.util.List;

public interface AchPatentMapper {

    List<AchPatent> selectAchPatentList(AchPatent query);

    int insertAchPatent(AchPatent record);

    AchPatent selectAchPatentById(Integer patentid);

    int updateAchPatent(AchPatent record);

    int updateStatusAchPatent(AchPatent record);

    int queryIfDuplicate(AchPatent record);
}