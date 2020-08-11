package xyz.example.demo.service.impl;

import org.springframework.stereotype.Service;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.example.demo.bean.DeployedContractAddress;
import xyz.example.demo.bean.DeployedContracts;
import xyz.example.demo.contract.DeviceContract;
import xyz.example.demo.contract.TaskContract;
import xyz.example.demo.contract.UserContract;
import xyz.example.demo.repository.UserRepository;
import xyz.example.demo.service.BrowserService;
import xyz.example.demo.utils.UserTokenUtil;
import xyz.example.demo.web3j.EthCallUtil;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Service
public class BrowserServiceImpl implements BrowserService {

    EthCallUtil ethCallUtil;
    Web3j web3j;
    Credentials credentials;
    UserTokenUtil userTokenUtil;
    DeployedContractAddress deployedContractAddress;
    UserContract userContract;
    TaskContract taskContract;
    DeviceContract deviceContract;
    ContractGasProvider contractGasProvider;
    UserRepository userRepository;

    public BrowserServiceImpl(EthCallUtil ethCallUtil, Web3j web3j, Credentials credentials, UserTokenUtil userTokenUtil, DeployedContractAddress deployedContractAddress, UserContract userContract, TaskContract taskContract, DeviceContract deviceContract, ContractGasProvider contractGasProvider, UserRepository userRepository) {
        this.ethCallUtil = ethCallUtil;
        this.web3j = web3j;
        this.credentials = credentials;
        this.userTokenUtil = userTokenUtil;
        this.deployedContractAddress = deployedContractAddress;
        this.userContract = userContract;
        this.taskContract = taskContract;
        this.deviceContract = deviceContract;
        this.contractGasProvider = contractGasProvider;
        this.userRepository = userRepository;
    }

    private TaskContract loadTaskContract(String username) {
        return TaskContract.load(deployedContractAddress.getContractAddress(DeployedContracts.TaskContract), web3j, Credentials.create(userRepository.findByUsername(username).get().getPrivateKey()), contractGasProvider);
    }

    //根据用户名查询地址
    @Override
    public String getAddr(String username) throws Exception {
        List<Type> userInfo = userContract.getUserInformation(username).sendAsync().get();
        String addr = userInfo.get(0).getValue().toString();
        return addr;
    }

    //查看用户余额(wei)
    @Override
    public BigInteger getUserBalance(String username) throws Exception {
        String addr = getAddr(username);
        EthGetBalance ethGetBalance = web3j
                .ethGetBalance(addr, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();
        return ethGetBalance.getBalance();
    }

    //查看用户交易次数
    @Override
    public BigInteger getUserTXCount(String username) throws Exception {
        String addr = getAddr(username);
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(addr, DefaultBlockParameterName.LATEST).sendAsync().get();
        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        return nonce;
    }

    /**
     * 获取某个用户所有交易记录，要获取最近几条，可在Controller自行获取；没经过测试，可能只能获取合约的交易记录
     * @param 用户名
     * @return
     * @throws Exception
     * wait to modify
     */
    @Override
    public List<EthLog.LogResult> getUserTXRecord(String username) throws Exception {
        String addr = getAddr(username);
        return getLogResults(addr);
    }

    //查询任务合约的所有交易记录
    @Override
    public List<EthLog.LogResult> getTaskContractTXRecord() throws Exception {
        String addr = deployedContractAddress.getContractAddress(DeployedContracts.TaskContract);
        return getLogResults(addr);
    }

    private List<EthLog.LogResult> getLogResults(String addr) throws java.io.IOException {
        EthFilter ethFilter =
                new EthFilter(
                        DefaultBlockParameterName.EARLIEST,
                        DefaultBlockParameterName.LATEST,
                        addr
                );
        Event event = new Event("Transfer", Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        ethFilter.addSingleTopic(EventEncoder.encode(event));
        EthLog ethLog = web3j.ethGetLogs(ethFilter).send();
        List<EthLog.LogResult> logs = ethLog.getLogs();
        return logs;
    }
}
