package com.ruoyi.performance.mapper;

import com.ruoyi.performance.domain.PerTeamperformance;
import java.util.List;

public interface PerTeamperformanceMapper {


    int updatePerTeamperformance(PerTeamperformance record);
    int insertPerTeamperformance(PerTeamperformance record);

    int updatePerTeamperformanceDeleted(PerTeamperformance record);

    int updatePerTeamperformanceDeletedById(PerTeamperformance record);

    int updatePerTeamperformanceByStatusAndType(PerTeamperformance record);

    int updatePerTeamperformancePoint(PerTeamperformance record);

    List<PerTeamperformance> selectPerTeamperformance(PerTeamperformance record);

    Integer selectPerTeamperformancePointsSum(PerTeamperformance record);

    int deleteByPrimaryKey(Integer performanceid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_TeamPerformance
     *
     * @mbg.generated Sat Mar 13 07:36:51 CST 2021
     */
    int insert(PerTeamperformance record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_TeamPerformance
     *
     * @mbg.generated Sat Mar 13 07:36:51 CST 2021
     */
    PerTeamperformance selectByPrimaryKey(Integer performanceid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_TeamPerformance
     *
     * @mbg.generated Sat Mar 13 07:36:51 CST 2021
     */


    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PER_TeamPerformance
     *
     * @mbg.generated Sat Mar 13 07:36:51 CST 2021
     */
    int updateByPrimaryKey(PerTeamperformance record);
}