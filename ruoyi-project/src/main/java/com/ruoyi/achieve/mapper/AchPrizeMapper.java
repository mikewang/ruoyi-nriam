package com.ruoyi.achieve.mapper;

import com.ruoyi.achieve.domain.AchPrize;
import com.ruoyi.achieve.domain.AchPrize;

import java.util.List;

public interface AchPrizeMapper {

    List<AchPrize>  selectAchPrize(AchPrize record);

    AchPrize selectAchPrizeById(Integer prizeid);

    int insertAchPrize(AchPrize record);

    int updateAchPrize(AchPrize record);

    int updateAchPrizeStatus(AchPrize record);
    
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Prize
     *
     * @mbg.generated Sun Mar 14 08:08:44 CST 2021
     */
    int deleteByPrimaryKey(Integer prizeid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Prize
     *
     * @mbg.generated Sun Mar 14 08:08:44 CST 2021
     */
    int insert(AchPrize record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Prize
     *
     * @mbg.generated Sun Mar 14 08:08:44 CST 2021
     */


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Prize
     *
     * @mbg.generated Sun Mar 14 08:08:44 CST 2021
     */
    List<AchPrize> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Prize
     *
     * @mbg.generated Sun Mar 14 08:08:44 CST 2021
     */
    int updateByPrimaryKey(AchPrize record);
}