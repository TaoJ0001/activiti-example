# activiti-example

笔记：
    1、保存工作流程为图片
        ①、holiday.bpmn文件改为holiday.xml文件(最好先保存一份到桌面)
        ②、点击holiday.xml文件右键 -> 选择Diagrams -> 选择 show 2.0 BPMN Designer...
        ③、在打开的窗口中选择导出文件按钮：Export to Image File
        ④、选择保存路径即可
        ⑤、再将holiday.xml文件改为holiday.bpmn文件(这里可能会出现乱码)  
            注意：   
                点击holiday.xml文件右键没有Diagrams选项
                    打开设置 -> 选择Plugins -> 搜索bpm -> 开启JBoss jBPM ->重启idea即可 
                    idea再次打开JBoss jBPM可能会自动关闭
                        打开设置 -> 选择Plugins -> 选择Installed -> 找到JBoss jBPM再次开启即可
    2、保存工作流程为图片时出现乱码 
        ①、打开设置 -> 选择Editor -> 选择File Encodings -> 将Global Encoding和Project Encoding都改为UTF-8
        ②、打开idea工具bin目录下的idea64.exe.vmoptions文件 -> 在后面追加内容：-Dfile.encoding=UTF-8
        ③、打开C盘 -> 用户 —> idea -> config下的idea64.exe.vmoptions文件 -> 在后面追加内容：-Dfile.encoding=UTF-8
        ④、重启idea工具
    3、数据库表相关  
        ACT_RE_…… 流程定义和流程资源先关表
        ACT_RU_…… 运行时、流程实例、任务、变量先关表
        ACT_HI_…… 历史先关表
        ACT_GE_…… 通用先关表
        ACT_RE_DEPLOYMENT 流程部署表，每部署一次都会增加一条记录
        ACT_RE_PROCDEF 流程定义表
        ACT_GE_BYTEARRAY 流程资源表 
        ACT_HI_ACTINST 流程实例执行历史
        ACT_HI_IDENTITYLINK 流程参与者的历史信息
        ACT_HI_PROCINST 流程实例的历史信息
        ACT_HI_TASKINST 任务的历史信息
        ACT_RU_EXECUTION 流程执行的信息
        ACT_RU_IDENTITYLINK 流程参与者信息
        ACT_RU_TASK 正在执行中任务信息
    4、服务接口：用于流程的部署、执行、管理、使用这些接口就是在操作对应的数据库表
        // 、获取流程引擎
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        // 资源管理类
        processEngine.getRepositoryService();
        // 运行时管理类
        processEngine.getRuntimeService();
        // 任务管理类
        processEngine.getTaskService();
        // 历史数据管理类
        processEngine.getHistoryService();
        // 流程引擎管理类
        processEngine.getManagementService(); 
        
        
