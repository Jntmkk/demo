package xyz.example.demo.service;

import xyz.example.demo.controller.UserController;
import xyz.example.demo.models.CrowdBCTask;
import xyz.example.demo.models.User;

import java.util.List;

/**
 * web3j 需要接口,暂定这些接口，接口参数待修正，可酌情修改
 */
public interface Web3jService {
    /**
     * 提交报告，参数不确定 自行确定
     * @return
     */
    boolean submitReport();

    /**
     *
     * @param userName
     * @return
     */
    List<CrowdBCTask> getPostedTask(String userName);

    /**
     *
     * @param userName
     * @return
     */
    List<CrowdBCTask> getAllTask(String userName);

    User getUserInfo(String userName);

    boolean submitTask(CrowdBCTask crowdBCTask);
}
