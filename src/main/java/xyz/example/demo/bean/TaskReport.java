package xyz.example.demo.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TaskReport {
    BigInteger id;
    BigInteger belongsToTask;
    String solution;
    String pointer;
    BigInteger level;

    public TaskReport() {
    }

    public TaskReport(BigInteger belongToTask, String solution, String pointer) {
        this.belongsToTask = belongToTask;
        this.solution = solution;
        this.pointer = pointer;
    }
}
