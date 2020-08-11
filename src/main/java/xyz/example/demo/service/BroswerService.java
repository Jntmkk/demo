package xyz.example.demo.service;

import java.math.BigInteger;

/**
 *  BroswerService
 */
public interface BroswerService {

    //查看用户余额
    public BigInteger getUserBalance(String username) throws Exception ;

    //查看用户最近5条交易记录（余额变化）
//    public List<u> getTXRecord(String username) throws Exception;


}
