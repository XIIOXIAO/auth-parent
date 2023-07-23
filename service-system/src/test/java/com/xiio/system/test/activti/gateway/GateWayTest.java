package com.xiio.system.test.activti.gateway;

import io.jsonwebtoken.MalformedJwtException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/21 14:56
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class GateWayTest {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    //1.部署流程定义 创建流程实例
    @Test
    public void deployePro(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/qingjiaMax.bpmn20.xml")
                .name("网关流程定义")
                .deploy();
        System.out.println("流程部署id："+deployment.getId());
        System.out.println("流程部署名称："+deployment.getName());


    }

    @Test
    public void startPro(){
        //按照流程图在 流程实例化前会进入排他网关 判断天数
        //创建流程变量 传入day及值
        Map<String,Object> dayMap = new HashMap<>();
        dayMap.put("day",3);
        ProcessInstance processIns = runtimeService
                .startProcessInstanceByKey("qingjiaMax",dayMap);
        System.out.println("流程实例源："+processIns.getProcessDefinitionId());
        System.out.println("流程实例id："+processIns.getId());
    }

    //2.查询当前流程任务执行
    @Test
    public void findGroupTaskList(){
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("汪总监")
                .list();
        for (Task task : list) {
            System.out.println("任务id："+task.getId());
            System.out.println("任务名称："+task.getName());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("流程id："+task.getProcessInstanceId());
        }
    }

}
