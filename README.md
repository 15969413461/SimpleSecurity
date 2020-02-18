# SimpleSecurity
SpringSecurity基本实现，使用方法与SpringSecurity大体相同，未实现oauth协议

## 框架构成
* 1.SimpleAnnotationTool : 包扫描与注解解析，逻辑入口为 **AbstractRelectHandler** 实现类
* 2.SimpleAspect ： aop支持，使用javassist解析字节码操作，当前只实现了前置通知（@Before）与后置通知（@After）的字节码操作，所有框架的运行并未与此module相关。
* 3.SimpleRepositoryCache ： 原设计意图是框架的集群环境支持，但当前各项Cache接口分布于*SimpleSecurity*与*SimpleSecurityCore*之中，代替了此module的职能，所有此module下无代码。
* 4.SimpleSecurity ： Security逻辑调用层和配置解析，逻辑入口为被@WebFilter标注的 *FilterChainProxy* Filter实现类。
* 5.SimpleSecurityCore ： Security逻辑实现层，提供调用层所需的各项实现类

## 提供的过滤器
*（根据过滤器执行顺序排列，在配置类中默认开启debug模式显示其执行相关信息）*
* 1.SecurityContextPersistenceFilter ：负责为当前ThreadLocal关联一个SecurityContext对象。<br/>
* 2.CsrfFilter ：csrf防御逻辑实现，有两个自雷，分别对应着csrf凭证存储在cookie中和session中，推荐存储于session中，防止cookie被到后csrf凭证泄露<br/>
* 3.LogoutFilter ： 监控一个实际为退出功能的 URL（默认为login?logout），并且在匹配的时候完成用户的退出功能。<br/>
* 4.UsernamePasswordAuthenticationFilter ： 监控一个使用用户名和密码基于form认证的URL（默认为/login），并在URL匹配的情况下尝试认证该用户。<br/>
* 5.DefaultLoginPageGeneratingFilter ： 根据请求参数动态的构建用户登录界面。<br/>
* 6.RememberMeAuthenticationFilter ： 负责解析remember cookie，并进行身份验证。<br/>
* 7.AnonymousAuthenticationFilter ： 如果用户到这一步还没有经过认证，将会为这个请求关联一个认证的 token，标识此用户是匿名用户。<br/>
* 8.SessionManagementFilter ： 检测session是否过期。<br/>
* 9.AccessManagementFilter ： 访问管理的Filter实现，具体的授权验证机制参见 **com.zy.security.web.config.subject.Authorization** <br/>

## 对SpringSecurity相关功能的不同实现补充
* 1.自动配置，支持Java配置式自定义。配置方式与SpringSecurity相同，需继承自 **WebSecurityConfigurerAdapter**，覆写其configure方法即可，
    但子类需被 @EnableSecurityConfiguration 注解所标记才能被识别。
* 2.与SpringSecurity相同的身份验证逻辑，在自行实现的权限验证逻辑中，引入Shiro之中三层权限表达方式，参见Shiro的 **WildcardPermission** 类，<br/>
    在项目中的具体实现为 **PluralisticRolePermission** 类，通过此类以实现权限的封装与比对。
    同时也保留了原SprngSecurity的权限表达方式，可在配置类中灵活切换。
* 3.提供Csrf防御和会话固定攻击防御。会话固定攻击防御与SpringSecurity实现思路相同，具体实现为 **ChangeSessionIdAuthenticationStrategy** 。
    通过 **CsrfAuthenticationStrategy** 类和 **CsrfFilter** 实现csrf防御，可在配置类中指定需要csrf防御的URL。而对于客户端提供提供给服务端的csrf凭证，
    则可使用*CsrfContextHolder*的相关静态方法获得，建议在Controller之中使用，以传递给前端页面。比如要传递给thymeleaf则可使用Model或ModelAndView对象转交。因为旨在保证用户登录到退出这段时间之内请求的有效性，所以csrf凭证在用户身份验证成功后才会被创建，也就是说身份验证请求在csrf防御圈之外。

## 框架使用
* 1.项目拥有Web.xml文件：只需将 *com.zy.security.web.config.subject* 包下的 **FilterChainProxy** 类注册为Filter即可，若需进行自定义配置则需继承 **WebSecurityConfigurerAdapter**。
* 2.项目不包含Web.xml文件：则配置 **com.zy.security.web.config.subject**  的包扫描，通过@WebFilter将此包路径下的FilterChainProxy注册为FIlter。
    比如使用SpringBoot的项目则需在启动类上标注 `@ServletComponentScan(basePackages = { "com.zy.security.web.config.subject" })` 注解。
* 3.项目中包名尽量不要为 **com.zy.security.web.session** ，在此包名下可调用**CsrfContextHolder**的 ```setCsrf(String sessionId,String csrf)```方法，使用sessionid更改服务器端保存的csrf凭证。
---    
框架测试项目地址：https://github.com/azurite-Y/SimpleSecurityTest
