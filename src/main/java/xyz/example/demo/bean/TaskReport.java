package xyz.example.demo.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TaskReport {
    BigInteger belongToTask;
    String solution;
    String pointer;

    public TaskReport() {
    }

    public TaskReport(BigInteger belongToTask, String solution, String pointer) {
        this.belongToTask = belongToTask;
        this.solution = solution;
        this.pointer = pointer;
    }
}
