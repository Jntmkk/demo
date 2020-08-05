package xyz.example.demo.web3j;

import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;

public class DefaultGasProvider implements ContractGasProvider {
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(6721975);
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(41000L);
//    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(800000L);
//    public static final BigInteger GAS_PRICE = BigInteger.valueOf(41000000L);
    @Override
    public BigInteger getGasPrice(String s) {
        return GAS_PRICE;
    }

    @Override
    public BigInteger getGasPrice() {
        return GAS_PRICE;
    }

    @Override
    public BigInteger getGasLimit(String s) {
        return GAS_LIMIT;
    }

    @Override
    public BigInteger getGasLimit() {
        return GAS_LIMIT;
    }
}
