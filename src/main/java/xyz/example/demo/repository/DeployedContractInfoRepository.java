package xyz.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.example.demo.models.DeployedContractInfo;

import java.util.List;
@Deprecated
public interface DeployedContractInfoRepository extends JpaRepository<DeployedContractInfo, Long> {
    List<DeployedContractInfo> findByContractNameOrderByIdDesc(String contractName);
}
