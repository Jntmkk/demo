package xyz.example.demo.service.impl;

import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
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

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class Web3jServiceImpl implements Web3jService {


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

    public Web3jServiceImpl(EthCallUtil ethCallUtil, Web3j web3j, Credentials credentials, UserTokenUtil userTokenUtil, DeployedContractAddress deployedContractAddress, UserContract userContract, TaskContract taskContract, DeviceContract deviceContract, ContractGasProvider contractGasProvider, UserRepository userRepository) {
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

    //用户是否已注册
    @Override
    public Boolean isRegister(User user) throws Exception {
        return userContract.isRegister(user.getAddress(), user.getUsername()).sendAsync().get().getValue();
    }

    // 用户注册
    @Override
    public void register(User user) throws ExecutionException, InterruptedException {
        userContract.register(user.getAddress(), user.getUsername(), user.getPassword(), user.getProfile()).sendAsync().get();
    }

    // 用户登录
    @Override
    public Boolean login(User user) throws Exception {
        return userContract.login(user.getUsername(), user.getPassword()).sendAsync().get().getValue();
    }

    //获取用户信息
    @Override
    public User getUserInfo(String userName) throws Exception {
//        String address = ethCallUtil.getUserAddress(userName);
//        User getUserInfo = ethCallUtil.getObject("getUserInfo", address, User.class, userName);
//        UserContract load = UserContract.load("0xce8f694256d5cbF990A606Fd97AdA4F23ee346D7", service, Credentials.create("d9142d22f032c5016f66674e222b4783e8e65c3f9e4feb4bbfe4f5a04800d3be"), new DefaultGasProvider());
        List<Type> userInfo = userContract.getUserInformation(userName).sendAsync().get();
        User user = new User();
        user.setAddress(userInfo.get(0).getValue().toString());
        user.setUsername(userName);
        user.setProfile(userInfo.get(1).getValue().toString());
        user.setRegisterTime(new Long(userInfo.get(2).getValue().toString()));
        user.setProcessTaskNum(new Long(userInfo.get(3).getValue().toString()));
        user.setFinishedTaskNum(new Long(userInfo.get(4).getValue().toString()));
        user.setReputation(new Integer(userInfo.get(5).getValue().toString()));

        return user;
    }

    // 更新密码
    @Override
    public void updatePassword(String userName, String newPassword) throws Exception {
        userContract.updatePassword(userName, newPassword).sendAsync().get();
    }

    // 更新简介
    @Override
    public void updateProfile(String userName, String newProfile) throws Exception {
        userContract.updatePassword(userName, newProfile).sendAsync().get();
    }

    //发布任务
    @Override
    public void postTask(CrowdBCTask task, String posterName) throws Exception {
        loadTaskContract(posterName).postTask(
                task.getTitle(), posterName, task.getDescription(), task.getDeposit(),
                task.getDeadline(), task.getMaxWorkerNum(), task.getMinReputation(), task.getTaskType(),
                task.getPointer(), task.getReward()
        ).sendAsync().get();
    }

    //根据任务id，查看任务信息
    @Override
    public CrowdBCTask getTaskInfo(BigInteger taskId) throws Exception {
        List<Type> info = taskContract.getTaskInformation(taskId).sendAsync().get();
        CrowdBCTask task = new CrowdBCTask();
        task.setId(taskId);
        task.setTitle(info.get(0).getValue().toString());
        task.setDescription(info.get(1).getValue().toString());

        task.setReward(new BigInteger(info.get(2).getValue().toString()));
        task.setDeposit(new BigInteger(info.get(3).getValue().toString()));
        task.setDeadline(new BigInteger(info.get(4).getValue().toString()));
        task.setMaxWorkerNum(new BigInteger(info.get(5).getValue().toString()));
        task.setCurrentWorkerNum(new BigInteger(info.get(6).getValue().toString()));
        task.setMinReputation(new BigInteger(info.get(7).getValue().toString()));
        task.setTaskType(new BigInteger(info.get(8).getValue().toString()));

        task.setStatus(CrowdBCTask.TaskStatus.get(Integer.valueOf(info.get(9).getValue().toString()).intValue())); // ????
        task.setPointer(info.get(10).getValue().toString());
        task.setCreateDate(taskContract.getTaskCreateDate(taskId).sendAsync().get().getValue());

        return task;
    }

    //根据id 获取任务列表
    @Override
    public List<CrowdBCTask> getTasksByIds(List<BigInteger> idList) throws Exception {
        List<CrowdBCTask> crowdBCTasks = new LinkedList<>();
        for (BigInteger id : idList) {
//            Web3jCrowdBCTask getTaskInformation = ethCallUtil.getObject("getTaskInformation", address, Web3jCrowdBCTask.class, o);
//            crowdBCTasks.add(getTaskInformation.convert());
            CrowdBCTask task = getTaskInfo(id);
            crowdBCTasks.add(task);
        }
        return crowdBCTasks;
    }

    // 获取所有任务
    @Override
    public List<CrowdBCTask> getAllTask() throws Exception {
//        List<Uint256> idList = taskContract.getAllTaskId().sendAsync().get().getValue();
//        List<BigInteger> idListNew = new LinkedList<>();
//        for(Uint256 id : idList) {
//            idListNew.add( new BigInteger(idList.get(0).toString()) );
//        }

        List<BigInteger> idListNew = convertDynamicArrayToList(taskContract.getAllTaskId().sendAsync().get());
        return getTasksByIds(idListNew);
    }

    //获取用户已接受任务
    @Override
    public List<CrowdBCTask> getAcceptedTask(String username) throws Exception {
        List<BigInteger> list = convertDynamicArrayToList(userContract.getAcceptTask(username).sendAsync().get());
        return getTasksByIds(list);
    }

    private List<BigInteger> convertDynamicArrayToList(DynamicArray<Uint256> dynamicArray) {
        List<BigInteger> list = new LinkedList<>();
        dynamicArray.getValue().stream().forEach(o -> {
            list.add(o.getValue());
        });
        return list;
    }

    //获取用户已发布任务
    @Override
    public List<CrowdBCTask> getPostedTask(String username) throws Exception {
        List<BigInteger> list = new LinkedList<>();
        userContract.getPostTaskList(username).sendAsync().get().getValue().stream().forEach(o -> {
            list.add(o.getValue());
        });
        return getTasksByIds(list);
    }

    //检查是否满足接收条件
    @Override
    public Boolean checkAcceptCondition(String username, BigInteger taskId) throws Exception {
        String fullFill = taskContract.checkAcceptCondition(username, taskId).sendAsync().get().toString();
        return "conglatulation".equals(fullFill);
    }

    //接受任务,成功返回true;
    @Override
    public Boolean acceptTask(String username, BigInteger taskId, BigInteger deposit) throws Exception {
        if (checkAcceptCondition(username, taskId) == true) {
            loadTaskContract(username).acceptTask(username, taskId, deposit).sendAsync().get();
            return true;
        }
        return false;
    }

    //检查是否满足接受报告条件
    @Override
    public Boolean checkSubmitReportCondition(String username, BigInteger taskId) throws Exception {
        return loadTaskContract(username).checkSubmitCondition(username, taskId).sendAsync().get().getValue();
    }

    //查看报告详细信息
    @Override
    public TaskReport getReportInfo(BigInteger id) throws Exception {
        TaskReport taskReport = new TaskReport();
        List<Type> list = taskContract.getSolutionInfo(id).sendAsync().get();
        String workerName = list.get(0).getValue().toString();
        taskReport.setSolution(list.get(1).getValue().toString());
        taskReport.setPointer(list.get(2).getValue().toString());
        BigInteger submitDate = new BigInteger(list.get(0).getValue().toString());
        taskReport.setLevel(new BigInteger(list.get(4).getValue().toString()));

        return taskReport;
    }

    //查看某个任务的所有报告
    @Override
    public List<TaskReport> getTaskAllReport(BigInteger taskId) throws Exception {
        List<TaskReport> list = new LinkedList<>();
        //wait to modify
        return list;
    }

    //提交报告
    @Override
    public Boolean submitReport(String username, TaskReport taskReport) throws Exception {
        if (checkSubmitReportCondition(username, taskReport.getBelongsToTask())) {
            loadTaskContract(username).submitSolution(username, taskReport.getSolution(), taskReport.getPointer(), taskReport.getBelongsToTask()).sendAsync().get();
            return true;
        }
        return false;
    }

    //评估报告，并返还押金，给予报酬
    @Override
    public void evaluateReport(String username, TaskReportEvaluation evaluation) throws ExecutionException, InterruptedException {
        loadTaskContract(username).evaluateSolution(evaluation.getBelongsToTaskId(), evaluation.getBelongsToReportId(), evaluation.getLevel()).sendAsync().get();
    }

    //返还剩余报酬给任务发布者
    @Override
    public void returnBalance(BigInteger taskId, String username) throws Exception {
        loadTaskContract(username).returnBalance(taskId).sendAsync().get();
    }
}
