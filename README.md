# 使用方式
## 必要依赖
1. 数据库，创建名 `spring-security-jwt` 的数据库，配置用户名密码
2. 区块链。因公网速度很慢，所以采用私有链，默认端口`7545`，配置文件可更改
3. 区块链账户。请添加区块链账户。其中 `bezkoder.key` 字段代表管理的秘钥，同时也是合约默认部署账户。

## 使用方法
- 启动 `java -jar you-jar-file=name`，例如: `java -jar demo-1.0.0-SNAPSHOT.jar`

## 说明
- 在 `web3j` 字段下有三个与合约相关的字段，分别是:`uc`、`dc`、`tc`。分别代表用户管理合约、设备管理合约、任务管理合约
填入的字段可为秘钥和地址。若为秘钥则部署新合约，若为地址则加载合约。
- 在字段 `bezkoder`中，`jwtSecret`代表登录后密码加密的`salt`，`86400000`代表凭证存活时间。
- 覆盖默认配置文件方法: 在当前目录创建`config/appication.yml`,已提供 `application-local.yml`示例。

