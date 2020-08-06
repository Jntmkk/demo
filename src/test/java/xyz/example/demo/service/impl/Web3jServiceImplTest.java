package xyz.example.demo.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.web3j.protocol.Web3j;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class Web3jServiceImplTest {
    @Autowired
    Web3j web3j;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void submitReport() {
        /**
         * demo
         */
        assertThat(web3j).isNotNull();
    }

    @Test
    void getPostedTask() {
    }

    @Test
    void getAllTask() {
    }

    @Test
    void getUserInfo() {
    }

    @Test
    void submitTask() {
    }

    @Test
    void register() {
    }
}