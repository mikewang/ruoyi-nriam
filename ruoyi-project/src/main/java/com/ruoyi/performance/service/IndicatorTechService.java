package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.AchTech;
import com.ruoyi.achieve.mapper.AchTechMapper;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.PerIndicatortech;
import com.ruoyi.performance.domain.PerIndicatortech;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.domain.PerTeamperformance;
import com.ruoyi.performance.mapper.PerIndicatortechMapper;
import com.ruoyi.performance.mapper.PerRelationMapper;
import com.ruoyi.performance.mapper.PerTeamperformanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorTechService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorTechService.class);

    @Resource
    PerIndicatortechMapper indicatortechMapper;


    @Resource
    AchTechMapper techMapper;

    @Resource
    PerRelationMapper relationMapper;

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;

    public PerIndicatortech selectPerIndicatortechById(Integer fundid) {

        PerIndicatortech fund = indicatortechMapper.selectPerIndicatortechById(fundid);

        return fund;
    }


    private String getDescription(PerIndicatortech indicatorsoftware) {
        String description = "农业部主推技术-" + "每项" + "-" + indicatorsoftware.getPoints() + "分";

        return description;
    }


    private Integer addPerTeamperformances(PerIndicatortech indicatorsoftware){
        Integer result = 1;

        String currYear = DateUtils.dateTimeNow("yyyy");
        AchTech record = new AchTech();
        record.setStatus(AchieveStatus.ZhengChang.getCode());
        record.setTechyear(Integer.valueOf(currYear));

        List<AchTech> list =   techMapper.selectAchTech(record);

        if (list.size() > 0) {
            //农业部主推技术
            PerRelation relation = new PerRelation();
            relation.setIndicatortype("农业部主推技术");
            List<PerRelation>  relationList = relationMapper.selectPerRelation(relation);

            PerRelation rel = relationList.get(0);

            for(AchTech pz : list) {

                PerTeamperformance teamperformance = new PerTeamperformance();

                teamperformance.setTeamid(pz.getTeamid());
                teamperformance.setPerformanceyear(Integer.valueOf(currYear));
                teamperformance.setIndicatortype("农业部主推技术");
                teamperformance.setIndicatorid(indicatorsoftware.getIndicatortechid());
                teamperformance.setAchieveid(pz.getTechid());

                String description = getDescription(indicatorsoftware);
                teamperformance.setDescription(description);
                teamperformance.setPoints(indicatorsoftware.getPoints());
                teamperformance.setLevel1id(rel.getLevel1id());
                teamperformance.setLevel1name(rel.getLevel1name());
                teamperformance.setLevel2id(rel.getLevel2id());
                teamperformance.setLevel2name(rel.getLevel2name());
                result =  teamperformanceMapper.insertPerTeamperformance(teamperformance);

            }

        }
        return result;
    }

    private Integer deletePerTeamperformances(PerIndicatortech indicatorsoftware) {
        Integer result = 1;

        Integer curYear = Integer.valueOf(DateUtils.dateTimeNow("yyyy"));
        PerTeamperformance teamperformance = new PerTeamperformance();
        teamperformance.setPerformanceyear(curYear);
        teamperformance.setIndicatortype("农业部主推技术");
        teamperformance.setIndicatorid(indicatorsoftware.getIndicatortechid());
        teamperformance.setStatus("待确认");
        result = teamperformanceMapper.updatePerTeamperformanceDeleted(teamperformance);

        return result;
    }


    @Transactional
    public Integer updatePerIndicatortech(PerIndicatortech tech) {
        Integer result = 1;

        result = indicatortechMapper.updatePerIndicatortech(tech);

        // 评分记录需要修改，因为农业部主推技术变了。

        //更新评分记录
        // 先删除
        deletePerTeamperformances(tech);

        //再增加新的
        addPerTeamperformances(tech);

        return result;
    }

}
