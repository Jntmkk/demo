package xyz.example.demo.bean;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

@Data
public class Web3jUser {


    String address;
    String profile;
    BigInteger registerTime;
    BigInteger processingTaskNum;
    BigInteger finishedTaskNum;
    BigInteger reputation;

    public Web3jUser(String address, String profile, BigInteger registerTime, BigInteger processingTaskNum, BigInteger finishedTaskNum, BigInteger reputation) {
        this.address = address;
        this.profile = profile;
        this.registerTime = registerTime;
        this.processingTaskNum = processingTaskNum;
        this.finishedTaskNum = finishedTaskNum;
        this.reputation = reputation;
    }
}
