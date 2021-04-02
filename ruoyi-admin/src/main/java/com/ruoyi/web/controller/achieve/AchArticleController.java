package com.ruoyi.web.controller.achieve;

import com.ruoyi.achieve.domain.AchArticle;
import com.ruoyi.achieve.service.AchArticleService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.AchieveStatus;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/achieve/article")
public class AchArticleController extends BaseController {
    @Resource
    private AchArticleService articleService;

    @Resource
    private TokenService tokenService;

    @Resource
    private ServerConfig serverConfig;

    private Long getCurrentLoginUserId(){
        LoginUser loginUser=tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId=loginUser.getUser().getUserId();

        userId=87L; // 测试用户 changchun 。
        return userId;
    }

    @PreAuthorize("@ss.hasPermi('achieve:article:list')")
    @GetMapping("/list")
    public TableDataInfo list(AchArticle query) {

        logger.debug("query  is " + query.toString());

        startPage();
        List<AchArticle> list = articleService.selectAchArticleList(query);

        return getDataTable(list);
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:article:list')")
    @GetMapping(value = { "/{articleid}" })
    public AjaxResult getArticle(@PathVariable(value = "articleid", required = false) Integer articleid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(articleid))
        {
            AchArticle p = articleService.selectAchArticleById(articleid);
            if (p != null) {
                ajax.put(AjaxResult.DATA_TAG, articleService.selectAchArticleById(articleid));
            }
            else {
                ajax = AjaxResult.error("articleid：" + articleid.toString() + " 不存在");
            }

        }
        return ajax;
    }

    @PreAuthorize("@ss.hasPermi('achieve:article:list')")
    @GetMapping( "/unique")
    public AjaxResult getIfDuplicate(AchArticle article)
    {
        AjaxResult ajax = AjaxResult.success();

        logger.debug("query parameters getArticlename is " + article.getArticlename());

        Integer articleid = article.getArticleid();

        String articlename = article.getArticlename();

        if (StringUtils.isNotNull(articlename))
        {
            ajax.put(AjaxResult.DATA_TAG, articleService.queryIfDuplicate(article));
        }
        return ajax;
    }


    @PreAuthorize("@ss.hasPermi('achieve:article:list')")
    @Log(title = "成果管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody AchArticle article) {

        Long userId = getCurrentLoginUserId();

        article.setCreateuserid(userId.intValue());

        Integer status = article.getStatus();
        logger.debug("AchArticle .getStatus is " + status.toString());

        if (StringUtils.isNotEmpty(article.getArticlename())
                && UserConstants.NOT_UNIQUE.equals(articleService.queryIfDuplicate(article)))
        {
            return AjaxResult.error("操作'" + article.getArticlename() + "'失败，论文名称已存在");
        }

        article.setStatus(AchieveStatus.DaiQueRen.getCode());
        Integer rows = articleService.insertAchArticle(article);

        if (rows > 0 ) {
            Integer articleid = article.getArticleid();

            AjaxResult ajax = AjaxResult.success();
            ajax.put(AjaxResult.DATA_TAG, articleid);
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }
    }

    @PreAuthorize("@ss.hasPermi('achieve:article:list')")
    @Log(title = "成果管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody  AchArticle article) {

        AchArticle undoArticle = articleService.selectAchArticleById(article.getArticleid());

        if (StringUtils.isNotEmpty(article.getArticlename()) &&undoArticle.getArticlename() != article.getArticlename()) {

            if (UserConstants.NOT_UNIQUE.equals(articleService.queryIfDuplicate(article)))
            {
                return AjaxResult.error("操作'" + article.getArticlename() + "'失败，著作已存在");
            }
        }
        Integer status = article.getStatus();
        logger.debug("article.getStatus is " + status.toString());

        Integer res = articleService.updateAchArticle(article);

        AjaxResult ajax = AjaxResult.success();

        if (res == 1) {
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    @PreAuthorize("@ss.hasPermi('achieve:toconfirm:list')")
    @Log(title = "成果审核", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm")
    public AjaxResult confirm(@Validated @RequestBody  AchArticle article) {

        AchArticle undoArticle = articleService.selectAchArticleById(article.getArticleid());
        logger.debug("undoArticle is " + undoArticle.toString());
        if (undoArticle.getStatus() != AchieveStatus.DaiQueRen.getCode())
        {
            return AjaxResult.error("操作'" + article.getArticlename() + "' 失败，状态不对");
        }

        Integer status = article.getStatus();
        logger.debug("article.getStatus is " + status.toString());

        Integer res = articleService.confirmAchArticle(article);

        AjaxResult ajax = AjaxResult.success();

        if (res == 1) {
            return  ajax;
        }
        else {
            return AjaxResult.error(" 操作失败，请联系管理员");
        }

    }

    /**
     * 根据编号获取审核详细信息。
     */
    @PreAuthorize("@ss.hasPermi('achieve:article:list')")
    @GetMapping(value = { "/confirm/{articleid}/{status}" })
    public AjaxResult getArticleConfirm(@PathVariable(value = "articleid") Integer articleid, @PathVariable(value = "status") Integer status)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(articleid))
        {
            ajax.put(AjaxResult.DATA_TAG, articleService.selectApplyByTypeAndRelatedID(articleid, status));
        }
        return ajax;
    }

    /**
     * 根据编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('achieve:article:list')")
    @DeleteMapping(value = { "/{articleid}" })
    public AjaxResult removeArticle(@PathVariable(value = "articleid", required = false) Integer articleid)
    {
        AjaxResult ajax = AjaxResult.success();

        if (StringUtils.isNotNull(articleid))
        {
            Integer rows = articleService.removeAchArticle(articleid);
            if (rows != 1) {
                ajax = AjaxResult.error("articleid：" + articleid.toString() + " 删除失败");
            }
        }
        return ajax;
    }
}