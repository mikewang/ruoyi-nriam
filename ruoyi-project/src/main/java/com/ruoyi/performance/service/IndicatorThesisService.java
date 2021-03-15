package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.domain.AchThesis;
import com.ruoyi.achieve.mapper.AchPrizeMapper;
import com.ruoyi.achieve.mapper.AchThesisMapper;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.PerIndicatorthesis;
import com.ruoyi.performance.domain.PerIndicatorthesis;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.domain.PerTeamperformance;
import com.ruoyi.performance.mapper.PerIndicatorthesisMapper;
import com.ruoyi.performance.mapper.PerRelationMapper;
import com.ruoyi.performance.mapper.PerTeamperformanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorThesisService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorThesisService.class);

    @Resource
    PerIndicatorthesisMapper indicatorthesisMapper;

    @Resource
    AchThesisMapper thesisMapper;

    @Resource
    PerRelationMapper relationMapper;

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;


    public List<PerIndicatorthesis> selectPerIndicatorthesis(PerIndicatorthesis thesis) {

        List<PerIndicatorthesis> list = indicatorthesisMapper.selectPerIndicatorthesis(thesis);

        return list;
    }


    private String getDescription(PerIndicatorthesis indicatorthesis) {
        String description = "论文-" + indicatorthesis.getThesislevel() + "-" + indicatorthesis.getPoints() + "分";

        return description;
    }


    private Integer addPerTeamperformances(PerIndicatorthesis indicatorthesis){
        Integer result = 1;

        String currYear = DateUtils.dateTimeNow("yyyy");
        AchThesis record = new AchThesis();
        record.setStatus(AchieveStatus.ZhengChang.getCode());
        record.setYear(Integer.valueOf(currYear));
        record.setThesislevel(indicatorthesis.getThesislevel());

        List<AchThesis>   list =      thesisMapper.selectAchThesis(record);

        if (list.size() > 0) {
            //论文
            PerRelation relation = new PerRelation();
            relation.setIndicatortype("论文");
            List<PerRelation>  relationList = relationMapper.selectPerRelation(relation);

            PerRelation rel = relationList.get(0);

            for(AchThesis pz : list) {

                PerTeamperformance teamperformance = new PerTeamperformance();

                teamperformance.setTeamid(pz.getTeamid());
                teamperformance.setPerformanceyear(Integer.valueOf(currYear));
                teamperformance.setIndicatortype("论文");
                teamperformance.setIndicatorid(indicatorthesis.getIndicatorthesisid());
                teamperformance.setAchieveid(pz.getThesisid());

                String description = getDescription(indicatorthesis);
                teamperformance.setDescription(description);
                teamperformance.setPoints(indicatorthesis.getPoints());
                teamperformance.setLevel1id(rel.getLevel1id());
                teamperformance.setLevel1name(rel.getLevel1name());
                teamperformance.setLevel2id(rel.getLevel2id());
                teamperformance.setLevel2name(rel.getLevel2name());
                result =  teamperformanceMapper.insertPerTeamperformance(teamperformance);

            }

        }
        return result;
    }

    private Integer deletePerTeamperformances(PerIndicatorthesis indicatorthesis) {
        Integer result = 1;

        Integer curYear = Integer.valueOf(DateUtils.dateTimeNow("yyyy"));
        PerTeamperformance teamperformance = new PerTeamperformance();
        teamperformance.setPerformanceyear(curYear);
        teamperformance.setIndicatortype("论文");
        teamperformance.setIndicatorid(indicatorthesis.getIndicatorthesisid());
        teamperformance.setStatus("待确认");
        result = teamperformanceMapper.updatePerTeamperformanceDeleted(teamperformance);

        return result;
    }


    @Transactional
    public Integer updatePerIndicatorthesis(PerIndicatorthesis thesis) {
        Integer result = 1;

        result = indicatorthesisMapper.updatePerIndicatorthesis(thesis);

        // 评分记录需要修改，因为标准变了。
        //更新评分记录
        // 先删除
        deletePerTeamperformances(thesis);

        //再增加新的
        addPerTeamperformances(thesis);

        return result;
    }

}
