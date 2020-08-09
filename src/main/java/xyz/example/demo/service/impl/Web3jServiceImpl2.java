package xyz.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.example.demo.bean.*;
import xyz.example.demo.contract.DeviceContract;
import xyz.example.demo.contract.TaskContract;
import xyz.example.demo.contract.UserContract;
import xyz.example.demo.models.CrowdBCTask;
import xyz.example.demo.models.User;
import xyz.example.demo.repository.UserRepository;
import xyz.example.demo.service.Web3jService;
import xyz.example.demo.utils.UserTokenUtil;
import xyz.example.demo.web3j.EthCallUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.Arrays;
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
    public List<TaskReport> getReport(String username, BigInteger taskId) {
        return null;
    }

    private List<CrowdBCTask> getTasksByIds(List<Object> lists, String address) throws NoSuchMethodException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException, IOException {
        List<CrowdBCTask> crowdBCTasks = new LinkedList<>();
        for (Object o : lists) {
            Web3jCrowdBCTask getTaskInformation = ethCallUtil.getObject("getTaskInformation", address, Web3jCrowdBCTask.class, o);
            crowdBCTasks.add(getTaskInformation.convert());
        }
        return crowdBCTasks;
    }

    @Override
    public List<CrowdBCTask> getAcceptedTask(String username) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {
        String address = ethCallUtil.getUserAddress("admin");
        List<Object> value = ethCallUtil.getValue("getAcceptedTask", address);
        return getTasksByIds(value, address);
    }

    @Override
    public List<CrowdBCTask> getAll() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {
        String address = ethCallUtil.getUserAddress("admin");
        List<Object> value = ethCallUtil.getValue("getAllTaskId", address);
        return getTasksByIds(value, address);
    }

    @Override
    public void submitReport(String username, TaskReport taskReport) {
        loadTaskContract(username).submitSolution(username, taskReport.getSolution(), taskReport.getPointer(), taskReport.getBelongToTask());
    }

    @Override
    public List<CrowdBCTask> getPostedTask(String userName) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException {
        String address = ethCallUtil.getUserAddress(userName);
        List<Object> lists = ethCallUtil.getValue("getPostTaskList", address, userName);
        return getTasksByIds((List<Object>) lists.get(0), address);
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
        loadTaskContract(username).postTask(crowdBCTask.getTitle(), username, crowdBCTask.getDescription(), crowdBCTask.getDeposit(), crowdBCTask.getDeadline(), crowdBCTask.getMaxWorkerNum(), crowdBCTask.getMinReputation(), crowdBCTask.getTaskType(), crowdBCTask.getPointer());
    }

    @Override
    public void register(User user) {
        userContract.register(user.getAddress(), user.getUsername(), user.getPassword(), "");
    }

    @Override
    public void evaluateReport(TaskReportEvaluation evaluation) {
        
    }
}
