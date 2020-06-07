package contract;

import cn.mrray.raybaas.contract.base.server.service.ContractStub;
import cn.mrray.raybaas.contract.base.vo.ContractVo;
import cn.mrray.raybaas.contract.base.vo.Response;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 跨链sdk接入的证书管理合约
 */
public class CrossSdkCertContract {
    private static final String NODE_LIST_KEY = DigestUtils.md5Hex("NODE_LIST_KEY");

    public Response save(ContractStub contractStub, ContractVo contractVo) {
        CrossSdkCert crossSdkCert = JSON.parseObject(contractVo.getData(), CrossSdkCert.class);
        String crossSdkId = crossSdkCert.getCrossSdkId();
        contractStub.putState(crossSdkId, contractVo.getData());
        if (crossSdkCert.isNode()) {
            JSONArray array = JSON.parseArray(contractStub.getState(NODE_LIST_KEY));
            if (array == null) {
                array = new JSONArray();
            }
            if (!array.contains(crossSdkId)) {
                array.add(crossSdkId);
                contractStub.putState(NODE_LIST_KEY, array.toJSONString());
            }
        }
        return newSuccessResponse(null, null);
    }

    public Response delete(ContractStub contractStub, ContractVo contractVo) {
        String crossSdkId = contractVo.getData();
        contractStub.delState(crossSdkId);
        return newSuccessResponse(null, null);
    }

    public List<CrossSdkCert> getAll(ContractStub contractStub, ContractVo contractVo) {
        JSONArray array = JSON.parseArray(contractStub.getState(NODE_LIST_KEY));
        List<CrossSdkCert> crossSdkCerts = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            String state = contractStub.getState(array.getString(i));
            if (state != null) {
                crossSdkCerts.add(JSON.parseObject(state, CrossSdkCert.class));
            }
        }
        return crossSdkCerts;
    }

    protected static Response newSuccessResponse(String message, String payload) {
        return new Response(Response.SUCCESS_STATUS, message, payload);
    }

    public static class CrossSdkCert {
        private String crossSdkId;
        private String certContent;
        private String alg;
        private String ip;
        private int grpcPort;
        private boolean node;

        public boolean isNode() {
            return node;
        }

        public CrossSdkCert setNode(boolean node) {
            this.node = node;
            return this;
        }

        public String getCrossSdkId() {
            return crossSdkId;
        }

        public void setCrossSdkId(String crossSdkId) {
            this.crossSdkId = crossSdkId;
        }

        public String getCertContent() {
            return certContent;
        }

        public void setCertContent(String certContent) {
            this.certContent = certContent;
        }

        public String getAlg() {
            return alg;
        }

        public void setAlg(String alg) {
            this.alg = alg;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getGrpcPort() {
            return grpcPort;
        }

        public void setGrpcPort(int grpcPort) {
            this.grpcPort = grpcPort;
        }
    }
}
