package xyz.example.demo.bean;

import lombok.Data;
import xyz.example.demo.models.CrowdBCTask;

import java.math.BigInteger;

@Data
public class Web3jCrowdBCTask {
    String title;
    String description;
    BigInteger reward;
    BigInteger deposit;
    BigInteger deadline;
    BigInteger maxWorkerNum;
    BigInteger currentWorkerNum;
    BigInteger minReputation;
    BigInteger taskType;
    String status;
    String pointer;
    public Web3jCrowdBCTask(String title, String description, BigInteger reward, BigInteger deposit, BigInteger deadline, BigInteger maxWorkerNum, BigInteger currentWorkerNum, BigInteger minReputation, BigInteger taskType, String status, String pointer) {
        this.title = title;
        this.description = description;
        this.reward = reward;
        this.deposit = deposit;
        this.deadline = deadline;
        this.maxWorkerNum = maxWorkerNum;
        this.currentWorkerNum = currentWorkerNum;
        this.minReputation = minReputation;
        this.taskType = taskType;
        this.status = status;
        this.pointer = pointer;
    }

    public CrowdBCTask convert() {
        CrowdBCTask crowdBCTask = new CrowdBCTask();
        crowdBCTask.setCurrentWorkerNum(currentWorkerNum);
        crowdBCTask.setDeadline(deadline);
        crowdBCTask.setDeposit(deposit);
        crowdBCTask.setDescription(description);
        crowdBCTask.setMaxWorkerNum(maxWorkerNum);
        crowdBCTask.setMinReputation(minReputation);
        crowdBCTask.setPointer(pointer);
        crowdBCTask.setReward(reward);
        crowdBCTask.setTaskType(taskType);
        crowdBCTask.setTitle(title);
        crowdBCTask.setStatus(CrowdBCTask.TaskStatus.valueOf(status));
        return crowdBCTask;
    }

}
