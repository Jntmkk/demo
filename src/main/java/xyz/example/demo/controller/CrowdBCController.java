package xyz.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Array;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.example.demo.contract.DeviceContract;
import xyz.example.demo.contract.TaskContract;
import xyz.example.demo.contract.UserContract;
import xyz.example.demo.models.CrowdBCTask;
import xyz.example.demo.models.DeployedContractInfo;
import xyz.example.demo.models.User;
import xyz.example.demo.repository.CrowdBCTaskRepository;
import xyz.example.demo.repository.DeployedContractInfoRepository;
import xyz.example.demo.repository.UserRepository;
import xyz.example.demo.service.OneNetService;
import xyz.example.demo.utils.UserTokenUtil;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Data
@Api(tags = "处理区块链请求")
@RestController
@RequestMapping("/api/")
public class CrowdBCController {
    @Autowired
    UserTokenUtil userTokenUtil;
    @Autowired
    UserContract userContract;
    @Autowired
    TaskContract taskContract;
    @Autowired
    DeviceContract deviceContract;
    @Autowired
    DeployedContractInfoRepository contractInfoRepository;

    @Autowired
    OneNetService oneNetService;

    public CrowdBCController(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, CrowdBCTaskRepository crowdBCTaskRepository) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.contractGasProvider = contractGasProvider;
//        this.userSummary = userSummary;
        this.crowdBCTaskRepository = crowdBCTaskRepository;
    }

    Web3j web3j;
    Credentials credentials;
    ContractGasProvider contractGasProvider;
    CrowdBCTaskRepository crowdBCTaskRepository;
    @Autowired
    UserRepository userRepository;

    @ApiOperation(value = "获取任务列表", notes = "不加参数默认获取所有任务,可附加参数过滤")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", paramType = "query", required = true, dataType = "String", value = "用户名", defaultValue = ""),
            @ApiImplicitParam(name = "type", paramType = "query", required = false, dataType = "String", value = "接收的任务-received,发布的任务-post"),
            @ApiImplicitParam(name = "status", paramType = "query", required = false, dataType = "String", value = "任务状态")})
    @GetMapping("task")
    public List<CrowdBCTask> getTask(@PathVariable(required = false) String username, @PathVariable(required = false) String type, @PathVariable(required = false) CrowdBCTask.TaskStatus taskStatus) throws Exception {
//        DeployedContractInfo task = contractInfoRepository.findByContractNameOrderByIdDesc("TaskContract").get(0);
//        TaskContract load = TaskContract.load(task.getContractAddress(), web3j, Credentials.create(userTokenUtil.getUser().getPrivateKey()), contractGasProvider);
//        TransactionReceipt send = userContract.getPostTaskList(userTokenUtil.getUserName()).send();
//        web3j.ethCall(n)
        Function function = new Function("getUserReputation", Arrays.<Type>asList(), Arrays.<TypeReference<?>>asList(new TypeReference<Array<Uint8>>() {
                                                                                                                   }
        ));
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(userTokenUtil.getUser().getAddress(), deviceContract.getContractAddress(), null);
        EthCall send = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();
        List<Type> decode = FunctionReturnDecoder.decode(send.getValue(), function.getOutputParameters());
        return crowdBCTaskRepository.findAll();
    }

    @PostMapping("task")
    public String submitTask(@RequestBody @Valid CrowdBCTask crowdBCTask) throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        crowdBCTask.setCreatedDate(new Date());

        deployTaskContract(crowdBCTask, username);
        crowdBCTaskRepository.save(crowdBCTask);
        return "success";
    }

    @ApiOperation(value = "发送onenet物联网请求")
    @ApiImplicitParams({@ApiImplicitParam(name = "命令", value = "cmd", required = true),
            @ApiImplicitParam(name = "设备ID", value = "deviceId", required = true)})
    @PostMapping("onenet/command")
    public JSONObject command(@PathVariable String cmd, @PathVariable String deviceId) {
        return oneNetService.sendCommand(deviceId, cmd);
    }

    private void deployTaskContract(CrowdBCTask crowdBCTask, String username) throws Exception {
        Optional<User> byUsername = userRepository.findByUsername(username);
        User user = byUsername.get();
        log.info("deploy user:" + username + "`s contract");
        DeployedContractInfo deployedContractInfo = contractInfoRepository.findByContractNameOrderByIdDesc("UserContract").get(0);
        DeployedContractInfo task = contractInfoRepository.findByContractNameOrderByIdDesc("TaskContract").get(0);
        TaskContract load = TaskContract.load(task.getContractAddress(), web3j, Credentials.create(userTokenUtil.getUser().getPrivateKey()), contractGasProvider);
//        taskContract.postTask(username,crowdBCTask.getDescription(),crowdBCTask.getDeposit(),crowdBCTask.getDeadline(),crowdBCTask.getMaxWorkerNum(),crowdBCTask.getMinReputation(),crowdBCTask.getTaskType(),crowdBCTask.getPointer()).send();
        load.postTask(username, crowdBCTask.getDescription(), crowdBCTask.getDeposit(), crowdBCTask.getDeadline(), crowdBCTask.getMaxWorkerNum(), crowdBCTask.getMinReputation(), crowdBCTask.getTaskType(), crowdBCTask.getPointer()).send();
    }
}
