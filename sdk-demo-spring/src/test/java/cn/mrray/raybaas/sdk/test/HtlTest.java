//package cn.mrray.raybaas.sdk.test;
//
//import cn.mrray.raybaas.common.data.enums.ContractLanguageTypeEnum;
//import cn.mrray.raybaas.common.data.vo.CommonResponse;
//import cn.mrray.raybaas.common.encryption.algorithm.SHAUtils;
//import cn.mrray.raybaas.demo.DemoSpringbootApplication;
//import cn.mrray.raybaas.sdk.client.SdkClient;
//import cn.mrray.raybaas.sdk.client.SdkClientFactory;
//import com.alibaba.fastjson.JSON;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DemoSpringbootApplication.class)
//@Slf4j
//public class HtlTest {
//    @Autowired
//    private SdkClientFactory sdkClientFactory;
//
//
//    private SdkClient sdkClient;
//    private SdkClient sdkClient2;
//
//    @PostConstruct
//    public void init() {
//        sdkClient = sdkClientFactory.get("mychannel");
//        sdkClient2 = sdkClientFactory.get("mychannel2");
//    }
//
//    private static final String CURRENCY_IDENTITY = "Currency";
//    private static final String HTL_IDENTITY = "HtlForCurrency";
//    private static final String CONTRACT_VERSION = "1.0";
//    private static final String PATH = "G:\\raybaas-demo\\contract-demo-java\\src\\main\\java\\contract\\htl\\";
//    private static final String TX_ID = DigestUtils.md5Hex("test");
//
//    /**
//     * 初始化4个合约
//     */
//    @Test
//    public void initA() throws InterruptedException {
//        log.info("init contract------------------------------");
//        log.info("init currency A");
//        CommonResponse initA = sdkClient.initContractWithResult(CURRENCY_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                PATH + "CurrencyA.java");
//        Assert.assertTrue(initA.getMessage(), initA.isSuccess());
//        log.info("init currency A success");
//        log.info("init HTL A");
//        CommonResponse initHtlA = sdkClient.initContractWithResult(HTL_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                PATH + "HtlForCurrencyA.java");
//        Assert.assertTrue(initHtlA.getMessage(), initHtlA.isSuccess());
//        log.info("init HTL A success");
//
//        Thread.sleep(2000);
//
//        log.info("init HTL account------------------------------");
//        log.info("init HTL account A");
//        CommonResponse initLockAccountA = sdkClient.invoke(HTL_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "initLockAccount", "content for sign"
//        );
//        Assert.assertTrue(initLockAccountA.getMessage(), initLockAccountA.isSuccess());
//        log.info("init HTL account A success");
//
//        log.info("init user account------------------------------");
//        log.info("init Alice A");
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("userId", "Alice");
//        map1.put("amount", "1000");
//        CommonResponse initAliceA = sdkClient.invoke(CURRENCY_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "initAmount", JSON.toJSONString(map1)
//        );
//        Assert.assertTrue(initAliceA.getMessage(), initAliceA.isSuccess());
//        log.info("init Alice A success");
//
//        log.info("init Bob A");
//        Map<String, Object> map3 = new HashMap<>();
//        map3.put("userId", "Bob");
//        map3.put("amount", "0");
//        CommonResponse initBobA = sdkClient.invoke(CURRENCY_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "initAmount", JSON.toJSONString(map3)
//        );
//        Assert.assertTrue(initBobA.getMessage(), initBobA.isSuccess());
//        log.info("init Bob A success");
//
//        Thread.sleep(2000);
//        testQueryA();
//    }
//
//    @Test
//    public void initB() throws InterruptedException {
//        log.info("init contract------------------------------");
//        log.info("init currency B");
//        CommonResponse initB = sdkClient2.initContractWithResult(CURRENCY_IDENTITY + "B", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                PATH + "CurrencyB.java");
//        Assert.assertTrue(initB.getMessage(), initB.isSuccess());
//        log.info("init currency B success");
//        log.info("init HTL B");
//        CommonResponse initHtlB = sdkClient2.initContractWithResult(HTL_IDENTITY + "B", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                PATH + "HtlForCurrencyB.java");
//        Assert.assertTrue(initHtlB.getMessage(), initHtlB.isSuccess());
//        log.info("init HTL B success");
//
//
//        Thread.sleep(2000);
//
//        log.info("init HTL account------------------------------");
//        log.info("init HTL account B");
//        CommonResponse initLockAccountB = sdkClient2.invoke(HTL_IDENTITY + "B", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "initLockAccount", "content for sign"
//        );
//        Assert.assertTrue(initLockAccountB.getMessage(), initLockAccountB.isSuccess());
//        log.info("init HTL account B success");
//
//        log.info("init user account------------------------------");
//        log.info("init Alice B");
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("key", "Alice");
//        map2.put("value", "0");
//        CommonResponse initAliceB = sdkClient2.invoke(CURRENCY_IDENTITY + "B", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "initAmount", JSON.toJSONString(map2)
//        );
//        Assert.assertTrue(initAliceB.getMessage(), initAliceB.isSuccess());
//        log.info("init Alice B success");
//
//        log.info("init Bob B");
//        Map<String, Object> map4 = new HashMap<>();
//        map4.put("key", "Bob");
//        map4.put("value", "1000");
//        CommonResponse initBobB = sdkClient2.invoke(CURRENCY_IDENTITY + "B", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "initAmount", JSON.toJSONString(map4)
//        );
//        Assert.assertTrue(initBobB.getMessage(), initBobB.isSuccess());
//        log.info("init Bob B success");
//
//
//        Thread.sleep(2000);
//        testQueryB();
//    }
//
//    @Test
//    public void initAccount() {
//        for (int i = 0; i < 10000; i++) {
//            Map<String, Object> map1 = new HashMap<>();
//            map1.put("userId", "Alice" + i);
//            map1.put("amount", "1000");
//            CommonResponse initAliceA = sdkClient.invoke(CURRENCY_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                    "initAmount", JSON.toJSONString(map1)
//            );
//            Map<String, Object> map3 = new HashMap<>();
//            map3.put("userId", "Bob" + i);
//            map3.put("amount", "0");
//            CommonResponse initBobA = sdkClient.invoke(CURRENCY_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                    "initAmount", JSON.toJSONString(map3)
//            );
//            Map<String, Object> map2 = new HashMap<>();
//            map2.put("key", "Alice" + i);
//            map2.put("value", "0");
//            CommonResponse initAliceB = sdkClient2.invoke(CURRENCY_IDENTITY + "B", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                    "initAmount", JSON.toJSONString(map2)
//            );
//            Map<String, Object> map4 = new HashMap<>();
//            map4.put("key", "Bob" + i);
//            map4.put("value", "1000");
//            CommonResponse initBobB = sdkClient2.invoke(CURRENCY_IDENTITY + "B", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                    "initAmount", JSON.toJSONString(map4)
//            );
//        }
//    }
//
//    @Test
//    public void testQueryA() {
//        //queryA("febe394e5433773bd789f2aabb52a705");
//        //queryA("Alice2");
//        queryA("Alice");
//        queryA("Bob");
//    }
//
//    @Test
//    public void testQueryB() {
//        //queryB("febe394e5433773bd789f2aabb52a705");
//        queryB("Alice");
//        queryB("Bob");
//    }
//
//    @Test
//    public void testQueryAll() {
//        testQueryA();
//        testQueryB();
//    }
//
//    @Test
//    public void queryAccount() {
//        List<Integer> l=new ArrayList<>();
//        for (int i = 0; i < 10000; i++) {
//            int aa = queryA("Alice" + i);
//            int ba = queryA("Bob" + i);
//            int ab = queryB("Alice" + i);
//            int bb = queryB("Bob" + i);
//            if ((aa + ba) != 1000) {
//                System.out.println("A i=" + i + " aa=" + aa + " ba=" + ba);
//                //l.add(i);
//            }
//            if ((ab + bb) != 1000) {
//                System.out.println("B i=" + i + " ab=" + ab + " bb=" + bb);
//                //l.add(i);
//            }
//            //System.out.println(i);
//        }
//    }
//
//    /**
//     * 正常流程
//     *
//     * @throws InterruptedException
//     */
//    @Test
//    public void test() throws InterruptedException {
//        String key = "931926355";
//        queryA("Alice");
//        queryA("Bob");
//        queryB("Alice");
//        queryB("Bob");
//        log.info("lock Alice A 100 60s");
//        String hash = SHAUtils.encryptBySha(key, "SHA-256");
//        lockA(TX_ID, "Alice", 100, (long) 60 * 1000, hash);
//        log.info("lock Bob B 10 30s");
//        lockB(TX_ID, "Bob", 10, (long) 30 * 1000, hash);
//        Thread.sleep(2000);
//        log.info("unlock Bob to Alice");
//        unlockB(TX_ID, "Alice", key);
//        log.info("unlock Alice to Bob");
//        unlockA(TX_ID, "Bob", key);
//        Thread.sleep(2000);
//        queryA("Alice");
//        queryA("Bob");
//        queryB("Alice");
//        queryB("Bob");
//    }
//
//    @Test
//    public void fun() {
//        System.out.println(SHAUtils.encryptBySha("931926355", "SHA-256"));
//    }
//
//    /**
//     * 查询lockrecord
//     */
//    @Test
//    public void queryLockRecord() {
//        queryLockRecordA();
//        queryLockRecordB();
//    }
//
//    /**
//     * test rollback
//     */
//    @Test
//    public void testRollBack() throws InterruptedException {
//        String txId = DigestUtils.md5Hex("lwl");
//        String key = "931926355";
//        queryA("Alice");
//        log.info("lock Alice A 100 30s");
//        String hash = SHAUtils.encryptBySha(key, "SHA-256");
//        lockA(txId, "Alice", 100, (long) 30 * 1000, hash);
//        Thread.sleep(2000);
//        queryA("Alice");
//        Map<String, Object> map = new HashMap<>();
//        map.put("txId", txId);
//        CommonResponse rollback = sdkClient.invoke(HTL_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "rollback", JSON.toJSONString(map)
//        );
//        Assert.assertFalse(rollback.isSuccess());
//        log.info(rollback.getMessage());
//        Thread.sleep(30 * 1000);
//        CommonResponse rollback2 = sdkClient.invoke(HTL_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "rollback", JSON.toJSONString(map)
//        );
//        Assert.assertTrue(rollback2.getMessage(), rollback2.isSuccess());
//        Thread.sleep(2000);
//        queryA("Alice");
//    }
//
//    private void lockA(String txId, String name, int amount, long timeout, String hash) {
//        LockRequest lockRequest = new LockRequest();
//        lockRequest.setTxId(txId);
//        lockRequest.setFrom(name);
//        lockRequest.setAmount(amount);
//        lockRequest.setHash(hash);
//        lockRequest.setExpiredTime(timeout);
//        CommonResponse lock = sdkClient.invoke(HTL_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "lock", JSON.toJSONString(lockRequest)
//        );
//        Assert.assertTrue(lock.getMessage(), lock.isSuccess());
//        log.info(lock.getData().toString());
//    }
//
//    private void lockB(String txId, String name, int amount, long timeout, String hash) {
//        LockRequest lockRequest = new LockRequest();
//        lockRequest.setTxId(txId);
//        lockRequest.setFrom(name);
//        lockRequest.setAmount(amount);
//        lockRequest.setHash(hash);
//        lockRequest.setExpiredTime(timeout);
//        CommonResponse lock = sdkClient2.invoke(HTL_IDENTITY + "B", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "lock", JSON.toJSONString(lockRequest)
//        );
//        Assert.assertTrue(lock.getMessage(), lock.isSuccess());
//        log.info(lock.getData().toString());
//    }
//
//    private void unlockA(String txId, String name, String key) {
//        UnlockRequest unlockRequest = new UnlockRequest();
//        unlockRequest.setTo(name);
//        unlockRequest.setKey(key);
//        unlockRequest.setTxId(txId);
//        CommonResponse unlock = sdkClient.invoke(HTL_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "unlock", JSON.toJSONString(unlockRequest)
//        );
//        Assert.assertTrue(unlock.getMessage(), unlock.isSuccess());
//        log.info(unlock.getData().toString());
//    }
//
//    private void unlockB(String txId, String name, String key) {
//        UnlockRequest unlockRequest = new UnlockRequest();
//        unlockRequest.setTo(name);
//        unlockRequest.setKey(key);
//        unlockRequest.setTxId(txId);
//        CommonResponse unlock = sdkClient2.invoke(HTL_IDENTITY + "B", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "unlock", JSON.toJSONString(unlockRequest)
//        );
//        Assert.assertTrue(unlock.getMessage(), unlock.isSuccess());
//        log.info(unlock.getData().toString());
//    }
//
//    private int queryA(String name) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("userId", name);
//        CommonResponse queryAmount = sdkClient.invoke(CURRENCY_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "queryAmount", JSON.toJSONString(map));
//        //log.info(name + " on A " + queryAmount.getData());
//        return Integer.parseInt(JSON.parseObject(queryAmount.getData().toString(), String.class));
//    }
//
//    private int queryB(String name) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("key", name);
//        CommonResponse queryAmount = sdkClient2.invoke(CURRENCY_IDENTITY + "B", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "queryAmount", JSON.toJSONString(map));
//        //log.info(name + " on B " + queryAmount.getData());
//        return Integer.parseInt(JSON.parseObject(queryAmount.getData().toString(), String.class));
//    }
//
//    private void queryLockRecordA() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("txId", TX_ID);
//        CommonResponse status = sdkClient.invoke(HTL_IDENTITY + "A", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "status", JSON.toJSONString(map));
//        Assert.assertNotNull(status.getData());
//        log.info(status.getData().toString());
//    }
//
//    private void queryLockRecordB() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("txId", TX_ID);
//        CommonResponse status = sdkClient2.invoke(HTL_IDENTITY + "B", CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "status", JSON.toJSONString(map));
//        Assert.assertNotNull(status.getData());
//        log.info(status.getData().toString());
//    }
//
//    @Test
//    public void param() {
//        Map<String, Object> map1 = new HashMap<>();
//        map1.put("from", "Alice");
//        map1.put("to", "Bob");
//        map1.put("amount", 100);
//        System.out.println(JSON.toJSONString(map1));
//
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("to", "Alice");
//        map2.put("from", "Bob");
//        map2.put("amount", 10);
//        System.out.println(JSON.toJSONString(map2));
//    }
//
//    public static class BaseParam {
//        private String from;
//        private Integer amount;
//        private String hash;
//        private Long expiredTime;
//        private String key;
//
//        public String getKey() {
//            return key;
//        }
//
//        public BaseParam setKey(String key) {
//            this.key = key;
//            return this;
//        }
//
//        public String getFrom() {
//            return from;
//        }
//
//        public BaseParam setFrom(String from) {
//            this.from = from;
//            return this;
//        }
//
//        public Integer getAmount() {
//            return amount;
//        }
//
//        public BaseParam setAmount(Integer amount) {
//            this.amount = amount;
//            return this;
//        }
//
//        public String getHash() {
//            return hash;
//        }
//
//        public BaseParam setHash(String hash) {
//            this.hash = hash;
//            return this;
//        }
//
//        public Long getExpiredTime() {
//            return expiredTime;
//        }
//
//        public BaseParam setExpiredTime(Long expiredTime) {
//            this.expiredTime = expiredTime;
//            return this;
//        }
//    }
//
//    public static class LockRequest extends BaseParam {
//        private String txId;
//
//        public String getTxId() {
//            return txId;
//        }
//
//        public LockRequest setTxId(String txId) {
//            this.txId = txId;
//            return this;
//        }
//    }
//
//    public static class UnlockRequest {
//        private String txId;
//        private String key;
//        private String to;
//
//        public String getTo() {
//            return to;
//        }
//
//        public UnlockRequest setTo(String to) {
//            this.to = to;
//            return this;
//        }
//
//        public String getTxId() {
//            return txId;
//        }
//
//        public UnlockRequest setTxId(String txId) {
//            this.txId = txId;
//            return this;
//        }
//
//        public String getKey() {
//            return key;
//        }
//
//        public UnlockRequest setKey(String key) {
//            this.key = key;
//            return this;
//        }
//    }
//
//    public static class LockRecord extends BaseParam {
//        private String status;
//
//        public String getStatus() {
//            return status;
//        }
//
//        public LockRecord setStatus(String status) {
//            this.status = status;
//            return this;
//        }
//    }
//}
