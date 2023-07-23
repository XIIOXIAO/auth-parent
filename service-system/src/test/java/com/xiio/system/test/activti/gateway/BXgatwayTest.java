package com.xiio.system.test.activti.gateway;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/22 9:34
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BXgatwayTest {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Test
    public void deployePro(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/qingjiaBX.bpmn20.xml")
                .name("并行网关流程定义")
                .deploy();
        System.out.println("流程部署id："+deployment.getId());
        System.out.println("流程部署名称："+deployment.getName());


    }

    @Test
    public void startPro(){
        ProcessInstance processIns = runtimeService
                .startProcessInstanceByKey("qingjiaBX");
        System.out.println("流程实例源："+processIns.getProcessDefinitionId());
        System.out.println("流程实例id："+processIns.getId());
    }

    //2.1查询并行部门经理流程任务执行情况
    @Test
    public void findGroup1TaskList(){
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("BX咪咪部门经理")
                .list();
        for (Task task : list) {
            System.out.println("任务id："+task.getId());
            System.out.println("任务名称："+task.getName());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("流程id："+task.getProcessInstanceId());
        }
    }

    //2.2查询并行总经理流程任务执行情况
    @Test
    public void findGroup2TaskList(){
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("BX汪汪总经理")
                .list();
        for (Task task : list) {
            System.out.println("任务id："+task.getId());
            System.out.println("任务名称："+task.getName());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("流程id："+task.getProcessInstanceId());
        }
    }

    @Test
    //3.1 部门经理 完成任务
    public void completTask(){
        Task task = taskService.createTaskQuery()
                        .taskAssignee("BX咪咪部门经理")
                        .singleResult();
        taskService.complete(task.getId());
    }

    @Test
    //3.2 总经理 完成任务
    public void completeTaskz(){
        Task task = taskService.createTaskQuery()
                .taskAssignee("BX汪汪总经理")
                .singleResult();
        taskService.complete(task.getId());
    }

    @Test
    //4.人事查询 自己代办任务
    public void queryRenShi(){
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee("BX人事处")
                .list();
        for (Task task : list) {
            System.out.println("任务id："+task.getId());
            System.out.println("任务名称："+task.getName());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("流程id："+task.getProcessInstanceId());
        }
    }

    //5.人事完成任务 审批结束

}
