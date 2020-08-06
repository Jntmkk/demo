package xyz.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.example.demo.contract.DeviceContract;
import xyz.example.demo.contract.TaskContract;
import xyz.example.demo.contract.UserContract;
import xyz.example.demo.models.CrowdBCTask;
import xyz.example.demo.models.DeployedContractInfo;
import xyz.example.demo.models.User;
import xyz.example.demo.repository.DeployedContractInfoRepository;
import xyz.example.demo.service.Web3jService;
import xyz.example.demo.utils.UserTokenUtil;
import xyz.example.demo.web3j.EthCallUtil;

import javax.validation.constraints.NotNull;
import java.util.List;

@Service
public class Web3jServiceImpl implements Web3jService {
    Web3j web3j;
    /**
     * 此凭证为默认为部署合约的凭证
     */
    Credentials credentials;
    UserTokenUtil userTokenUtil;
    /**
     * 获取已部署合约的信息，若有多个符合要求 按ID递减排序，也就是取一个就是获取最新的
     */
    DeployedContractInfoRepository deployedContractInfoRepository;

    public Web3jServiceImpl(Web3j web3j, Credentials credentials, UserTokenUtil userTokenUtil, DeployedContractInfoRepository deployedContractInfoRepository) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.userTokenUtil = userTokenUtil;
        this.deployedContractInfoRepository = deployedContractInfoRepository;
    }

    /**
     * 合约实例
     */
    @Autowired
    UserContract userContract;
    @Autowired
    TaskContract taskContract;
    @Autowired
    DeviceContract deviceContract;
    @Autowired
    ContractGasProvider contractGasProvider;
    @Autowired
    public boolean submitReport() {
        return false;
    }

    @Override
    public List<CrowdBCTask> getPostedTask(String userName) {
        return null;
    }

    @Override
    public List<CrowdBCTask> getTask(String userName) {
        return null;
    }

    @Override
    public User getUserInfo(String userName) {
        return null;
    }

    @Override
    public boolean submitTask(CrowdBCTask crowdBCTask) {
        return false;
    }

    @Override
    public void register(User user) {
        userContract.register(user.getAddress(), user.getUsername(), user.getPassword(), "");
    }
}
