package xyz.example.demo.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Slf4j
@Entity
public class DeployedContractInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ApiModelProperty(name = "contract name")
    @NotNull
    String contractName;
    @NotNull
    String contractAddress;
    String managerAddress;
    @NotNull
    String managerPrivateKey;
}
