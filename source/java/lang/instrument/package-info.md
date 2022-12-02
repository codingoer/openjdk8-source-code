# Package java.lang.instrument

[Oracle Docs](https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/package-summary.html#package.description)

Since:JDK1.5

## Description

Provides services that allow Java programming language agents to instrument programs running on the JVM. 
The mechanism for instrumentation is modification of the byte-codes of methods.

提供允许Java编程语言代理检测运行在JVM上的程序的服务。检测的机制是修改方法的字节码。

### Package Specification

An agent is deployed as a JAR file. 
An attribute in the JAR file manifest specifies the agent class which will be loaded to start the agent. 
For implementations that support a command-line interface, an agent is started by specifying an option on the command-line. 
Implementations may also support a mechanism to start agents some time after the VM has started. 
For example, an implementation may provide a mechanism that allows a tool to attach to a running application, and initiate the loading of the tool's agent into the running application. 
The details as to how the load is initiated, is implementation dependent.

代理作为JAR文件部署。JAR文件清单中的一个属性指定启动代理时将加载的代理类。对于支持命令行接口的实现，代理通过在命令行上指定一个选项来启动。
实现还可能支持在JVM启动后一段时间启动代理的机制。
例如，实现可以提供一种机制，允许工具附加到正在运行的应用程序，并初始化将工具的代理加载到正在运行的应用程序中。
关于如何启动加载的细节取决于实现。

### Command-Line Interface

An implementation is not required to provide a way to start agents from the command-line interface. 
On implementations that do provide a way to start agents from the command-line interface, an agent is started by adding this option to the command-line:

不需要实现来提供从命令行接口启动代理的方法。如果实现提供了从命令行界面启动代理的方法，则通过将此选项添加到命令行来启动代理:

```bash
-javaagent:jarpath[=options]
```

jarpath is the path to the agent JAR file. options is the agent options. 
This switch may be used multiple times on the same command-line, thus creating multiple agents. 
More than one agent may use the same jarpath. An agent JAR file must conform to the JAR file specification.

jarpath是代理JAR文件的路径。Options是代理选项。 这个开关可以在同一个命令行上多次使用，从而创建多个代理。多个代理可以使用相同的jarpath。代理JAR文件必须符合JAR文件规范。

The manifest of the agent JAR file must contain the attribute Premain-Class. The value of this attribute is the name of the agent class. 
The agent class must implement a public static premain method similar in principle to the main application entry point. 
After the Java Virtual Machine (JVM) has initialized, each premain method will be called in the order the agents were specified, then the real application main method will be called. 
Each premain method must return in order for the startup sequence to proceed.

代理JAR文件的清单必须包含属性Premain-Class。此属性的值是代理类的名称。代理类必须实现一个公共静态premain方法，在原理上类似于主应用程序入口点。
在Java虚拟机(JVM)初始化之后，将按照指定代理的顺序调用每个预主方法，然后调用真正的应用程序主方法。每个premain方法必须返回，以便启动顺序继续进行。

The premain method has one of two possible signatures. The JVM first attempts to invoke the following method on the agent class:

premain方法有两个可能的签名。JVM首先尝试在代理类上调用以下方法:

```java
public static void premain(String agentArgs, Instrumentation inst);
```

If the agent class does not implement this method then the JVM will attempt to invoke:

如果代理类没有实现这个方法，那么JVM将尝试调用:

```java
public static void premain(String agentArgs);
```

The agent class may also have an agentmain method for use when the agent is started after VM startup. 
When the agent is started using a command-line option, the agentmain method is not invoked.

代理类还可以有一个agentmain方法，用于在VM启动后启动代理时使用。当使用命令行选项启动代理时，不会调用agentmain方法。

The agent class will be loaded by the system class loader (see **ClassLoader.getSystemClassLoader**). 
This is the class loader which typically loads the class containing the application main method. 
The premain methods will be run under the same security and classloader rules as the application main method. 
There are no modeling restrictions on what the agent premain method may do. Anything application main can do, including creating threads, is legal from premain.

代理类将由系统类装入器装入(参见**ClassLoader.getSystemClassLoader**)。这是类加载器，通常加载包含应用程序主方法的类。
premain方法将在与应用程序主方法相同的安全和类加载器规则下运行。对于代理premain方法可以做什么，没有建模限制。任何应用程序main可以做的事情，包括创建线程，都是premain合法的。

Each agent is passed its agent options via the agentArgs parameter. 
The agent options are passed as a single string, any additional parsing should be performed by the agent itself.
If the agent cannot be resolved (for example, because the agent class cannot be loaded, or because the agent class does not have an appropriate premain method), the JVM will abort. 
If a premain method throws an uncaught exception, the JVM will abort.

每个代理通过agentArgs参数传递其代理选项。代理选项作为单个字符串传递，任何额外的解析都应该由代理本身执行。
如果代理无法解析(例如，因为代理类无法加载，或者因为代理类没有适当的premain方法)，JVM将中止。如果premain方法抛出未捕获的异常，JVM将中止。

### Starting Agents After VM Startup

An implementation may provide a mechanism to start agents sometime after the the VM has started. 
The details as to how this is initiated are implementation specific but typically the application has already started and its main method has already been invoked. 
In cases where an implementation supports the starting of agents after the VM has started the following applies:

1. The manifest of the agent JAR must contain the attribute Agent-Class. The value of this attribute is the name of the agent class.
2. The agent class must implement a public static agentmain method.
3. The system class loader ( ClassLoader.getSystemClassLoader) must support a mechanism to add an agent JAR file to the system class path.

实现可以提供一种机制，在VM启动后启动代理。启动方式的细节取决于具体的实现，但通常应用程序已经启动，其main方法已经被调用。
如果某个实现支持在虚拟机启动后启动代理，则适用以下情况:

1. 代理JAR的清单必须包含属性Agent-Class。此属性的值是代理类的名称。
2. 代理类必须实现一个公共静态agentmain方法。
3. 系统类装入器(ClassLoader.getSystemClassLoader)必须支持将代理JAR文件添加到系统类路径的机制。

The agent JAR is appended to the system class path. This is the class loader that typically loads the class containing the application main method. 
The agent class is loaded and the JVM attempts to invoke the agentmain method. The JVM first attempts to invoke the following method on the agent class:

代理JAR添加到系统类路径。这是类装入器，通常装入包含应用程序主方法的类。代理类被加载，JVM尝试调用agentmain方法。JVM首先尝试在代理类上调用以下方法:

```java
public static void agentmain(String agentArgs, Instrumentation inst);
```

If the agent class does not implement this method then the JVM will attempt to invoke:

如果代理类没有实现这个方法，那么JVM将尝试调用:

```java
public static void agentmain(String agentArgs);
```

The agent class may also have an premain method for use when the agent is started using a command-line option. 
When the agent is started after VM startup the premain method is not invoked. The agent is passed its agent options via the agentArgs parameter. 
The agent options are passed as a single string, any additional parsing should be performed by the agent itself.

代理类还可以有一个premain方法，以便在使用命令行选项启动代理时使用。当代理在VM启动后启动时，premain方法不会被调用。 
代理通过agentArgs参数传递其代理选项。代理选项作为单个字符串传递，任何额外的解析都应该由代理本身执行。

The agentmain method should do any necessary initialization required to start the agent. When startup is complete the method should return. 
If the agent cannot be started (for example, because the agent class cannot be loaded, or because the agent class does not have a conformant agentmain method), the JVM will not abort. 
If the agentmain method throws an uncaught exception it will be ignored.

agentmain方法应该执行启动代理所需的任何必要的初始化。当启动完成时，该方法应该返回。
如果代理无法启动(例如，因为代理类无法加载，或者因为代理类没有符合要求的agentmain方法)，JVM将不会中止。如果agentmain方法抛出未捕获的异常，则该异常将被忽略。

### Manifest Attributes

The following manifest attributes are defined for an agent JAR file:

 - Premain-Class
   
   When an agent is specified at JVM launch time this attribute specifies the agent class. 
   That is, the class containing the premain method. When an agent is specified at JVM launch time this attribute is required. 
   If the attribute is not present the JVM will abort. Note: this is a class name, not a file name or path.
   
 - Agent-Class
   
   If an implementation supports a mechanism to start agents sometime after the VM has started then this attribute specifies the agent class. 
   That is, the class containing the agentmain method. This attribute is required, if it is not present the agent will not be started. 
   Note: this is a class name, not a file name or path.
   
 - Boot-Class-Path
   
   A list of paths to be searched by the bootstrap class loader. 
   Paths represent directories or libraries (commonly referred to as JAR or zip libraries on many platforms). 
   These paths are searched by the bootstrap class loader after the platform specific mechanisms of locating a class have failed. 
   Paths are searched in the order listed. Paths in the list are separated by one or more spaces. 
   A path takes the syntax of the path component of a hierarchical URI. 
   The path is absolute if it begins with a slash character ('/'), otherwise it is relative. 
   A relative path is resolved against the absolute path of the agent JAR file. 
   Malformed and non-existent paths are ignored. 
   When an agent is started sometime after the VM has started then paths that do not represent a JAR file are ignored. This attribute is optional.
   
 - Can-Redefine-Classes
   
   Boolean (true or false, case irrelevant). 
   Is the ability to redefine classes needed by this agent. 
   Values other than true are considered false. This attribute is optional, the default is false.
   
 - Can-Retransform-Classes
   
   Boolean (true or false, case irrelevant). 
   Is the ability to retransform classes needed by this agent. 
   Values other than true are considered false. This attribute is optional, the default is false.
   
 - Can-Set-Native-Method-Prefix
   
   Boolean (true or false, case irrelevant). 
   Is the ability to set native method prefix needed by this agent. 
   Values other than true are considered false. This attribute is optional, the default is false.

以下清单属性被定义为一个代理JAR文件:

 - Premain-Class 
   
   当在JVM启动时指定代理时，此属性指定代理类。也就是说，包含premain方法的类。当在JVM启动时指定代理时，此属性是必需的。
   如果该属性不存在，则JVM将中止。注意:这是一个类名，而不是文件名或路径。
   
 - Agent-Class
   
   如果实现支持在VM启动后某个时间启动代理的机制，则此属性指定代理类。 也就是说，包含agentmain方法的类。此属性是必需的，如果它不存在，代理将不会启动。
   注意:这是一个类名，而不是文件名或路径。
   
 - Boot-Class-Path
   
   引导类装入器要搜索的路径列表。 路径表示目录或库(在许多平台上通常称为JAR或zip库)。在定位类的特定于平台的机制失败后，引导类装入器会搜索这些路径。
   按照列出的顺序搜索路径。列表中的路径由一个或多个空格分隔。路径采用分层URI的路径组件的语法。如果路径以斜杠字符('/')开头，则为绝对路径，否则为相对路径。
   根据代理JAR文件的绝对路径解析相对路径。畸形和不存在的路径将被忽略。
   当代理在VM启动后的某个时间启动时，不代表JAR文件的路径将被忽略。这个属性是可选的。
   
 - Can-Redefine-Classes
   
   布尔值(真或假，大小写无关)。 是重新定义此代理所需的类的能力。非true的值被认为是false。该属性是可选的，默认值为false。
   
 - Can-Retransform-Classes
   
   布尔值(真或假，大小写无关)。是转换此代理所需的类的能力。非true的值被认为是false。该属性是可选的，默认值为false。
   
 - Can-Set-Native-Method-Prefix
   
   布尔值(真或假，大小写无关)。是设置此代理所需的本机方法前缀的能力。非true的值被认为是false。该属性是可选的，默认值为false。


An agent JAR file may have both the Premain-Class and Agent-Class attributes present in the manifest. 
When the agent is started on the command-line using the -javaagent option then the Premain-Class attribute specifies the name of the agent class and the Agent-Class attribute is ignored. 
Similarly, if the agent is started sometime after the VM has started, then the Agent-Class attribute specifies the name of the agent class (the value of Premain-Class attribute is ignored).

一个代理JAR文件可能同时具有清单中的Premain-Class和agent - class属性。
当使用-javaagent选项在命令行上启动代理时，Premain-Class属性指定代理类的名称，而agent - class属性将被忽略。
类似地，如果代理是在虚拟机启动后某个时间启动的，那么agent - class属性指定代理类的名称(Premain-Class属性的值被忽略)。

## Summary

### Interface Summary

 - ClassFileTransformer

   An agent provides an implementation of this interface in order to transform class files. 代理提供这个接口的实现，以便转换类文件。

 - Instrumentation

   This class provides services needed to instrument Java programming language code. 该类提供了检测Java编程语言代码所需的服务。

### Class Summary

 - ClassDefinition

   This class serves as a parameter block to the Instrumentation.redefineClasses method.该类充当Instrumentation的参数块。redefineClasses方法。

### Exception Summary

 - IllegalClassFormatException

   Thrown by an implementation of `ClassFileTransformer.transform` when its input parameters are invalid. 

 - UnmodifiableClassException

   Thrown by an implementation of `Instrumentation.redefineClasses` when one of the specified classes cannot be modified. 