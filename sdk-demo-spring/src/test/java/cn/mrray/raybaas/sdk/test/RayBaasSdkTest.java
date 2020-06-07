package cn.mrray.raybaas.sdk.test;

import cn.mrray.raybaas.common.data.vo.CommonResponse;
import cn.mrray.raybaas.common.lang.BaseThreadFactory;
import cn.mrray.raybaas.common.util.JsonUtils;
import cn.mrray.raybaas.demo.DemoSpringbootApplication;
import cn.mrray.raybaas.demo.config.ContractProperties;
import cn.mrray.raybaas.demo.service.ContractService;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoSpringbootApplication.class)
public class RayBaasSdkTest {

    @Autowired
    private ContractProperties contractDemoProperties;

    @Autowired
    private ContractService contractService;

    /**
     * 测试客户端编号
     */
    private int clientIndex = 1;

    /**
     * 每个测试客户端需要初始化的账号个数
     */
    private int eachClientHandleCount = 2500000;

    /**
     * 每个账号的初始化金额（元）
     */
    private int initAmount = 10000;

    private AtomicInteger failCount = new AtomicInteger(0);

    private ExecutorService executorService = new ThreadPoolExecutor(100, 100, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(5000),
            new BaseThreadFactory("send-transaction", true));
    /**
     * 初始化合约
     */
    @Test
    public void testInitContract() {
        CommonResponse rpcReply = contractService.initContract(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(), contractDemoProperties.getContractPath());
        System.out.println(new Gson().toJson(rpcReply));
    }

    /**
     * 初始化账户资金
     */
    @Test
    public void testInitAmount() {
        //每个sdk启动50个并发线程来初始化账户资金
        for (int i = 0; i < 100; i++) {
            executorService.execute(new UserAmountThread(i, "initAmount"));
        }
        while (true) {

        }
    }

    /**
     * 初始化账户资金
     */
    @Test
    public void testSingleInitAmount() throws InterruptedException {
        //每个sdk启动50个并发线程来初始化账户资金
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "user_split111");
        map.put("amount", 111);
        String method = "initAmount";
        String content = JsonUtils.toJson(map);
        CommonResponse commonResponse = contractService.invoke(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(), method, content);
        System.out.println(JSON.toJSONString(commonResponse));
        Thread.sleep(3000);
    }

    /**
     * 跨合约测试
     */
    @Test
    public void testCrossContract() throws InterruptedException {
        //每个sdk启动50个并发线程来初始化账户资金
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "sunqi");
        map.put("userName", "孙七");
        map.put("amount", 777);
        String method = "initUser";
        String content = JsonUtils.toJson(map);
        CommonResponse commonResponse = contractService.invoke(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(), method, content);
        System.out.println(JSON.toJSONString(commonResponse));
        Thread.sleep(3000);
    }

    /**
     * 查询账户资金
     */
    @Test
    public void testQueryAmount() {
        //每个sdk启动50个并发线程来查询账户资金
        for (int i = 0; i < 50; i++) {
            executorService.execute(new UserAmountThread(i, "queryAmount"));
        }
        while (true) {

        }
    }

    /**
     * 账户间转账
     */
    @Test
    public void testTransferAmount() {
        //每个sdk启动50个并发线程来查询账户资金
        for (int i = 0; i < 50; i++) {
            executorService.execute(new TransferAmountThread(i));
        }
        while (true) {

        }
    }

    /**
     * 用户资金参数
     *
     * @param userId
     * @return
     */
    private String buildUserAmount(int userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", "user_" + userId);
        map.put("amount", initAmount);
        return JsonUtils.toJson(map);
    }

    /**
     * 账户间转账参数
     *
     * @param fromUserId
     * @param toUserId
     * @param transferAmount
     * @return
     */
    private String buildTransferAmount(int fromUserId, int toUserId, int transferAmount) {
        Map<String, Object> map = new HashMap<>();
        map.put("fromUserId", "user_" + fromUserId);
        map.put("toUserId", "user_" + toUserId);
        map.put("transferAmount", transferAmount);
        return JsonUtils.toJson(map);
    }

    public class UserAmountThread implements Runnable {

        /**
         * 每个线程初始化50000个账号
         */
        private static final int count = 50000;

        private int i;

        private String method;

        public UserAmountThread(int i, String method) {
            this.i = i;
            this.method = method;
        }

        @Override
        public void run() {
            //每个线程处理的起始账号id
            int beginUserId = (i * count) + (clientIndex * eachClientHandleCount);
            //每个线程处理的结束账号id
            int endUserId = ((i + 1) * count) + (clientIndex * eachClientHandleCount);
            for (int j = beginUserId; j < endUserId; j++) {
                String content = buildUserAmount(j);
                CommonResponse commonResponse = contractService.invoke(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(), method, content);
                if(commonResponse.getCode() != 200){
                    System.out.println(commonResponse.getMessage());
                }else{
                    switch (method) {
                        case "queryAmount":
                            Map   amt = (Map) commonResponse.getData();
                            if(!StringUtils.isNumeric(amt.get("payload").toString())){
                                System.out.println("用户=user_" + j + "，当前账户资金初始化失败,目前失败总数=" + failCount.incrementAndGet());
                            }
                            break;
                        case "initAmount":
//                            System.out.println("用户=user_" + j + "，账户资金初始化请求提交完成，对应transactionHash=" + commonResponse.getTransactionHash());
                            break;
                        default:
                            System.out.println("无效的方法");
                    }
                }
            }
        }
    }

    public class TransferAmountThread implements Runnable {

        /**
         * 每个线程处理50000个账号间的不重复转账
         * 类似：用户0给用户1转，用户2给用户3转，总共转25000次
         */
        private static final int count = 50000;

        /**
         * 每次转账金额都为100元
         */
        private static final int tAmt = 100;


        private int i;

        public TransferAmountThread(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            //每个线程处理的起始账号id
            int beginUserId = (i * count) + (clientIndex * eachClientHandleCount);
            //每个线程处理的结束账号id
            int endUserId = ((i + 1) * count) + (clientIndex * eachClientHandleCount);
            for (int j = beginUserId; j < endUserId; j++) {
                int fromUserId = j;
                int toUserId = ++j;
                String content = buildTransferAmount(fromUserId, toUserId, tAmt);
                CommonResponse commonResponse = contractService.invoke(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(),"transferAmount", content);
//                commonResponse commonResponse = contractService.invokeOs(contractDemoProperties.getContractIdentity(), contractDemoProperties.getContractVersion(),"transferAmount", content);
//                System.out.println("用户=user_" + j + "，账户资金初始化请求提交完成，对应transactionHash=" + commonResponse.getTransactionHash());
            }
        }
    }
}
