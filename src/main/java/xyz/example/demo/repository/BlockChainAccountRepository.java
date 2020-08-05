package xyz.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.example.demo.models.BlockChainAccount;
import xyz.example.demo.models.User;

import javax.persistence.Id;

public interface BlockChainAccountRepository extends JpaRepository<BlockChainAccount, Long> {
}
