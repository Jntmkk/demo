package xyz.example.demo.service;

import javafx.concurrent.Task;
import xyz.example.demo.bean.TaskReportEvaluation;
import xyz.example.demo.bean.TaskReport;
import xyz.example.demo.models.CrowdBCTask;
import xyz.example.demo.models.User;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.util.List;

/**
 * web3j 需要接口,暂定这些接口，接口参数待修正，可酌情修改
 */
public interface Web3jService {
    List<CrowdBCTask> getAcceptedTask(String username) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException;

    List<CrowdBCTask> getAll() throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException;

    /**
     * 提交报告，参数不确定 自行确定
     *
     * @return
     */
    void submitReport(String username, TaskReport taskReport);

    List<TaskReport> getReport(String username, BigInteger taskId);

    /**
     * @param userName
     * @return
     */
    List<CrowdBCTask> getPostedTask(String userName) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException;

    /**
     * @param userName
     * @return
     */
    List<CrowdBCTask> getTask(String userName) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException;

    User getUserInfo(String userName) throws NoSuchMethodException, IllegalAccessException, InstantiationException, IOException, InvocationTargetException, ClassNotFoundException;

    void submitTask(String username, CrowdBCTask crowdBCTask) throws NoSuchMethodException, IllegalAccessException, InstantiationException, ClassNotFoundException, InvocationTargetException, IOException;

    void register(User user);

    void evaluateReport(TaskReportEvaluation evaluation);
}
