package xyz.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.example.demo.models.IotInfo;

import java.util.Optional;

public interface IotInfoRepository extends JpaRepository<IotInfo, Long> {
    Optional<IotInfo> findByPath(String path);
}
