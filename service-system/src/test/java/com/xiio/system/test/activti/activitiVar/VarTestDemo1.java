package com.xiio.system.test.activti.activitiVar;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
 * @time: 2023/5/15 11:35
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class VarTestDemo1 {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    //1.部署流程定义
    @Test
    public void deployProcess(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban.bpmn20.xml")
                .name("变量使用流程定义")
                .deploy();
        System.out.println("流程部署id："+deployment.getId());
        System.out.println("流程部署名称："+deployment.getName());
    }

    //2.运行流程实例
    @Test
    public void runProcess(){
        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("assignee1","变量李四");

        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("jiaban",userMap);

        //该流程从那个已部署实例id
        System.out.println("实例运行的部署id："+processInstance.getProcessDefinitionId());
        //流程实例化后的id
        System.out.println("实例id："+processInstance.getId());
    }

    //3.提交完成一个流程任务
    @Test
    public void completeProcess(){
        String assign = "变量李四";
        Task task = taskService.createTaskQuery()
                .taskAssignee(assign)
                .singleResult();

        System.out.println(task);
        Map<String, Object> variables = new HashMap<>();
        variables.put("assignee2","变量张三");
        taskService.complete(task.getId(),variables);
    }

    //4.查询当前流程执行状态
    @Test
    public void queryProcess(){
        String assginee = "变量张三";
        List<Task> process = taskService.createTaskQuery()
                .taskAssignee(assginee)
                .list();

        for (Task task : process) {
            System.out.println("流程实例id："+task.getProcessInstanceId());
            System.out.println("任务id："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());
            System.out.println("流程部署id："+task.getProcessDefinitionId());
        }
    }



}
