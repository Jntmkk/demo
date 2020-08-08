package xyz.example.demo.controller;

import com.alibaba.fastjson.JSON;
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
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
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
import xyz.example.demo.bean.DeployedContractAddress;
import xyz.example.demo.bean.DeployedContracts;
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
import xyz.example.demo.service.Web3jService;
import xyz.example.demo.utils.UserTokenUtil;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Data
@Api(tags = "处理区块链请求")
@RestController
@RequestMapping("/api/")
public class CrowdBCController {
    Web3jService web3jService;
    OneNetService oneNetService;
    UserTokenUtil userTokenUtil;
    DeployedContractAddress deployedContractAddress;
    @Autowired
    Web3j web3j;
    public CrowdBCController(Web3jService web3jService, OneNetService oneNetService, UserTokenUtil userTokenUtil, DeployedContractAddress deployedContractAddress) {
        this.web3jService = web3jService;
        this.oneNetService = oneNetService;
        this.userTokenUtil = userTokenUtil;
        this.deployedContractAddress = deployedContractAddress;
    }





    @ApiOperation(value = "获取任务列表", notes = "不加参数默认获取所有任务,可附加参数过滤")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", paramType = "query", required = true, dataType = "String", value = "用户名", defaultValue = ""),
            @ApiImplicitParam(name = "type", paramType = "query", required = false, dataType = "String", value = "接收的任务-received,发布的任务-post"),
            @ApiImplicitParam(name = "status", paramType = "query", required = false, dataType = "String", value = "任务状态")})
    @GetMapping("task")
    public List<CrowdBCTask> getTask(@RequestParam(required = false) String username, @RequestParam(required = false) String type, @RequestParam(required = false) CrowdBCTask.TaskStatus taskStatus) throws Exception {
        List<CrowdBCTask> tasks = new LinkedList<>();
        Function function=new Function("getPostTaskList",Arrays.<Type>asList(new Utf8String("admin")),Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {
        }));
        Transaction transaction = Transaction.createEthCallTransaction("0x9F324Ab92FBAD8D3c36FC9E4674bAf644259b73a",deployedContractAddress.getContractAddress(DeployedContracts.UserContract), FunctionEncoder.encode(function));
        EthCall send = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
        List<Type> decode = FunctionReturnDecoder.decode(send.getValue(), function.getOutputParameters());
        log.info(JSON.toJSONString(decode));
//        if (username == null) {
//            return web3jService.getAll();
//        }
//        if (type != null) {
//            if (type.equals("post")) {
//                tasks = web3jService.getPostedTask(username);
//            }
//            if (type.equals("received")) {
//                tasks = web3jService.getAcceptedTask(username);
//            }
//        }
//        if (taskStatus != null) {
//            tasks = tasks.stream().filter(task -> {
//                if (task.getStatus() != taskStatus)
//                    return false;
//                else return true;
//            }).collect(Collectors.toList());
//        }
        return tasks;
    }

    @PostMapping("task")
    public String submitTask(@RequestBody @Valid CrowdBCTask crowdBCTask) throws Exception {
        web3jService.submitTask(userTokenUtil.getUserName(), crowdBCTask);
        return "success";
    }

    @ApiOperation(value = "发送onenet物联网请求")
    @ApiImplicitParams({@ApiImplicitParam(name = "命令", value = "cmd", required = true),
            @ApiImplicitParam(name = "设备ID", value = "deviceId", required = true)})
    @PostMapping("onenet/command")
    public JSONObject command(@PathVariable String cmd, @PathVariable String deviceId) {
        return oneNetService.sendCommand(deviceId, cmd);
    }

//    private void deployTaskContract(CrowdBCTask crowdBCTask, String username) throws Exception {
//        Optional<User> byUsername = userRepository.findByUsername(username);
//        User user = byUsername.get();
//        log.info("deploy user:" + username + "`s contract");
//        DeployedContractInfo deployedContractInfo = contractInfoRepository.findByContractNameOrderByIdDesc("UserContract").get(0);
//        DeployedContractInfo task = contractInfoRepository.findByContractNameOrderByIdDesc("TaskContract").get(0);
//        TaskContract load = TaskContract.load(task.getContractAddress(), web3j, Credentials.create(userTokenUtil.getUser().getPrivateKey()), contractGasProvider);
////        taskContract.postTask(username,crowdBCTask.getDescription(),crowdBCTask.getDeposit(),crowdBCTask.getDeadline(),crowdBCTask.getMaxWorkerNum(),crowdBCTask.getMinReputation(),crowdBCTask.getTaskType(),crowdBCTask.getPointer()).send();
//        load.postTask(crowdBCTask.getTitle(),username, crowdBCTask.getDescription(), crowdBCTask.getDeposit(), crowdBCTask.getDeadline(), crowdBCTask.getMaxWorkerNum(), crowdBCTask.getMinReputation(), crowdBCTask.getTaskType(), crowdBCTask.getPointer()).send();
//    }
}
