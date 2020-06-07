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
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController("Contracts")
@Api(value = "合约信息", tags = "合约信息")
@Slf4j
public class ContractController {
    @Autowired
    private ContractProperties contractDemoProperties;

    @Autowired
    private ContractService contractService;

    @GetMapping("/initContract")
    @ApiOperation(value = "初始化合约", notes = "初始化合约")
    public CommonResponse initContract() {
        return contractService.initContract(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(), contractDemoProperties.getContractPath());

    }

    @GetMapping("/initAmount")
    @ApiOperation(value = "初始化账户资金", notes = "初始化账户资金")
    public CommonResponse initAmount(@ApiParam("用户名") @RequestParam String username, @ApiParam("初始化金额") @RequestParam int amount) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", username);
        map.put("amount", amount);
        String method = "initAmount";
        String content = JsonUtils.toJson(map);
        return contractService.invoke(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(), method, content);
    }

    @GetMapping("/transfer")
    @ApiOperation(value = "转账", notes = "转账")
    public CommonResponse transfer(@ApiParam("转账账户") @RequestParam String fromUserName, @ApiParam("收款账户") @RequestParam String toUserName, @ApiParam("转账金额") @RequestParam int amount) {
        Map<String, Object> map = new HashMap<>();
        map.put("fromUserId", fromUserName);
        map.put("toUserId", toUserName);
        map.put("transferAmount", amount);
        String content = JsonUtils.toJson(map);
        String method = "transferAmount";
        return contractService.invoke(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(), method, content);
    }

    @GetMapping("/queryAmount")
    @ApiOperation(value = "查询账户余额", notes = "查询账户余额")
    public CommonResponse queryAmount(@ApiParam("用户名") @RequestParam String username) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", username);
        String content = JsonUtils.toJson(map);
        String method = "queryAmount";
        return contractService.invoke(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(), method, content);
    }
}
