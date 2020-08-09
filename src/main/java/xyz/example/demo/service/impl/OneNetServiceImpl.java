package xyz.example.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;
import xyz.example.demo.service.OneNetService;
@Service
public class OneNetServiceImpl implements OneNetService {
    @Override
    public JSONObject sendCommand(String deviceId, String apiKey, String cmd) {
        return null;
    }

    @Override
    public JSONObject getInfo(String deviceId, String apiKey) {
        return null;
    }
}
