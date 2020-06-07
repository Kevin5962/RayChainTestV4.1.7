package cn.mrray.raybaas.demo.config;

import cn.mrray.raybaas.sdk.client.SdkClient;
import cn.mrray.raybaas.sdk.client.SdkClientFactory;
import cn.mrray.raybaas.sdk.client.support.SdkClientImpl;
import cn.mrray.raybaas.sdk.grpc.start.client.Channel;
import cn.mrray.raybaas.sdk.model.Peer;
import cn.mrray.raybaas.sdk.model.SdkOrder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author: guanxianfei
 * @date: 2019/10/24
 */

@Component
public class RayBaasConfig {
    @Resource
    private RayBaasProperties rayBaasProperties;

    public Channel channel(RayBaasProperties.ChannelAttribute channelAttribute) {
        List<Peer> peers = new ArrayList<>();
        String allPeers = channelAttribute.getPeer().getHostAndPort();
        String[] ips = allPeers.split(";");
        for (String ip : ips) {
            String[] hostAndPort = ip.split(":");
            peers.add(new cn.mrray.raybaas.sdk.model.Peer(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
        }
        List<SdkOrder> orders = new ArrayList<>();
        String allOrders = channelAttribute.getOrder().getHostAndPort();
        String[] orderIps = allOrders.split(";");
        for (String ip : orderIps) {
            String[] hostAndPort = ip.split(":");
            orders.add(new SdkOrder(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
        }
        String signAlgorithm =  "SM2";
        //使用单项认证,或者开启ssl单项认证
        Channel channel;
        if (rayBaasProperties.getSsl() == null) {
            channel = new Channel(peers, orders, channelAttribute.getName(), channelAttribute.getConsensus(), rayBaasProperties.getApp().getPrivateKeyPath(), rayBaasProperties.getApp().getAppId(), signAlgorithm, false);
        } else {
            if (rayBaasProperties.getSsl().isSslMutual()) {
                //使用双向加密验证
                return new Channel(peers, orders, channelAttribute.getName(), channelAttribute.getConsensus(), rayBaasProperties.getApp().getPrivateKeyPath(), rayBaasProperties.getSsl().getSslCertFilePath(), rayBaasProperties.getSsl().getSslPrivateKeyPath(), rayBaasProperties.getSsl().getSslTrustCertFilePath(), rayBaasProperties.getApp().getAppId(), signAlgorithm, rayBaasProperties.getSsl().isSslMutual());
            } else {
                channel = new Channel(peers, orders, channelAttribute.getName(), channelAttribute.getConsensus(), rayBaasProperties.getApp().getPrivateKeyPath(), rayBaasProperties.getApp().getAppId(), signAlgorithm, true);
            }
        }
        return channel;
    }

    @Bean
    public SdkClientFactory sdkClientFactory() {
        SdkClientFactory clientFactory = new SdkClientFactory();
        for (RayBaasProperties.ChannelAttribute channelProperties : rayBaasProperties.getChannels().getChannel()) {
            Channel channel = channel(channelProperties);
            clientFactory.put(channelProperties.getName(), new SdkClientImpl(channel));
        }
        return clientFactory;
    }

    @Bean
    public SdkClient sdkClient() {
        Channel channel = channel(rayBaasProperties.getChannels().getChannel().get(0));
        return new SdkClientImpl(channel);
    }
}
