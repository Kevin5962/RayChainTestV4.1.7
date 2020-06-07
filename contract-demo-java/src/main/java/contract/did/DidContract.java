//package contract.did;
//
//import cn.mrray.raybaas.common.encryption.algorithm.SHAUtils;
//import cn.mrray.raybaas.common.encryption.util.SignHandler;
//import cn.mrray.raybaas.contract.base.server.service.ContractStub;
//import cn.mrray.raybaas.contract.base.vo.ContractVo;
//import cn.mrray.raybaas.contract.base.vo.Response;
//import com.alibaba.fastjson.JSON;
//import org.apache.commons.codec.digest.DigestUtils;
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.*;
//
///**
// * Description:
// *
// * @author: guanxf
// * @date: 2020/3/13
// */
//public class DidContract {
//    private static final String ID_PREFIX = "did:raybaas:";
//    private static final String ID_CONTEXT = "http://www.mrray.cn/raybaas/did/v1";
//    private static final String DID_NOT_EXISTS = "did不存在";
//    private static final String DID_DEACTIVATED = "did已经失效";
//
//    /***
//     * 注册DID
//     * @param contractStub
//     * @param contractVo
//     * @return
//     */
//    public Response create(ContractStub contractStub, ContractVo contractVo) {
//        CreateDidDocument createDidDocument = JSON.parseObject(contractVo.getData(), CreateDidDocument.class);
//        if (createDidDocument.getPublicKey() == null) {
//            return newErrorResponse("公钥不能为空");
//        }
//        DidDocument didDocument = new DidDocument(ID_CONTEXT, Arrays.asList(createDidDocument.getPublicKey()), createDidDocument.getCreated());
//        didDocument.buildDId();
//        didDocument.buildAuthentication(createDidDocument.getPublicKey(), createDidDocument.getSignatureValue());
//        contractStub.putState(didDocument.getId(), JSON.toJSONString(didDocument));
//        return newSuccessResponse(didDocument.getId());
//    }
//
//
//    /***
//     * 查询DID
//     * @param contractStub
//     * @param contractVo
//     * @return
//     */
//    public Response get(ContractStub contractStub, ContractVo contractVo) {
//        String did = contractVo.getData();
//        String documentStr = contractStub.getState(did);
//        if (StringUtils.isEmpty(documentStr)) {
//            return newErrorResponse(DID_NOT_EXISTS);
//        }
//        DidDocument didDocument = JSON.parseObject(documentStr, DidDocument.class);
//        if (did.equals(didDocument.getId())) {
//            return newSuccessResponse(documentStr);
//        }
//        return newSuccessResponse();
//    }
//
//    /***
//     * 解析DID
//     * @param contractStub
//     * @param contractVo
//     * @return
//     */
//    public Response parse(ContractStub contractStub, ContractVo contractVo) {
//        ParseDidDocumentReq parseDidDocumentReq = JSON.parseObject(contractVo.getData(), ParseDidDocumentReq.class);
//        String documentStr = contractStub.getState(parseDidDocumentReq.getDid());
//        if (StringUtils.isEmpty(documentStr)) {
//            return newErrorResponse(DID_NOT_EXISTS);
//        }
//        documentStr = contractStub.getState(parseDidDocumentReq.getParseDid());
//        if (StringUtils.isEmpty(documentStr)) {
//            return newErrorResponse("需要解析的did不存在");
//        }
//        DidDocument didDocument = JSON.parseObject(documentStr, DidDocument.class);
//        didDocument.setPublicKeys(parsePublicKeys(didDocument.getPublicKeys(), parseDidDocumentReq.getDid()));
//        didDocument.setAuthentications(parseAuthentications(didDocument.getAuthentications(), parseDidDocumentReq.getDid()));
//        if (didDocument.getAuthentications().isEmpty()) {
//            didDocument.setServices(new ArrayList());
//        }
//        return newSuccessResponse(JSON.toJSONString(didDocument));
//    }
//
//    public Response addPublicKey(ContractStub contractStub, ContractVo contractVo) {
//        AddPublicKeyReq addPublicKeyReq = JSON.parseObject(contractVo.getData(), AddPublicKeyReq.class);
//        String documentStr = contractStub.getState(addPublicKeyReq.getDid());
//        if (StringUtils.isEmpty(documentStr)) {
//            return newErrorResponse(DID_NOT_EXISTS);
//        }
//        DidDocument didDocument = JSON.parseObject(documentStr, DidDocument.class);
//        if (!verifyStatus(didDocument)) {
//            return newErrorResponse(DID_DEACTIVATED);
//        }
//        Map<String, Object> signatureContentMap = new HashMap<>();
//        signatureContentMap.put("did", addPublicKeyReq.getDid());
//        signatureContentMap.put("publicKey", addPublicKeyReq.getPublicKey());
//        signatureContentMap.put("updated", addPublicKeyReq.getUpdated());
//        PublicKey pubKey = didDocument.getPublicKeys().get(0);
//        String content = JSON.toJSONString(signatureContentMap);
//        if (verify(addPublicKeyReq.getSignatureValue(), pubKey, content)) {
//            didDocument.getPublicKeys().add(addPublicKeyReq.getPublicKey());
//            didDocument.addAuthentications(addPublicKeyReq.getPublicKey());
//            didDocument.setUpdated(addPublicKeyReq.getUpdated());
//            contractStub.putState(didDocument.getId(), JSON.toJSONString(didDocument));
//        }
//        return newSuccessResponse();
//    }
//
//    public Response removePublicKey(ContractStub contractStub, ContractVo contractVo) {
//        RemovePublicKeyReq removePublicKeyReq = JSON.parseObject(contractVo.getData(), RemovePublicKeyReq.class);
//        String documentStr = contractStub.getState(removePublicKeyReq.getDid());
//        if (StringUtils.isEmpty(documentStr)) {
//            return newErrorResponse(DID_NOT_EXISTS);
//        }
//        DidDocument didDocument = JSON.parseObject(documentStr, DidDocument.class);
//        if (!verifyStatus(didDocument)) {
//            return newErrorResponse(DID_DEACTIVATED);
//        }
//        Map<String, Object> signatureContentMap = new HashMap<>();
//        signatureContentMap.put("did", removePublicKeyReq.getDid());
//        signatureContentMap.put("publicKeyId", removePublicKeyReq.getPublicKeyId());
//        signatureContentMap.put("updated", removePublicKeyReq.getUpdated());
//        PublicKey pubKey = didDocument.getPublicKeys().get(0);
//        String content = JSON.toJSONString(signatureContentMap);
//        if (verify(removePublicKeyReq.getSignatureValue(), pubKey, content)) {
//            for (int i = 0; i < didDocument.getPublicKeys().size(); i++) {
//                if (didDocument.getPublicKeys().get(i).getId().equals(removePublicKeyReq.getPublicKeyId())) {
//                    didDocument.getPublicKeys().remove(didDocument.getPublicKeys().get(i));
//                    didDocument.getAuthentications().remove(didDocument.getAuthentications().get(i));
//                }
//            }
//            didDocument.setUpdated(removePublicKeyReq.getUpdated());
//            contractStub.putState(didDocument.getId(), JSON.toJSONString(didDocument));
//        }
//        return newSuccessResponse();
//    }
//
//    public Response addService(ContractStub contractStub, ContractVo contractVo) {
//        AddServiceReq addServiceReq = JSON.parseObject(contractVo.getData(), AddServiceReq.class);
//        String documentStr = contractStub.getState(addServiceReq.getDid());
//        if (StringUtils.isEmpty(documentStr)) {
//            return newErrorResponse(DID_NOT_EXISTS);
//        }
//        DidDocument didDocument = JSON.parseObject(documentStr, DidDocument.class);
//        if (!verifyStatus(didDocument)) {
//            return newErrorResponse(DID_DEACTIVATED);
//        }
//        Map<String, Object> signatureContentMap = new HashMap<>();
//        signatureContentMap.put("did", addServiceReq.getDid());
//        signatureContentMap.put("service", addServiceReq.getService());
//        signatureContentMap.put("updated", addServiceReq.getUpdated());
//        PublicKey pubKey = didDocument.getPublicKeys().get(0);
//        String content = JSON.toJSONString(signatureContentMap);
//        if (verify(addServiceReq.getSignatureValue(), pubKey, content)) {
//            didDocument.getServices().add(addServiceReq.getService());
//            contractStub.putState(didDocument.getId(), JSON.toJSONString(didDocument));
//        }
//        return newSuccessResponse();
//    }
//
//    public Response removeService(ContractStub contractStub, ContractVo contractVo) {
//        RemoveServiceReq removeServiceReq = JSON.parseObject(contractVo.getData(), RemoveServiceReq.class);
//        String documentStr = contractStub.getState(removeServiceReq.getDid());
//        if (StringUtils.isEmpty(documentStr)) {
//            return newErrorResponse(DID_NOT_EXISTS);
//        }
//        DidDocument didDocument = JSON.parseObject(documentStr, DidDocument.class);
//        if (!verifyStatus(didDocument)) {
//            return newErrorResponse(DID_DEACTIVATED);
//        }
//        Map<String, Object> signatureContentMap = new HashMap<>();
//        signatureContentMap.put("did", removeServiceReq.getDid());
//        signatureContentMap.put("serviceId", removeServiceReq.getServiceId());
//        signatureContentMap.put("updated", removeServiceReq.getUpdated());
//        PublicKey pubKey = didDocument.getPublicKeys().get(0);
//        String content = JSON.toJSONString(signatureContentMap);
//        if (verify(removeServiceReq.getSignatureValue(), pubKey, content)) {
//            for (int i = 0; i < didDocument.getServices().size(); i++) {
//                if (didDocument.getServices().get(i).getId().equals(removeServiceReq.getServiceId())) {
//                    didDocument.getServices().remove(didDocument.getServices().get(i));
//                }
//            }
//            didDocument.setUpdated(removeServiceReq.getUpdated());
//            contractStub.putState(didDocument.getId(), JSON.toJSONString(didDocument));
//        }
//        return newSuccessResponse();
//    }
//
//    public Response deactivate(ContractStub contractStub, ContractVo contractVo) {
//        DeactivateReq deactivateReq = JSON.parseObject(contractVo.getData(), DeactivateReq.class);
//        String documentStr = contractStub.getState(deactivateReq.getDid());
//        if (StringUtils.isEmpty(documentStr)) {
//            return newErrorResponse(DID_NOT_EXISTS);
//        }
//        DidDocument didDocument = JSON.parseObject(documentStr, DidDocument.class);
//        if (!verifyStatus(didDocument)) {
//            return newErrorResponse(DID_DEACTIVATED);
//        }
//        Map<String, Object> signatureContentMap = new HashMap<>();
//        signatureContentMap.put("did", deactivateReq.getDid());
//        signatureContentMap.put("status", deactivateReq.getStatus());
//        signatureContentMap.put("updated", deactivateReq.getUpdated());
//        PublicKey pubKey = didDocument.getPublicKeys().get(0);
//        String content = JSON.toJSONString(signatureContentMap);
//        if (verify(deactivateReq.getSignatureValue(), pubKey, content)) {
//            didDocument.setStatus(deactivateReq.getStatus());
//            didDocument.setUpdated(deactivateReq.getUpdated());
//            contractStub.putState(didDocument.getId(), JSON.toJSONString(didDocument));
//        }
//        return newSuccessResponse();
//    }
//
//
//    private String getHashId(String id) {
//        return DigestUtils.md5Hex(id);
//    }
//
//    private boolean verify(String signatureValue, PublicKey publicKey, String content) {
//        return SignHandler.verifyUserSign(signatureValue, content, publicKey.getPublicKeyPem());
//    }
//
//    private boolean verifyStatus(DidDocument didDocument) {
//        return didDocument.getStatus() == null;
//    }
//
//    private List<PublicKey> parsePublicKeys(List<PublicKey> publicKeys, String did) {
//        List<PublicKey> result = new ArrayList();
//        for (PublicKey publicKey : publicKeys) {
//            if (publicKey.getController().equals(did)) {
//                result.add(publicKey);
//            }
//        }
//        return result;
//    }
//
//    private List<Authentication> parseAuthentications(List<Authentication> authentications, String did) {
//        List<Authentication> result = new ArrayList();
//        for (Authentication authentication : authentications) {
//            if (authentication.getController().equals(did)) {
//                result.add(authentication);
//            }
//        }
//        return result;
//    }
//
//    public static class DeactivateReq {
//        private String did;
//        private Status status;
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
//
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
//
//        public String getCreated() {
//            return created;
//        }
//
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
//        private List<Service> services = new ArrayList();
//        private Proof proof;
//        private Status status;
//
//        public DidDocument() {
//        }
//
//        public DidDocument(String context, List<PublicKey> publicKeys, String created) {
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
//
//        public void buildDId() {
//            StringBuffer content = new StringBuffer(publicKeys.get(0).getPublicKeyPem()).append(context).append(created);
//            this.id = ID_PREFIX.concat(SHAUtils.encryptBySha(content.toString(), "SHA-256"));
//        }
//
//        public void buildAuthentication(PublicKey publicKey, String signatureValue) {
//            if (StringUtils.isEmpty(publicKey.getId())) {
//                publicKey.setId(id);
//            }
//            addAuthentications(publicKey);
//            //构建证明
//            Proof proofAttr = new Proof();
//            proofAttr.setCreator(publicKey.getId());
//            proofAttr.setType(publicKey.getType());
//            proofAttr.setCreated(created);
//            proofAttr.setSignatureValue(signatureValue);
//            this.proof = proofAttr;
//        }
//
//        public void addAuthentications(PublicKey publicKey) {
//            //构建验证方式
//            Authentication authentication = new Authentication();
//            authentication.setId(publicKey.getId());
//            authentication.setType(publicKey.getType());
//            authentication.setController(publicKey.getController());
//            authentication.setPublicKeyPem(publicKey.getPublicKeyPem());
//            this.authentications.add(authentication);
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
//        private String created;
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
//    public static class Status {
//        private String id;
//        private String type;
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
//    }
//
//    protected static Response newSuccessResponse(String payload) {
//        return new Response(Response.SUCCESS_STATUS, "操作成功", payload);
//    }
//
//    protected static Response newSuccessResponse() {
//        return newSuccessResponse(null);
//    }
//
//    protected static Response newErrorResponse(String message, String payload) {
//        return new Response(Response.ERROR_STATUS, message, payload);
//    }
//
//    protected static Response newErrorResponse(String message) {
//        return newErrorResponse(message, null);
//    }
//}
