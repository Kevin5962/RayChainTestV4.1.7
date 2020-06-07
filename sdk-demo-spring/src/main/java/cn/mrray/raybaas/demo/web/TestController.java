package cn.mrray.raybaas.demo.web;

/**
 * Description:
 * Define a  {@code ContractController} implementations {@link InterfaceName}.
 *
 * @author: guanxianfei
 * @date: 2019/10/22
 */

import cn.mrray.raybaas.common.data.vo.CommonResponse;
import cn.mrray.raybaas.common.util.JsonUtils;
import cn.mrray.raybaas.demo.config.ContractProperties;
import cn.mrray.raybaas.demo.service.ContractService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController("test")
@Api(value = "并发测试", tags = "并发测试")
@Slf4j
public class TestController {
    @Autowired
    private  ContractProperties contractDemoProperties;

    @Autowired
    private ContractService contractService;

    /**
     * 每个Server需要初始化的账号个数
     */
    private int handleCount = 1000000;
    /**
     * 每个账号的初始化金额（元）
     */
    private int initAmount = 10000;

    private AtomicInteger currentUserId = new AtomicInteger(0);

    private AtomicInteger currentTransferCount = new AtomicInteger(0);

    @Value("${worker.id}")
    private int workerId;

    @GetMapping("/testJmeterInitAmount")
    @ApiOperation(value = "Jmeter初始化账号", notes = "Jmeter测试")
    public CommonResponse testJmeterInitAmount() {
        int maxAccountNum = (workerId + 1) * handleCount;
        if(currentUserId.addAndGet(0) == 0){
            currentUserId.addAndGet(workerId * handleCount);
        }else if(currentUserId.addAndGet(0) > maxAccountNum){
            log.info("当前server已经初始化了指定个数={}的账号", handleCount);
            System.exit(2);
            return new CommonResponse(200, "当前server已经初始化了指定个数的账号", null);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "userId" + currentUserId.addAndGet(1));
        map.put("amount", initAmount);
        String method = "initAmount";
        String content = JsonUtils.toJson(map);
        CommonResponse commonResponse = contractService.invoke(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(), method, content);
        if(commonResponse.getCode()!= 200){
            log.error("RpcReply msg={}",commonResponse.getMessage());
        }
        return commonResponse;
    }

    @GetMapping("/testJmeterTransfer")
    @ApiOperation(value = "Jmeter转账", notes = "Jmeter测试")
    public CommonResponse testJmeterTransfer() {
        int maxAccountNum = (workerId + 1) * handleCount;
        resetCurrentTransferCount(maxAccountNum);
        Map<String, Object> map = new HashMap<>();
        map.put("fromUserId", "userId" + currentTransferCount.addAndGet(1));
        map.put("toUserId", "userId" + currentTransferCount.addAndGet(1));
        map.put("transferAmount", 1);
        String content = JsonUtils.toJson(map);
        String method = "transferAmount";
        return  contractService.invoke(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(), method, content);
    }

    private synchronized void resetCurrentTransferCount(int maxAccountNum){
        if(currentTransferCount.addAndGet(0) == 0 || currentTransferCount.addAndGet(0) > maxAccountNum){
            currentTransferCount.addAndGet(workerId * handleCount);
//            currentTransferCount.addAndGet(0);
        }
    }
}
