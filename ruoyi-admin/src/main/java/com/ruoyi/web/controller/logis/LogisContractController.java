package com.ruoyi.web.controller.logis;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.logis.domain.LogisContract;
import com.ruoyi.logis.service.LogisContractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/logis/contract")
public class LogisContractController extends BaseController
{
    @Resource
    private LogisContractService logisContractService;

    @PreAuthorize("@ss.hasPermi('logis:contract:list')")
    @GetMapping("/list")
    public TableDataInfo list(LogisContract contract)
    {
        startPage();
        List<LogisContract> list = logisContractService.selectLogisContractList(contract);
        return getDataTable(list);
    }


}
