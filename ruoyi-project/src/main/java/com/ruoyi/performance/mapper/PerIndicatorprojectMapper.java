package com.ruoyi.performance.mapper;

import com.ruoyi.performance.domain.PerIndicatorproject;
import java.util.List;

public interface PerIndicatorprojectMapper {

    List<PerIndicatorproject> selectIndicatorproject(PerIndicatorproject project);

    PerIndicatorproject selectIndicatorprojectById(Integer indicatorprojectid);
    int updateIndicatorprojectIfDeletedById(Integer indicatorprojectid);
    int insertIndicatorproject(PerIndicatorproject record);
    int updateIndicatorproject(PerIndicatorproject record);


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorProject
     *
     * @mbg.generated Sat Mar 13 07:34:55 CST 2021
     */
    int deleteByPrimaryKey(Integer indicatorprojectid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorProject
     *
     * @mbg.generated Sat Mar 13 07:34:55 CST 2021
     */
    int insert(PerIndicatorproject record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorProject
     *
     * @mbg.generated Sat Mar 13 07:34:55 CST 2021
     */
    PerIndicatorproject selectByPrimaryKey(Integer indicatorprojectid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorProject
     *
     * @mbg.generated Sat Mar 13 07:34:55 CST 2021
     */
    List<PerIndicatorproject> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorProject
     *
     * @mbg.generated Sat Mar 13 07:34:55 CST 2021
     */
    int updateByPrimaryKey(PerIndicatorproject record);
}