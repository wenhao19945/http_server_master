http_server example projects

netty http server 示例项目

（一）Application执行顺序

  1.启动：Application.run(Start.class, args)

  2.获取根目录路径: basePackage

  3.自动扫描根目录下所有class: new ClasspathPackageScanner(basePackage).getFullyQualifiedClassNameList()

  4.遍历扫描到的class, 获取注解并注册到ApplicationData: new AnnotationScanner().begin(nameList)

  5.读取自定义配置settings.json: new SettingUtil().load()

  6.加载mysql并测试连接: MsqlBase.load()

  7.启动自定义的netty http服务器: httpServerStart()

  8.项目启动完毕,执行其他自定义任务: new JobStarter(ApplicationData.RUNNER).run();

（二）注解说明

  1.@Action: api的类注解

  2.@RequestApi: api的方法注解

  3.@Component: 需要注册到ApplicationData的类注解

  4.@Runner: Job任务的方法注解,需要配合@Component一起使用,在项目启动完成后需要执行的方法


