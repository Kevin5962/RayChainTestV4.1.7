package cn.mrray.raybaas.demo.service;

import cn.mrray.raybaas.common.data.enums.ContractLanguageTypeEnum;
import cn.mrray.raybaas.common.data.vo.CommonResponse;
import cn.mrray.raybaas.sdk.client.SdkClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author guanxf
 */
@Service
public class ContractService {
    @Autowired
    private SdkClient sdkClient;
    /***
     * 上传并初始化智能合约
     * 只能执行一次
     */
    public CommonResponse initContract(String contractIdentity, String version, String contractCodePath) {

        return sdkClient.initContractWithResult(contractIdentity,version, ContractLanguageTypeEnum.JAVA,contractCodePath);
    }
    /***
     * 调用智能合约
     */
    public CommonResponse invoke(String contractIdentity, String version, String methodName, String methodParam) {

        return sdkClient.invoke(contractIdentity, version, ContractLanguageTypeEnum.JAVA, methodName, methodParam);
    }

    public CommonResponse invokeSystemContract(String contractIdentity, String version, String methodName, String methodParam) {
        return sdkClient.invokeSystemContract(contractIdentity, version, methodName, methodParam);
    }
}
