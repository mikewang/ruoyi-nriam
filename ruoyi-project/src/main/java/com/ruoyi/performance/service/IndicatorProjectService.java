package com.ruoyi.performance.service;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.fourtech.domain.AudFourtech;
import com.ruoyi.performance.domain.PerIndicator;
import com.ruoyi.performance.domain.PerIndicatorproject;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.domain.PerTeamperformance;
import com.ruoyi.performance.mapper.PerIndicatorMapper;
import com.ruoyi.performance.mapper.PerIndicatorprojectMapper;
import com.ruoyi.performance.mapper.PerRelationMapper;
import com.ruoyi.performance.mapper.PerTeamperformanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorProjectService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorProjectService.class);

    @Resource
    PerIndicatorprojectMapper indicatorprojectMapper;

    @Resource
    PerRelationMapper relationMapper;

    @Resource
    PerIndicatorMapper indicatorMapper;

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;


    public List<PerIndicatorproject> selectIndicatorproject(PerIndicatorproject project) {

        List<PerIndicatorproject> projectList = indicatorprojectMapper.selectIndicatorproject(project);

        log.debug("request projectList  is " + projectList.toString());

        return projectList;
    }

    public List<PerRelation> selectPerRelation(PerRelation relation) {

        List<PerRelation> relationList = relationMapper.selectPerRelation(relation);

        log.debug("request relationList  is " + relationList.toString());

        return relationList;
    }


    @Transactional
    public Integer updateIndicatorRelation(PerRelation relation) {
        Integer result = 1;

        relationMapper.updatePerRelation(relation);

        String currYear = DateUtils.dateTimeNow("yyyy");

        PerTeamperformance teamperformance = new PerTeamperformance();
        teamperformance.setLevel1id(relation.getLevel1id());
        teamperformance.setLevel1name(relation.getLevel1name());
        teamperformance.setLevel2id(relation.getLevel2id());
        teamperformance.setLevel2name(relation.getLevel2name());
        teamperformance.setIndicatortype(relation.getIndicatortype());
        teamperformance.setPerformanceyear(Integer.valueOf(currYear));
        teamperformance.setStatus("待确认");
        teamperformanceMapper.updatePerTeamperformance(teamperformance);


//        sql = "update PER_Relation set Level1ID = " + level1ID
//                + " ,Level1Name='" + level1Name + "'"
//                + " ,Level2ID = " + level2ID
//                + " ,Level2Name='" + level2Name + "'"
//                + " where IndicatorType = '" + indicatorType + "'";
//        DBAccess.DB.ExecuteNonQuery(CommandType.Text, sql);
//
//        //更新团队绩效表里的所有待确认的项
//        try
//        {
//            string curYear = DateTime.Now.ToString("yyyy");
//            string upsql = "update PER_TeamPerformance set Level1ID = " + level1ID
//                    + " ,Level1Name='" + level1Name + "'"
//                    + " ,Level2ID = " + level2ID
//                    + " ,Level2Name='" + level2Name + "' "          //只更新当年未确认的
//                    + " where IndicatorType = '" + indicatorType + "' "
//                    + " and PerformanceYear = " + curYear + " and Status = '待确认' ";
//            DBAccess.DB.ExecuteNonQuery(CommandType.Text, upsql);
//        }
//        catch (Exception exp)
//        {
//            LogRecorder.WriteLog("设置关系后更新团队绩效表时发生错误", exp.Message);
//        }

        return result;
    }

    public List<PerIndicator> selectPerIndicator(PerIndicator indicator) {

        List<PerIndicator> indicatorList = indicatorMapper.selectPerIndicator(indicator);

        log.debug("request indicatorList  is " + indicatorList.toString());

        return indicatorList;
    }

    @Transactional
    public Integer addIndicatorproject(PerIndicatorproject project) {
        Integer result = 1;

        project.setIfdeleted(false);
        result = indicatorprojectMapper.insertIndicatorproject(project);

        return result;
    }


    @Transactional
    public Integer updateIndicatorproject(PerIndicatorproject project) {
        Integer result = 1;

        result = indicatorprojectMapper.updateIndicatorproject(project);

        return result;
    }


    @Transactional
    public Integer updateIndicatorprojectIfDeletedById(Integer projectid) {
        Integer result = 1;

        result = indicatorprojectMapper.updateIndicatorprojectIfDeletedById(projectid);

        return result;
    }
}
