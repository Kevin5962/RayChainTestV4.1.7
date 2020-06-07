package contract.core;

import cn.mrray.raybaas.contract.base.server.service.ContractStub;
import cn.mrray.raybaas.contract.base.vo.ContractVo;
import cn.mrray.raybaas.contract.base.vo.Response;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

public class ContractCross {

    /**
     * 初始化账户资金
     *
     * @param contractStub
     * @param contractVo
     * @return
     */
    public Response initUser(ContractStub contractStub, ContractVo contractVo) {
        JSONObject jsonNode = JSON.parseObject(contractVo.getData());
        Response response = (Response) contractStub.invokeOtherContract("amt-test", "1.0", "queryAmount", contractVo.getData());
        System.out.println("code=" + response.getCode());
        System.out.println("message=" + response.getMessage());
        System.out.println("payload=" + response.getPayload());
        if (StringUtils.isBlank(response.getPayload())) {
            contractStub.invokeOtherContract("amt-test", "1.0", "initAmount", contractVo.getData());
            contractStub.putState(jsonNode.getString("userId"), jsonNode.getString("userName"));
            contractStub.invokeOtherContract("chain1", "1.0", "initAmount", contractVo.getData());
        }
        return newSuccessResponse(null, null);
    }

    protected static Response newSuccessResponse(String message, String payload) {
        return new Response(Response.SUCCESS_STATUS, message, payload);
    }

    protected static Response newSuccessResponse() {
        return newSuccessResponse(null, null);
    }

    protected static Response newSuccessResponse(String payload) {
        return newSuccessResponse(null, payload);
    }

    protected static Response newErrorResponse(String message, String payload) {
        return new Response(Response.ERROR_STATUS, message, payload);
    }

    protected static Response newErrorResponse(String message) {
        return newErrorResponse(message, null);
    }

}
