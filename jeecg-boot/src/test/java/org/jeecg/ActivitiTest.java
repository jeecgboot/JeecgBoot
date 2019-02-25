package org.jeecg;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = JeecgApplication.class)
public class ActivitiTest {

    @Autowired
    RuntimeService runtimeService;

    @Test
    public void TestStartProcess() {
        Map<String, Object> variables = new HashMap<>();
        variables.put("applyUserId", "admin");
        variables.put("email", "john.doe@activiti.com");
        variables.put("phoneNumber", "123456789");
        runtimeService.startProcessInstanceByKey("myProcess", variables);
    }
}
