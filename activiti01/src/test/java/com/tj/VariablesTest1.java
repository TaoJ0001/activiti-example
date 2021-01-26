package com.tj;

import com.tj.entity.Evention;
import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用流程变量(测试小于3的分支)
 * 启动流程的时候设置流程变量
 */
public class VariablesTest1 {

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
                .name("出差申请流程-variables")
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
        // 设置流程变量
        Evention evention = new Evention();
        evention.setNum(2d);
        // 流程变量的Map
        Map<String, Object> variables = new HashMap<>();
        // evection是在流程设置里设置好的
        variables.put("evection", evention);
        // 设定负责人
        variables.put("assignee0", "张三");
        variables.put("assignee1", "王经理");
        variables.put("assignee2", "杨总经理");
        variables.put("assignee3", "张财务");
        // 4、启动流程
        runtimeService.startProcessInstanceByKey("myEvection-global", variables);
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask() {
        // 流程定义key
        String key = "myEvection-global";
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
            // 根据任务ID完成任务
            taskService.complete(task.getId());
        }
    }

}
