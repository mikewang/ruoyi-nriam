package com.ruoyi.project.mapper;

import com.ruoyi.project.domain.PmProjectjoinorganization;

import java.util.List;

public interface PmProjectjoinorganizationMapper {

    List<PmProjectjoinorganization> selectProjectjoinorganizationList(PmProjectjoinorganization join);


    int deleteByPrimaryKey(Integer joid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PM_ProjectJoinOrganization
     *
     * @mbg.generated Wed Jan 27 12:05:02 CST 2021
     */
    int insert(PmProjectjoinorganization record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PM_ProjectJoinOrganization
     *
     * @mbg.generated Wed Jan 27 12:05:02 CST 2021
     */
    PmProjectjoinorganization selectByPrimaryKey(Integer joid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PM_ProjectJoinOrganization
     *
     * @mbg.generated Wed Jan 27 12:05:02 CST 2021
     */
    List<PmProjectjoinorganization> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PM_ProjectJoinOrganization
     *
     * @mbg.generated Wed Jan 27 12:05:02 CST 2021
     */
    int updateByPrimaryKey(PmProjectjoinorganization record);
}