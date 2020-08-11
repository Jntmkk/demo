package xyz.example.demo.service;

import org.web3j.protocol.core.methods.response.EthLog;

import java.math.BigInteger;
import java.util.List;

/**
 * BroswerService
 */
public interface BrowserService {

    //根据用户名查询地址
    String getAddr(String username) throws Exception;

    //查看用户余额
    public BigInteger getUserBalance(String username) throws Exception;

    //查看用户交易次数
    BigInteger getUserTXCount(String username) throws Exception;

    //查看用户de所有交易记录（余额变化）
    public List<EthLog.LogResult> getUserTXRecord(String username) throws Exception;

    //查询任务合约的所有交易记录
    List<EthLog.LogResult> getTaskContractTXRecord() throws Exception;
}
