package xyz.example.demo.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TransactionRecord {
    String txnHash;
    BigInteger block;
    String from;
    String to;
    // 转移的gas 比如转账
    BigInteger value;
    BigInteger txnFee;
    BigInteger gasPrice;
    BigInteger gasLimit;
    BigInteger timestamp;
}
