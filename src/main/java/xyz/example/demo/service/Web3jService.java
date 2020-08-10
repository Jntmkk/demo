package xyz.example.demo.service;

import xyz.example.demo.bean.TaskReportEvaluation;
import xyz.example.demo.bean.TaskReport;
import xyz.example.demo.models.CrowdBCTask;
import xyz.example.demo.models.User;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * web3j 需要接口,暂定这些接口，接口参数待修正，可酌情修改
 */
public interface Web3jService {

    //根据id 获取任务列表
    List<CrowdBCTask> getTasksByIds(List<BigInteger> idList) throws Exception;

    //获取所有任务
    List<CrowdBCTask> getAllTask() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException, Exception;

    //获取用户已接受任务
    List<CrowdBCTask> getAcceptedTask(String username) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException, Exception;

    //更新密码
    void updatePassword(String userName, String newPassword) throws Exception;

    //更新简介
    void updateProfile(String userName, String newProfile) throws Exception;

    //发布任务
    void postTask(CrowdBCTask task, String posterName) throws Exception;

    //根据任务id，查看任务信息
    CrowdBCTask getTaskInfo(BigInteger taskId) throws Exception;

    //检查是否满足接收条件
    Boolean checkAcceptCondition(String username, BigInteger taskId) throws Exception;

    //接受任务
    Boolean acceptTask(String username, BigInteger taskId, BigInteger deposit) throws Exception;

    //检查是否满足接受报告条件
    Boolean checkSubmitReportCondition(String username, BigInteger taskId) throws Exception;

    //查看报告详细信息
    TaskReport getReportInfo(BigInteger Id) throws Exception;

    //查看某个任务的所有报告
    List<TaskReport> getTaskAllReport(BigInteger taskId) throws Exception;

    //提交报告
    Boolean submitReport(String username, TaskReport taskReport) throws Exception;

    //查看用户已发布任务
    List<CrowdBCTask> getPostedTask(String userName) throws Exception;

    //用户登录
    Boolean login(User user) throws Exception;

    //获取用户信息
    User getUserInfo(String userName) throws Exception;

    //用户是否已注册
    Boolean isRegister(User user) throws Exception;

    //用户注册
    void register(User user) throws Exception;

    //评估报告，并返还押金，给予报酬
    void evaluateReport(String username, TaskReportEvaluation evaluation) throws ExecutionException, InterruptedException;

    //返还剩余报酬给任务发布者
    void returnBalance(BigInteger taskId, String username) throws Exception;
}
