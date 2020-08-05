package xyz.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.example.demo.web3j.DefaultGasProvider;

@Configuration
public class Web3jConfig {

    @Value("${bezkoder.app.key}")
    protected String key;
    @Value("${web3j.reg}")
    protected String reg;
    @Value("${web3j.summary}")
    protected String summary;

    @Bean
    public Credentials credentials() {
        return Credentials.create(key);
    }

    @Bean
    public ContractGasProvider contractGasProvider() {
        return new DefaultGasProvider();
    }

//    @ConditionalOnProperty("web3j.reg")
//    @Bean
//    public RegisterFactoryBean registerFactoryBean() {
//        return new RegisterFactoryBean(reg);
//    }
//
//    @ConditionalOnProperty("web3j.summary")
//    @Bean
//    UserSummaryFactoryBean userSummaryFactoryBean() {
//        return new UserSummaryFactoryBean(summary);
//    }
}
