package com.ruoyi.performance.service;

import com.ruoyi.achieve.domain.AchProduct;
import com.ruoyi.achieve.mapper.AchProductMapper;
import com.ruoyi.achieve.mapper.AchProductMapper;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.performance.domain.PerIndicatorproduct;
import com.ruoyi.performance.domain.PerIndicatorproduct;
import com.ruoyi.performance.domain.PerRelation;
import com.ruoyi.performance.domain.PerTeamperformance;
import com.ruoyi.performance.mapper.PerIndicatorproductMapper;
import com.ruoyi.performance.mapper.PerRelationMapper;
import com.ruoyi.performance.mapper.PerTeamperformanceMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class IndicatorProductService {

    private static final Logger log = LoggerFactory.getLogger(IndicatorProductService.class);

    @Resource
    PerIndicatorproductMapper indicatorproductMapper;


    @Resource
    AchProductMapper standardMapper;

    @Resource
    PerRelationMapper relationMapper;

    @Resource
    PerTeamperformanceMapper teamperformanceMapper;

    public PerIndicatorproduct selectPerIndicatorproductById(Integer fundid) {

        PerIndicatorproduct fund = indicatorproductMapper.selectPerIndicatorproductById(fundid);

        return fund;
    }


    private String getDescription(PerIndicatorproduct indicatorsoftware) {
        String description = "农机新产品-" + "每项" + "-" + indicatorsoftware.getPoints() + "分";

        return description;
    }


    private Integer addPerTeamperformances(PerIndicatorproduct indicatorsoftware){
        Integer result = 1;

        String currYear = DateUtils.dateTimeNow("yyyy");
        AchProduct record = new AchProduct();
        record.setStatus(AchieveStatus.ZhengChang.getCode());
        record.setProductyear(Integer.valueOf(currYear));

        List<AchProduct> list =   standardMapper.selectAchProduct(record);

        if (list.size() > 0) {
            //农机新产品
            PerRelation relation = new PerRelation();
            relation.setIndicatortype("农机新产品");
            List<PerRelation>  relationList = relationMapper.selectPerRelation(relation);

            PerRelation rel = relationList.get(0);

            for(AchProduct pz : list) {

                PerTeamperformance teamperformance = new PerTeamperformance();

                teamperformance.setTeamid(pz.getTeamid());
                teamperformance.setPerformanceyear(Integer.valueOf(currYear));
                teamperformance.setIndicatortype("农机新产品");
                teamperformance.setIndicatorid(indicatorsoftware.getIndicatorproductid());
                teamperformance.setAchieveid(pz.getProductid());

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

    private Integer deletePerTeamperformances(PerIndicatorproduct indicatorsoftware) {
        Integer result = 1;

        Integer curYear = Integer.valueOf(DateUtils.dateTimeNow("yyyy"));
        PerTeamperformance teamperformance = new PerTeamperformance();
        teamperformance.setPerformanceyear(curYear);
        teamperformance.setIndicatortype("农机新产品");
        teamperformance.setIndicatorid(indicatorsoftware.getIndicatorproductid());
        teamperformance.setStatus("待确认");
        result = teamperformanceMapper.updatePerTeamperformanceDeleted(teamperformance);

        return result;
    }


    @Transactional
    public Integer updatePerIndicatorproduct(PerIndicatorproduct standard) {
        Integer result = 1;

        result = indicatorproductMapper.updatePerIndicatorproduct(standard);

        // 评分记录需要修改，因为农机新产品变了。



        //对去年的旧记录，直接修改分值
//        string upSql = "update PER_TeamPerformance set Description = '" + description
//                + "',BasePoints = " + points
//                + ",Points = " + points
//                + " where IndicatorType = '" + ConstantParameter.CHString_Product + "' "
//                + " and Status = '待确认' ";    //直接修改待确认的
//        DBAccess.DB.ExecuteNonQuery(CommandType.Text, upSql);
        PerTeamperformance teamperformance = new PerTeamperformance();
        teamperformance.setIndicatortype("农机新产品");
        teamperformance.setStatus("待确认");
        teamperformance.setDescription(getDescription(standard));
        teamperformance.setBasepoints(standard.getPoints());
        teamperformance.setPoints(standard.getPoints());

        teamperformanceMapper.updatePerTeamperformanceByStatusAndType(teamperformance);

        //更新评分记录
        // 先删除
        deletePerTeamperformances(standard);

        //再增加新的
        addPerTeamperformances(standard);

        return result;
    }
}
