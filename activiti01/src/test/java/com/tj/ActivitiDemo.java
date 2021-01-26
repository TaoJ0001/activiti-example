package com.tj;

import org.activiti.engine.*;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.zip.ZipInputStream;

public class ActivitiDemo {

    /**
     * 测试流程部署(单文件部署)
     */
    @Test
    public void testDeployment() {
        // 1、创建ProcessEngine
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RepositoryServcie
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、使用service进行流程的部署，定义一个流程的名字，把bpmn和png部署到数据库中
        Deployment deployment = repositoryService.createDeployment()
                .name("出差申请")
                .addClasspathResource("bpmn/evection.bpmn")
                .addClasspathResource("bpmn/evection.png")
                .deploy();
        // 4、输出部署信息
        System.out.println("流程部署ID：" + deployment.getId());
        System.out.println("流程部署名字：" + deployment.getName());
    }

    /**
     * 启动流程实例(创建任务，如：创建一个出差申请)
     */
    @Test
    public void testStartProcess() {
        // 1、创建ProcessEngine
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RunTimeService
        RuntimeService runtimeService = defaultProcessEngine.getRuntimeService();
        // 3、根据流程定义的id启动流程
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("myEvection");
        // 4、输出内容
        System.out.println("流程定义ID：" + instance.getProcessDefinitionId());
        System.out.println("流程实例ID：" + instance.getId());
        System.out.println("当前活动ID：" + instance.getActivityId());
    }

    /**
     * 查询个人待执行的任务(查询ACT_RU_TASK表)
     */
    @Test
    public void testFindPersonalTaskList() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3、根据流程key和任务的负责人查询任务
        List<Task> taskList = taskService.createTaskQuery()
                .processDefinitionKey("myEvection") // 流程key
                .taskAssignee("zhangsan") // 要查询的负责人
                .list();
        // 4、输出
        for (Task task : taskList) {
            System.out.println("流程实例ID：" + task.getProcessInstanceId());
            System.out.println("任务ID：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }

    /**
     * 完成个人任务
     */
    /*@Test
    public void testCompleteTask() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();
        // 3、根据任务id完成任务
        taskService.complete("2505");
    }*/

    /**
     * 完成个人任务
     */
    @Test
    public void completeTask() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取TaskService
        TaskService taskService = processEngine.getTaskService();

        // 3、获取zhangsan、myEvection对应的任务 --------- > zhangsan
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                .taskAssignee("zhangsan")
                .singleResult();

        /*// 3、获取jerry、myEvection对应的任务 --------- > jerry
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                .taskAssignee("jerry")
                .singleResult();*/
        /*// 3、获取jack、myEvection对应的任务 --------- > jack
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                .taskAssignee("jack")
                .singleResult();*/

        /*// 3、获取rose、myEvection对应的任务 --------- > rose
        Task task = taskService.createTaskQuery()
                .processDefinitionKey("myEvection")
                .taskAssignee("rose")
                .singleResult();*/

        System.out.println("流程实例ID：" + task.getProcessInstanceId());
        System.out.println("任务ID：" + task.getId());
        System.out.println("任务负责人：" + task.getAssignee());
        System.out.println("任务名称：" + task.getName());
        // 4、根据任务id完成任务
        taskService.complete(task.getId());
    }

    /**
     * 使用zip包进行批量部署
     */
    @Test
    public void deployProcessByZip() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、流程部署
        // 读取资源包文件，构造成InputStream
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("bpmn/evection.zip"); // getResourceAsStream会读取到resources目录，所以要加上bpmn目录
        // 用InputStream构造成ZipInputStream
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 使用压缩包的流进行流程的部署(因为批量部署，可能包含多种流程，所以不指定流程的名字)
        Deployment deploy = repositoryService.createDeployment()
                .addZipInputStream(zipInputStream)
                .deploy();
        // 4、输出
        System.out.println("流程部署ID：" + deploy.getId());
        System.out.println("流程部署名称：" + deploy.getName());
    }

    /**
     * 查询流程定义
     */
    @Test
    public void queryProcessDefinition() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、获取ProcessDefinitionQuery对象
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        // 4、查询当前所有的流程定义
        List<ProcessDefinition> processDefinitions = processDefinitionQuery.processDefinitionKey("myEvection")
                .orderByProcessDefinitionVersion() // 排序
                .desc() // 指定排序的方式：倒序
                .list();
        // 5、输出信息
        for (ProcessDefinition processDefinition : processDefinitions) {
            System.out.println("流程定义ID：" + processDefinition.getId());
            System.out.println("流程定义名称：" + processDefinition.getName());
            System.out.println("流程定义key：" + processDefinition.getKey());
            System.out.println("流程定义版本：" + processDefinition.getVersion());
            System.out.println("流程部署ID：" + processDefinition.getDeploymentId());
        }
    }

    /**
     * 删除流程部署信息(删除流程操作的表跟部署流程操作的表是一样的)
     * ACT_GE_BYTEARRAY
     * ACT_RE_DEPLOYMENT
     * ACT_RE_PROCDEF
     */
    @Test
    public void deleteDeployMent() {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、通过部署id来删除流程部署信息
        String deploymentId = "12501";
        // 如果某个流程实例(如出差申请)未完成，就不能成功删除，如果需要删除，需使用特殊方式删除：级联删除
//        repositoryService.deleteDeployment(deploymentId);
        // 级联删除
        repositoryService.deleteDeployment(deploymentId, true);
    }

    /**
     * 下载资源文件
     * 方案1：使用Activiti提供的api来下载资源文件，保存到文件目录
     * 方案2：自己写代码从数据库中下载文件，使用jdbc对blob类型、clob类型数据读取出来，保存到文件目录(数据库中保存流程文件的数据类型是longblob类型)，解决IO操作：commons-io.jar
     * 本次测试使用方案1，Activiti提供的api：RepositoryService
     */
    @Test
    public void getDeployment() throws IOException {
        // 1、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取RepositoryService
        RepositoryService repositoryService = processEngine.getRepositoryService();
        // 3、获取ProcessDefinitionQuery对象查询流程定义信息
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("myEvection")
                .singleResult();
        // 4、通过流程定义信息，获取部署ID
        String deploymentId = processDefinition.getDeploymentId();
        // 5、通过RepositoryService，传递部署id参数，读取资源信息(bpmn和png文件)
        // 5.1、获取png图片的流
        // 通过流程定义表中获取png图片的目录和名字
        String pgnName = processDefinition.getDiagramResourceName();
        // 通过部署id和文件名字来获取图片的资源
        InputStream pngInput = repositoryService.getResourceAsStream(deploymentId, pgnName);
        // 5.2、获取bpmn的流
        String bpmnName = processDefinition.getResourceName();
        InputStream bpmnInput = repositoryService.getResourceAsStream(deploymentId, bpmnName);
        // 6、构造OutputStream流
        // 保存到E盘下的temp目录，保存名字为evectionflow01.png
        File pngFile = new File("E:\\temp\\evectionflow01.png");
        // 保存到E盘下的temp目录，保存名字为evectionflow01.bpmn
        File bpmnFile = new File("E:\\temp\\evectionflow01.bpmn");
        FileOutputStream pngOutStream = new FileOutputStream(pngFile);
        FileOutputStream bpmnOutStream = new FileOutputStream(bpmnFile);
        // 7、输入流、输出流的转换
        // 使用commons-io的IOUtils工具
        IOUtils.copy(pngInput, pngOutStream);
        IOUtils.copy(bpmnInput, bpmnOutStream);
        // 8、关闭流
        pngOutStream.close();
        bpmnOutStream.close();
        pngInput.close();
        bpmnInput.close();
    }

    /**
     * 查看历史信息
     */
    @Test
    public void findHistoryInfo() {
        // 1、获取引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 2、获取HistoryService
        HistoryService historyService = processEngine.getHistoryService();
        // 获取ACT_HI_ACTINST表的查询对象
        HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService.createHistoricActivityInstanceQuery();
        // 3、查询ACT_HI_ACTINST表，条件：根据InstanceId查询(PROC_INST_ID_字段)
//        historicActivityInstanceQuery.processInstanceId("10001");
        // 查询ACT_HI_ACTINST表，条件：根据DefinitionId查询(PROC_DEF_ID_字段)
        historicActivityInstanceQuery.processDefinitionId("myEvection:1:7504");
        // 添加排序
        historicActivityInstanceQuery.orderByHistoricActivityInstanceStartTime().asc();
        // 查询所有内容
        List<HistoricActivityInstance> historicActivityInstances = historicActivityInstanceQuery.list();
        // 4、输出
        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            System.out.println(historicActivityInstance.getActivityId());
            System.out.println(historicActivityInstance.getActivityName());
            System.out.println(historicActivityInstance.getProcessDefinitionId());
            System.out.println(historicActivityInstance.getProcessInstanceId());
            System.out.println("<< --------------------------------- >>");
        }
    }


}
