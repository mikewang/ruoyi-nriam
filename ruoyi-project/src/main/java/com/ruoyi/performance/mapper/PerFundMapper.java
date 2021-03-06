package com.ruoyi.performance.mapper;

import com.ruoyi.performance.domain.PerFund;
import org.apache.poi.hpsf.Decimal;

import java.util.List;

public interface PerFundMapper {


    int insertPerFund(PerFund record);
    int updatePerFund(PerFund record);
    int updatePerFundDeletedById(PerFund record);
    int updatePerFundFundById(PerFund record);

    int updatePerFundStatusById(PerFund record);

    List<PerFund> selectPerFund(PerFund record);

    PerFund selectPerFundById(Integer fundid);

    java.math.BigDecimal  selectCaculateTotalNationalByTeamidYear(PerFund record);

    java.math.BigDecimal  selectCaculateTotalOtherByTeamidYear(PerFund record);


    int deleteByPrimaryKey(Integer fundid);



    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_Fund
     *
     * @mbg.generated Thu Mar 25 10:01:01 CST 2021
     */
    List<PerFund> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_Fund
     *
     * @mbg.generated Thu Mar 25 10:01:01 CST 2021
     */
    int updateByPrimaryKey(PerFund record);
}