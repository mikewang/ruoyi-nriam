package com.ruoyi.achieve.mapper;

import com.ruoyi.achieve.domain.AchSoftware;
import com.ruoyi.achieve.domain.AchSoftware;

import java.util.List;

public interface AchSoftwareMapper {

    List<AchSoftware> selectAchSoftware(AchSoftware record);

    AchSoftware selectAchSoftwareById(Integer softwareid);
    int insertAchSoftware(AchSoftware record);
    int updateAchSoftware(AchSoftware record);
    int updateAchSoftwareStatus(AchSoftware record);

    int deleteByPrimaryKey(Integer softwareid);




    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Software
     *
     * @mbg.generated Mon Mar 15 13:46:26 CST 2021
     */
    int insert(AchSoftware record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Software
     *
     * @mbg.generated Mon Mar 15 13:46:26 CST 2021
     */


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Software
     *
     * @mbg.generated Mon Mar 15 13:46:26 CST 2021
     */
    List<AchSoftware> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Software
     *
     * @mbg.generated Mon Mar 15 13:46:26 CST 2021
     */
    int updateByPrimaryKey(AchSoftware record);
}