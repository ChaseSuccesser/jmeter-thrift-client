# 使用JMeter测试thrift接口步骤
## 第一步
首先加入jmeter的依赖：
```
<dependency>
    <groupId>org.apache.jmeter</groupId>
    <artifactId>ApacheJMeter_java</artifactId>
    <version>2.13</version>
</dependency>
<dependency>
    <groupId>org.apache.jmeter</groupId>
    <artifactId>ApacheJMeter_core</artifactId>
    <version>2.13</version>
</dependency>
```

之后，执行`mvn install`会提示`
The following artifacts could not be resolved: 
commons-math3:commons-math3:jar:3.4.1, 
commons-pool2:commons-pool2:jar:2.3: Could not find artifact commons-math3:commons-math3:jar:3.4.1`

说是找不到依赖。临时的解决方法是：先下载  
`org.apache.commons:commons-math3:3.4.1`  
和  
`org.apache.commons:commons-pool2:2.3`  
这两个jar到本地

然后，执行下面的命令，安装到本地maven仓库

`mvn install:install-file -Dfile=d:/tmp/commons-math3-3.4.1.jar -DgroupId=commons-math3 -DartifactId=commons-math3 -Dversion=3.4.1 -Dpackaging=jar`

`mvn install:install-file -Dfile=d:/tmp/commons-pool2-2.3.jar -DgroupId=commons-pool2 -DartifactId=commons-pool2 -Dversion=2.3 -Dpackaging=jar`


## 第二步
写一个Java类，继承`AbstractJavaSamplerClient`.在实现方法里面执行thrift的逻辑.
例如:`com.ligx.demo.idl.JmeterThriftIdlClient`


## 第三步
执行`mvn clean install`打包.

因为需要把项目中所有相关的jar包都放在`<jmeter-home>\lib\ext`目录下，所以最好使用`maven-assembly-plugin`插件打一个fat jar.

打完包之后，只需要把这个fat jar放到`<jmeter-home>\lib\ext`目录即可.


## 第四步
启动JMeter

在新建的线程组里面新建Sampler -> Java请求

在出现的窗口中，类名称右边的下拉框里面选择例如`com.ligx.thrift.AbstractThriftSwiftClient`类名.

最后，配置相应的线程数，循环次数，就可以进行压力测试了.


# 如何使用这个项目
> 1.首先，`git clone git@github.com:ChaseSuccesser/jmeter-thrift-client.git`.
> 2.然后，检查本地是否有上面`第一步`提到的两个jar包，没有的话下载到本地maven仓库中.  
> 3.接下来，针对要测试的场景，分了下面两类:

## 第一种场景: 新建单元测试

1. 创建自己的单元测试类  
> 如果要测试thrift idl接口，就继承`AbstractThriftIdlClient`; 如果要测试thrift swift接口，就继承`AbstractThriftSwiftClient`;  
> 使用`@ThriftClient`注解标注要调用的thrift client;  
> 在`doTest()`方法里面编写调用thrift client的真正逻辑;  

2. 重复上面的`第三步`和`第四步`。

3. 然后在JMeter里定义ip和port参数。

## 第二种场景: 修改已经存在的单元测试

1. 修改完已经存在的单元测试类

2. 重复上面的`第三步`和`第四步`。

3. 然后在JMeter里定义ip和port参数。