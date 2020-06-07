package cn.mrray.raybaas.sdk.test;

import cn.mrray.raybaas.common.data.vo.CommonResponse;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/4/16
 */
public class PrintResponse {
    public static void print(CommonResponse reply) {
//        System.out.println("上链状态：" + reply.isSuccess());
        System.out.println("上链code：" + reply.getCode());
        System.out.println("上链Message：" + reply.getMessage());
        System.out.println("上链返回值：" + reply.getData());
//        System.out.println("上链hash：" + reply.getTxHash());
    }
}
