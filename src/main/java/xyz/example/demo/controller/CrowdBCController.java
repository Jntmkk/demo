package xyz.example.demo.controller;

import ch.qos.logback.core.pattern.color.BlackCompositeConverter;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.utils.Convert;
import xyz.example.demo.contract.Register;
import xyz.example.demo.contract.TaskManagement;
import xyz.example.demo.contract.UserSummary;
import xyz.example.demo.models.CrowdBCTask;
import xyz.example.demo.models.DeployedContractInfo;
import xyz.example.demo.models.User;
import xyz.example.demo.repository.CrowdBCTaskRepository;
import xyz.example.demo.repository.DeployedContractInfoRepository;
import xyz.example.demo.repository.UserRepository;
import xyz.example.demo.service.OneNetService;
import xyz.example.demo.service.UserDetailsService;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Data
@Api(tags = "处理区块链请求")
@RestController
@RequestMapping("/api/")
public class CrowdBCController {
    @Autowired
    DeployedContractInfoRepository contractInfoRepository;
    @Autowired
    Register register;
    @Autowired
    OneNetService oneNetService;
    public CrowdBCController(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, UserSummary userSummary, CrowdBCTaskRepository crowdBCTaskRepository) {
        this.web3j = web3j;
        this.credentials = credentials;
        this.contractGasProvider = contractGasProvider;
        this.userSummary = userSummary;
        this.crowdBCTaskRepository = crowdBCTaskRepository;
    }
    Web3j web3j;
    Credentials credentials;
    ContractGasProvider contractGasProvider;
    UserSummary userSummary;
    CrowdBCTaskRepository crowdBCTaskRepository;
    @Autowired
    UserRepository userRepository;

    @ApiOperation(value = "获取任务列表", notes = "不加参数默认获取所有任务,可附加参数过滤")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", paramType = "query", required = true, dataType = "String", value = "用户名", defaultValue = ""),
            @ApiImplicitParam(name = "type", paramType = "query", required = false, dataType = "String", value = "接收的任务-received,发布的任务-post"),
            @ApiImplicitParam(name = "status", paramType = "query", required = false, dataType = "String", value = "任务状态")})
    @GetMapping("task")
    public List<CrowdBCTask> getTask(@PathVariable(required = false) String username, @PathVariable(required = false) String type, @PathVariable(required = false) CrowdBCTask.TaskStatus taskStatus) {

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

    @PostMapping("onenet/command")
    public JSONObject command(@PathVariable String cmd, @PathVariable String deviceId) {
        return null;
    }

    private void deployTaskContract(CrowdBCTask crowdBCTask, String username) throws Exception {
        Optional<User> byUsername = userRepository.findByUsername(username);
        User user = byUsername.get();
        BigInteger balance = web3j.ethGetBalance(user.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance();
        log.info("balance : " + balance);
//        Transfer.sendFunds(web3j, Credentials.create(user.getPrivateKey()), "0x1d2Ed2151ebb0101c85Af0a33f4768052C2D52Ca", BigDecimal.valueOf(1.0), Convert.Unit.ETHER).send();
        log.info("deploy user:" + username + "`s contract");
        DeployedContractInfo deployedContractInfo = contractInfoRepository.findByContractNameOrderByIdDesc("Register").get(0);
        TaskManagement send = TaskManagement.deploy(web3j, Credentials.create(user.getPrivateKey()), contractGasProvider, BigInteger.valueOf(70L), deployedContractInfo.getContractAddress(), deployedContractInfo.getManagerAddress(), username, crowdBCTask.getDescription(), crowdBCTask.getDeposit(), crowdBCTask.getDeadline(), crowdBCTask.getMaxWorkerNum(), crowdBCTask.getMinReputation(), crowdBCTask.getTaskType(), crowdBCTask.getPointer()).send();

    }
}
