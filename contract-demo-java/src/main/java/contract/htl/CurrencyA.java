package contract.htl;

import cn.mrray.raybaas.contract.base.server.service.ContractStub;
import cn.mrray.raybaas.contract.base.vo.ContractVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CurrencyA {
    //@Override
    public void initAmount(ContractStub contractStub, ContractVo contractVo) {
        JSONObject jsonNode = JSON.parseObject(contractVo.getData());
        contractStub.putState(jsonNode.getString("userId"), jsonNode.getString("amount"));
        //return new Response(Response.SUCCESS_STATUS, "initAmountSuccess", null);
    }

    public String queryAmount(ContractStub contractStub, ContractVo contractVo) {
        JSONObject jsonNode = JSON.parseObject(contractVo.getData());
        String userId = jsonNode.getString("userId");
        String amount = contractStub.getState(userId);
        if (amount == null || "".equals(amount.trim())) {
            throw new RuntimeException("用户:" + userId + "不存在");
        }
        return amount;
    }

    public void transferAmount(ContractStub contractStub, ContractVo contractVo) {
        JSONObject jsonNode = JSON.parseObject(contractVo.getData());
        String fromUserId = jsonNode.getString("fromUserId");
        String toUserId = jsonNode.getString("toUserId");
        String transferAmount = jsonNode.getString("transferAmount");
        String fromUserAmount = contractStub.getState(fromUserId);
        String toUserAmount = contractStub.getState(toUserId);
        if (transferAmount == null || "".equals(transferAmount.trim())) {
            throw new RuntimeException("转账资金为空");
        }
        int tAmt = Integer.parseInt(transferAmount);
        if (tAmt <= 0) {
            throw new RuntimeException("转账资金必须大于0");
        }
        if (fromUserAmount == null || "".equals(fromUserAmount.trim())) {
            throw new RuntimeException("转账用户:" + fromUserId + "资金账户不存在");
        }
        if (toUserAmount == null || "".equals(toUserAmount.trim())) {
            throw new RuntimeException("收款用户:" + toUserId + "资金账户不存在");
        }
        int toUserIdAmount = Integer.parseInt(toUserAmount);
        int fromUserIdAmount = Integer.parseInt(fromUserAmount);
        if (fromUserIdAmount < tAmt) {
            throw new RuntimeException("转账用户:" + fromUserId + "当前余额小于转账金额");
        }
        //转账账户减金额
        contractStub.putState(fromUserId, String.valueOf(fromUserIdAmount - tAmt));
        //收款账户加金额
        contractStub.putState(toUserId, String.valueOf(toUserIdAmount + tAmt));
        //return new Response(Response.SUCCESS_STATUS, "transferAmountSuccess", null);
    }

    public void add(ContractStub contractStub, ContractVo contractVo) {
        JSONObject jsonNode = JSON.parseObject(contractVo.getData());
        String userId = jsonNode.getString("fromUserId");
        String amount = jsonNode.getString("transferAmount");
        if (amount == null || "".equals(amount.trim())) {
            throw new RuntimeException("转账资金为空");
        }
        String userAmount = contractStub.getState(userId);
        if (userAmount == null || "".equals(userAmount.trim())) {
            throw new RuntimeException("转账用户:" + userAmount + "资金账户不存在");
        }
        int userIdAmount = Integer.parseInt(userAmount);
        int result = userIdAmount + Integer.parseInt(amount);
        if (result < 0) {
            throw new RuntimeException("计算结果小于0");
        }
        contractStub.putState(userId, String.valueOf(result));
    }
}
