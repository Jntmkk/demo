package xyz.example.demo.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.Date;

@Data
@Entity
public class CrowdBCTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    private BlockChainAccount blockChainAccount;

    public static enum PostType {
        IMMEDIATE, CLOCKING, DELAY
    }

    public static enum TaskStatus {
        PENDING("PENDING"),
        UNACCEPTED("UNACCEPTED"),
        ACCEPTED("ACCEPTED"),
        EVALUATING("EVALUATING"),
        COMPLETED("COMPLETED");
        String s;

        TaskStatus(String s) {
            this.s = s;
        }
    }

    //    Date createdDate;
    @NotNull
    String title;
    @NotNull
    String description;
    @NotNull
    BigInteger reward;
    @NotNull
    BigInteger deposit;
    @NotNull
    BigInteger deadline;
    @NotNull
    BigInteger maxWorkerNum;
    @Column(columnDefinition = "decimal(19,2) default '0' ")
    BigInteger currentWorkerNum;
    @NotNull
    BigInteger minReputation;
    @NotNull
    BigInteger taskType;
    @Column(columnDefinition = "varchar(255) default 'PENDING'")
    TaskStatus status;
    @NotNull
    String pointer;

//    @NotNull
//    @NotEmpty
//    private String category;
//    @NotNull
//    private TaskStatus taskStatus;
//    @NotNull
//    @NotEmpty
//    private String title;
//    @NotNull
//    private Date deadline;
//    @NotNull
//    private Date closedDate;
//    private Date createdDate;
//    @NotNull
//    private PostType submitType;
//    @NotNull
//    private Date submitDate;
//    @NotNull
//    private Integer deposit;
//    @NotNull
//    private Integer workerNum;
//    @NotNull
//    private Integer reward;
//    @NotNull
//    private Integer requiredReputation;
//    @NotNull
//    @NotEmpty
//    private String accountAddr;
//    @NotNull
//    @NotEmpty
//    private String description;
}
