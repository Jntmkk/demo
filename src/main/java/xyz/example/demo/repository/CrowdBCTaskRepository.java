package xyz.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.example.demo.models.CrowdBCTask;

public interface CrowdBCTaskRepository extends JpaRepository<CrowdBCTask,Long> {
}
