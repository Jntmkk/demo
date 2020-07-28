package xyz.example.demo.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class User {
    @ApiModelProperty("user id")
    private String id;
}
