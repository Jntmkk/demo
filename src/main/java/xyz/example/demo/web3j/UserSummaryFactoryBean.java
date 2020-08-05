package xyz.example.demo.web3j;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.crypto.Credentials;
import xyz.example.demo.contract.Register;
import xyz.example.demo.contract.UserSummary;
import xyz.example.demo.models.DeployedContractInfo;
import xyz.example.demo.repository.DeployedContractInfoRepository;

@Slf4j
public class UserSummaryFactoryBean extends ContractFactoryBean<UserSummary> {
    boolean flag = false;
    private String addrOrKey;
    @Autowired
    DeployedContractInfoRepository repository;

    public UserSummaryFactoryBean(String addrOrKey) {
        this.addrOrKey = addrOrKey;
    }

    @Override
    public Class<?> getObjectType() {
        return UserSummary.class;
    }

    void saveContract(UserSummary userSummary) {
        DeployedContractInfo deployedContractInfo = new DeployedContractInfo();
        deployedContractInfo.setContractAddress(userSummary.getContractAddress());
        deployedContractInfo.setContractName("UserSummary");
        deployedContractInfo.setManagerPrivateKey(addrOrKey);
        repository.save(deployedContractInfo);
        flag = true;
    }

    @Override
    protected Object createInstance() throws Exception {
        if (addrOrKey.length() == 64) {
            log.info("contract create key:" + addrOrKey);
            UserSummary send = UserSummary.deploy(this.web3j, Credentials.create(addrOrKey), this.contractGasProvider).send();
            if (!flag)
                saveContract(send);
            return send;
        } else {
            return UserSummary.load(addrOrKey, this.web3j, this.credentials, this.contractGasProvider);
        }
    }

//    @Override
//    protected Object createInstance() throws Exception {
//        return UserSummary.load("0xfe7f947fe5b5f45e7139516572bd2bde267041a8", this.web3j, this.credentials, this.contractGasProvider);
//    }
//

//    /**
//     * private
//     *
//     * @return
//     * @throws Exception
//     */
//    @Override
//    protected Object createInstance() throws Exception {
//        return UserSummary.load("0x576042cf5dd33297027e3489907cda0b14633920", this.web3j, this.credentials, this.contractGasProvider);
//    }

//    @Override
//    protected Object createInstance() throws Exception {
//        return UserSummary.deploy(this.web3j, this.credentials, this.contractGasProvider).send();
//    }
}
