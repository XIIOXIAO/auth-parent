package com.xiio.system.test.activti;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/14 9:45
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivitiTest {
    @Autowired
    private RepositoryService repositoryService;
    @Test
    public void deployProcess(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia.xml")
                .addClasspathResource("process/qingjia.png")
                .name("请假申请流程")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }
}
