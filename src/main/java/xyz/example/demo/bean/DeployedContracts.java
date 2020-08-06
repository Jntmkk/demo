package xyz.example.demo.bean;

public enum DeployedContracts {
    USER_CONTRACT("UserContract"), TASK_CONTRACT("TaskContract"), DEVICE_CONTRACT("DeviceContract");

    DeployedContracts(String s) {

    }

    public String getValue() {
        return this.toString();
    }
}
