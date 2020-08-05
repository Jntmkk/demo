pragma solidity ^0.6.0;

contract UserContract{
    struct User{
        address payable addr;
        string username;
        bytes32 password;
        string profile;
        uint registerTime;
        uint processTaskNum;
        uint finishTaskNum;
        uint reputation;
        uint256[] postTaskList;
        uint256[] acceptTaskList;
    }
    
    address managerAddr;
    uint maxRegisters = 99999;
    uint registerNum = 0;
    uint reputationSum = 0;
    address[] userAddrList;
    mapping(string => User) userPool;
    
    constructor() public {
        managerAddr = msg.sender;
    }
    
    function isRegister(address addr,  string memory username) public view returns (bool) {
        for(uint i = 0; i < userAddrList.length; i++){
            if(userAddrList[i] == addr){
                return true;
            }
        }
        return userPool[username].addr != address(0);
    }
    
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
    
    function login(string memory username, string memory password) public view returns (bool) {
        return userPool[username].password == keccak256(bytes(password));
    }
    
    function getUserInformation(string memory username) public view returns (address, string memory, uint, uint, uint, uint) {
        User memory user = userPool[username];
        return (user.addr,user.profile, user.registerTime, user.processTaskNum, user.finishTaskNum, user.reputation);
    }
    
    function updatePassword(string memory username,string memory newPassword) public {
        // require(msg.sender == managerAddr, "msg.sender must be manager!");
        userPool[username].password = keccak256(bytes(newPassword));
    }
    
    function updateProfile(string memory username, string memory newProfile) public {
        // require(msg.sender == managerAddr, "msg.sender must be manager!");
        userPool[username].profile = newProfile;
    }
    
    function getManagerAddr() public view returns (address){
        return managerAddr;
    }
    
    //TaskContract call:
    function checkAddr(string memory username, address addr) public view returns (bool) {
        return userPool[username].addr == addr;
    }
    
    function getUserAddr(string memory username) public view returns (address payable) {
        return userPool[username].addr;
    }
    
    function getReputationAvg() public view returns (uint) {
        return reputationSum/registerNum;
    }
    
    function getUserReputation(string memory username) public view returns (uint) {
        return userPool[username].reputation;
    }
    
    function addPostTask(string memory username,uint256 taskId) public {
        require(userPool[username].addr != address(0));
        // address TaskContractAddr = msg.sender;
        User storage postMan = userPool[username];
        postMan.postTaskList.push(taskId);
    }
    
    function getPostTaskList(string memory username) public view returns (uint256[] memory) {
        return userPool[username].postTaskList;
    }
    
     function addAcceptTask(string memory username,uint256 taskId) public {
        require(userPool[username].addr != address(0));
        // address TaskContractAddr = msg.sender;
        User storage acceptMan = userPool[username];
        acceptMan.acceptTaskList.push(taskId);
    }
    
    function getAcceptTask(string memory username) public view returns (uint256[] memory) {
        return userPool[username].acceptTaskList;
    }
    
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
    enum Status {
        Pending,
        Unaccepted,
        Accepted,
        Evaluating,
        Completed
    }
    
    struct Task{
        uint256 id;
        string posterName;
        address payable posterAddr;
        string description;
        uint reward;
        uint deposit;
        uint deadline;
        uint maxWorkerNum;
        uint currentWorkerNum;
        uint minReputation;
        uint taskType;
        Status status;
        string pointer;
        
        uint solutionNum;
        uint evaluatedNum;
        
        // don't make it complex:mapping in struct,don't do this!
        // mapping(uint256 => Solution) solutionPool;
        uint256[] solutionList;
    }
    
    struct Solution{
        uint256 id;
        uint256 taskId;
        address payable workerAddr;
        string workerName;
        string solution;
        string pointer;
        uint submitTime;
        uint level;
    }
    
    UserContract reg;
    address managerAddr;
   
    uint256 taskNum;
    mapping(uint256 => Task) taskPool;
    uint256 solutionAllNum = 0;
    mapping(uint256 => Solution) solutionPool;

    constructor(address regAddr) public{
        reg = UserContract(regAddr);
        require(msg.sender == reg.getManagerAddr(),"must be manager");
        managerAddr = msg.sender;
        taskNum = 0;
    }
    
    function postTask(string memory posterName,string memory description, uint deposit, 
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
            taskPool[taskNum] = Task(taskNum,posterName,posterAddr,description,reward,deposit,deadline,maxWorkerNum,0,minReputation,taskType,Status.Pending,pointer,0,0,solutionList);
            // The origin of the problem
            reg.addPostTask(taskPool[taskNum].posterName,taskNum);
            uint taskId = taskNum;
            updateStatus(taskId);
            taskNum++;
            
    }
        
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
    
    function getTaskInformation(uint256 taskId) public returns  (string memory, uint, uint, uint, uint, uint, uint, uint, Status, string memory) {
        // wait to modify
        updateStatus(taskId);
        Task memory task = taskPool[taskId];
        return (task.description, task.reward, task.deposit, task.deadline, task.maxWorkerNum, task.currentWorkerNum,task.minReputation, task.taskType, task.status, task.pointer);
    }
    
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
    
    function checkSubmitCondition(string memory workerName,uint256 taskId) public view returns (bool) {
        uint256[] memory userAcceptedList = reg.getAcceptTask(workerName);
        for(uint i = 0;i < userAcceptedList.length; i++){
            if(taskId == userAcceptedList[i])
                return true;
        }
        return false;
    }
    
    function submitSolution(string memory workerName, string memory solution, string memory pointer,uint256 taskId) public {
        Task storage task = taskPool[taskId];
        require(checkSubmitCondition(workerName,taskId) == true, "you has not Accepted the task");
        require(msg.sender == reg.getUserAddr(workerName), "msg.sender != managerAddr");
        require(now <= task.deadline, "Out of deadline");
        task.solutionNum++;
        solutionAllNum++;
        uint256 solutionId = solutionAllNum;
        solutionPool[solutionId] = Solution(solutionId,taskId,reg.getUserAddr(workerName),workerName,solution,pointer,now,0);
        updateStatus(taskId);
    }
    
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
    
    function getSolutionLength(uint256 taskId) public view returns (uint) {
        return taskPool[taskId].solutionNum;
    }
    
    function getSolutionInfo(uint solutionId) public view returns (string memory, string memory, string memory, uint, uint){
        Solution memory solution = solutionPool[solutionId];
        return (solution.workerName, solution.solution, solution.pointer, solution.submitTime, solution.level);
    }
    
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

struct Device {
    string deviceName;
    uint deviceId;//only one to 
    string deviceType;
    string APIurl;
    string accessPlatform;
    string description;
    DeviceTestType testType;
}

enum DeviceTestType {
    Online,
    Offline
}

contract DeviceTestManagement {
    mapping(uint => Device) devicePool;
    uint deviceNum;
    // Task task;
    // TaskManagement taskMagContract;
    
    constructor() public {
        deviceNum = 0;
    }
    
    function addDevice(uint deviceId, string memory deviceName, string memory deviceType,
        string memory APIurl, string memory accessPlatform, string memory description, uint testType) public{
            if(testType == 0)
                devicePool[deviceId] = Device(deviceName,deviceId,deviceType,APIurl,accessPlatform,description,DeviceTestType.Online);
            else
                devicePool[deviceId] = Device(deviceName,deviceId,deviceType,APIurl,accessPlatform,description,DeviceTestType.Online);
            deviceNum++;
    }
    
    function getDeviceInfo(uint deviceId) public view returns(uint, string memory, string memory, string memory, string memory, string memory, DeviceTestType) {
        Device memory device = devicePool[deviceId];
        return(device.deviceId,device.deviceName,device.deviceType,device.APIurl,device.accessPlatform,device.description,device.testType);
    }
    
    function getDeviceNum() public view returns(uint) {
        return deviceNum;
    }
    
}







