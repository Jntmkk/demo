package xyz.example.demo.web3j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.tx.Contract;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.example.demo.contract.Register;
import xyz.example.demo.repository.DeployedContractInfoRepository;

import javax.annotation.PostConstruct;

public abstract class ContractFactoryBean<T> extends AbstractFactoryBean {

    @Autowired
    protected Web3j web3j;
    @Autowired
    protected Credentials credentials;
    @Autowired
    protected ContractGasProvider contractGasProvider;
}
