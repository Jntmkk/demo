package xyz.example.demo.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class User {
    public User() {
    }

    public User(String id) {
        this.id = id;
    }
    @NotNull(message = "id must not be null")
    @NotEmpty(message = "id must not be empty")
    @ApiModelProperty("user id")
    private String id;
    @NotNull(message = "name must not be null")
    @NotEmpty(message = "name must not be empty")
    @ApiModelProperty("user name")
    private String name;
}
