package com.ruoyi.achieve.mapper;

import com.ruoyi.achieve.domain.AchAuthor;

import java.util.List;

public interface AchAuthorMapper {

    List<AchAuthor> selectAchAuthorList(AchAuthor query);

    int insertAchAuthor(AchAuthor record);

    AchAuthor selectAchAuthorById(Integer authorid);

    int updateAchAuthor(AchAuthor record);

    int deleteAchAuthorById(Integer authorid);

    int deleteAchAuthor(AchAuthor record);

}