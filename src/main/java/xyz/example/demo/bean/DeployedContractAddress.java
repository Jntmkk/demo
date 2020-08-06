package xyz.example.demo.bean;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DeployedContractAddress {
    Map<String, String> maps = new HashMap<>();

    public String getContractAddress(String contractName) {
        return maps.get(contractName);
    }

    public String getContractAddress(DeployedContracts deployedContracts) {
        return maps.get(deployedContracts.getValue());
    }

    public void setContractAddress(String contractName, String contractAddress) {
        maps.put(contractName, contractAddress);
    }

    public void setContractAddress(DeployedContracts deployedContracts, String contractAddress) {
        maps.put(deployedContracts.getValue(), contractAddress);
    }
}
