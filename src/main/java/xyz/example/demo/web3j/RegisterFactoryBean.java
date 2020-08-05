package xyz.example.demo.web3j;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import xyz.example.demo.contract.Register;
import xyz.example.demo.contract.UserSummary;
import xyz.example.demo.models.DeployedContractInfo;
import xyz.example.demo.repository.DeployedContractInfoRepository;

@Slf4j
public class RegisterFactoryBean extends ContractFactoryBean<Register> {
    boolean flag = false;
    private String addrOrKey;
    @Autowired
    DeployedContractInfoRepository deployedContractInfoRepository;

    public RegisterFactoryBean(String addrOrKey) {
        this.addrOrKey = addrOrKey;
    }

    @Override
    public Class<?> getObjectType() {
        return Register.class;
    }

    private void saveContract(Register register) throws Exception {
        DeployedContractInfo deployedContractInfo = new DeployedContractInfo();
        deployedContractInfo.setContractAddress(register.getContractAddress());
        deployedContractInfo.setContractName("Register");
        deployedContractInfo.setManagerAddress(register.getManagerAddr().send());
        deployedContractInfo.setManagerPrivateKey(addrOrKey);
        deployedContractInfoRepository.save(deployedContractInfo);
        flag = true;
    }

    @Override
    protected Object createInstance() throws Exception {
        if (addrOrKey.length() == 64) {
            log.info("contract create key:" + addrOrKey);
            Register send = Register.deploy(this.web3j, Credentials.create(addrOrKey), this.contractGasProvider).send();
            if (!flag)
                saveContract(send);
            return send;
        } else {
            return Register.load(addrOrKey, this.web3j, this.credentials, this.contractGasProvider);
        }
    }


//    @Override
//    public Class<?> getObjectType() {
//        return Register.class;
//    }

//    @Override
//    protected Object createInstance() throws Exception {
//        return Register.load("0xd1dbc4e7cd5504f485c6341ac9ecd29f6dba2930", web3j, credentials, contractGasProvider);
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
//        return Register.load("0x7d8ef61e3964568d0aa889fb4525c5b6bcf0b920", web3j, credentials, contractGasProvider);
//    }

//    @Override
//    protected Object createInstance() throws Exception {
//        return Register.deploy(web3j, credentials, contractGasProvider).send();
//    }

}
