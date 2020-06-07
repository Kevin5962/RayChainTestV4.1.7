//package cn.mrray.raybaas.sdk.test;
//
//import cn.mrray.raybaas.common.data.enums.ContractLanguageTypeEnum;
//import cn.mrray.raybaas.common.data.vo.CommonResponse;
//import cn.mrray.raybaas.demo.DemoSpringbootApplication;
//import cn.mrray.raybaas.sdk.client.SdkClient;
//import cn.mrray.raybaas.sdk.client.SdkClientFactory;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = DemoSpringbootApplication.class)
//@Slf4j
//public class CrossCertTest {
//    @Autowired
//    private SdkClientFactory sdkClientFactory;
//    private static final String IDENTITY = "CrossSdkCertContract";
//    private static final String CONTRACT_VERSION = "1.0";
//    private static final String PATH = "G:\\raybaas-demo\\contract-demo-java\\src\\main\\java\\contract\\";
//
//    @Test
//    public void init() throws InterruptedException {
//        SdkClient sdkClient = sdkClientFactory.get("raycross");
//        CommonResponse init = sdkClient.initContractWithResult(IDENTITY, CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                PATH + "CrossSdkCertContract.java");
//
//        Thread.sleep(2000);
//        for (int i = 0; i < 10; i++) {
//            CrossSdkCert crossSdkCert = new CrossSdkCert();
//            crossSdkCert.setAlg("SM2");
//            crossSdkCert.setCertContent(DigestUtils.md5Hex("test"));
//            crossSdkCert.setCrossSdkId(String.valueOf(i));
//            crossSdkCert.setGrpcPort(50053);
//            crossSdkCert.setNode(true);
//            crossSdkCert.setIp("192.168.125.14");
//            sdkClient.invoke(IDENTITY, CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                    "save", JSON.toJSONString(crossSdkCert));
//            Thread.sleep(2000);
//        }
//        CommonResponse invoke = sdkClient.invoke(IDENTITY, CONTRACT_VERSION, ContractLanguageTypeEnum.JAVA,
//                "getAll", "nothing");
//        if (invoke.isSuccess()) {
//            JSONArray array = JSON.parseArray(invoke.getData().toString());
//            if (array != null && array.size() > 0) {
//                List<CrossSdkCert> crossSdkCerts = array.toJavaList(CrossSdkCert.class);
//                for (CrossSdkCert crossSdkCert : crossSdkCerts) {
//                    System.out.println(crossSdkCert.getCrossSdkId() + " " + crossSdkCert.getIp());
//                }
//            }
//        }
//        System.out.println(invoke.getCode());
//        System.out.println(invoke.getMessage());
//        System.out.println(invoke.getData());
//    }
//
//    public static class CrossSdkCert {
//        private String crossSdkId;
//        private String certContent;
//        private String alg;
//        private String ip;
//        private int grpcPort;
//        private boolean node;
//
//        public boolean isNode() {
//            return node;
//        }
//
//        public CrossSdkCert setNode(boolean node) {
//            this.node = node;
//            return this;
//        }
//
//        public String getCrossSdkId() {
//            return crossSdkId;
//        }
//
//        public void setCrossSdkId(String crossSdkId) {
//            this.crossSdkId = crossSdkId;
//        }
//
//        public String getCertContent() {
//            return certContent;
//        }
//
//        public void setCertContent(String certContent) {
//            this.certContent = certContent;
//        }
//
//        public String getAlg() {
//            return alg;
//        }
//
//        public void setAlg(String alg) {
//            this.alg = alg;
//        }
//
//        public String getIp() {
//            return ip;
//        }
//
//        public void setIp(String ip) {
//            this.ip = ip;
//        }
//
//        public int getGrpcPort() {
//            return grpcPort;
//        }
//
//        public void setGrpcPort(int grpcPort) {
//            this.grpcPort = grpcPort;
//        }
//    }
//}
