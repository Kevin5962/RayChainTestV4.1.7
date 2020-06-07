/**
package contract.htl;

import cn.mrray.raybaas.common.encryption.algorithm.SHAUtils;
import cn.mrray.raybaas.contract.base.server.service.ContractStub;
import cn.mrray.raybaas.contract.base.vo.ContractVo;
import cn.mrray.raybaas.contract.base.vo.htl.BaseHtlContract;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class HtlForCurrencyA extends BaseHtlContract {
    private static final String CONTRACT_IDENTITY = "CurrencyA";
    private static final String CONTRACT_VERSION = "1.0";

    public void initLockAccount(ContractStub contractStub, ContractVo contractVo) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", DigestUtils.md5Hex(ACCOUNT));
        map.put("amount", "0");
        contractStub.invokeOtherContract(
                CONTRACT_IDENTITY,
                CONTRACT_VERSION,
                "initAmount",
                JSON.toJSONString(map)
        );
    }

    public void lock(ContractStub contractStub, ContractVo contractVo) {
        JSONArray lockRequest = JSON.parseArray(contractVo.getData());
        Map<String, Object> map = new HashMap<>();
        String accountAddress = lockRequest.getString(1);
        Integer amount = lockRequest.getInteger(0);
        String txId = lockRequest.getString(5);
        map.put("fromUserId", accountAddress);
        //map.put("toUserId", DigestUtils.md5Hex(ACCOUNT));
        map.put("transferAmount", -amount);
        contractStub.invokeOtherContract(
                CONTRACT_IDENTITY,
                CONTRACT_VERSION,
                //"transferAmount",
                "add",
                JSON.toJSONString(map)
        );
        LockRecord lockRecord = new LockRecord();
        lockRecord.setFrom(accountAddress);
        lockRecord.setAmount(amount);
        lockRecord.setHash(lockRequest.getString(3));
        lockRecord.setStatus(LOCK);
        lockRecord.setExpiredTime(lockRequest.getLong(4));
        lockRecord.setKey(lockRequest.getString(6));
        contractStub.putState(txId, JSON.toJSONString(lockRecord));
    }

    public void unlock(ContractStub contractStub, ContractVo contractVo) {
        JSONArray unlockRequest = JSON.parseArray(contractVo.getData());
        String key = unlockRequest.getString(3);
        String hash = SHAUtils.encryptBySha(key, "SHA-256");
        String txId = unlockRequest.getString(4);
        LockRecord lockRecord = JSON.parseObject(contractStub.getState(txId), LockRecord.class);
        if (UNLOCK.equals(lockRecord.getStatus())) {
            throw new RuntimeException("already unlocked");
        }
        if (ROLLBACK.equals(lockRecord.getStatus())) {
            throw new RuntimeException("already rollback");
        }
        if (!hash.equals(lockRecord.getHash())) {
            throw new RuntimeException("key wrong");
        }
        Map<String, Object> map = new HashMap<>();
        //map.put("fromUserId", DigestUtils.md5Hex(ACCOUNT));
        //map.put("toUserId", unlockRequest.getString(2));
        map.put("fromUserId", unlockRequest.getString(2));
        map.put("transferAmount", lockRecord.getAmount());
        contractStub.invokeOtherContract(
                CONTRACT_IDENTITY,
                CONTRACT_VERSION,
                //"transferAmount",
                "add",
                JSON.toJSONString(map)
        );
        lockRecord.setStatus(UNLOCK);
        contractStub.putState(txId, JSON.toJSONString(lockRecord));
    }

    public void rollback(ContractStub contractStub, ContractVo contractVo) {
        JSONArray jsonNode = JSON.parseArray(contractVo.getData());
        String txId = jsonNode.getString(3);
        LockRecord lockRecord = JSON.parseObject(contractStub.getState(txId), LockRecord.class);
        if (UNLOCK.equals(lockRecord.getStatus())) {
            throw new RuntimeException("already unlocked");
        }
        if (ROLLBACK.equals(lockRecord.getStatus())) {
            throw new RuntimeException("already rollback");
        }
        if (lockRecord.getExpiredTime() > System.currentTimeMillis()) {
            throw new RuntimeException("not timeout can not rollback");
        }
        Map<String, Object> map = new HashMap<>();
        //map.put("fromUserId", DigestUtils.md5Hex(ACCOUNT));
        //map.put("toUserId", lockRecord.getFrom());
        map.put("fromUserId", lockRecord.getFrom());
        map.put("transferAmount", lockRecord.getAmount());
        contractStub.invokeOtherContract(
                CONTRACT_IDENTITY,
                CONTRACT_VERSION,
                //"transferAmount",
                "add",
                JSON.toJSONString(map)
        );
        lockRecord.setStatus(ROLLBACK);
        contractStub.putState(txId, JSON.toJSONString(lockRecord));
    }

    public String status(ContractStub contractStub, ContractVo contractVo) {
        JSONArray jsonNode = JSON.parseArray(contractVo.getData());
        String txId = jsonNode.getString(3);
        String state = contractStub.getState(txId);
        if (StringUtils.isBlank(state)) {
            //throw new RuntimeException("txId:" + txId + " 不存在");
            return null;
        }
        LockRecord lockRecord = JSON.parseObject(state, LockRecord.class);
        return lockRecord.getStatus();
    }

    public String getKey(ContractStub contractStub, ContractVo contractVo) {
        JSONArray jsonNode = JSON.parseArray(contractVo.getData());
        String txId = jsonNode.getString(3);
        String state = contractStub.getState(txId);
        if (StringUtils.isBlank(state)) {
            throw new RuntimeException("txId:" + txId + " 不存在");
        }
        LockRecord lockRecord = JSON.parseObject(state, LockRecord.class);
        return lockRecord.getKey();
    }
}
 */