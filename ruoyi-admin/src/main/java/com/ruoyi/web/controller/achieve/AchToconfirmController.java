package com.ruoyi.web.controller.achieve;

import com.ruoyi.achieve.domain.AchPatent;
import com.ruoyi.achieve.service.AchPatentService;
import com.ruoyi.achieve.service.AchToconfirmService;
import com.ruoyi.audit.domain.AudApplyAchieve;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.enums.AchieveType;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.project.domain.AudProjectdoc;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/achieve/toconfirm")
public class AchToconfirmController extends BaseController {
    @Resource
    private AchToconfirmService toconfirmService;

    @Resource
    private TokenService tokenService;

    @Resource
    private ServerConfig serverConfig;

    @PreAuthorize("@ss.hasPermi('achieve:toconfirm:list')")
    @GetMapping("/list")
    public TableDataInfo list(AudApplyAchieve query) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();
        logger.debug("query  is " + query.toString());

        HashMap map = new HashMap();

        if (query.getApplytype() == null) {
            List<String> achieveTypeOptions = new ArrayList<>();
            for (HashMap t : getArchieveTypeList()) {
                achieveTypeOptions.add((String) t.get("info"));
            }
            map.put("achieveTypeOptions", achieveTypeOptions);
        }
        else {
            List<String > options = new ArrayList<>();
            options.add(query.getApplytype());
            map.put("achieveTypeOptions", options);
        }
        if (query.getTeamid() != null) {
            map.put("teamid", query.getTeamid());
        }
        if (query.getName() != null) {
            map.put("name", query.getName());
        }

        startPage();
        List<AudApplyAchieve> list = toconfirmService.selectAchieveToConfirmList(map);

        return getDataTable(list);
    }

    private List<HashMap> getArchieveTypeList() {
        List<HashMap> list = new ArrayList<HashMap>();

        HashMap map = new HashMap();
        map.put("info", AchieveType.PATENT.getInfo());
        map.put("link", AchieveType.PATENT.getLink());
        list.add(map);
        map = new HashMap();
        map.put("info", AchieveType.THESIS.getInfo());
        map.put("link", AchieveType.THESIS.getLink());
        list.add(map);
        map = new HashMap();
        map.put("info", AchieveType.ARTICLE.getInfo());
        map.put("link", AchieveType.ARTICLE.getLink());
        list.add(map);
        map = new HashMap();
        map.put("info", AchieveType.PRIZE.getInfo());
        map.put("link", AchieveType.PRIZE.getLink());
        list.add(map);
        map = new HashMap();
        map.put("info", AchieveType.STANDARD.getInfo());
        map.put("link", AchieveType.STANDARD.getLink());
        list.add(map);
        map = new HashMap();
        map.put("info", AchieveType.SOFTWARE.getInfo());
        map.put("link", AchieveType.SOFTWARE.getLink());
        list.add(map);
        map = new HashMap();
        map.put("info", AchieveType.PRODUCT.getInfo());
        map.put("link", AchieveType.PRODUCT.getLink());
        list.add(map);
        map = new HashMap();
        map.put("info", AchieveType.TECH.getInfo());
        map.put("link", AchieveType.TECH.getLink());
        list.add(map);
        map = new HashMap();
        map.put("info", AchieveType.APPRAISAL.getInfo());
        map.put("link", AchieveType.APPRAISAL.getLink());
        list.add(map);

        return list;
    }

    @PreAuthorize("@ss.hasPermi('achieve:toconfirm:list')")
    @GetMapping("/achievetype/list")
    public AjaxResult achievetypelist(AudProjectdoc query) {
        AjaxResult ajax = AjaxResult.success();
        ajax.put(AjaxResult.DATA_TAG, getArchieveTypeList());
        return ajax;
    }

}