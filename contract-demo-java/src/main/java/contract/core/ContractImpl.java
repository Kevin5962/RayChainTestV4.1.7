package contract.core;

import cn.mrray.raybaas.contract.base.server.service.ContractStub;
import cn.mrray.raybaas.contract.base.vo.ContractVo;
import cn.mrray.raybaas.contract.base.vo.Response;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ContractImpl {

    /**
     *  初始化账户资金
     * @param contractStub
     * @param contractVo
     * @return
     */
    public Response initAmount(ContractStub contractStub, ContractVo contractVo) {
        JSONObject jsonNode = JSON.parseObject(contractVo.getData());
        contractStub.putState(jsonNode.getString("userId"), jsonNode.getString("amount"));
        return newSuccessResponse(null, null);
    }

    /**
     * 查询账户资金
     * @param contractStub
     * @param contractVo
     * @return
     */
    public Response queryAmount(ContractStub contractStub, ContractVo contractVo) {
        JSONObject jsonNode = JSON.parseObject(contractVo.getData());
        String userId = jsonNode.getString("userId");
        return newSuccessResponse(contractStub.getState(userId));
    }

    /**
     * 转移账户资金
     * @param contractStub
     * @param contractVo
     * @return
     */
    public Response transferAmount(ContractStub contractStub, ContractVo contractVo) {
        JSONObject jsonNode = JSON.parseObject(contractVo.getData());
        String fromUserId = jsonNode.getString("fromUserId");
        String toUserId = jsonNode.getString("toUserId");
        String transferAmount = jsonNode.getString("transferAmount");
        String fromUserAmount = contractStub.getState(fromUserId);
        String toUserAmount = contractStub.getState(toUserId);

        if(transferAmount == null || "".equals(transferAmount.trim())){
            return newErrorResponse("转账资金为空");
        }

        int tAmt = Integer.parseInt(transferAmount);
        if(tAmt <= 0){
            return newErrorResponse("转账资金必须大于0");
        }

        if(fromUserAmount == null || "".equals(fromUserAmount.trim())){
            return newErrorResponse("转账用户:" + fromUserId + "资金账户不存在");
        }

        if(toUserAmount == null || "".equals(toUserAmount.trim())){
            return newErrorResponse("收款用户:" + toUserId + "资金账户不存在");
        }
        int toUserIdAmount = Integer.parseInt(toUserAmount);

        int fromUserIdAmount = Integer.parseInt(fromUserAmount);
        if(fromUserIdAmount < tAmt){
            return newErrorResponse("转账用户:" + fromUserId + "当前余额小于转账金额");
        }
        //转账账户减金额
        contractStub.putState(fromUserId, String.valueOf(fromUserIdAmount - tAmt));
        //收款账户加金额
        contractStub.putState(toUserId, String.valueOf(toUserIdAmount + tAmt));

        return newSuccessResponse();
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
