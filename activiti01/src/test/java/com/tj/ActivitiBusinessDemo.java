package com.tj;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;

public class ActivitiBusinessDemo {

    /**
     * 添加业务key到Activiti的表
     * 操作表：ACT_RU_EXECUTION
     */
    @Test
    public void addBusinessKey() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、启动流程的过程中，添加businessKey
        // 参数1：流程定义的ID
        // 参数2：businessKey(业务key，如张三出差申请单的ID)
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection", "1001");
        // 4、输出
        System.out.println("businessKey：" + instance.getBusinessKey());
    }

    /**
     * 全部流程的挂起和激活
     * 说明：
     *      流程定义：如出差申请流程
     *      流程实例：如张三或李四的出差申请
     * 流程定义为挂起状态，则不允许添加新的流程的实例，同时，该定义下的所有流程实例全部执行挂起操作
     * 操作表：ACT_RU_TASK，字段SUSPENSION_STATE_：1激活，2挂起
     * 操作表：ACT_RE_PROCDEF，字段SUSPENSION_STATE_：1激活，2挂起
     * 操作表：ACT_RU_EXECUTION，字段SUSPENSION_STATE_：1激活，2挂起
     */
    @Test
    public void suspendAllProcessInstance() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、查询流程定义，获取流程定义的查询对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        // 4、获取当前流程定义的实例是否都是挂起状态
        boolean suspended = processDefinition.isSuspended();
        // 5、获取流程定义的ID
        String definitionId = processDefinition.getId();
        // 6、如果是挂起状态，改为激活状态，如果是激活状态，改为挂起状态
        if (suspended) {
            // 如果是挂起，可以激活
            // 参数1：流程定义ID
            // 参数2：是否激活
            // 参数3：激活时间
            repositoryService.activateProcessDefinitionById(definitionId, true, null);
            System.out.println("流程定义ID：" + definitionId + "，已激活");
        } else {
            // 如果是激活，可以挂起
            // 参数1：流程定义ID
            // 参数2：是否挂起/暂停
            // 参数3：激活时间
            repositoryService.suspendProcessDefinitionById(definitionId, true, null);
            System.out.println("流程定义ID：" + definitionId + "，已挂起");
        }
    }

    /**
     * 挂起、激活单个流程实例
     * 某个流程实例挂起，则此流程的实例不再继续执行，如果想完成该实例，会报错
     * 操作表：ACT_RU_TASK，字段SUSPENSION_STATE_：1激活，2挂起
     * 操作表：ACT_RU_EXECUTION，字段SUSPENSION_STATE_：1激活，2挂起
     */
    @Test
    public void suspendSingleProcessInstance() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、通过RuntimeService获取流程实例对象
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId("22501")
                .singleResult();
        // 4、得到当前流程实例的暂停状态，true暂停，false激活
        boolean suspended = instance.isSuspended();
        // 5、获取流程实例ID
        String instanceId = instance.getId();
        // 6、判断是否已经暂停，如果已经暂停，就执行激活操作，如果已经激活，就执行暂停操作
        if (suspended) {
            // 如果已经暂停，就执行激活操作
            runtimeService.activateProcessInstanceById(instanceId);
            System.out.println("流程定义ID：" + instanceId + "，已激活");
        } else {
            // 如果已经激活，就执行暂停操作
            runtimeService.suspendProcessInstanceById(instanceId);
            System.out.println("流程定义ID：" + instanceId + "，已挂起");
        }
    }


}
