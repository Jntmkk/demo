package xyz.example.demo.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TaskReport {
    Long belongTo;
    String solution;
    String pointer;
    BigInteger level;
}
