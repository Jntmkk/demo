package xyz.example.demo.service;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;


@Api(tags = "OneNet service")
public interface OneNetService {
    /**
     * 发送指令
     *
     * @param deviceId 设备ID
     * @param apiKey   接口密钥
     * @param cmd      命令
     * @return String  错误代码（0为未出错）
     * @apiNote JSONObject = JSON.parse(String)
     */
    String sendCommand(String deviceId, String apiKey, String cmd);


    /**
     * 获取
     *
     * @param deviceId  设备ID
     * @param apiKey    接口密钥
     * @return String   硬件反馈（灯光状态）
     */
    String getInfo(String deviceId, String apiKey);

}
