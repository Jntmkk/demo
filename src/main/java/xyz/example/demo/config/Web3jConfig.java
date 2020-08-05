package xyz.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.example.demo.web3j.DCContractFactoryBean;
import xyz.example.demo.web3j.DefaultGasProvider;
import xyz.example.demo.web3j.TCContractFactoryBean;
import xyz.example.demo.web3j.UCContractFactoryBean;

@Configuration
public class Web3jConfig {

    @Value("${bezkoder.app.key}")
    protected String key;

    @Value("${web3j.dc}")
    protected String dc;
    @Value("${web3j.tc}")
    protected String tc;
    @Value("${web3j.uc}")
    protected String uc;

    @Bean
    public Credentials credentials() {
        return Credentials.create(key);
    }

    @Bean
    public ContractGasProvider contractGasProvider() {
        return new DefaultGasProvider();
    }

    @ConditionalOnProperty("web3j.dc")
    @Bean
    public DCContractFactoryBean dcFactoryBean() {
        return new DCContractFactoryBean(dc);
    }

    @ConditionalOnProperty("web3j.dc")
    @Bean
    public UCContractFactoryBean ucFactoryBean() {
        return new UCContractFactoryBean(uc);
    }

    @ConditionalOnProperty("web3j.dc")
    @Bean
    public TCContractFactoryBean tcFactoryBean() {
        return new TCContractFactoryBean(tc);
    }

}
