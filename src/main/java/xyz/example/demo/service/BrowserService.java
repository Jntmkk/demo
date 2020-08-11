package xyz.example.demo.service;

import xyz.example.demo.bean.TransactionRecord;

import java.math.BigInteger;
import java.util.List;

/**
 * BroswerService
 */
public interface BrowserService {

    //查看用户余额
    public BigInteger getUserBalance(String username) throws Exception;

    //查看用户最近5条交易记录（余额变化）
    public List<TransactionRecord> getTXRecord(String username) throws Exception;


}
