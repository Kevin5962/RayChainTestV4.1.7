package cn.mrray.raybaas.demo.config;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * RayBaas配置
 */
@Getter
@Setter
@Component
@AllArgsConstructor
@NoArgsConstructor
public class ContractProperties {
    @Value("${raybaas.contract.demo.identity}")
    private  String contractIdentity; //智能合约标识
    @Value("${raybaas.contract.demo.version}")
    private  String contractVersion; //智能合约版本
    @Value("${raybaas.contract.demo.path}")
    private  String contractPath; //智能合约路径
    @Bean
    public  ContractProperties contractDemoProperties(){
        //if (!new FileSystemResource(contractPath).exists()){
        //     throw  new RuntimeException("合约文件不存在");
        //}
        return  new ContractProperties(contractIdentity,contractVersion,contractPath);
    }
}

