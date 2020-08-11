package xyz.example.demo.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OneNetCommandWrapper {
    @NotNull
    String cmd;
    @NotNull
    String apiKey;
    @NotNull
    String deviceId;
}
