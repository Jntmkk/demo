package xyz.example.demo.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
public class BlockChainAccount {
    @TableGenerator(name = "block_chain_account")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @NotNull
//    @NotEmpty
//    private String accountAddr;
    @NotNull
    @NotEmpty
    private String privateKey;
    @ManyToOne
    private User user;

//    @OneToMany(targetEntity = CrowdBCTask.class, mappedBy = "blockChainAccount")
//    @MapKey(name = "id")
//    private Set<CrowdBCTask> tasks;
}
