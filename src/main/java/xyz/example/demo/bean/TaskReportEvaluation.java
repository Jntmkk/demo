package xyz.example.demo.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TaskReportEvaluation {
    BigInteger reportId;
    String category;
    BigInteger belongsToReportId;
    BigInteger belongsToTaskId;
    BigInteger level;
    String comments;
}
