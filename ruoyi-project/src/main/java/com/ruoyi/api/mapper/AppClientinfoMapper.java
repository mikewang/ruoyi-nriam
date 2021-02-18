package com.ruoyi.api.mapper;

import com.ruoyi.api.domain.AppClientinfo;
import java.util.List;

public interface AppClientinfoMapper {

    List<AppClientinfo>  selectAppClientinfoByUserid(Integer userid);

}