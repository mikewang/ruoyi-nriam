package com.ruoyi.performance.domain;

import java.math.BigDecimal;

public class PerIndicatorsoftware {

    private Integer indicatorsoftwareid;


    private BigDecimal points;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PER_IndicatorSoftware.indicatorSoftwareID
     *
     * @return the value of PER_IndicatorSoftware.indicatorSoftwareID
     *
     * @mbg.generated Sun Mar 14 20:57:10 CST 2021
     */
    public Integer getIndicatorsoftwareid() {
        return indicatorsoftwareid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PER_IndicatorSoftware.indicatorSoftwareID
     *
     * @param indicatorsoftwareid the value for PER_IndicatorSoftware.indicatorSoftwareID
     *
     * @mbg.generated Sun Mar 14 20:57:10 CST 2021
     */
    public void setIndicatorsoftwareid(Integer indicatorsoftwareid) {
        this.indicatorsoftwareid = indicatorsoftwareid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PER_IndicatorSoftware.Points
     *
     * @return the value of PER_IndicatorSoftware.Points
     *
     * @mbg.generated Sun Mar 14 20:57:10 CST 2021
     */
    public BigDecimal getPoints() {
        return points;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PER_IndicatorSoftware.Points
     *
     * @param points the value for PER_IndicatorSoftware.Points
     *
     * @mbg.generated Sun Mar 14 20:57:10 CST 2021
     */
    public void setPoints(BigDecimal points) {
        this.points = points;
    }
}