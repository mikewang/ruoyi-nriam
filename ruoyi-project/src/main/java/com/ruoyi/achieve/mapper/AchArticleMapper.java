package com.ruoyi.achieve.mapper;

import com.ruoyi.achieve.domain.AchArticle;
import com.ruoyi.achieve.domain.AchArticle;

import java.util.List;

public interface AchArticleMapper {

    List<AchArticle> selectAchArticle(AchArticle record);
    AchArticle selectAchArticleById(Integer articleid);

    int queryIfDuplicate(AchArticle record);
    int insertAchArticle(AchArticle record);

    int updateAchArticle(AchArticle record);

    int updateAchArticleStatus(AchArticle record);

}