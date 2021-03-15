package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.AchSoftware;
import com.ruoyi.achieve.mapper.AchSoftwareMapper;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.PerIndicatorsoftware;
import com.ruoyi.performance.domain.PerIndicatorsoftware;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.domain.PerTeamperformance;
import com.ruoyi.performance.mapper.PerIndicatorMapper;
import com.ruoyi.performance.mapper.PerIndicatorsoftwareMapper;
import com.ruoyi.performance.mapper.PerRelationMapper;
import com.ruoyi.performance.mapper.PerTeamperformanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorSoftwareService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorSoftwareService.class);

    @Resource
    PerIndicatorsoftwareMapper indicatorsoftwareMapper;

    @Resource
    AchSoftwareMapper standardMapper;

    @Resource
    PerRelationMapper relationMapper;

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;

    public PerIndicatorsoftware selectPerIndicatorsoftwareById(Integer fundid) {

        PerIndicatorsoftware fund = indicatorsoftwareMapper.selectPerIndicatorsoftwareById(fundid);

        return fund;
    }



    private String getDescription(PerIndicatorsoftware indicatorsoftware) {
        String description = "软件著作权-" + "每项" + "-" + indicatorsoftware.getPoints() + "分";

        return description;
    }


    private Integer addPerTeamperformances(PerIndicatorsoftware indicatorsoftware){
        Integer result = 1;

        String currYear = DateUtils.dateTimeNow("yyyy");
        AchSoftware record = new AchSoftware();
        record.setStatus(AchieveStatus.ZhengChang.getCode());
        record.setCertifacatedate(currYear);

        List<AchSoftware> list =   standardMapper.selectAchSoftware(record);

        if (list.size() > 0) {
            //软件著作权
            PerRelation relation = new PerRelation();
            relation.setIndicatortype("软件著作权");
            List<PerRelation>  relationList = relationMapper.selectPerRelation(relation);

            PerRelation rel = relationList.get(0);

            for(AchSoftware pz : list) {

                PerTeamperformance teamperformance = new PerTeamperformance();

                teamperformance.setTeamid(pz.getTeamid());
                teamperformance.setPerformanceyear(Integer.valueOf(currYear));
                teamperformance.setIndicatortype("软件著作权");
                teamperformance.setIndicatorid(indicatorsoftware.getIndicatorsoftwareid());
                teamperformance.setAchieveid(pz.getSoftwareid());

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

    private Integer deletePerTeamperformances(PerIndicatorsoftware indicatorsoftware) {
        Integer result = 1;

        Integer curYear = Integer.valueOf(DateUtils.dateTimeNow("yyyy"));
        PerTeamperformance teamperformance = new PerTeamperformance();
        teamperformance.setPerformanceyear(curYear);
        teamperformance.setIndicatortype("软件著作权");
        teamperformance.setIndicatorid(indicatorsoftware.getIndicatorsoftwareid());
        teamperformance.setStatus("待确认");
        result = teamperformanceMapper.updatePerTeamperformanceDeleted(teamperformance);

        return result;
    }


    @Transactional
    public Integer updatePerIndicatorsoftware(PerIndicatorsoftware standard) {
        Integer result = 1;

        result = indicatorsoftwareMapper.updatePerIndicatorsoftware(standard);

        // 评分记录需要修改，因为软件著作权变了。
        //更新评分记录
        // 先删除
        deletePerTeamperformances(standard);

        //再增加新的
        addPerTeamperformances(standard);

        return result;
    }

}
