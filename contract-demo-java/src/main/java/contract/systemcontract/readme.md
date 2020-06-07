### 系统合约使用：
在META-INF/services/BaseContract下加入系统合约

cn.mrray.raybaas.peer.contract.systemcontract.contract.BaseContract

如下：
```
cn.mrray.raybaas.peer.contract.systemcontract.contract.BaseOperateContract
cn.mrray.raybaas.peer.contract.systemcontract.contract.UserContract
cn.mrray.raybaas.peer.contract.systemcontract.contract.TokenContract
``
cn.mrray.raybaas.peer.contract.systemcontract.contract.demo.ContractImpl
```

### 用户合约转换为系统合约

```java
import cn.mrray.raybaas.peer.contract.systemcontract.contract.BaseContract;
public class  自定义合约 implements BaseContract {
     public boolean isNew() {
            return true;
        }
        public ContractID contractId() {
            return ContractID.newBuilder()
                    .setName( "amt-test") //合约标识
                    .setVersion("1.0") //合约版本
                    .setLanguageType("2") //合约语言类型
                    .setType("1") //系统合约标识
                    .build();
        }
}
```


