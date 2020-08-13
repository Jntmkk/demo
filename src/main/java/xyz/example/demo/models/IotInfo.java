package xyz.example.demo.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class IotInfo {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long id;
    String deviceId;
    String deviceToken;
    String platform;
    String path;

    public static IotInfo create(String deviceId, String deviceToken, String platform, String path) {
        IotInfo iotInfo = new IotInfo();
        iotInfo.setDeviceId(deviceId);
        iotInfo.setDeviceToken(deviceToken);
        iotInfo.setPath(path);
        iotInfo.setPlatform(platform);
        return iotInfo;
    }

    public boolean check() {
        return deviceId != null && deviceToken != null && platform != null && path != null;
    }
}
