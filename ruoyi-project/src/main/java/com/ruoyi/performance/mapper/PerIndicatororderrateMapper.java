package com.ruoyi.performance.mapper;

import com.ruoyi.performance.domain.PerIndicatororderrate;
import java.util.List;

public interface PerIndicatororderrateMapper {

    List<PerIndicatororderrate> selectPerIndicatororderrate(PerIndicatororderrate record);
    int updatePerIndicatororderrate(PerIndicatororderrate record);

    PerIndicatororderrate selectPerIndicatororderrateById(Integer indicatororderrateid);

    int deleteByPrimaryKey(Integer indicatororderrateid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorOrderRate
     *
     * @mbg.generated Sun Mar 14 20:58:26 CST 2021
     */
    int insert(PerIndicatororderrate record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorOrderRate
     *
     * @mbg.generated Sun Mar 14 20:58:26 CST 2021
     */


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorOrderRate
     *
     * @mbg.generated Sun Mar 14 20:58:26 CST 2021
     */

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_IndicatorOrderRate
     *
     * @mbg.generated Sun Mar 14 20:58:26 CST 2021
     */

}