//package cn.mrray.raybaas.sdk.test.systemcontract;
//
//import cn.mrray.raybaas.common.data.vo.CommonResponse;
//import cn.mrray.raybaas.common.encryption.signature.SignatureType;
//import cn.mrray.raybaas.demo.DemoSpringbootApplication;
//import cn.mrray.raybaas.sdk.client.SdkClient;
//import cn.mrray.raybaas.sdk.systemcontract.TokenContract;
//import cn.mrray.raybaas.sdk.systemcontract.UserAccountInfoContract;
//import cn.mrray.raybaas.sdk.systemcontract.UserType;
//import cn.mrray.raybaas.sdk.test.BaseTest;
//import cn.mrray.raybaas.sdk.test.PrintResponse;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * Description:
// * @author: guanxianfei
// * @date: 2019/11/14
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DemoSpringbootApplication.class)
//public class TokenContractTest  extends BaseTest {
//   private final String  tokenType="bitCoin";
//   private final SignatureType signatureType=SignatureType.RSA;
//    @Autowired
//    private SdkClient sdkClient;
//
//    @Test
//    public void enable() {
//        TokenContract tokenContract=new TokenContract(sdkClient);
//        CommonResponse reply = tokenContract.enable();
//        PrintResponse.print(reply);
//    }
//    @Test
//    public void testInitRecallUser() {
//        TokenContract tokenContract=new TokenContract(sdkClient);
//        CommonResponse reply = tokenContract.initRecallUser(tokenType);
//        PrintResponse.print(reply);
//    }
//
//    @Test
//    public void totalRecall() {
//        TokenContract tokenContract=new TokenContract(sdkClient);
//        CommonResponse reply = tokenContract.totalRecall(tokenType);
//        System.out.println("账户剩余："+reply.getData());
//        PrintResponse.print(reply);
//    }
//
//    public static final String publishUser="token_user_bit_1000";
//    @Test
//    public void testAddPublishUser() {
//        TokenContract tokenContract=new TokenContract(sdkClient);
//        CommonResponse reply = tokenContract.addUser(publishUser,"123456", UserType.PUBLISH,signatureType);
//        PrintResponse.print(reply);
//    }
//
//
//    @Test
//    public void testInitPublishUser() {
//        TokenContract tokenContract=new TokenContract(sdkClient);
//        CommonResponse reply = tokenContract.initUser(publishUser, tokenType);
//        PrintResponse.print(reply);
//    }
//    @Test
//    public void testQueryPublishUser() {
//        TokenContract tokenContract=new TokenContract(sdkClient);
//        CommonResponse reply = tokenContract.balanceOf(publishUser, tokenType);
//        System.out.println("账户剩余："+reply.getData());
//        PrintResponse.print(reply);
//    }
//    @Test
//    public void testPublish() {
//        UserAccountInfoContract userAccountInfoContract=new UserAccountInfoContract(sdkClient);
//         String privateKeyContent=userAccountInfoContract.getPriKey(publishUser,"123456").getData().toString();
//        TokenContract tokenContract=new TokenContract(sdkClient,privateKeyContent);
//        CommonResponse reply = tokenContract.publish(publishUser,tokenType, 1000000);
//        PrintResponse.print(reply);
//    }
//
//    public static final String  normalUser="token_user_bit_N10001";
//    @Test
//    public void testAddNormalUser() {
//        TokenContract tokenContract=new TokenContract(sdkClient);
//        CommonResponse reply = tokenContract.addUser(normalUser,"123456", UserType.NORMAL,signatureType);
//        PrintResponse.print(reply);
//    }
//    @Test
//    public void testInitNormalUser() {
//        TokenContract tokenContract=new TokenContract(sdkClient);
//        CommonResponse reply = tokenContract.initUser(normalUser, tokenType);
//        PrintResponse.print(reply);
//    }
//    @Test
//    public void testQueryNormalUser() {
//        TokenContract tokenContract=new TokenContract(sdkClient);
//        CommonResponse reply = tokenContract.balanceOf(normalUser, tokenType);
//        System.out.println("Normal账户剩余："+reply.getData());
//        PrintResponse.print(reply);
//    }
//    @Test
//    public void testTransferUser() {
//        UserAccountInfoContract userAccountInfoContract=new UserAccountInfoContract(sdkClient);
//        String privateKeyContent=userAccountInfoContract.getPriKey(publishUser,"123456").getData().toString();
//        TokenContract tokenContract=new TokenContract(sdkClient,privateKeyContent);
//        CommonResponse reply = tokenContract.transfer(publishUser,normalUser, 20,tokenType);
//        PrintResponse.print(reply);
//    }
//    @Test
//    public void testRecallUser() {
//        TokenContract tokenContract=new TokenContract(sdkClient);
//        CommonResponse reply = tokenContract.recall(normalUser, 1000,tokenType);
//        PrintResponse.print(reply);
//    }
//}
