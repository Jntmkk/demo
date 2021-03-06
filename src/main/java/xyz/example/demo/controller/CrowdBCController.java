package xyz.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
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
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import xyz.example.demo.bean.*;
import xyz.example.demo.contract.DeviceContract;
import xyz.example.demo.contract.TaskContract;
import xyz.example.demo.contract.UserContract;
import xyz.example.demo.models.CrowdBCTask;
import xyz.example.demo.models.DeployedContractInfo;
import xyz.example.demo.models.IotInfo;
import xyz.example.demo.models.User;
import xyz.example.demo.repository.CrowdBCTaskRepository;
import xyz.example.demo.repository.DeployedContractInfoRepository;
import xyz.example.demo.repository.IotInfoRepository;
import xyz.example.demo.repository.UserRepository;
import xyz.example.demo.service.BrowserService;
import xyz.example.demo.service.OneNetService;
import xyz.example.demo.service.Web3jService;
import xyz.example.demo.utils.UserTokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
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
    @Autowired
    BrowserService browserService;
    @Autowired
    IotInfoRepository iotInfoRepository;
    @Value("${file.upload-dir}")
    String uploadPath;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");

    public CrowdBCController(Web3jService web3jService, OneNetService oneNetService, UserTokenUtil userTokenUtil, DeployedContractAddress deployedContractAddress) {
        this.web3jService = web3jService;
        this.oneNetService = oneNetService;
        this.userTokenUtil = userTokenUtil;
        this.deployedContractAddress = deployedContractAddress;
    }

    @PostMapping("/file_upload")
    public String upload(@RequestParam("file") MultipartFile uploadFile, @RequestParam("belongsToTask") String belongsToTask, @RequestParam("username") String username, @RequestParam("solution") String solution, RedirectAttributes redirectAttributes, HttpServletRequest request) throws Exception {
        // 在 uploadPath 文件夹中通过日期对上传的文件归类保存
        // 比如：/2019/06/06/cf13891e-4b95-4000-81eb-b6d70ae44930.png
        String format = sdf.format(new Date());
        File folder = new File(uploadPath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
        // 对上传的文件重命名，避免文件重名
        String oldName = uploadFile.getOriginalFilename();
        int i = oldName.lastIndexOf(".");
        String suffix = "";
        if (i != -1)
            suffix = oldName.substring(i);
        String newName = UUID.randomUUID().toString() + suffix;
        try {
            // 文件保存
            uploadFile.transferTo(new File(folder, newName));

            // 返回上传文件的访问路径
            String filePath = request.getScheme() + "://" + request.getServerName()
                    + ":" + request.getServerPort() + "/" + format + newName;
            web3jService.submitReport(username, new TaskReport(BigInteger.valueOf(Integer.valueOf(belongsToTask)), solution, newName));

            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newName;
        //        if (file.isEmpty()) {
//            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
//            return "redirect:uploadResult";
//        }
//        try {
//            ApplicationHome applicationHome = new ApplicationHome(getClass());
//            File rootPath = applicationHome.getDir();
//            File uploadDir = new File(Paths.get(rootPath.getAbsolutePath(),upload).toString());
//            if (!uploadDir.exists())
//                uploadDir.mkdirs();
//            File fileDestination = new File(Paths.get(uploadDir.getAbsolutePath(), file.getOriginalFilename().replace(" ", "_")).toString());
//            FileUtils.copyInputStreamToFile(file.getInputStream(), fileDestination);
//            web3jService.submitReport(username, new TaskReport(BigInteger.valueOf(Integer.valueOf(belongsToTask)), solution, "upload/" + file.getOriginalFilename().replace(" ", "_")));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "redirect:/uploadResult";
    }

    @ApiOperation(value = "获取任务列表", notes = "不加参数默认获取所有任务,可附加参数过滤")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", paramType = "query", required = true, dataType = "String", value = "用户名", defaultValue = ""),
            @ApiImplicitParam(name = "type", paramType = "query", required = false, dataType = "String", value = "接收的任务-received,发布的任务-post"),
            @ApiImplicitParam(name = "status", paramType = "query", required = false, dataType = "String", value = "任务状态")})
    @GetMapping("task")
    public List<CrowdBCTask> getTask(@RequestParam(required = false) Boolean isall, @RequestParam(required = false) String type, @RequestParam(required = false) CrowdBCTask.TaskStatus taskStatus) throws Exception {
        List<CrowdBCTask> tasks = new LinkedList<>();
//        Function function = new Function("getPostTaskList", Arrays.<Type>asList(new Utf8String("admin")), Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<Uint256>>() {
//        }));
//        Transaction transaction = Transaction.createEthCallTransaction("0x9F324Ab92FBAD8D3c36FC9E4674bAf644259b73a", deployedContractAddress.getContractAddress(DeployedContracts.UserContract), FunctionEncoder.encode(function));
//        EthCall send = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST).send();
//        List<Type> decode = FunctionReturnDecoder.decode(send.getValue(), function.getOutputParameters());
//        log.info(JSON.toJSONString(decode));
        String username = userTokenUtil.getUserName();
        if (isall != null && isall) {
            return loadAdditionalInformation(web3jService.getAllTask());
        }
        if (type != null) {
            if (type.equals("post")) {
                tasks = web3jService.getPostedTask(username);
            }
            if (type.equals("received")) {
                tasks = web3jService.getAcceptedTask(username);
            }
        }
        log.info(JSON.toJSONString(tasks));
        if (taskStatus != null) {
            tasks = tasks.stream().filter(task -> {
                if (task.getStatus() != taskStatus)
                    return false;
                else return true;
            }).collect(Collectors.toList());
        }
        return loadAdditionalInformation(tasks);
    }

    private CrowdBCTask loadAdditionalInformation(CrowdBCTask crowdBCTask) {
        IotInfo byPath = iotInfoRepository.findByPath(crowdBCTask.getPointer()).get();
        if (byPath != null) {
            crowdBCTask.setPlatform(byPath.getPlatform());
            crowdBCTask.setDeviceId(byPath.getDeviceId());
            crowdBCTask.setDeviceToken(byPath.getDeviceToken());
        }
        return crowdBCTask;

    }

    private List<CrowdBCTask> loadAdditionalInformation(List<CrowdBCTask> crowdBCTasks) {
        List<CrowdBCTask> list = new LinkedList<>();
        for (CrowdBCTask task : crowdBCTasks)
            list.add(loadAdditionalInformation(task));

        return list;

    }

    @PostMapping("task")
    public String submitTask(@RequestBody @Valid CrowdBCTask crowdBCTask) throws Exception {
        crowdBCTask.setCreateDate(BigInteger.valueOf(new Date().getTime()));
        web3jService.postTask(crowdBCTask, userTokenUtil.getUserName());
        IotInfo iotInfo = IotInfo.create(crowdBCTask.getDeviceId(), crowdBCTask.getDeviceToken(), crowdBCTask.getPlatform(), crowdBCTask.getPointer());
        if (iotInfo.check())
            iotInfoRepository.save(iotInfo);
        return "success";
    }

    @GetMapping("task/report")
    public List<TaskReport> getTaskReport(@RequestParam String taskId) throws Exception {
        return web3jService.getTaskAllReport(BigInteger.valueOf(Integer.valueOf(taskId)));
    }

//    @PostMapping("task/report")
//    public void getTaskReport(@RequestBody TaskReport taskReport) throws Exception {
//        web3jService.submitReport(userTokenUtil.getUserName(), taskReport);
//    }

    @GetMapping("task/acceptance")
    public void acceptTask(@RequestParam String taskId, @RequestParam String deposit) throws Exception {
        if (!web3jService.acceptTask(userTokenUtil.getUserName(), BigInteger.valueOf(Integer.valueOf(taskId)), BigInteger.valueOf(Integer.valueOf(deposit))))
            throw new Exception("接收失败！");
    }

    @GetMapping("transaction")
    public List<EthLog.LogResult> getTransaction() throws Exception {
        return browserService.getUserTXRecord(userTokenUtil.getUserName());
    }

    @GetMapping("balance")
    public BigInteger getBalance() throws Exception {
        return browserService.getUserBalance(userTokenUtil.getUserName());
    }

    @PostMapping("task/evaluation")
    public String evaluation(@RequestBody TaskReportEvaluation taskReportEvaluation) throws ExecutionException, InterruptedException {
        web3jService.evaluateReport(userTokenUtil.getUserName(), taskReportEvaluation);
        return "success";
    }

    @ApiOperation(value = "发送onenet物联网请求")
    @ApiImplicitParams({@ApiImplicitParam(name = "命令", value = "cmd", required = true),
            @ApiImplicitParam(name = "设备ID", value = "deviceId", required = true)})
    @PostMapping("onenet/command")
    public String command(@RequestBody @Valid OneNetCommandWrapper oneNetCommandWrapper) {
        oneNetService.sendCommand(oneNetCommandWrapper.getDeviceId(), oneNetCommandWrapper.getApiKey(), oneNetCommandWrapper.getCmd());
        return oneNetService.getInfo(oneNetCommandWrapper.getDeviceId(), oneNetCommandWrapper.getApiKey());
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
