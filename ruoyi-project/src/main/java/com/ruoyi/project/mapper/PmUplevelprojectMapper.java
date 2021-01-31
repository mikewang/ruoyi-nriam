package com.ruoyi.project.mapper;

import com.ruoyi.project.domain.PmUplevelproject;
import java.util.List;

public interface PmUplevelprojectMapper {


    List<PmUplevelproject> selectUplevelprojectList(PmUplevelproject record);

    PmUplevelproject selectUplevelProjectByProjectid(Integer projectid);

    int deleteUplevelProject(PmUplevelproject record);

    int insertUplevelProject(PmUplevelproject record);
    int updateUplevelProject(PmUplevelproject record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PM_UpLevelProject
     *
     * @mbg.generated Wed Jan 27 14:21:55 CST 2021
     */
    int deleteByPrimaryKey(Integer infoid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PM_UpLevelProject
     *
     * @mbg.generated Wed Jan 27 14:21:55 CST 2021
     */
    int insert(PmUplevelproject record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PM_UpLevelProject
     *
     * @mbg.generated Wed Jan 27 14:21:55 CST 2021
     */
    PmUplevelproject selectByPrimaryKey(Integer infoid);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PM_UpLevelProject
     *
     * @mbg.generated Wed Jan 27 14:21:55 CST 2021
     */
    List<PmUplevelproject> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table PM_UpLevelProject
     *
     * @mbg.generated Wed Jan 27 14:21:55 CST 2021
     */
    int updateByPrimaryKey(PmUplevelproject record);
}