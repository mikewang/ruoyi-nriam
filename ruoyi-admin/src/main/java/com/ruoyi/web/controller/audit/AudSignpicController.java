package com.ruoyi.web.controller.audit;

import com.ruoyi.audit.domain.AudSignpic;
import com.ruoyi.audit.service.AudSignpicService;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.ServerConfig;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.sheet.domain.AudBudgetpay;
import io.swagger.models.auth.In;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/audit/signpic")
public class AudSignpicController extends BaseController {
    @Resource
    private AudSignpicService audSignpicService;


    @Resource
    private TokenService tokenService;

    @Resource
    private ServerConfig serverConfig;

    @PreAuthorize("@ss.hasPermi('audit:signpic:list')")
    @GetMapping("/list")
    public TableDataInfo list(AudSignpic audSignpic) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        Long userId = loginUser.getUser().getUserId();

        startPage();
        List<AudSignpic> list = audSignpicService.selectSignpicList(audSignpic);

        // 本地资源路径

        String fileDirPath = serverConfig.getUrl() + Constants.RESOURCE_PREFIX + "/upload" +  "/signpic/";

        for (AudSignpic s : list) {
            if (s.getSignpicName() != null && s.getSignpicName().isEmpty() == false ) {
                String checkFilePath = RuoYiConfig.getUploadPath() + "/signpic/" + s.getSignpicName();
                logger.debug("checkFilePath is " + s.getSignpicName());
                File desc = new File(checkFilePath);
                if (desc.exists() == true) {
                    String filePath = fileDirPath + s.getSignpicName();
                    s.setSignpicName(filePath);
                    logger.debug("url is " + filePath);
                }
                else {
                    s.setSignpicName("");
                }
            }
            else {
                s.setSignpicName("");
            }
        }
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('audit:signpic:list')")
    @GetMapping(value = { "/{userId}" })
    public AjaxResult getSignpic(@PathVariable(value = "userId", required = true) Integer userId) {

        AudSignpic s = audSignpicService.selectSignpicByUserId(userId);

        // 本地资源路径

        String fileDirPath = serverConfig.getUrl() + Constants.RESOURCE_PREFIX + "/upload" +  "/signpic/";

        if (s.getSignpicName() != null && s.getSignpicName().isEmpty() == false ) {
            String checkFilePath = RuoYiConfig.getUploadPath() + "/signpic/" + s.getSignpicName();
            logger.debug("checkFilePath is " + s.getSignpicName());
            File desc = new File(checkFilePath);
            if (desc.exists() == true) {
                String filePath = fileDirPath + s.getSignpicName();
                s.setSignpicName(filePath);
                logger.debug("url is " + filePath);
            }
            else {
                s.setSignpicName("");
            }
        }
        else {
            s.setSignpicName("");
        }

        AjaxResult ajax = AjaxResult.success();

        if (s.getSignpicName().equals(""))
        {
            ajax = AjaxResult.error("签名：" + s.getRealName() + " 不存在");
        }
        else {
            ajax.put(AjaxResult.DATA_TAG, s);
        }
        return ajax;
    }



    /**
     * 签名图片上传
     */
    @PreAuthorize("@ss.hasPermi('audit:signpic:add')")
    @Log(title = "签名图片上传", businessType = BusinessType.UPDATE)
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file, @RequestParam("userId") Long userId) throws IOException {
        logger.debug("file getOriginalFilename is " + file.getOriginalFilename());
        try {
            LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
            String originalFilename = file.getOriginalFilename();
            // 上传文件路径
            String filePath = RuoYiConfig.getUploadPath() + "/signpic";
            // 上传并返回新文件名称
            String pathFileName = FileUploadUtils.upload(filePath, file);
            String[] split_file = pathFileName.split("/");
            String fileName = split_file[(split_file.length - 1)];

            AudSignpic signpic = new AudSignpic();
            signpic.setUserId(userId);
            signpic.setSignpicName(fileName);
            Integer result = audSignpicService.mergeSignpic(signpic);
            if (result == 1) {
                AjaxResult ajax = AjaxResult.success(pathFileName);
                String url = serverConfig.getUrl() + pathFileName;
                ajax.put("name", fileName);
                ajax.put("url", url);

                return ajax;
            } else {
                return AjaxResult.error("上传文件成功，但保存数据库异常，请联系管理员");
            }

        } catch (Exception e) {
            return AjaxResult.error(e.getMessage() + " 上传文件异常，请联系管理员");
        }
    }


    /**
     * 消息已读设置 更新操作
     */
    @PreAuthorize("@ss.hasPermi('audit:signpic:remove')")
    @Log(title = "签名图片删除", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    public AjaxResult remove(@PathVariable List<Long> userIds) {
        logger.debug("签名图片删除 userIds are " + userIds);

        return toAjax(audSignpicService.removeSignpicByUserIds(userIds));
    }

}