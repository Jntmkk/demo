package xyz.example.demo.web3j;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.example.demo.contract.DeviceContract;
import xyz.example.demo.contract.TaskContract;
import xyz.example.demo.models.DeployedContractInfo;
import xyz.example.demo.repository.DeployedContractInfoRepository;
@Slf4j
public class DCContractFactoryBean extends ContractFactoryBean<DeviceContract> {
    private String key;
    @Autowired
    DeployedContractInfoRepository infoRepository;

    public DCContractFactoryBean(String key) {
        this.key = key;
    }

    @Override
    public Class<?> getObjectType() {
        return DeviceContract.class;
    }

    @Override
    protected Object createInstance() throws Exception {
        /**
         * key.length==42 means that  the key is a address ,so invoke load method ,otherwise it is a secrete(length 64) invoke deploy method
         */
        if (key.length() == 42) {
            return DeviceContract.load(key, web3j, credentials, contractGasProvider);
        } else {
            DeviceContract send = DeviceContract.deploy(web3j, credentials, contractGasProvider).send();
            DeployedContractInfo deployedContractInfo = new DeployedContractInfo();
            deployedContractInfo.setManagerPrivateKey(key);
            deployedContractInfo.setManagerAddress("");
            deployedContractInfo.setContractName("DeviceContract");
            deployedContractInfo.setContractAddress(send.getContractAddress());
            infoRepository.save(deployedContractInfo);
            log.info("deploy contact "+ deployedContractInfo.getContractAddress());
            return send;
        }
    }
}
