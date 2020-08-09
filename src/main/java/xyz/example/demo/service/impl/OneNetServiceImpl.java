package xyz.example.demo.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.springframework.stereotype.Service;
import xyz.example.demo.service.OneNetService;

import java.util.Objects;

@Service
public class OneNetServiceImpl implements OneNetService {
    @Override
    public String sendCommand(String deviceId, String apiKey, String cmd) {
        String errorCode = null;
        try{
                /*{
                    'datastreams':[
                        {
                            'id':'code',
                            'datapoints':[
                                {
                                    'value':
                                        """
                                            GPIO.output(17, GPIO.HIGH)
                                            GPIO.output(27, GPIO.HIGH)
                                        """
                                }
                            ]
                        }
                    ]
                   }*/
            //封装JSON
            JSONObject jsonObjectTemp = new JSONObject();
            JSONObject jsonObjectTemp2 = new JSONObject();
            JSONObject[] jsonArrayTemp = new JSONObject[1];
            JSONObject[] jsonArrayTemp2 = new JSONObject[1];
            JSONObject json = new JSONObject();
            jsonObjectTemp.put("value",cmd);
            jsonArrayTemp[0] = jsonObjectTemp;
            jsonObjectTemp2.put("datapoints",jsonArrayTemp);
            jsonObjectTemp2.put("id", "code");
            jsonArrayTemp2[0] = jsonObjectTemp2;
            json.put("datastreams", jsonArrayTemp2);
            //通过client发起请求
            OkHttpClient client = new OkHttpClient();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
            Request request = new Request.Builder()
                    .post(body)
                    .url("http://api.heclouds.com/devices/" + deviceId + "/datapoints")
                    .header("api-key", apiKey)
                    .build();
            Response response= client.newCall(request).execute();
            String errorResponse = Objects.requireNonNull(response.body()).string();
//            System.out.println(errorResponse);
            JSONObject jsonObject = JSONObject.parseObject(errorResponse);
            //返回错误代码（0未未出错）
            errorCode = jsonObject.getString("errno");
//            System.out.println(errorCode);
            //返回错误提示
            String errorMessage = jsonObject.getString("error");
//            System.out.println(errorMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorCode;
    }

    @Override
    public String getInfo(String deviceId, String apiKey) {
        String lightStatus = null;
        try{
            OkHttpClient client = new OkHttpClient();
            //创建一个Request
            Request request = new Request.Builder()
                    .get()
                    .url("http://api.heclouds.com/devices/" + deviceId + "/datapoints")
                    .header("api-key", apiKey)
                    .build();
            //通过client发起请求
            Response response = client.newCall(request).execute();
            String responseData = Objects.requireNonNull(response.body()).string();
            //解析JSON
            JSONObject jsonObject = JSONObject.parseObject(responseData);
            String data = jsonObject.getString("data");
            JSONObject dataJson = JSONObject.parseObject(data);
            String datastreams = dataJson.getString("datastreams");
            JSONArray datastreamsArray = JSONArray.parseArray(datastreams);
            JSONObject datapointsJson = datastreamsArray.getJSONObject(0);
            String datapoints = datapointsJson.getString("datapoints");
            JSONArray datapointsArray = JSONArray.parseArray(datapoints);
            JSONObject codeJson = datapointsArray.getJSONObject(0);
            //获得收到的命令
            String code = codeJson.getString("value");
//            System.out.println("cmd = " + code);
            JSONObject datapointsJson2 = datastreamsArray.getJSONObject(1);
            String datapoints2 = datapointsJson2.getString("datapoints");
            JSONArray datapointsArray2 = JSONArray.parseArray(datapoints2);
            JSONObject codeJson2 = datapointsArray2.getJSONObject(0);
            String lightSensor = codeJson2.getString("value");
            //获得光敏电阻反馈信息
//                    System.out.println(lightSensor);
            JSONObject lightSensorJson = JSONObject.parseObject(lightSensor);
            //获得灯光状态
            lightStatus = lightSensorJson.getString("Light_sensor");
//            System.out.println("lightStatus = " + lightStatus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lightStatus;
    }
}
