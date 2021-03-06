package xyz.example.demo.web3j;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.example.demo.bean.DeployedContractAddress;
import xyz.example.demo.bean.DeployedContracts;
import xyz.example.demo.contract.UserContract;
import xyz.example.demo.models.DeployedContractInfo;
import xyz.example.demo.repository.DeployedContractInfoRepository;
@Slf4j
public class UCContractFactoryBean extends ContractFactoryBean<UserContract> {
    @Autowired
    DeployedContractAddress deployedContractAddress;
    private String key;

    public UCContractFactoryBean(String key) {
        this.key = key;
    }

    @Override
    public Class<?> getObjectType() {
        return UserContract.class;
    }

    @Override
    protected Object createInstance() throws Exception {
        /**
         * key.length==42 means that  the key is a address ,so invoke load method ,otherwise it is a secrete(length 64) invoke deploy method
         */
        if (key.length() == 42) {
            deployedContractAddress.setContractAddress(DeployedContracts.UserContract,key);
            return UserContract.load(key, web3j, credentials, contractGasProvider);
        } else {
            UserContract send = UserContract.deploy(web3j, credentials, contractGasProvider).send();
            DeployedContractInfo deployedContractInfo = new DeployedContractInfo();
            deployedContractInfo.setManagerPrivateKey(key);
            deployedContractInfo.setManagerAddress("");
            deployedContractInfo.setContractName("UserContract");
            deployedContractInfo.setContractAddress(send.getContractAddress());
            deployedContractAddress.setContractAddress(DeployedContracts.UserContract,send.getContractAddress());
            log.info("deploy contact "+ deployedContractInfo.getContractAddress());
            return send;
        }
    }
}
