package xyz.example.demo.web3j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AbstractFactoryBean;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;

public abstract class ContractFactoryBean<T> extends AbstractFactoryBean {

    @Autowired
    protected Web3j web3j;
    @Autowired
    protected Credentials credentials;
    @Autowired
    protected ContractGasProvider contractGasProvider;
}
