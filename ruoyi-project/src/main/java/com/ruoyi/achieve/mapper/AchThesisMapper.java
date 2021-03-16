package com.ruoyi.achieve.mapper;

import com.ruoyi.achieve.domain.AchThesis;
import java.util.List;

public interface AchThesisMapper {

    List<AchThesis> selectAchThesis(AchThesis record);

    AchThesis selectAchThesisById(Integer thesisid);


    int deleteByPrimaryKey(Integer thesisid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Thesis
     *
     * @mbg.generated Mon Mar 15 11:38:35 CST 2021
     */
    int insert(AchThesis record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Thesis
     *
     * @mbg.generated Mon Mar 15 11:38:35 CST 2021
     */


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Thesis
     *
     * @mbg.generated Mon Mar 15 11:38:35 CST 2021
     */
    List<AchThesis> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table ACH_Thesis
     *
     * @mbg.generated Mon Mar 15 11:38:35 CST 2021
     */
    int updateByPrimaryKey(AchThesis record);
}