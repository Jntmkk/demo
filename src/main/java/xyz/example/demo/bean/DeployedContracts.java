package xyz.example.demo.bean;

public enum DeployedContracts {
    UserContract("UserContract"), TaskContract("TaskContract"), DeviceContract("DeviceContract");
    String type;

    private DeployedContracts(String s) {
        type = s;
    }
    public String getValue() {
        return type;
    }
}
