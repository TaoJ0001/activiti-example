package com.tj;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

/**
 * 创建activiti的表
 * 测试activiti所需要的25张表的生成
 */
public class CreateActivitiTable {

    @Test
    public void contextLoads() {
        // 方式一，使用ProcessEngines(默认，配置文件不能改变)
        ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println(defaultProcessEngine);

        // 方式二，使用ProcessEngineConfiguration(自定义，配置文件名可以改变)
        // 1.创建ProcessEngineConfiguration对象，参数1：配置文件，参数2：ProcessEngineConfiguration的bean的id(默认是processEngineConfiguration，如果改了别名需要指定)
        // ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml","processEngineConfiguration");
        //ProcessEngineConfiguration configuration = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        // 2.创建ProcesEngine对象
        //ProcessEngine processEngine = configuration.buildProcessEngine();
        //3.输出processEngine对象
        //System.out.println(processEngine);
    }

}
