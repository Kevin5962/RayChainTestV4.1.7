package cn.mrray.raybaas.demo.web;

/**
 * Description:
 * Define a  {@code ContractController} implementations {@link InterfaceName}.
 *
 * @author: guanxianfei
 * @date: 2019/10/22
 */

import cn.mrray.raybaas.common.data.vo.CommonResponse;
import cn.mrray.raybaas.common.data.vo.ResponseBuilder;
import cn.mrray.raybaas.sdk.client.SdkClientFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("blocks")
@Api(value = "交易和区块信息", tags = "交易和区块信息")
@Slf4j
public class BlockController {
    @Autowired
    private SdkClientFactory sdkClientFactory;

    @GetMapping("/findBlockByHeight")
    @ApiOperation(value = "通过区块高度查区块", notes = "通过区块高度查区块")
    public CommonResponse findBlockByHeight(String channelName, long height) {
        return ResponseBuilder.success(sdkClientFactory.get(channelName).findBlockByHeight(height));
    }

    @GetMapping("/findBlockByTxHash")
    @ApiOperation(value = "通过交易哈希查区块", notes = "通过交易哈希查区块")
    public CommonResponse findBlockByTxHash(String channelName, String hash) {
        return ResponseBuilder.success(sdkClientFactory.get(channelName).findBlockByTxHash(hash));
    }

    @GetMapping("/findBlockLatest")
    @ApiOperation(value = "查询通道最新区块", notes = "查询通道最新区块")
    public CommonResponse findBlockLatest(String channelName) {
        return ResponseBuilder.success(sdkClientFactory.get(channelName).findBlockLatest());
    }

    @GetMapping("/findBlockList")
    @ApiOperation(value = "分页查询区块信息", notes = "分页查询区块信息")
    public CommonResponse findBlockList(String channelName, int page, int size) {
        return ResponseBuilder.success(sdkClientFactory.get(channelName).findBlockList(page, size));
    }

    @GetMapping("/findBlockHeight")
    @ApiOperation(value = "查区块高度", notes = "查区块高度")
    public CommonResponse findBlockHeight(String channelName) {
        return ResponseBuilder.success(sdkClientFactory.get(channelName).findBlockHeight());
    }


    @GetMapping("/findTransactionByHash")
    @ApiOperation(value = "通过交易哈希查交易", notes = "通过交易哈希查交易")
    public CommonResponse findTransactionByHash(String channelName, String hash) {
        return ResponseBuilder.success(sdkClientFactory.get(channelName).findTransactionByHash(hash));
    }

    @GetMapping("/findTransactionList")
    @ApiOperation(value = "交易列表分页查询", notes = "交易列表分页查询")
    public CommonResponse findTransactionList(String channelName, int page, int size) {
        return ResponseBuilder.success(sdkClientFactory.get(channelName).findTransactionList(page, size));
    }

    @GetMapping("/findTransactionTotal")
    @ApiOperation(value = "交易总数查询", notes = "交易总数查询")
    public CommonResponse findTransactionTotal(String channelName) {
        return ResponseBuilder.success(sdkClientFactory.get(channelName).findTransactionTotal());
    }

}
