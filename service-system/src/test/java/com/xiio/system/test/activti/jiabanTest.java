package com.xiio.system.test.activti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
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

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/14 11:04
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class jiabanTest {
    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;
    ////////

    @Test
    public void deployProy(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiabanListener.bpmn20.xml")
                .name("加班流程定义Listener")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    @Test
    public void runInst(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiabanListener");
        //该流程从那个已部署实例id
        System.out.println(processInstance.getProcessDefinitionId());
        //流程实例化后的id
        System.out.println(processInstance.getId());
    }

    ////////
    ////////////
    @Test
    public void deployPro(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiabanMethod.bpmn20.xml")
                .name("加班流程定义Method")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    @Test
    public void runIns(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("jiabanMethod");
        //该流程从那个已部署实例id
        System.out.println(processInstance.getProcessDefinitionId());
        //流程实例化后的id
        System.out.println(processInstance.getId());
    }


    ///////////
    //部署流程定义
    @Test
    public void deployProcess(){
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("process/jiaban.bpmn20.xml")
                .name("加班申请流程")
                .deploy();
        System.out.println(deployment.getId());
        System.out.println(deployment.getName());
    }

    //启动流程实例
    @Test
    public void startProcessInstance(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("assignee1","lucy");
        map.put("assignee2","mary");

        ProcessInstance processIns = runtimeService
                .startProcessInstanceByKey("jiaban",map);
        //该流程从那个已部署实例id
        System.out.println(processIns.getProcessDefinitionId());
        //流程实例化后的id
        System.out.println(processIns.getId());
    }


    //查询个人代办任务
    @Test
    public void findTaskList(){
        String assign = "咪咪";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign).list();
        for (Task task: list){
            System.out.println("流程实例id："+task.getProcessInstanceId());
            System.out.println("任务id："+task.getId());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("任务名称："+task.getName());
            System.out.println("流程部署id："+task.getProcessDefinitionId());
        }
    }

    @Test
    //删除流程部署
    public void deleteDeployment(){

        repositoryService.deleteDeployment("3eb34f0c-f2be-11ed-827d-005056c00008");
    }
}
