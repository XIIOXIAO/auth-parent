package com.xiio.system.test.activti;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * @description:
 * @author: xiio
 * @time: 2023/5/15 10:22
 */
public class ActivitiListener implements TaskListener {
    @Override
    public void notify(DelegateTask task) {
        if (task.getName().equals("经理审批")){
            task.setAssignee("咪咪");
        }else if(task.getName().equals("人事审批")){
            task.setAssignee("汪汪");
        }
    }
}
