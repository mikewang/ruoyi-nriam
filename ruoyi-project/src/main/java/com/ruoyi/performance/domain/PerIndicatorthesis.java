package com.ruoyi.performance.domain;

import java.math.BigDecimal;

public class PerIndicatorthesis {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PER_IndicatorThesis.IndicatorThesisID
     *
     * @mbg.generated Sun Mar 14 20:55:54 CST 2021
     */
    private Integer indicatorthesisid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PER_IndicatorThesis.ThesisLevel
     *
     * @mbg.generated Sun Mar 14 20:55:54 CST 2021
     */
    private String thesislevel;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column PER_IndicatorThesis.Points
     *
     * @mbg.generated Sun Mar 14 20:55:54 CST 2021
     */
    private BigDecimal points;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PER_IndicatorThesis.IndicatorThesisID
     *
     * @return the value of PER_IndicatorThesis.IndicatorThesisID
     *
     * @mbg.generated Sun Mar 14 20:55:54 CST 2021
     */
    public Integer getIndicatorthesisid() {
        return indicatorthesisid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PER_IndicatorThesis.IndicatorThesisID
     *
     * @param indicatorthesisid the value for PER_IndicatorThesis.IndicatorThesisID
     *
     * @mbg.generated Sun Mar 14 20:55:54 CST 2021
     */
    public void setIndicatorthesisid(Integer indicatorthesisid) {
        this.indicatorthesisid = indicatorthesisid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PER_IndicatorThesis.ThesisLevel
     *
     * @return the value of PER_IndicatorThesis.ThesisLevel
     *
     * @mbg.generated Sun Mar 14 20:55:54 CST 2021
     */
    public String getThesislevel() {
        return thesislevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PER_IndicatorThesis.ThesisLevel
     *
     * @param thesislevel the value for PER_IndicatorThesis.ThesisLevel
     *
     * @mbg.generated Sun Mar 14 20:55:54 CST 2021
     */
    public void setThesislevel(String thesislevel) {
        this.thesislevel = thesislevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column PER_IndicatorThesis.Points
     *
     * @return the value of PER_IndicatorThesis.Points
     *
     * @mbg.generated Sun Mar 14 20:55:54 CST 2021
     */
    public BigDecimal getPoints() {
        return points;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column PER_IndicatorThesis.Points
     *
     * @param points the value for PER_IndicatorThesis.Points
     *
     * @mbg.generated Sun Mar 14 20:55:54 CST 2021
     */
    public void setPoints(BigDecimal points) {
        this.points = points;
    }
}