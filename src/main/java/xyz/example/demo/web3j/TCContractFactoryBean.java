package xyz.example.demo.web3j;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.example.demo.bean.DeployedContractAddress;
import xyz.example.demo.bean.DeployedContracts;
import xyz.example.demo.contract.TaskContract;
import xyz.example.demo.contract.UserContract;
import xyz.example.demo.models.DeployedContractInfo;
import xyz.example.demo.repository.DeployedContractInfoRepository;
@Slf4j
public class TCContractFactoryBean extends ContractFactoryBean<TaskContract> {
    private String key;
    @Autowired
    DeployedContractAddress deployedContractAddress;

    public TCContractFactoryBean(String key) {
        this.key = key;
    }

    @Override
    public Class<?> getObjectType() {
        return TaskContract.class;
    }

    @Override
    protected Object createInstance() throws Exception {
        /**
         * key.length==42 means that  the key is a address ,so invoke load method ,otherwise it is a secrete(length 64) invoke deploy method
         */
        if (key.length() == 42) {
            return TaskContract.load(key, web3j, credentials, contractGasProvider);
        } else {
            String contractAddress = deployedContractAddress.getContractAddress(DeployedContracts.USER_CONTRACT);
            TaskContract send = TaskContract.deploy(web3j, credentials, contractGasProvider,contractAddress).send();
            DeployedContractInfo deployedContractInfo = new DeployedContractInfo();
            deployedContractInfo.setManagerPrivateKey(key);
            deployedContractInfo.setManagerAddress("");
            deployedContractInfo.setContractName("TaskContract");
            deployedContractInfo.setContractAddress(send.getContractAddress());
            deployedContractAddress.setContractAddress(DeployedContracts.TASK_CONTRACT,send.getContractAddress());
            log.info("deploy contact "+ deployedContractInfo.getContractAddress());
            return send;
        }
    }
}
