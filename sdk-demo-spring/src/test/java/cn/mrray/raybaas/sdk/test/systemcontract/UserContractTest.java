package cn.mrray.raybaas.sdk.test.systemcontract;

import cn.mrray.raybaas.common.data.vo.CommonResponse;
import cn.mrray.raybaas.demo.DemoSpringbootApplication;
import cn.mrray.raybaas.sdk.client.SdkClient;
import cn.mrray.raybaas.sdk.systemcontract.UserAccountInfoContract;
import cn.mrray.raybaas.sdk.test.BaseTest;
import cn.mrray.raybaas.sdk.test.PrintResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 * @author: guanxianfei
 * @date: 2019/11/14
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoSpringbootApplication.class)
public class UserContractTest  extends BaseTest {
    @Autowired
    private SdkClient sdkClient;
    @Test
    public void testAddUser() {
        UserAccountInfoContract userAccountInfoContract=new UserAccountInfoContract(sdkClient);
        CommonResponse reply = userAccountInfoContract.add("test0001", "test");
        PrintResponse.print(reply);
    }

    @Test
    public void testGetUser() {
        UserAccountInfoContract userAccountInfoContract=new UserAccountInfoContract(sdkClient);
        CommonResponse reply = userAccountInfoContract.get("test0001");
        PrintResponse.print(reply);
    }


}
