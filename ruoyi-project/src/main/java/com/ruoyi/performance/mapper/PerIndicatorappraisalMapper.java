package com.ruoyi.performance.mapper;

import com.ruoyi.performance.domain.PerIndicatorappraisal;


import java.util.List;

public interface PerIndicatorappraisalMapper {

    PerIndicatorappraisal selectPerIndicatorappraisalById(Integer indicatorappraisalid);

    int updatePerIndicatorappraisal(PerIndicatorappraisal record);
    
    int deleteByPrimaryKey(Integer indicatorappraisalid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorAppraisal
     *
     * @mbg.generated Sun Mar 14 20:58:03 CST 2021
     */
    int insert(PerIndicatorappraisal record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorAppraisal
     *
     * @mbg.generated Sun Mar 14 20:58:03 CST 2021
     */
    PerIndicatorappraisal selectByPrimaryKey(Integer indicatorappraisalid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorAppraisal
     *
     * @mbg.generated Sun Mar 14 20:58:03 CST 2021
     */
    List<PerIndicatorappraisal> selectAll();

    int updateByPrimaryKey(PerIndicatorappraisal record);
}