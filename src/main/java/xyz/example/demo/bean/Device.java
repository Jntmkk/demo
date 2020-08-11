package xyz.example.demo.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Device {
    String deviceName;
    BigInteger deviceId;
    String deviceType; //设备类型
    String APIUrl; //测试设备链接
    String accessPlatform; //云平台名字
    String description; //描述
    String testType; //测试类型：线上、线下
}
