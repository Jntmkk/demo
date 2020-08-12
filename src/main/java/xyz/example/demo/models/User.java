package xyz.example.demo.models;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
//                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    private String username;

//    @NotBlank
//    @Size(max = 50)
//    @Email
//    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();
    @NotEmpty
    @NotNull
    private String address;
    private String usc;
    private String profile;
    private Long registerTime;
    private Long processTaskNum;
    private Long finishedTaskNum;
    private Integer reputation;
    @NotNull
    @Column(columnDefinition = "varchar(255)")
    private String privateKey;
    @OneToMany(targetEntity = BlockChainAccount.class,mappedBy = "user")
    @MapKey(name = "id")
    private Set<BlockChainAccount> accounts=new HashSet<>();
    String avatar = "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif";
    public User() {
    }

    public User(String username, String password) {
        this.username = username;
//        this.email = email;
        this.password = password;
    }
}