package xyz.example.demo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

@Api(tags = "OneNet service")
public interface OneNetService {
    /**
     * 发送指令
     *
     * @param deviceId 设备ID
     * @param apiKey   接口密钥
     * @param cmd      命令
     * @return 这个我不确定是什么 你自己酌情修改.
     * @apiNote JSONObject = JSON.parse(String)
     */
    default JSONObject sendCommand(String deviceId, String apiKey, String cmd){
        new Thread(new Runnable() {
            @Override
            public void run() {
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

                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), String.valueOf(json));
                    Request request = new Request.Builder()
                            .post(body)
                            .url("http://api.heclouds.com/devices/" + deviceId + "/datapoints")
                            .header("api-key", apiKey)
                            .build();
                    Response response= client.newCall(request).execute();
                    String message= Objects.requireNonNull(response.body()).string();
                    System.out.println(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return null;
    }


    /**
     * 获取
     *
     * @param deviceId  设备ID
     * @param apiKey    接口密钥
     * @return jsonArray
     */
    default JSONObject getInfo(String deviceId, String apiKey){
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                    JSONObject jsonObject = JSONObject.parseObject(responseData);
                    String data = jsonObject.getString("data");
                    JSONObject dataJson = JSONObject.parseObject(data);
                    String datastreams = dataJson.getString("datastreams");
                    JSONArray datastreamsArray = JSONArray.parseArray(datastreams);
                    JSONObject datapointsJson = datastreamsArray.getJSONObject(0);
                    String datapoints = datapointsJson.getString("datapoints");
                    JSONArray datapointsArray = JSONArray.parseArray(datapoints);
                    JSONObject codeJson = datapointsArray.getJSONObject(0);
                    String code = codeJson.getString("value");
                    System.out.println("cmd = " + code);
                    JSONObject datapointsJson2 = datastreamsArray.getJSONObject(1);
                    String datapoints2 = datapointsJson2.getString("datapoints");
                    JSONArray datapointsArray2 = JSONArray.parseArray(datapoints2);
                    JSONObject codeJson2 = datapointsArray2.getJSONObject(0);
                    String lightSensor = codeJson2.getString("value");
//                    System.out.println(lightSensor);
                    JSONObject lightSensorJson = JSONObject.parseObject(lightSensor);
                    String lightStatus = lightSensorJson.getString("Light_sensor");
                    System.out.println("lightStatus = " + lightStatus);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return null;
    }

}
