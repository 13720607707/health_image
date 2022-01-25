package com.health;

import com.health.dao.UserDao;
import com.health.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;



@SpringBootTest(classes = HealthServiceProviderApplication.class)
class HealthServiceProviderApplicationTests {

    @Autowired
    private UserService userService;
    @Test
    void contextLoads() {

    }

}
