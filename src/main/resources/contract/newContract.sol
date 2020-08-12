pragma solidity ^0.6.0;

contract UserContract{
    //用户结构体
    struct User{
        address payable addr; //用户地址
        string username; //用户名
        bytes32 password; //密码
        string profile; //简介
        uint registerTime; //注册时间
        uint processTaskNum; //发布任务数量
        uint finishTaskNum; //接收任务数量
        uint reputation; //声誉
        uint256[] postTaskList; //用户已发布任务id列表
        uint256[] acceptTaskList; //用户已接受任务id列表
    }
    
    address managerAddr; //管理员地址
    uint maxRegisters = 99999; //注册用户上线
    uint registerNum = 0; //已注册用户数量
    uint reputationSum = 0; //总声誉
    address[] userAddrList; //注册用户地址列表
    mapping(string => User) userPool; //用户名到用户结构体的映射，用于保存注册用户
    
    //构造函数，部署合约的人即为管理员
    constructor() public {
        managerAddr = msg.sender;
    }
    
    //检查用户是否已注册，已注册返回true
    function isRegister(address addr,  string memory username) public view returns (bool) {
        for(uint i = 0; i < userAddrList.length; i++){
            if(userAddrList[i] == addr){
                return true;
            }
        }
        return userPool[username].addr != address(0);
    }
    
    //用户注册
    function register(address payable addr, string memory username, string memory password, string memory profile) public{
        // require(msg.sender == managerAddr, "msg.sender must be manager!");
        require(registerNum < maxRegisters);
        require(isRegister(addr,username) == false, "User has been registered!");
        
        //don not know which is right: storage or memory?memory! storage will not deploy contract!
        uint256[] memory postTaskList;
        uint256[] memory acceptTaskList;
        userPool[username] = User(addr,username,keccak256(bytes(password)),profile,now,0,0,60,postTaskList,acceptTaskList);
        userAddrList.push(addr);
        registerNum++;
        reputationSum += 60;
    }
    
    //用户登录，成功返回true
    function login(string memory username, string memory password) public view returns (bool) {
        return userPool[username].password == keccak256(bytes(password));
    }
    
    //获取用户信息，参数：用户名，返回：地址，简介，注册时间，正在处理任务数量，已完成任务数量，声誉
    function getUserInformation(string memory username) public view returns (address, string memory, uint, uint, uint, uint) {
        User memory user = userPool[username];
        return (user.addr,user.profile, user.registerTime, user.processTaskNum, user.finishTaskNum, user.reputation);
    }
    
    //更新密码
    function updatePassword(string memory username,string memory newPassword) public {
        // require(msg.sender == managerAddr, "msg.sender must be manager!");
        userPool[username].password = keccak256(bytes(newPassword));
    }
    
    //更新简介
    function updateProfile(string memory username, string memory newProfile) public {
        // require(msg.sender == managerAddr, "msg.sender must be manager!");
        userPool[username].profile = newProfile;
    }
    
    //获取管理员地址，一般不需要用到
    function getManagerAddr() public view returns (address){
        return managerAddr;
    }
    
    //TaskContract call:下面的函数大部分是供任务合约调用

    //检查用户名和地址是否对应，后端不需要用到
    function checkAddr(string memory username, address addr) public view returns (bool) {
        return userPool[username].addr == addr;
    }
    
    //获取用户名对应的地址，后端不需要用到
    function getUserAddr(string memory username) public view returns (address payable) {
        return userPool[username].addr;
    }
    
    //获取平均声誉，后端不需要用到
    function getReputationAvg() public view returns (uint) {
        return reputationSum/registerNum;
    }
    
    //获取用户声誉，后端不需要用到，前面有个函数可以得到用户详情
    function getUserReputation(string memory username) public view returns (uint) {
        return userPool[username].reputation;
    }
    
    //添加用户已发布任务，无需后端调用
    function addPostTask(string memory username,uint256 taskId) public {
        require(userPool[username].addr != address(0));
        // address TaskContractAddr = msg.sender;
        User storage postMan = userPool[username];
        postMan.postTaskList.push(taskId);
    }
    
    //获取用户已发布任务id列表
    function getPostTaskList(string memory username) public view returns (uint256[] memory) {
        return userPool[username].postTaskList;
    }
    
    //添加用户已接受任务，无需后端调用
    function addAcceptTask(string memory username,uint256 taskId) public {
        require(userPool[username].addr != address(0));
        // address TaskContractAddr = msg.sender;
        User storage acceptMan = userPool[username];
        acceptMan.acceptTaskList.push(taskId);
    }
    
    //获取用户已接受任务id列表
    function getAcceptTask(string memory username) public view returns (uint256[] memory) {
        return userPool[username].acceptTaskList;
    }
    
    //更新用户声誉，无需后端调用
    function updateUserReputation(string memory username, uint newReputation) public {
        require(userPool[username].addr != address(0));
        require(newReputation >= 0 && newReputation <= 100,"new reputation wrong");
        // for(uint i = 0; i < taskList.length; i++){
        //     if(msg.sender == taskList[i]) {
                reputationSum += (newReputation - userPool[username].reputation);
                userPool[username].reputation = newReputation;
        //         break;
        //     }
        // }
    }
    
    //更新用户已完成任务数量，无需后端调用
    function updateFinishTaskNum(string memory username) public {
        require(userPool[username].addr != address(0));
        // for(uint i = 0; i < taskList.length; i++) {
        //     if(msg.sender == taskList[i]) {
                --userPool[username].processTaskNum;
                ++userPool[username].finishTaskNum;
        //         break;
        //     }
        // }
    } 
    
}

contract TaskContract{
    //任务状态枚举类
    enum Status {
        Pending, //处理中
        Unaccepted, //未接收
        Accepted, //已接收
        Evaluating, //评估中
        Completed //已完成
    }
    
    //任务结构体
    struct Task{
        uint256 id; //任务id
        string title;
        string posterName; //发布者名称
        address payable posterAddr; //发布者地址
        string description; //任务描述
        uint reward; //报酬
        uint deposit; //押金
        uint deadline; //截至日期
        uint maxWorkerNum; //最大工作者数量
        uint currentWorkerNum; //当前工作者数量
        uint minReputation; //最小声誉
        uint taskType; //任务类型
        Status status; //任务状态
        string pointer; //任务所需文件保存路径
        
        uint solutionNum; //解决方案数量
        uint evaluatedNum; //已评估数量
        
        // don't make it complex:mapping in struct,don't do this!
        // mapping(uint256 => Solution) solutionPool;
        uint256[] solutionList; //解决方案id列表

        uint createDate;
    }
    
    //解决方案结构体
    struct Solution{
        uint256 id; //解决方案id
        uint256 taskId; //任务id
        address payable workerAddr; //工作者地址
        string workerName; //工作者姓名
        string solution; //解决方案描述
        string pointer; //解决方案文件保存路径
        uint submitTime; //提交时间
        uint level; //解决方案评估级别
    }
    
    UserContract reg; //用户合约
    address managerAddr; //管理员地址
   
    uint256[] taskIdList;
    uint256 taskNum; //任务数量
    mapping(uint256 => Task) taskPool; //任务id到任务结构体的映射
    uint256 solutionAllNum = 0; //解决方案总数
    mapping(uint256 => Solution) solutionPool; //解决方案id到解决方案的映射

    //构造函数，需要传入用户合约的地址，而且需要管理员才能部署
    constructor(address regAddr) public{
        reg = UserContract(regAddr);
        require(msg.sender == reg.getManagerAddr(),"must be manager");
        managerAddr = msg.sender;
        taskNum = 0;
    }
    
    //发布任务，参数：title,发布者姓名，描述，押金，截止日期，最大工作者数，最小声誉，任务类型，任务保存文件路径,创建日期
    function postTask(string memory title, string memory posterName, string memory description, uint deposit, 
        uint deadline, uint maxWorkerNum, uint minReputation, uint taskType, string memory pointer) public payable{
            address payable posterAddr = msg.sender;
            require(reg.checkAddr(posterName, posterAddr) == true,"posterAddr must equal with posterName!");
            uint reward = msg.value;//the money!!!
            
            // struct Task{
            //     uint256 id;
            //     string posterName;
            //     address posterAddr;
            //     string description;
            //     uint reward;
            //     uint deposit;
            //     uint deadline;
            //     uint maxWorkerNum;
            //     uint currentWorkerNum;
            //     uint minReputation;
            //     uint taskType;
            //     Status status;
            //     string pointer;
                
            //     uint solutionNum;
            //     uint evaluatedNum;
                
            //     mapping(uint256 => Solution) solutionPool;
            // }
            uint256[] memory solutionList;
            taskPool[taskNum] = Task(taskNum,title,posterName,posterAddr,description,reward,deposit,deadline,maxWorkerNum,0,minReputation,taskType,Status.Pending,pointer,0,0,solutionList,now);
            // The origin of the problem
            reg.addPostTask(taskPool[taskNum].posterName,taskNum);
            uint taskId = taskNum;
            updateStatus(taskId);
            taskIdList.push(taskId);
            taskNum++;
            
    }
    
    //接收任务，参数：工作者名字，任务id
    function acceptTask(string memory workerName,uint256 taskId) public payable {
        Task storage task = taskPool[taskId];
        string memory returnMsg = checkAcceptCondition(workerName,taskId);
        require(keccak256(bytes(returnMsg)) == keccak256(bytes("conglatulation")), returnMsg);
        require(reg.checkAddr(workerName, msg.sender) == true,"Addr must equal with workerName!");
        require(msg.value > task.deposit, "msg.value must larger than task.deposit");
        // workerList.push(Worker(msg.sender, workerName, "", "", 0, 0));
        reg.addAcceptTask(workerName,taskId);
        task.currentWorkerNum ++;
        
        //wait to modifiy
        updateStatus(taskId);
    }
    
    //获取任务信息，返回: title, 任务描述，报酬，押金，截至日期，最大工作者数，当前工作者数，最低所需声誉，任务类型，任务状态，任务保存文件路径
    function getTaskInformation(uint256 taskId) public returns  (string memory, string memory, uint, uint, uint, uint, uint, uint, uint, Status, string memory ) {
        // wait to modify
        updateStatus(taskId);
        Task memory task = taskPool[taskId];
        return (task.title, task.description, task.reward, task.deposit, task.deadline, task.maxWorkerNum, task.currentWorkerNum,task.minReputation, task.taskType, task.status, task.pointer);
    }
    
    //获取任务日期
    function getTaskCreateDate(uint256 taskId) public returns  (uint) {
        // wait to modify
        updateStatus(taskId);
        Task memory task = taskPool[taskId];
        return (task.createDate);
    }
    
    //所有任务
    function getAllTaskId() public view returns (uint[] memory ){
        return taskIdList;
    }
    
    
    //所有solution
    function getAllTaskSolutionList(uint256 taskId) public view returns (uint[] memory ){
        Task memory task = taskPool[taskId];
        uint256[] memory solutionList = task.solutionList;
        return solutionList;
    }
    
    //检查是否满足接收条件，满足会返回字符串“conglatulation”
    function checkAcceptCondition(string memory workerName,uint256 taskId) public returns (string memory) {
        updateStatus(taskId);
        Task memory task = taskPool[taskId];
        
        if( keccak256(abi.encodePacked((task.posterName))) == keccak256(abi.encodePacked((workerName))) )
            return "not poster";
        if(now >= task.deadline)
            return "deadline is out of date";
        if(task.status != Status.Unaccepted)
            return "Status must be Unaccepted";
        if(msg.sender.balance < task.deposit)
            return "balance must larger than deposit";
        if(reg.getUserReputation(workerName) < task.minReputation )
            return "reputation is not enough";
        uint256[] memory userAcceptedList = reg.getAcceptTask(workerName);
        for(uint i = 0; i < userAcceptedList.length; i++){
            if(taskId == userAcceptedList[i])
                return "you have been Accepted";
        }
        return "conglatulation";
    }
    
    //检查是否满足提交任务条件，满足则返回true
    function checkSubmitCondition(string memory workerName,uint256 taskId) public view returns (bool) {
        uint256[] memory userAcceptedList = reg.getAcceptTask(workerName);
        for(uint i = 0;i < userAcceptedList.length; i++){
            if(taskId == userAcceptedList[i])
                return true;
        }
        return false;
    }

    //提交解决方案，参数：工作者名字，解决方案描述，解决方案文件保存路径，任务id
    function submitSolution(string memory workerName, string memory solution, string memory pointer,uint256 taskId) public {
        Task storage task = taskPool[taskId];
        require(checkSubmitCondition(workerName,taskId) == true, "you has not Accepted the task");
        require(msg.sender == reg.getUserAddr(workerName), "msg.sender != managerAddr");
        require(now <= task.deadline, "Out of deadline");
        task.solutionNum++;
        solutionAllNum++;
        uint256 solutionId = solutionAllNum;
        solutionPool[solutionId] = Solution(solutionId,taskId,reg.getUserAddr(workerName),workerName,solution,pointer,now,0);
        uint256[] storage solutionList = task.solutionList;
        solutionList.push(solutionId);
        updateStatus(taskId);
    }
    
    //评估解决方案，参数：任务id，解决方案id，评估级别
    function evaluateSolution(uint256 taskId, uint256 solutionId,  uint level) public {
        Task storage task = taskPool[taskId];
        Solution storage solution = solutionPool[solutionId];
        
        require(msg.sender == task.posterAddr, "must be posterAddrAddr");
        require(solution.level == 0, "has been evaluated!");
        solution.level = level;

        //
        string storage workerName = solution.workerName;
        uint rep = reg.getUserReputation(workerName);
        // 计算奖励 wait to modify? 
        uint sendReward = task.deposit;
        // 平均奖励
        uint reward = task.reward / task.maxWorkerNum + task.deposit;
        // 高评估 高信誉 => 加信誉 + 奖励
        if (level >= 5 && rep >= reg.getReputationAvg()) {
            reg.updateUserReputation(workerName, rep + 1);
            sendReward += reward;
        }

        // 低评估 高信誉 => 减信誉
        else if (level < 5 && rep >= reg.getReputationAvg() + 1) {
            reg.updateUserReputation(workerName, rep - 1);
        }

        // 低评估 平均信誉 => 阈值
        else if (level < 5 && rep == reg.getReputationAvg()) {
            reg.updateUserReputation(workerName, 60);
        }

        // 低信誉 => 加信誉
        else if (rep < reg.getReputationAvg()){
            reg.updateUserReputation(workerName, rep + 1);
        }

        // 发送奖励 wait to modify
        solution.workerAddr.transfer(reward);

        // 更新数量
        reg.updateFinishTaskNum(workerName);

        // 更新评估数量
        task.evaluatedNum++;

        // 更新任务状态
        updateStatus(taskId);
    }
    
    //获取某个任务的解决方案长度，参数：任务id
    function getSolutionLength(uint256 taskId) public view returns (uint) {
        return taskPool[taskId].solutionNum;
    }
    
    //获取解决方案详细信息，参数：解决方案id
    function getSolutionInfo(uint solutionId) public view returns (string memory, string memory, string memory, uint, uint){
        Solution memory solution = solutionPool[solutionId];
        return (solution.workerName, solution.solution, solution.pointer, solution.submitTime, solution.level);
    }
    
    //更新状态，后端无需调用
    function updateStatus(uint256 taskId) private returns (Status) {
        Task storage task = taskPool[taskId];
        
        task.status = Status.Pending; // 0 等待......

        // 未满人数 && 未到时间

        if(task.currentWorkerNum < task.maxWorkerNum && now <= task.deadline)

            task.status = Status.Unaccepted; // 1 未领取完

        // 已满人数 && 未到时间

        if(task.currentWorkerNum == task.maxWorkerNum && now <= task.deadline)

            task.status = Status.Accepted; // 2 已领取完

        // 评估未完 && 到达时间

        if( task.solutionNum >  task.evaluatedNum && now >= task.deadline)

            task.status = Status.Evaluating; // 3 评估中

        // (评估完毕 && 到达时间) || (评估完毕 && 到达人数)

        if(( task.solutionNum ==  task.evaluatedNum && now >= task.deadline) || 

            ( task.solutionNum ==  task.evaluatedNum &&  task.evaluatedNum == task.maxWorkerNum))

            task.status = Status.Completed; // 4 已完成

    }
    
    //返还剩余报酬
    function returnBalance(uint256 taskId) public payable{
        // require(msg.sender == managerAddr);
        Task memory task = taskPool[taskId];
        updateStatus(taskId);
        if(task.status == Status.Completed){
            // task.posterAddr.transfer(address(this).balance);
            // wait to modify
            uint returnNum = task.maxWorkerNum - task.currentWorkerNum;
            uint returnReward = (task.reward/task.maxWorkerNum)*returnNum;
            task.posterAddr.transfer(returnReward);
        }
    }
}



contract DeviceContract {
    //设备结构体
    struct Device {
        string deviceName; //设备名
        uint deviceId; //only one to ，设备id
        string deviceType; //设备类型
        string APIurl; //测试设备链接
        string accessPlatform; //云平台名字
        string description; //描述
        DeviceTestType testType; //测试类型：线上、线下
    }

    //测试类型，
    enum DeviceTestType {
        Online, //线上
        Offline //线下
    }

    mapping(uint => Device) devicePool; //设备id到设备结构体的映射
    uint deviceNum; //设备数量
    
    //结构体
    constructor() public {
        deviceNum = 0;
    }
    
    //添加设备，参数：设备id，设备名称，类型，api链接，云平台名字，描述，测试类型
    function addDevice(uint deviceId, string memory deviceName, string memory deviceType,
        string memory APIurl, string memory accessPlatform, string memory description, uint testType) public{
            if(testType == 0)
                devicePool[deviceId] = Device(deviceName,deviceId,deviceType,APIurl,accessPlatform,description,DeviceTestType.Online);
            else
                devicePool[deviceId] = Device(deviceName,deviceId,deviceType,APIurl,accessPlatform,description,DeviceTestType.Online);
            deviceNum++;
    }
    
    //获取设备信息
    function getDeviceInfo(uint deviceId) public view returns(uint, string memory, string memory, string memory, string memory, string memory, DeviceTestType) {
        Device memory device = devicePool[deviceId];
        return(device.deviceId,device.deviceName,device.deviceType,device.APIurl,device.accessPlatform,device.description,device.testType);
    }
    
    //获取设备数
    function getDeviceNum() public view returns(uint) {
        return deviceNum;
    }
    
}







