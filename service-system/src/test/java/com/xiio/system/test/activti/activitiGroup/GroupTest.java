package com.xiio.system.test.activti.activitiGroup;

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

import java.util.List;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/15 15:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GroupTest {
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
                .addClasspathResource("process/jiabanGroup2.bpmn20.xml")
                .name("任务组流程定义")
                .deploy();
        System.out.println("流程部署id："+deployment.getId());
        System.out.println("流程部署名称："+deployment.getName());

        ProcessInstance processIns = runtimeService
                .startProcessInstanceByKey("jiabanGroup2");
        System.out.println("流程实例源："+processIns.getProcessDefinitionId());
        System.out.println("流程实例id："+processIns.getId());
    }

    //2.查询组任务
    @Test
    public void queryGroup(){
        List<Task> taskList = taskService.createTaskQuery()
                .taskCandidateUser("汪汪2")
                .list();
        for (Task task : taskList) {
            System.out.println("任务id："+task.getId());
            System.out.println("任务名称："+task.getName());
            System.out.println("任务负责人："+task.getAssignee());
            System.out.println("流程id："+task.getProcessInstanceId());
        }
    }

    //3.拾取组任务
    @Test
    public void clarmTask(){
        //拾取任务，即使该用户不是候选人也能拾取
        //校验该用户有没有拾取任务的资格
        Task task = taskService.createTaskQuery()
                .taskCandidateUser("咪咪2")//根据候选人进行查询
                .singleResult();
        if(task!=null){
            //拾取任务
            taskService.claim(task.getId(), "咪咪2");
            System.out.println("任务拾取成功");
        }else{
            System.out.println("不是该组成员无法拾取!");
        }
    }

    //4.查询个人代办任务
    @Test
    public void findGroupPendingTaskList(){
        //任务负责人
        String assignee = "咪咪2";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assignee)//只查询该任务负责人的任务
                .list();

        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    //5.办理个人任务
    @Test
    public void completeTask(){
        Task task = taskService.createTaskQuery()
                                .taskAssignee("咪咪2")//要查询的负责人
                                .singleResult();
        taskService.complete(task.getId());
    }

    //6.归还组任务
    @Test
    public void assigneeToGroupTask(){

    }


}
