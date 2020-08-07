package xyz.example.demo.service;

import xyz.example.demo.TaskReport;
import xyz.example.demo.controller.UserController;
import xyz.example.demo.models.CrowdBCTask;
import xyz.example.demo.models.User;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * web3j 需要接口,暂定这些接口，接口参数待修正，可酌情修改
 */
public interface Web3jService {
    /**
     * 提交报告，参数不确定 自行确定
     *
     * @return
     */
    void submitReport(String username, TaskReport taskReport);

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
}
