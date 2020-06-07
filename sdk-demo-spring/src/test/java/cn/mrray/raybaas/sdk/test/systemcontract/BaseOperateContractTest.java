//package cn.mrray.raybaas.sdk.test.systemcontract;
//
//import cn.mrray.raybaas.common.data.vo.CommonResponse;
//import cn.mrray.raybaas.demo.DemoSpringbootApplication;
//import cn.mrray.raybaas.sdk.client.SdkClient;
//import cn.mrray.raybaas.sdk.systemcontract.BaseOperateContract;
//import cn.mrray.raybaas.sdk.test.BaseTest;
//import cn.mrray.raybaas.sdk.test.PrintResponse;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import java.util.Arrays;
///**
// * Description:
// * @author: guanxianfei
// * @date: 2019/11/13
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DemoSpringbootApplication.class)
//public class BaseOperateContractTest  extends BaseTest {
//    public static final String contractIdentity="t_test";
//    @Autowired
//    private SdkClient sdkClient;
//    @Test
//    public void testCreate(){
//        BaseOperateContract baseOperateContract = new BaseOperateContract(sdkClient);
//        CommonResponse reply = baseOperateContract.create(contractIdentity,"t0001","test");
//        PrintResponse.print(reply);
//    }
//
//    @Test
//    public void testUpdate(){
//        BaseOperateContract baseOperateContract = new BaseOperateContract(sdkClient);
//        CommonResponse reply = baseOperateContract.update(contractIdentity,"t0001","content0004");
//        PrintResponse.print(reply);
//    }
//
//    @Test
//    public void testRetrieve(){
//        BaseOperateContract baseOperateContract = new BaseOperateContract(sdkClient);
//        CommonResponse reply = baseOperateContract.retrieve(contractIdentity,Arrays.asList("t0001"));
//        PrintResponse.print(reply);
//    }
//
//    @Test
//    public void testDelete(){
//        BaseOperateContract baseOperateContract = new BaseOperateContract(sdkClient);
//        CommonResponse reply = baseOperateContract.delete(contractIdentity,"t0001");
//        PrintResponse.print(reply);
//    }
//}
