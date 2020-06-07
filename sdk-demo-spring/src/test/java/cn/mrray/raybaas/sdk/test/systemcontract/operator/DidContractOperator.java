//package cn.mrray.raybaas.sdk.test.systemcontract.operator;
//
//import cn.mrray.raybaas.common.data.vo.CommonResponse;
//import cn.mrray.raybaas.common.data.vo.PublicAndPrivateKey;
//import cn.mrray.raybaas.common.encryption.signature.SignatureFactory;
//import cn.mrray.raybaas.common.encryption.signature.SignatureType;
//import cn.mrray.raybaas.sdk.client.SdkClient;
//import com.alibaba.fastjson.JSON;
//
//import java.util.*;
//
///**
// * Description:
// *
// * @author: guanxf
// * @String: 2020/3/17
// */
//public class DidContractOperator {
//    private SdkClient sdkClient;
//
//    public DidContractOperator(SdkClient sdkClient) {
//        this.sdkClient = sdkClient;
//    }
//
//    private CommonResponse execute(String methodName, String payload) {
//        return sdkClient.invokeSystemContract("did", "1.0.0", methodName, payload);
//    }
//
//    public CommonResponse create(PublicAndPrivateKey publicAndPrivateKey) {
//        PublicKey publicKey=new PublicKey();
//        publicKey.setType(SignatureType.SM2.name());
//        publicKey.setController("");
//        publicKey.setPublicKeyPem(publicAndPrivateKey.getPublicKey());
//        CreateDidDocument createDidDocument=new CreateDidDocument();
//        createDidDocument.setCreated(new Date().toString());
//        createDidDocument.setPublicKey(publicKey);
//        String signature = SignatureFactory.getSignature(SignatureType.SM2).signWithHashKeyContent("1",publicAndPrivateKey.getPrivateKey());
//        createDidDocument.setSignatureValue(signature);
//        return this.execute("create", JSON.toJSONString(createDidDocument));
//    }
//
//    public CommonResponse get(String  did) {
//        return this.execute("get", did);
//    }
//
//    public CommonResponse parse(String  did,String parseDid) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("did", did);
//        map.put("parseDid", parseDid);
//        return this.execute("parse", JSON.toJSONString(map));
//    }
//
//    public CommonResponse  addPublicKey(String pubKeyId, String  did, String priKey, String controller, PublicAndPrivateKey publicAndPrivateKey){
//        Map<String, Object> map = new HashMap<>();
//        map.put("did", did);
//        PublicKey publicKey=new PublicKey();
//        publicKey.setId(pubKeyId);
//        publicKey.setType(SignatureType.SM2.name());
//        publicKey.setController(controller);
//        publicKey.setPublicKeyPem(publicAndPrivateKey.getPublicKey());
//        map.put("publicKey",publicKey);
//        map.put("updated",new Date().toString());
//        String signature = SignatureFactory.getSignature(SignatureType.SM2).signWithKeyPath(JSON.toJSONString(map),priKey);
//        map.put("signatureValue",signature);
//        return this.execute("addPublicKey", JSON.toJSONString(map));
//    }
//
//    public CommonResponse   removePublicKey(String  did,String publicKeyId,String privateKey){
//        Map<String, Object> map = new HashMap<>();
//        map.put("did", did);
//        map.put("publicKeyId", publicKeyId);
//        map.put("updated",new Date().toString());
//        String signature = SignatureFactory.getSignature(SignatureType.SM2).signWithKeyPath( JSON.toJSONString(map), privateKey);
//        map.put("signatureValue",signature);
//        return this.execute("removePublicKey", JSON.toJSONString(map));
//    }
//
//
//    public CommonResponse  addService(String  did,String privateKey,String serviceId) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("did", did);
//        Service service=new Service();
//        service.setId(serviceId);
//        service.setType("AgentService");
//        service.setServiceEndpoint("https://agent.example.com/8377464");
//        service.setAttributes("哈佛大学毕业");
//        map.put("service", service);
//        map.put("updated",new Date().toString());
//        String signature = SignatureFactory.getSignature(SignatureType.SM2).signWithKeyPath(JSON.toJSONString(map), privateKey);
//        map.put("signatureValue",signature);
//        return this.execute("addService", JSON.toJSONString(map));
//    }
//    public CommonResponse removeService(String  did,String serviceId,String privateKey) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("did", did);
//        map.put("serviceId", serviceId);
//        map.put("updated",new Date().toString());
//        String signature = SignatureFactory.getSignature(SignatureType.SM2).signWithKeyPath( JSON.toJSONString(map), privateKey);
//        map.put("signatureValue",signature);
//        return this.execute("removeService", JSON.toJSONString(map));
//    }
//
//    public CommonResponse deactivate(String  did,String privateKey){
//        Map<String, Object> map = new HashMap<>();
//        map.put("did", did);
//        Status status=new Status();
//        status.setId("https://example.edu/status/24");
//        status.setType("CredentialStatusList2017");
//        map.put("status", status);
//        map.put("updated",new Date().toString());
//        String signature = SignatureFactory.getSignature(SignatureType.SM2).signWithKeyPath( JSON.toJSONString(map), privateKey);
//        map.put("signatureValue",signature);
//        return this.execute("deactivate", JSON.toJSONString(map));
//    }
//
//
//    public static class  DeactivateReq{
//        private String did;
//        private  Status status;
//        private String signatureValue;
//        private String updated;
//
//        public String getDid() {
//            return did;
//        }
//
//        public void setDid(String did) {
//            this.did = did;
//        }
//
//        public Status getStatus() {
//            return status;
//        }
//
//        public void setStatus(Status status) {
//            this.status = status;
//        }
//
//        public String getSignatureValue() {
//            return signatureValue;
//        }
//
//        public void setSignatureValue(String signatureValue) {
//            this.signatureValue = signatureValue;
//        }
//
//        public String getUpdated() {
//            return updated;
//        }
//
//        public void setUpdated(String updated) {
//            this.updated = updated;
//        }
//    }
//
//    public static class RemoveServiceReq {
//        private String did;
//        private String serviceId;
//        private String signatureValue;
//        private String updated;
//        public String getDid() {
//            return did;
//        }
//
//        public void setDid(String did) {
//            this.did = did;
//        }
//
//        public String getServiceId() {
//            return serviceId;
//        }
//
//        public void setServiceId(String serviceId) {
//            this.serviceId = serviceId;
//        }
//
//        public String getSignatureValue() {
//            return signatureValue;
//        }
//
//        public void setSignatureValue(String signatureValue) {
//            this.signatureValue = signatureValue;
//        }
//
//        public String getUpdated() {
//            return updated;
//        }
//
//        public void setUpdated(String updated) {
//            this.updated = updated;
//        }
//    }
//
//    public static class AddServiceReq {
//        private String did;
//        private Service service;
//        private String signatureValue;
//        private String updated;
//
//        public String getDid() {
//            return did;
//        }
//
//        public void setDid(String did) {
//            this.did = did;
//        }
//
//        public Service getService() {
//            return service;
//        }
//
//        public void setService(Service service) {
//            this.service = service;
//        }
//
//        public String getSignatureValue() {
//            return signatureValue;
//        }
//
//        public void setSignatureValue(String signatureValue) {
//            this.signatureValue = signatureValue;
//        }
//
//        public String getUpdated() {
//            return updated;
//        }
//
//        public void setUpdated(String updated) {
//            this.updated = updated;
//        }
//    }
//
//
//    public static class RemovePublicKeyReq {
//        private String did;
//        private String publicKeyId;
//        private String signatureValue;
//        private String updated;
//
//        public String getDid() {
//            return did;
//        }
//
//        public void setDid(String did) {
//            this.did = did;
//        }
//
//        public String getPublicKeyId() {
//            return publicKeyId;
//        }
//
//        public void setPublicKeyId(String publicKeyId) {
//            this.publicKeyId = publicKeyId;
//        }
//
//        public String getSignatureValue() {
//            return signatureValue;
//        }
//
//        public void setSignatureValue(String signatureValue) {
//            this.signatureValue = signatureValue;
//        }
//
//        public String getUpdated() {
//            return updated;
//        }
//
//        public void setUpdated(String updated) {
//            this.updated = updated;
//        }
//    }
//
//    public static class AddPublicKeyReq {
//        private String did;
//        private PublicKey publicKey;
//        private String signatureValue;
//        private String updated;
//
//        public String getDid() {
//            return did;
//        }
//
//        public void setDid(String did) {
//            this.did = did;
//        }
//
//        public PublicKey getPublicKey() {
//            return publicKey;
//        }
//
//        public void setPublicKey(PublicKey publicKey) {
//            this.publicKey = publicKey;
//        }
//
//        public String getSignatureValue() {
//            return signatureValue;
//        }
//
//        public void setSignatureValue(String signatureValue) {
//            this.signatureValue = signatureValue;
//        }
//
//        public String getUpdated() {
//            return updated;
//        }
//
//        public void setUpdated(String updated) {
//            this.updated = updated;
//        }
//    }
//
//    public static class ParseDidDocumentReq {
//        private String did;
//        private String parseDid;
//
//        public String getDid() {
//            return did;
//        }
//
//        public void setDid(String did) {
//            this.did = did;
//        }
//
//        public String getParseDid() {
//            return parseDid;
//        }
//
//        public void setParseDid(String parseDid) {
//            this.parseDid = parseDid;
//        }
//    }
//
//    public static class CreateDidDocument {
//        private String created;
//        private PublicKey publicKey;
//        private String signatureValue;
//        public String getCreated() {
//            return created;
//        }
//        public void setCreated(String created) {
//            this.created = created;
//        }
//
//        public PublicKey getPublicKey() {
//            return publicKey;
//        }
//
//        public void setPublicKey(PublicKey publicKey) {
//            this.publicKey = publicKey;
//        }
//
//        public String getSignatureValue() {
//            return signatureValue;
//        }
//
//        public void setSignatureValue(String signatureValue) {
//            this.signatureValue = signatureValue;
//        }
//    }
//
//    public static class DidDocument {
//        private String context;
//        private String id;
//        private List<PublicKey> publicKeys;
//        private String created;
//        private String updated;
//        private List<Authentication> authentications = new ArrayList();
//        private List<Service> services=new ArrayList();
//        private Proof proof;
//        private Status status;
//
//        public DidDocument(String context, List<PublicKey> publicKeys, String  created) {
//            this.context = context;
//            this.publicKeys = publicKeys;
//            this.created = created;
//        }
//
//        public List<PublicKey> getPublicKeys() {
//            return publicKeys;
//        }
//
//        public void setPublicKeys(List<PublicKey> publicKeys) {
//            this.publicKeys = publicKeys;
//        }
//
//        public String getContext() {
//            return context;
//        }
//
//        public void setContext(String context) {
//            this.context = context;
//        }
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getCreated() {
//            return created;
//        }
//
//        public void setCreated(String created) {
//            this.created = created;
//        }
//
//        public String getUpdated() {
//            return updated;
//        }
//
//        public void setUpdated(String updated) {
//            this.updated = updated;
//        }
//
//        public List<Authentication> getAuthentications() {
//            return authentications;
//        }
//
//        public void setAuthentications(List<Authentication> authentications) {
//            this.authentications = authentications;
//        }
//
//        public List<Service> getServices() {
//            return services;
//        }
//
//        public void setServices(List<Service> services) {
//            this.services = services;
//        }
//
//        public Proof getProof() {
//            return proof;
//        }
//
//        public void setProof(Proof proof) {
//            this.proof = proof;
//        }
//
//        public Status getStatus() {
//            return status;
//        }
//
//        public void setStatus(Status status) {
//            this.status = status;
//        }
//    }
//
//    public static class PublicKey {
//        private String id;
//        private String type;
//        private String controller;
//        private String publicKeyPem;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getController() {
//            return controller;
//        }
//
//        public void setController(String controller) {
//            this.controller = controller;
//        }
//
//        public String getPublicKeyPem() {
//            return publicKeyPem;
//        }
//
//        public void setPublicKeyPem(String publicKeyPem) {
//            this.publicKeyPem = publicKeyPem;
//        }
//    }
//
//    public static class Authentication extends PublicKey {
//
//    }
//
//    public static class Service {
//        private String id;
//        private String type;
//        private String serviceEndpoint;
//        private String attributes;
//
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getServiceEndpoint() {
//            return serviceEndpoint;
//        }
//
//        public void setServiceEndpoint(String serviceEndpoint) {
//            this.serviceEndpoint = serviceEndpoint;
//        }
//
//        public String getAttributes() {
//            return attributes;
//        }
//
//        public void setAttributes(String attributes) {
//            this.attributes = attributes;
//        }
//    }
//
//    public static class Proof {
//        private String creator;
//        private String type;
//        private String  created;
//        private String signatureValue;
//
//        public String getCreator() {
//            return creator;
//        }
//
//        public void setCreator(String creator) {
//            this.creator = creator;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//
//        public String getCreated() {
//            return created;
//        }
//
//        public void setCreated(String created) {
//            this.created = created;
//        }
//
//        public String getSignatureValue() {
//            return signatureValue;
//        }
//
//        public void setSignatureValue(String signatureValue) {
//            this.signatureValue = signatureValue;
//        }
//    }
//
//    public static class Status{
//        private  String id;
//        private String type;
//        public String getId() {
//            return id;
//        }
//
//        public void setId(String id) {
//            this.id = id;
//        }
//
//        public String getType() {
//            return type;
//        }
//
//        public void setType(String type) {
//            this.type = type;
//        }
//    }
//
//    public static void main(String[] args) {
//        String  cont="{\"authentications\":[{\"controller\":\"\",\"id\":\"e97a667c2b5b4cb68e45d5db856890de\",\"publicKeyPem\":\"042D2BF416109FF759E4827715A8C11F50D16315A889CA176136A9EDF2B4DD4F107C83F589A5429B0DEB4B585999D8552A310A6EC5D72C037CD84E53C4822FE36A\",\"type\":\"SM2\"}],\"context\":\"http://www.mrray.cn/raybaas/did/v1\",\"created\":\"Thu Mar 19 11:13:31 CST 2020\",\"id\":\"did:raybaas:646bbcf717fbbc7077a319f761184f851378cc9405f000eaf0e524c425dde056\",\"proof\":{\"created\":\"Thu Mar 19 11:13:31 CST 2020\",\"creator\":\"e97a667c2b5b4cb68e45d5db856890de\",\"signatureValue\":\"MEUCIF3naBDcv6owu5b6cr/wActOfbE2Q2wDY5Y0WSnGVyO1AiEArn1lUlUM1RUyxlJ4qNUTfUIgJHNFAU0vQLj+srGekU0=\",\"type\":\"SM2\"},\"publicKeys\":[{\"controller\":\"\",\"id\":\"e97a667c2b5b4cb68e45d5db856890de\",\"publicKeyPem\":\"042D2BF416109FF759E4827715A8C11F50D16315A889CA176136A9EDF2B4DD4F107C83F589A5429B0DEB4B585999D8552A310A6EC5D72C037CD84E53C4822FE36A\",\"type\":\"SM2\"}],\"services\":[]}";
//        DidDocument didDocument = JSON.parseObject(cont, DidDocument.class);
//        System.out.println(didDocument.getId());
//    }
//}
