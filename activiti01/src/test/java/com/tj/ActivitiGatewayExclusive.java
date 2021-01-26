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
 * 测试排他网关
 * 两个分支的条件都满足，走id值最小的分支
 */
public class ActivitiGatewayExclusive {

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
                .name("出差申请流程-排他网关")
                .addClasspathResource("bpmn/evection-exclusive.bpmn")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署ID：" + deployment.getId());
        System.out.println("流程部署名字：" + deployment.getName());
    }

    /**
     * 启动流程实例，设置流程变量的值
     */
    @Test
    public void startProcess() {
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、设置流程变量
        // 流程定义的key
        String key = "myEvection-exclusive";
        // 设置出差天数
        Evention evention = new Evention();
        evention.setNum(2d);
        // 流程变量的Map
        Map<String, Object> variables = new HashMap<>();
        variables.put("evection", evention);
        // 4、启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(key, variables);
        // 5、输出
        System.out.println(processInstance.getId());
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask() {
        // 流程定义key
        String key = "myEvection-exclusive";
        // 任务负责人
        String assingee = "张财务";
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
            taskService.complete(task.getId());
        }
    }

}
