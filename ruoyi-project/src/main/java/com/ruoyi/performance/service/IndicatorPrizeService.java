package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.AchPrize;
import com.ruoyi.achieve.mapper.AchPrizeMapper;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.*;
import com.ruoyi.performance.mapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorPrizeService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorPrizeService.class);

    @Resource
    PerIndicatorprizeMapper indicatorprizeMapper;


    @Resource
    AchPrizeMapper prizeMapper;

    @Resource
    PerRelationMapper relationMapper;

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;

    @Resource
    PerIndicatorMapper indicatorMapper;

    public List<PerIndicatorprize> selectPerIndicatorprize(PerIndicatorprize prize) {

        List<PerIndicatorprize> list = indicatorprizeMapper.selectPerIndicatorprize(prize);

        log.debug("request PerIndicatorprize   is " + list.toString());

        return list;
    }

    public PerIndicatorprize selectPerIndicatorprizeById(Integer prizeid) {

        PerIndicatorprize prize = indicatorprizeMapper.selectPerIndicatorprizeById(prizeid);

        return prize;
    }

    private String getDescription(PerIndicatorprize prize) {
        String description = "";
        String level = "";
        if (prize.getPrizelevel() != null)
        {
            level = prize.getPrizelevellinktext() + "-";
        }
        String type = "";
        if (prize.getPrizetype() != null)
        {
            type = prize.getPrizetypelinktext() + "-";
        }
        String rank = "";
        if (prize.getPrizerank() != null)
        {
            rank = prize.getPrizeranklinktext() + "-";
        }

        description = level + type + rank + prize.getPoints() + "分";

        return description;

    }

    private Integer addPerTeamperformances(PerIndicatorprize prize){
        Integer result = 1;
        AchPrize record = new AchPrize();
        record.setStatus(AchieveStatus.ZhengChang.getCode());
        Integer curYear = Integer.valueOf(DateUtils.dateTimeNow("yyyy"));
        record.setPrizeyear(curYear);
        record.setPrizetype(prize.getPrizetype());
        record.setPrizelevel(prize.getPrizelevel());
        record.setPrizerank(prize.getPrizerank());

        List<AchPrize>   prizeList =      prizeMapper.selectAchPrize(record);

        if (prizeList.size() > 0) {
            //获奖成果
            PerRelation relation = new PerRelation();
            relation.setIndicatortype("获奖成果");
            List<PerRelation>  relationList = relationMapper.selectPerRelation(relation);

            PerRelation rel = relationList.get(0);

            for(AchPrize pz : prizeList) {

                PerTeamperformance teamperformance = new PerTeamperformance();

                teamperformance.setTeamid(pz.getTeamid());
                teamperformance.setPerformanceyear(curYear);
                teamperformance.setIndicatortype("获奖成果");

                teamperformance.setIndicatorid(prize.getIndicatorprizeid());
                teamperformance.setAchieveid(pz.getPrizeid());

                String description = getDescription(prize);
                teamperformance.setDescription(description);
                teamperformance.setPoints(prize.getPoints());
                teamperformance.setLevel1id(rel.getLevel1id());
                teamperformance.setLevel1name(rel.getLevel1name());
                teamperformance.setLevel2id(rel.getLevel2id());
                teamperformance.setLevel2name(rel.getLevel2name());
                result =  teamperformanceMapper.insertPerTeamperformance(teamperformance);

//                iteamper.AddNew(teamID, curYear, ConstantParameter.CHString_Prize, entity.IndicatorPrizeID.ToString(),
//                        achieveID, description, entity.Points, rela);
            }

        }
        return result;
    }

    private Integer deletePerTeamperformances(PerIndicatorprize prize) {
        Integer result = 1;

        Integer curYear = Integer.valueOf(DateUtils.dateTimeNow("yyyy"));
        PerTeamperformance teamperformance = new PerTeamperformance();
        teamperformance.setPerformanceyear(curYear);
        teamperformance.setIndicatortype("获奖成果");
        teamperformance.setIndicatorid(prize.getIndicatorprizeid());
        teamperformance.setStatus("待确认");
        result = teamperformanceMapper.updatePerTeamperformanceDeleted(teamperformance);

        return result;
    }

    @Transactional
    public Integer addPerIndicatorprize(PerIndicatorprize prize) {
        Integer result = 1;

        result = indicatorprizeMapper.insertPerIndicatorprize(prize);

        //级联更新 //增加评分记录

        addPerTeamperformances(prize);

        return result;
    }

    @Transactional
    public Integer updatePerIndicatorprize(PerIndicatorprize prize) {
        Integer result = 1;

        result = indicatorprizeMapper.updatePerIndicatorprize(prize);

        //更新评分记录
        // 先删除
        deletePerTeamperformances(prize);

        //再增加新的
        addPerTeamperformances(prize);

        return result;
    }

    @Transactional
    public Integer deletePerIndicatorprize(PerIndicatorprize prize) {
        Integer result = 1;

        // 先删除 评分记录
        result = deletePerTeamperformances(prize);

        result = indicatorprizeMapper.updatePerIndicatorprizeIfDeleted(prize);

        return result;
    }
}
