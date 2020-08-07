package xyz.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.example.demo.TaskReport;
import xyz.example.demo.bean.DeployedContractAddress;
import xyz.example.demo.bean.DeployedContracts;
import xyz.example.demo.contract.DeviceContract;
import xyz.example.demo.contract.TaskContract;
import xyz.example.demo.contract.UserContract;
import xyz.example.demo.models.CrowdBCTask;
import xyz.example.demo.models.DeployedContractInfo;
import xyz.example.demo.models.User;
import xyz.example.demo.repository.DeployedContractInfoRepository;
import xyz.example.demo.repository.UserRepository;
import xyz.example.demo.service.Web3jService;
import xyz.example.demo.utils.UserTokenUtil;
import xyz.example.demo.web3j.EthCallUtil;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

@Service
public class Web3jServiceImpl2 implements Web3jService {
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

    public Web3jServiceImpl2(EthCallUtil ethCallUtil, Web3j web3j, Credentials credentials, UserTokenUtil userTokenUtil, DeployedContractAddress deployedContractAddress, UserContract userContract, TaskContract taskContract, DeviceContract deviceContract, ContractGasProvider contractGasProvider, UserRepository userRepository) {
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

    @Override
    public void submitReport(String username, TaskReport taskReport) {
        loadTaskContract(username).submitSolution(username, taskReport.getSolution(), taskReport.getPointer(), BigInteger.valueOf(taskReport.getBelongTo()));
    }

    @Override
    public List<CrowdBCTask> getPostedTask(String userName) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {
        List<CrowdBCTask> crowdBCTasks = new LinkedList<>();
        String address = ethCallUtil.getUserAddress(userName);
        List<Object> lists = ethCallUtil.getValue("getPostTaskList", address, userName);
        for (Object o : lists) {
            CrowdBCTask getTaskInformation = ethCallUtil.getObject("getTaskInformation", address, CrowdBCTask.class, o);
            crowdBCTasks.add(getTaskInformation);
        }
        return crowdBCTasks;
    }

    @Override
    public List<CrowdBCTask> getTask(String userName) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {
        String address = ethCallUtil.getUserAddress(userName);
//        ethCallUtil.getValue("getTaskInformation", address, userName);
        throw new RuntimeException("此功能正在开发");
    }

    @Override
    public User getUserInfo(String userName) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {

//        String address = ethCallUtil.getUserAddress(userName);
//        User getUserInfo = ethCallUtil.getObject("getUserInfo", address, User.class, userName);
        throw new RuntimeException("此功能正在开发");
    }

    @Override
    public void submitTask(String username, CrowdBCTask crowdBCTask) {
        loadTaskContract(username).postTask(username, crowdBCTask.getDescription(), crowdBCTask.getDeposit(), crowdBCTask.getDeadline(), crowdBCTask.getMaxWorkerNum(), crowdBCTask.getMinReputation(), crowdBCTask.getTaskType(), crowdBCTask.getPointer());
    }

    @Override
    public void register(User user) {
        userContract.register(user.getAddress(), user.getUsername(), user.getPassword(), "");
    }
}
