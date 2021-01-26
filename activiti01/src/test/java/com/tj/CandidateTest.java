package com.tj;

import org.activiti.engine.*;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

/**
 * 任务的拾取、归还、交接测试
 */
public class CandidateTest {

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
                .name("出差申请流程-candidate")
                .addClasspathResource("bpmn/evection-candidate.bpmn")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署ID：" + deployment.getId());
        System.out.println("流程部署名字：" + deployment.getName());
    }

    /**
     * 启动流程实例
     */
    @Test
    public void testStartProcess() {
        // 1、创建ProcessEngine流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RuntimeService
        RuntimeService runtimeService = processEngine.getRuntimeService();
        // 3、启动流程
        // 流程定义的key
        String key = "myEvection-candidate";
        runtimeService.startProcessInstanceByKey(key);
    }

    /**
     * 查询组任务
     */
    @Test
    public void findGroupTaskList() {
        // 流程定义的key
        String key = "myEvection-candidate";
        // 任务候选人
        String candidateUser = "wangwu";
        // 1、创建ProcessEngine流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3、查询组任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey(key)
                .taskCandidateUser(candidateUser) // 根据候选人查询任务
                .list();
        // 4、输出
        for (Task task : taskList) {
            System.out.println(" << --------------------- >>");
            System.out.println("流程实例ID：" + task.getProcessInstanceId());
            System.out.println("任务ID：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
        }
    }

    /**
     * 候选人拾取任务
     */
    @Test
    public void claimTask() {
        // 1、创建ProcessEngine流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 当前任务的ID
        String taskId = "5002";
        // 任务候选人
        String candidateUser = "wangwu";
        // 3、查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskCandidateUser(candidateUser)
                .singleResult();
        // 4、拾取任务
        if (task != null) {
            taskService.claim(taskId, candidateUser);
            System.out.println("taskId：" + taskId + "，用户：" + candidateUser + "拾取任务完成");
        }
    }

    /**
     * 归还任务
     */
    @Test
    public void testAssigneeToGroupTask() {
        // 1、创建ProcessEngine流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 当前任务的ID
        String taskId = "5002";
        // 任务负责人
        String assignee = "wangwu";
        // 3、根据key和负责人查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        // 4、归还任务，就是把负责人设置为空
        if (task != null) {
            taskService.setAssignee(taskId, null);
            System.out.println("taskId：" + taskId + "归还任务完成");
        }
    }

    /**
     * 交接任务
     */
    @Test
    public void testAssigneeToCandidateUser() {
        // 1、创建ProcessEngine流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 当前任务的ID
        String taskId = "5002";
        // 任务负责人
        String assignee = "wangwu";
        // 任务候选人
        String candidateUser = "lisi";
        // 3、根据key和负责人查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .taskAssignee(assignee)
                .singleResult();
        // 4、交接任务，就是把负责人改为候选人
        if (task != null) {
            taskService.setAssignee(taskId, candidateUser);
            System.out.println("taskId：" + taskId + "交接任务完成");
        }
    }

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask() {
        // 流程定义key
        String key = "myEvection-candidate";
        // 任务负责人
        String assingee = "张三";
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
