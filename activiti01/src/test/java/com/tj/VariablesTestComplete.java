package com.tj;

import com.tj.entity.Evention;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用流程变量
 * 任务办理时设置变量
 */
public class VariablesTestComplete {

    /**
     * 流程部署
     */
    @Test
    public void testDeployment() {
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RepositoryServcie
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、使用service进行流程的部署，定义一个流程的名字，把bpmn和png部署到数据库中
        Deployment deployment = repositoryService.createDeployment()
                .name("出差申请流程-variables-complete")
                .addClasspathResource("bpmn/evection-global.bpmn")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署ID：" + deployment.getId());
        System.out.println("流程部署名字：" + deployment.getName());
    }

    /**
     * 启动流程实例 - 启动流程的时候设置流程变量
     * 设置流程变量num
     * 设置流程负责人
     */
    @Test
    public void startProcess() {
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、设置流程变量
        // 流程变量的Map
        Map<String, Object> variables = new HashMap<>();
        // 设定负责人
        variables.put("assignee0", "张三3");
        variables.put("assignee1", "王经理3");
        variables.put("assignee2", "杨总经理3");
        variables.put("assignee3", "张财务3");
        // 4、启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("myEvection-global", variables);
        // 5、输出
        System.out.println(processInstance.getId());
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask() {
        // 流程定义key
        String key = "myEvection-global";
        // 任务负责人
        String assingee = "张财务3";
        // 设置流程变量（在分支的时候使用）
        Evention evention = new Evention();
        evention.setNum(2d);
        Map<String, Object> variables = new HashMap<>();
        // evection是在流程设置里设置好的
        variables.put("evection", evention);
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3、查询任务
        Task task = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskAssignee(assingee)
                .singleResult();
        // 4、完成任务
        if (task != null) {
            // 根据任务ID完成任务，并传递流程变量
            taskService.complete(task.getId(), variables);
        }
    }

}
