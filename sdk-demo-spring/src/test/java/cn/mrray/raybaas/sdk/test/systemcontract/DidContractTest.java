//package cn.mrray.raybaas.sdk.test.systemcontract;
//
//import cn.mrray.raybaas.common.data.vo.CommonResponse;
//import cn.mrray.raybaas.common.data.vo.PublicAndPrivateKey;
//import cn.mrray.raybaas.demo.DemoSpringbootApplication;
//import cn.mrray.raybaas.sdk.client.SdkClient;
//import cn.mrray.raybaas.sdk.test.BaseTest;
//import cn.mrray.raybaas.sdk.test.PrintResponse;
//import cn.mrray.raybaas.sdk.test.systemcontract.operator.DidContractOperator;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.UUID;
//
///**
// * Description:
// *
// * @author: guanxianfei
// * @date: 2019/11/13
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DemoSpringbootApplication.class)
//public class DidContractTest  extends BaseTest {
//    @Autowired
//    private SdkClient sdkClient;
//
//    @Test
//    public void testCreate() {
//        /***
//         * DID did:raybaas:18b1c751070da40d2c9dcbc208a4f3af326a9166a968f159c19998d0b0f3d37e
//         * priKey: 00F778F0FB8885F78668AC48273AF48FE014A652D1ED2259D6A5443A424B0FF80C
//         */
//        DidContractOperator didContractOperator = new DidContractOperator(sdkClient);
//        KeyPairText keyPairText = SignatureFactory.getSignature(SignatureType.SM2).generateHashKey();
//        PublicAndPrivateKey publicAndPrivateKey=new PublicAndPrivateKey().setPublicKey(keyPairText.getPublicKey()).setPrivateKey(keyPairText.getPrivateKey()).setType(SignatureType.SM2.name());
//        CommonResponse commonResponse = didContractOperator.create(publicAndPrivateKey);
//        PrintResponse.print(commonResponse);
//        System.out.println("priKey:"+publicAndPrivateKey.getPrivateKey());
//    }
//
//    @Test
//    public void get() {
//        DidContractOperator didContractOperator = new DidContractOperator(sdkClient);
//        String did = "did:raybaas:afe04ebf50fd06fc5e0682cc919a72bf40a170311f396358e8102cf258dc2917";
//        CommonResponse commonResponse = didContractOperator.get(did);
//        PrintResponse.print(commonResponse);
//    }
//
//    @Test
//    public void addPublicKey() {
//        String did = "did:raybaas:18b1c751070da40d2c9dcbc208a4f3af326a9166a968f159c19998d0b0f3d37e";
//        String priKey= "00F778F0FB8885F78668AC48273AF48FE014A652D1ED2259D6A5443A424B0FF80C";
//        String controller="did:raybaas:8c6c525ce05e8650248394124b615a9f4b0d458631e14a2ba904b150a34725b6";
//        DidContractOperator didContractOperator = new DidContractOperator(sdkClient);
//        KeyPairText keyPairText = SignatureFactory.getSignature(SignatureType.SM2).generateHashKey();
//        PublicAndPrivateKey publicAndPrivateKey=new PublicAndPrivateKey().setPublicKey(keyPairText.getPublicKey()).setPrivateKey(keyPairText.getPrivateKey()).setType(SignatureType.SM2.name());
//        String pubKeyId=did.concat("#biometric-1");
//        System.out.println("pubKeyId:"+pubKeyId);
//        System.out.println("priKey:"+publicAndPrivateKey.getPrivateKey());
//        CommonResponse commonResponse = didContractOperator.addPublicKey(pubKeyId,did,priKey,controller,publicAndPrivateKey);
//        PrintResponse.print(commonResponse);
//    }
//
//
//    @Test
//    public void removePublicKey() {
//        DidContractOperator didContractOperator = new DidContractOperator(sdkClient);
//        String did = "did:raybaas:18b1c751070da40d2c9dcbc208a4f3af326a9166a968f159c19998d0b0f3d37e";
//        String priKey= "00F778F0FB8885F78668AC48273AF48FE014A652D1ED2259D6A5443A424B0FF80C";
//        String pubKeyId=did.concat("#biometric-1");
//        CommonResponse commonResponse = didContractOperator.removePublicKey(did, pubKeyId, priKey);
//        PrintResponse.print(commonResponse);
//    }
//
//    @Test
//    public void addService() {
//        String did = "did:raybaas:18b1c751070da40d2c9dcbc208a4f3af326a9166a968f159c19998d0b0f3d37e";
//        String priKey= "00F778F0FB8885F78668AC48273AF48FE014A652D1ED2259D6A5443A424B0FF80C";
//        DidContractOperator didContractOperator = new DidContractOperator(sdkClient);
//        String serviceId=did.concat("#AgentService1");
//        CommonResponse commonResponse = didContractOperator.addService(did, priKey,serviceId);
//        PrintResponse.print(commonResponse);
//    }
//
//    @Test
//    public void removeService() {
//        String did = "did:raybaas:18b1c751070da40d2c9dcbc208a4f3af326a9166a968f159c19998d0b0f3d37e";
//        String priKey= "00F778F0FB8885F78668AC48273AF48FE014A652D1ED2259D6A5443A424B0FF80C";
//        String serviceId=did.concat("#AgentService1");
//        DidContractOperator didContractOperator = new DidContractOperator(sdkClient);
//        CommonResponse commonResponse = didContractOperator.removeService(did, serviceId, priKey);
//        PrintResponse.print(commonResponse);
//    }
//
//    @Test
//    public void parse() {
//        DidContractOperator didContractOperator = new DidContractOperator(sdkClient);
//        String did = "did:raybaas:8c6c525ce05e8650248394124b615a9f4b0d458631e14a2ba904b150a34725b6";
//        String parseDid = "did:raybaas:18b1c751070da40d2c9dcbc208a4f3af326a9166a968f159c19998d0b0f3d37e";
//        CommonResponse commonResponse = didContractOperator.parse(did,parseDid);
//        PrintResponse.print(commonResponse);
//    }
//
//    @Test
//    public void deactivate() {
//        String did = "did:raybaas:4abe5c88b490928ee070ff2804dec30c7f3c307603c6b08bf0fc9e35c3957a8c";
//        String priKey= "574C722A744433A782F77B4143D06A0844C34C8243B18F5A755D9E5EFBD093A1";
//        DidContractOperator didContractOperator = new DidContractOperator(sdkClient);
//        CommonResponse commonResponse = didContractOperator.deactivate(did, priKey);
//        PrintResponse.print(commonResponse);
//    }
//    String getId() {
//        return UUID.randomUUID().toString().replaceAll("-", "");
//    }
//
//}
