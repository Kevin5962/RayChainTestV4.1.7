package cn.mrray.raybaas.sdk.test;

import org.junit.After;

/**
 * Description:
 *
 * @author: guanxf
 * @date: 2020/4/16
 */
public class BaseTest {
    @After
    public void after() throws InterruptedException {
        long  millis=6000;
        System.out.println(String.format("============%s Millis后执行完成正在等待=============",millis));
        Thread.sleep(millis);
    }
}
