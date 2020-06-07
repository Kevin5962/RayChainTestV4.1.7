package cn.mrray.raybaas.sdk.test;

import cn.mrray.raybaas.common.data.vo.CommonResponse;
import cn.mrray.raybaas.common.util.JsonUtils;
import cn.mrray.raybaas.demo.DemoSpringbootApplication;
import cn.mrray.raybaas.demo.config.ContractProperties;
import cn.mrray.raybaas.demo.service.ContractService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/4/30
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoSpringbootApplication.class)
public class ContractCrossTest   extends BaseTest {

    @Autowired
    private ContractService contractService;

    @Autowired
    private ContractProperties contractDemoProperties;
    @Test
    public void testSingleInitAmount() throws InterruptedException {
        //每个sdk启动50个并发线程来初始化账户资金
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "usert_2");
        map.put("amount", 2222);
        String method = "initAmount";
        String content = JsonUtils.toJson(map);
        CommonResponse reply = contractService.invokeSystemContract(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(), method, content);
        PrintResponse.print(reply);
    }

    @Test
    public void testContractCross()  {
        //每个sdk启动50个并发线程来初始化账户资金
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "A014");
        map.put("userName", "孙014");
        map.put("amount", 10000);
        String method = "initUser";
        String content = JsonUtils.toJson(map);
        CommonResponse commonResponse = contractService.invokeSystemContract("ContractCross", contractDemoProperties.getContractVersion(), method, content);
        PrintResponse.print(commonResponse);
    }
}
