package com.ruoyi.contract.mapper;

import com.ruoyi.contract.domain.AudContractpay;
import java.util.List;

public interface AudContractpayMapper {

    int insertContractPay(AudContractpay record);

    List<AudContractpay> selectContractPay(AudContractpay record);
}