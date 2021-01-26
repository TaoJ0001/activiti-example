package com.tj;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试使用监听器设置负责人
 */
public class AssigneeListenerTest {

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
                .name("测试监听器-listen")
                .addClasspathResource("bpmn/evection-listen.bpmn")
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
        runtimeService.startProcessInstanceByKey("myEvection-listen");
    }

}
