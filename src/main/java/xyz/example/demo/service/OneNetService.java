package xyz.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Component;

@Api(tags = "OneNet service")
public interface OneNetService {
    /**
     * 发送指令
     *
     * @param deviceId 设备ID
     * @param cmd      命令
     * @return 这个我不确定是什么 你自己酌情修改.
     * @apiNote JSONObject = JSON.parse(String)
     */
    JSONObject sendCommand(String deviceId, String cmd);


    /**
     * 获取
     *
     * @param deviceId
     * @return
     */
    JSONObject getInfo(String deviceId);
}
