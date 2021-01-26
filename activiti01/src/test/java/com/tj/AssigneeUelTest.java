package com.tj;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试使用uel设置负责人
 */
public class AssigneeUelTest {

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
                .name("出差申请流程-uel")
                .addClasspathResource("bpmn/evection-uel.bpmn")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署ID：" + deployment.getId());
        System.out.println("流程部署名字：" + deployment.getName());
    }

    /**
     * 启动流程实例
     * 启动成功后，可以在ACT_RU_VARIABLE表看到这里部署的流程所对应的数据
     */
    @Test
    public void startAssigneeUel() {
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取processEngine
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、启动流程实例
        // 设定assignee的值，用来替换uel表达式
        Map<String, Object> assigneeMap = new HashMap<>();
        assigneeMap.put("assignee0", "张三");
        assigneeMap.put("assignee1", "李经理");
        assigneeMap.put("assignee2", "王总经理");
        assigneeMap.put("assignee3", "赵财务");
        runtimeService.startProcessInstanceByKey("myEvection-uel", assigneeMap);

    }

}
