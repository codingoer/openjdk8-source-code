/*
 * Copyright (c) 2003, 2013, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package java.lang.instrument;

import  java.io.File;
import  java.io.IOException;
import  java.util.jar.JarFile;

/*
 * Copyright 2003 Wily Technology, Inc.
 */

/**
 * This class provides services needed to instrument Java programming language code.
 * 该类提供了检测Java编程语言代码所需的服务。
 * Instrumentation is the addition of byte-codes to methods for the purpose of gathering data to be utilized by tools.
 * 插装是将字节码添加到方法中，以收集供工具使用的数据。
 * Since the changes are purely additive, these tools do not modify application state or behavior.
 * 由于这些更改纯粹是附加的，所以这些工具不会修改应用程序的状态或行为。
 * Examples of such benign tools include monitoring agents, profilers, coverage analyzers, and event loggers.
 * 此类良性工具的例子包括监视代理、剖析器、覆盖分析程序和事件记录器。
 *
 * <P>
 * There are two ways to obtain an instance of the <code>Instrumentation</code> interface:
 * 有两种方法可以获得Instrumentation接口的实例:
 *
 * <ol>
 *   <li><p> When a JVM is launched in a way that indicates an agent class.
 *     In that case an <code>Instrumentation</code> instance is passed to the <code>premain</code> method of the agent class.
 *     当JVM以指示代理类的方式启动时。在这种情况下，Instrumentation实例被传递给代理类的premain方法。
 *     </p></li>
 *   <li><p> When a JVM provides a mechanism to start agents sometime after the JVM is launched.
 *     In that case an <code>Instrumentation</code> instance is passed to the <code>agentmain</code> method of the agent code.
 *     当JVM提供在JVM启动后某个时间启动代理的机制时。在这种情况下，Instrumentation实例被传递给代理代码的agentmain方法。
 *     </p> </li>
 * </ol>
 * <p>
 * These mechanisms are described in the {@linkplain java.lang.instrument package specification}.
 * 这些机制在包规范中进行了描述。（https://docs.oracle.com/javase/8/docs/api/java/lang/instrument/Instrumentation.html）
 * <p>
 * Once an agent acquires an <code>Instrumentation</code> instance, the agent may call methods on the instance at any time.
 * 一旦代理获得了Instrumentation实例，代理就可以在任何时候调用该实例上的方法。
 *
 * @since   1.5
 */
public interface Instrumentation {
    /**
     * Registers the supplied transformer. All future class definitions
     * will be seen by the transformer, except definitions of classes upon which any registered transformer is dependent.
     * 注册所提供的转换器。所有未来的类定义都将被转换器看到，除了任何已注册的转换器所依赖的类的定义。
     * The transformer is called when classes are loaded, when they are {@linkplain #redefineClasses redefined}.
     * and if <code>canRetransform</code> is true, when they are {@linkplain #retransformClasses retransformed}.
     * 当类被重新定义时，转换器被调用。如果canRetransform为true，则当它们被重新转换时。
     * See {@link java.lang.instrument.ClassFileTransformer#transform
     * ClassFileTransformer.transform} for the order of transform calls.
     * 查看 ClassFileTransformer#transform 里面的转换调用的顺序。
     * If a transformer throws an exception during execution,the JVM will still call the other registered transformers in order.
     * 如果某个转换器在执行期间抛出异常，JVM仍将按顺序调用其他注册的转换器。
     * The same transformer may be added more than once,
     * but it is strongly discouraged -- avoid this by creating a new instance of transformer class.
     * 同一个转换器可以被添加多次，但强烈建议这样做 -- 通过创建转换器类的新实例来避免这种情况。
     * <P>
     * This method is intended for use in instrumentation, as described in the
     * {@linkplain Instrumentation class specification}.
     *
     * @param transformer          the transformer to register 要注册的转换器
     * @param canRetransform       can this transformer's transformations be retransformed 这个转换器的转换可以重新转换吗
     * @throws java.lang.NullPointerException if passed a <code>null</code> transformer
     * @throws java.lang.UnsupportedOperationException if <code>canRetransform</code>
     * is true and the current configuration of the JVM does not allow
     * retransformation ({@link #isRetransformClassesSupported} is false)
     * @since 1.6
     */
    void
    addTransformer(ClassFileTransformer transformer, boolean canRetransform);

    /**
     * Registers the supplied transformer.
     * <P>
     * Same as <code>addTransformer(transformer, false)</code>.
     *
     * @param transformer          the transformer to register
     * @throws java.lang.NullPointerException if passed a <code>null</code> transformer
     * @see    #addTransformer(ClassFileTransformer,boolean)
     */
    void
    addTransformer(ClassFileTransformer transformer);

    /**
     * Unregisters the supplied transformer. Future class definitions will not be shown to the transformer.
     * 取消注册所提供的transformer。将来的类定义不会显示给转换器。
     * Removes the most-recently-added matching instance of the transformer.
     * Due to the multi-threaded nature of class loading, it is possible for a transformer to receive calls
     * after it has been removed. Transformers should be written defensively to expect this situation.
     * 移除最近添加的转换器匹配实例。由于类加载的多线程特性，转换器在被删除后还可以接收调用。
     *
     * @param transformer          the transformer to unregister
     * @return  true if the transformer was found and removed, false if the
     *           transformer was not found
     * @throws java.lang.NullPointerException if passed a <code>null</code> transformer
     */
    boolean
    removeTransformer(ClassFileTransformer transformer);

    /**
     * Returns whether or not the current JVM configuration supports retransformation of classes.
     * 返回当前JVM配置是否支持类的重新转换。
     * The ability to retransform an already loaded class is an optional capability of a JVM.
     * 重新转换已经加载的类的能力是JVM的可选能力。
     * Retransformation will only be supported if the
     * <code>Can-Retransform-Classes</code> manifest attribute is set to
     * <code>true</code> in the agent JAR file (as described in the
     * {@linkplain java.lang.instrument package specification}) and the JVM supports this capability.
     * 只有当代理JAR文件中的Can-Retransform-Classes清单属性设置为true(如包规范中所述)并且JVM支持此功能时，才支持重新转换。
     * During a single instantiation of a single JVM, multiple calls to this method will always return the same answer.
     * 在单个JVM的单个实例化期间，对该方法的多次调用将总是返回相同的答案。
     * @return  true if the current JVM configuration supports retransformation of
     *          classes, false if not.
     * @see #retransformClasses
     * @since 1.6
     */
    boolean
    isRetransformClassesSupported();

    /**
     * Retransform the supplied set of classes. 重新转换所提供的类集。
     *
     * <P>
     * This function facilitates the instrumentation of already loaded classes.
     * 这个函数有助于对已经加载的类进行插装。
     * When classes are initially loaded or when they are {@linkplain #redefineClasses redefined},
     * the initial class file bytes can be transformed with the
     * {@link java.lang.instrument.ClassFileTransformer ClassFileTransformer}.
     * 在最初加载类或重新定义类时，可以使用ClassFileTransformer转换初始类文件字节。
     * This function reruns the transformation process (whether or not a transformation has previously occurred).
     * This retransformation follows these steps:
     * 这个函数重新运行转换过程(不管之前是否发生了转换)。这个重新转换遵循以下步骤:
     *  <ul>
     *    <li>starting from the initial class file bytes 从初始类文件字节开始
     *    </li>
     *    <li>for each transformer that was added with <code>canRetransform</code>
     *      false, the bytes returned by {@link java.lang.instrument.ClassFileTransformer#transform transform}
     *      during the last class load or redefine are reused as the output of the transformation;
     *      note that this is equivalent to reapplying the previous transformation, unaltered;
     *      except that
     *      {@link java.lang.instrument.ClassFileTransformer#transform transform} is not called
     *      对于每个添加了canRetransform false的转换器，在最后一次类加载或重定义期间transform返回的字节将被重用为转换的输出;
     *      请注意，这等价于重新应用之前的转换，没有改变
     *      只不过，没有调用{@link java.lang.instrument.ClassFileTransformer#transform transform}
     *    </li>
     *    <li>for each transformer that was added with <code>canRetransform</code>
     *      true, the {@link java.lang.instrument.ClassFileTransformer#transform transform}
     *      method is called in these transformers
     *      对于每个添加了canRetransform true的变压器，在这些变压器中调用transform方法
     *    </li>
     *    <li>the transformed class file bytes are installed as the new
     *      definition of the class 转换后的类文件字节作为类的新定义安装
     *    </li>
     *  </ul>
     * <P>
     *
     * The order of transformation is described in the
     * {@link java.lang.instrument.ClassFileTransformer#transform transform} method.
     * This same order is used in the automatic reapplication of retransformation incapable transforms.
     * 同样的顺序也用于自动重新应用重构无能力的变换。？？？
     * <P>
     *
     * The initial class file bytes represent the bytes passed to
     * {@link java.lang.ClassLoader#defineClass ClassLoader.defineClass} or {@link #redefineClasses redefineClasses}
     * (before any transformations were applied), however they might not exactly match them.
     * 初始的类文件字节表示传递给ClassLoader.defineClass或redefineclass(在应用任何转换之前)的字节，但它们可能不完全匹配。
     *
     *  The constant pool might not have the same layout or contents.
     *  The constant pool may have more or fewer entries.
     *  Constant pool entries may be in a different order;
     *  however, constant pool indices in the bytecodes of methods will correspond.
     *
     *  Some attributes may not be present. Where order is not meaningful, for example the order of methods,
     *  order might not be preserved.
     *
     * <P>
     * This method operates on
     * a set in order to allow interdependent changes to more than one class at the same time
     * (a retransformation of class A can require a retransformation of class B).
     * 此方法操作一个集合，以便允许同时对多个类进行相互依赖的更改(类a的重新转换可能需要类B的重新转换)。
     * <P>
     * If a retransformed method has active stack frames, those active frames continue to
     * run the bytecodes of the original method.
     * The retransformed method will be used on new invokes.
     * 如果重新转换的方法具有活动的堆栈帧，这些活动帧将继续运行原始方法的字节码。重新转换的方法将用于新的调用。
     * <P>
     * This method does not cause any initialization except that which would occur
     * under the customary JVM semantics. In other words, redefining a class
     * does not cause its initializers to be run. The values of static variables
     * will remain as they were prior to the call.
     * 换句话说，重新定义类不会导致运行它的初始化式。静态变量的值将保持在调用之前的状态。
     * <P>
     * Instances of the retransformed class are not affected.
     *
     * <P>
     * The retransformation may change method bodies, the constant pool and attributes.
     * The retransformation must not add, remove or rename fields or methods, change the
     * signatures of methods, or change inheritance.  These restrictions maybe be
     * lifted in future versions.  The class file bytes are not checked, verified and installed
     * until after the transformations have been applied, if the resultant bytes are in
     * error this method will throw an exception.
     *
     * <P>
     * If this method throws an exception, no classes have been retransformed.
     * <P>
     * This method is intended for use in instrumentation, as described in the
     * {@linkplain Instrumentation class specification}.
     *
     * @param classes array of classes to retransform;
     *                a zero-length array is allowed, in this case, this method does nothing
     * @throws java.lang.instrument.UnmodifiableClassException if a specified class cannot be modified
     * ({@link #isModifiableClass} would return <code>false</code>)
     * @throws java.lang.UnsupportedOperationException if the current configuration of the JVM does not allow
     * retransformation ({@link #isRetransformClassesSupported} is false) or the retransformation attempted
     * to make unsupported changes
     * @throws java.lang.ClassFormatError if the data did not contain a valid class
     * @throws java.lang.NoClassDefFoundError if the name in the class file is not equal to the name of the class
     * @throws java.lang.UnsupportedClassVersionError if the class file version numbers are not supported
     * @throws java.lang.ClassCircularityError if the new classes contain a circularity
     * @throws java.lang.LinkageError if a linkage error occurs
     * @throws java.lang.NullPointerException if the supplied classes  array or any of its components
     *                                        is <code>null</code>.
     *
     * @see #isRetransformClassesSupported
     * @see #addTransformer
     * @see java.lang.instrument.ClassFileTransformer
     * @since 1.6
     */
    void
    retransformClasses(Class<?>... classes) throws UnmodifiableClassException;

    /**
     * Returns whether or not the current JVM configuration supports redefinition of classes.
     * 返回当前JVM配置是否支持类的重定义。
     * The ability to redefine an already loaded class is an optional capability of a JVM.
     * 重定义已经加载的类的能力是JVM的可选功能。
     * Redefinition will only be supported if the
     * <code>Can-Redefine-Classes</code> manifest attribute is set to
     * <code>true</code> in the agent JAR file (as described in the
     * {@linkplain java.lang.instrument package specification}) and the JVM supports
     * this capability.
     * During a single instantiation of a single JVM, multiple calls to this
     * method will always return the same answer.
     * @return  true if the current JVM configuration supports redefinition of classes,
     * false if not.
     * @see #redefineClasses
     */
    boolean
    isRedefineClassesSupported();

    /**
     * Redefine the supplied set of classes using the supplied class files.
     * 使用所提供的类文件重新定义所提供的类集。
     * <P>
     * This method is used to replace the definition of a class without reference
     * to the existing class file bytes, as one might do when recompiling from source for fix-and-continue debugging.
     * 此方法用于在不引用现有类文件字节的情况下替换类的定义，就像从源代码重新编译以进行“修复并继续”调试时可能做的那样
     * Where the existing class file bytes are to be transformed (for example in bytecode instrumentation)
     * {@link #retransformClasses retransformClasses} should be used.
     * 如果要转换现有的类文件字节(例如在字节码插桩中)，就应该使用retransformClasses。
     *
     * <P>
     * This method operates on
     * a set in order to allow interdependent changes to more than one class at the same time
     * (a redefinition of class A can require a redefinition of class B).
     * 此方法操作一个集合，以便允许同时对多个类进行相互依赖的更改(类a的重新定义可能需要类B的重新定义)。
     * <P>
     * If a redefined method has active stack frames, those active frames continue to
     * run the bytecodes of the original method.
     * The redefined method will be used on new invokes.
     * 如果重新定义的方法具有活动的堆栈帧，那么这些活动帧将继续运行原始方法的字节码。重新定义的方法将用于新的调用。
     * <P>
     * This method does not cause any initialization except that which would occur
     * under the customary JVM semantics. In other words, redefining a class
     * does not cause its initializers to be run. The values of static variables
     * will remain as they were prior to the call.
     *
     * <P>
     * Instances of the redefined class are not affected.
     *
     * <P>
     * The redefinition may change method bodies, the constant pool and attributes.
     * The redefinition must not add, remove or rename fields or methods, change the signatures of methods, or change inheritance.
     * These restrictions maybe be lifted in future versions.
     * The class file bytes are not checked, verified and installed until after the transformations have been applied,
     * if the resultant bytes are in error this method will throw an exception.
     * 重定义可能会改变方法主体、常量池和属性。重定义不能添加、删除或重命名字段或方法，不能更改方法的签名，也不能更改继承。
     * 这些限制可能会在未来的版本中被取消。在应用转换之前，不会检查、验证和安装类文件字节，如果生成的字节有错误，此方法将抛出异常。
     *
     * <P>
     * If this method throws an exception, no classes have been redefined.
     * <P>
     * This method is intended for use in instrumentation, as described in the
     * {@linkplain Instrumentation class specification}.
     *
     * @param definitions array of classes to redefine with corresponding definitions;
     *                    a zero-length array is allowed, in this case, this method does nothing
     * @throws java.lang.instrument.UnmodifiableClassException if a specified class cannot be modified
     * ({@link #isModifiableClass} would return <code>false</code>)
     * @throws java.lang.UnsupportedOperationException if the current configuration of the JVM does not allow
     * redefinition ({@link #isRedefineClassesSupported} is false) or the redefinition attempted
     * to make unsupported changes
     * @throws java.lang.ClassFormatError if the data did not contain a valid class
     * @throws java.lang.NoClassDefFoundError if the name in the class file is not equal to the name of the class
     * @throws java.lang.UnsupportedClassVersionError if the class file version numbers are not supported
     * @throws java.lang.ClassCircularityError if the new classes contain a circularity
     * @throws java.lang.LinkageError if a linkage error occurs
     * @throws java.lang.NullPointerException if the supplied definitions array or any of its components
     * is <code>null</code>
     * @throws java.lang.ClassNotFoundException Can never be thrown (present for compatibility reasons only)
     *
     * @see #isRedefineClassesSupported
     * @see #addTransformer
     * @see java.lang.instrument.ClassFileTransformer
     */
    void
    redefineClasses(ClassDefinition... definitions)
        throws  ClassNotFoundException, UnmodifiableClassException;


    /**
     * Determines whether a class is modifiable by
     * {@linkplain #retransformClasses retransformation} or {@linkplain #redefineClasses redefinition}.
     * 确定类是否可以通过重新转换或重新定义进行修改。如果类是可修改的，则此方法返回true。如果类不可修改，则此方法返回false。
     * If a class is modifiable then this method returns <code>true</code>.
     * If a class is not modifiable then this method returns <code>false</code>.
     * <P>
     * For a class to be retransformed, {@link #isRetransformClassesSupported} must also be true.
     * But the value of <code>isRetransformClassesSupported()</code> does not influence the value
     * returned by this function.
     * For a class to be redefined, {@link #isRedefineClassesSupported} must also be true.
     * But the value of <code>isRedefineClassesSupported()</code> does not influence the value
     * returned by this function.
     * <P>
     * Primitive classes (for example, <code>java.lang.Integer.TYPE</code>)
     * and array classes are never modifiable.
     *
     * @param theClass the class to check for being modifiable
     * @return whether or not the argument class is modifiable
     * @throws java.lang.NullPointerException if the specified class is <code>null</code>.
     *
     * @see #retransformClasses
     * @see #isRetransformClassesSupported
     * @see #redefineClasses
     * @see #isRedefineClassesSupported
     * @since 1.6
     */
    boolean
    isModifiableClass(Class<?> theClass);

    /**
     * Returns an array of all classes currently loaded by the JVM.
     *
     * @return an array containing all the classes loaded by the JVM, zero-length if there are none
     */
    @SuppressWarnings("rawtypes")
    Class[]
    getAllLoadedClasses();

    /**
     * Returns an array of all classes for which <code>loader</code> is an initiating loader.
     * If the supplied loader is <code>null</code>, classes initiated by the bootstrap class loader are returned.
     * 返回装入器为其初始化装入器的所有类的数组。如果提供的装入器为空，则返回由引导类装入器初始化的类。
     *
     * @param loader          the loader whose initiated class list will be returned
     * @return an array containing all the classes for which loader is an initiating loader,
     *          zero-length if there are none
     */
    @SuppressWarnings("rawtypes")
    Class[]
    getInitiatedClasses(ClassLoader loader);

    /**
     * Returns an implementation-specific approximation of the amount of storage consumed by
     * the specified object. The result may include some or all of the object's overhead,
     * and thus is useful for comparison within an implementation but not between implementations.
     *
     * The estimate may change during a single invocation of the JVM.
     *
     * @param objectToSize     the object to size
     * @return an implementation-specific approximation of the amount of storage consumed by the specified object
     * @throws java.lang.NullPointerException if the supplied Object is <code>null</code>.
     */
    long
    getObjectSize(Object objectToSize);


    /**
     * Specifies a JAR file with instrumentation classes to be defined by the bootstrap class loader.
     * 指定一个JAR文件，其中包含要由引导类装入器定义的插装类。
     *
     * <p> When the virtual machine's built-in class loader, known as the "bootstrap class loader",
     * unsuccessfully searches for a class, the entries in the {@link java.util.jar.JarFile JAR file} will be searched as well.
     * 当虚拟机的内置类装入器(称为“引导类装入器”)搜索类失败时，也会搜索JAR文件中的条目。
     *
     * <p> This method may be used multiple times to add multiple JAR files to be
     * searched in the order that this method was invoked.该方法可以多次使用，以按调用该方法的顺序添加要搜索的多个JAR文件。
     *
     * <p> The agent should take care to ensure that the JAR does not contain any
     * classes or resources other than those to be defined by the bootstrap
     * class loader for the purpose of instrumentation.
     * Failure to observe this warning could result in unexpected
     * behavior that is difficult to diagnose. For example, suppose there is a
     * loader L, and L's parent for delegation is the bootstrap class loader.
     * Furthermore, a method in class C, a class defined by L, makes reference to
     * a non-public accessor class C$1. If the JAR file contains a class C$1 then
     * the delegation to the bootstrap class loader will cause C$1 to be defined
     * by the bootstrap class loader. In this example an <code>IllegalAccessError</code>
     * will be thrown that may cause the application to fail. One approach to
     * avoiding these types of issues, is to use a unique package name for the
     * instrumentation classes.
     *
     * <p>
     * <cite>The Java&trade; Virtual Machine Specification</cite>
     * specifies that a subsequent attempt to resolve a symbolic
     * reference that the Java virtual machine has previously unsuccessfully attempted
     * to resolve always fails with the same error that was thrown as a result of the
     * initial resolution attempt. Consequently, if the JAR file contains an entry
     * that corresponds to a class for which the Java virtual machine has
     * unsuccessfully attempted to resolve a reference, then subsequent attempts to
     * resolve that reference will fail with the same error as the initial attempt.
     *
     * @param   jarfile
     *          The JAR file to be searched when the bootstrap class loader
     *          unsuccessfully searches for a class.
     *
     * @throws  NullPointerException
     *          If <code>jarfile</code> is <code>null</code>.
     *
     * @see     #appendToSystemClassLoaderSearch
     * @see     java.lang.ClassLoader
     * @see     java.util.jar.JarFile
     *
     * @since 1.6
     */
    void
    appendToBootstrapClassLoaderSearch(JarFile jarfile);

    /**
     * Specifies a JAR file with instrumentation classes to be defined by the system class loader.
     * 指定带有系统类装入器定义的检测类的JAR文件。
     *
     * When the system class loader for delegation (see
     * {@link java.lang.ClassLoader#getSystemClassLoader getSystemClassLoader()})
     * unsuccessfully searches for a class, the entries in the {@link
     * java.util.jar.JarFile JarFile} will be searched as well.
     *
     * <p> This method may be used multiple times to add multiple JAR files to be
     * searched in the order that this method was invoked.
     *
     * <p> The agent should take care to ensure that the JAR does not contain any
     * classes or resources other than those to be defined by the system class
     * loader for the purpose of instrumentation.
     * Failure to observe this warning could result in unexpected
     * behavior that is difficult to diagnose (see
     * {@link #appendToBootstrapClassLoaderSearch
     * appendToBootstrapClassLoaderSearch}).
     *
     * <p> The system class loader supports adding a JAR file to be searched if
     * it implements a method named <code>appendToClassPathForInstrumentation</code>
     * which takes a single parameter of type <code>java.lang.String</code>. The
     * method is not required to have <code>public</code> access. The name of
     * the JAR file is obtained by invoking the {@link java.util.zip.ZipFile#getName
     * getName()} method on the <code>jarfile</code> and this is provided as the
     * parameter to the <code>appendToClassPathForInstrumentation</code> method.
     *
     * <p>
     * <cite>The Java&trade; Virtual Machine Specification</cite>
     * specifies that a subsequent attempt to resolve a symbolic
     * reference that the Java virtual machine has previously unsuccessfully attempted
     * to resolve always fails with the same error that was thrown as a result of the
     * initial resolution attempt. Consequently, if the JAR file contains an entry
     * that corresponds to a class for which the Java virtual machine has
     * unsuccessfully attempted to resolve a reference, then subsequent attempts to
     * resolve that reference will fail with the same error as the initial attempt.
     *
     * <p> This method does not change the value of <code>java.class.path</code>
     * {@link java.lang.System#getProperties system property}.
     *
     * @param   jarfile
     *          The JAR file to be searched when the system class loader
     *          unsuccessfully searches for a class.
     *
     * @throws  UnsupportedOperationException
     *          If the system class loader does not support appending a
     *          a JAR file to be searched.
     *
     * @throws  NullPointerException
     *          If <code>jarfile</code> is <code>null</code>.
     *
     * @see     #appendToBootstrapClassLoaderSearch
     * @see     java.lang.ClassLoader#getSystemClassLoader
     * @see     java.util.jar.JarFile
     * @since 1.6
     */
    void
    appendToSystemClassLoaderSearch(JarFile jarfile);

    /**
     * Returns whether the current JVM configuration supports
     * {@linkplain #setNativeMethodPrefix(ClassFileTransformer,String)
     * setting a native method prefix}.
     * The ability to set a native method prefix is an optional
     * capability of a JVM.
     * Setting a native method prefix will only be supported if the
     * <code>Can-Set-Native-Method-Prefix</code> manifest attribute is set to
     * <code>true</code> in the agent JAR file (as described in the
     * {@linkplain java.lang.instrument package specification}) and the JVM supports
     * this capability.
     * During a single instantiation of a single JVM, multiple
     * calls to this method will always return the same answer.
     * @return  true if the current JVM configuration supports
     * setting a native method prefix, false if not.
     * @see #setNativeMethodPrefix
     * @since 1.6
     */
    boolean
    isNativeMethodPrefixSupported();

    /**
     * This method modifies the failure handling of
     * native method resolution by allowing retry
     * with a prefix applied to the name.
     * When used with the
     * {@link java.lang.instrument.ClassFileTransformer ClassFileTransformer},
     * it enables native methods to be
     * instrumented.
     * <p>
     * Since native methods cannot be directly instrumented
     * (they have no bytecodes), they must be wrapped with
     * a non-native method which can be instrumented.
     * For example, if we had:
     * <pre>
     *   native boolean foo(int x);</pre>
     * <p>
     * We could transform the class file (with the
     * ClassFileTransformer during the initial definition
     * of the class) so that this becomes:
     * <pre>
     *   boolean foo(int x) {
     *     <i>... record entry to foo ...</i>
     *     return wrapped_foo(x);
     *   }
     *
     *   native boolean wrapped_foo(int x);</pre>
     * <p>
     * Where <code>foo</code> becomes a wrapper for the actual native
     * method with the appended prefix "wrapped_".  Note that
     * "wrapped_" would be a poor choice of prefix since it
     * might conceivably form the name of an existing method
     * thus something like "$$$MyAgentWrapped$$$_" would be
     * better but would make these examples less readable.
     * <p>
     * The wrapper will allow data to be collected on the native
     * method call, but now the problem becomes linking up the
     * wrapped method with the native implementation.
     * That is, the method <code>wrapped_foo</code> needs to be
     * resolved to the native implementation of <code>foo</code>,
     * which might be:
     * <pre>
     *   Java_somePackage_someClass_foo(JNIEnv* env, jint x)</pre>
     * <p>
     * This function allows the prefix to be specified and the
     * proper resolution to occur.
     * Specifically, when the standard resolution fails, the
     * resolution is retried taking the prefix into consideration.
     * There are two ways that resolution occurs, explicit
     * resolution with the JNI function <code>RegisterNatives</code>
     * and the normal automatic resolution.  For
     * <code>RegisterNatives</code>, the JVM will attempt this
     * association:
     * <pre>{@code
     *   method(foo) -> nativeImplementation(foo)
     * }</pre>
     * <p>
     * When this fails, the resolution will be retried with
     * the specified prefix prepended to the method name,
     * yielding the correct resolution:
     * <pre>{@code
     *   method(wrapped_foo) -> nativeImplementation(foo)
     * }</pre>
     * <p>
     * For automatic resolution, the JVM will attempt:
     * <pre>{@code
     *   method(wrapped_foo) -> nativeImplementation(wrapped_foo)
     * }</pre>
     * <p>
     * When this fails, the resolution will be retried with
     * the specified prefix deleted from the implementation name,
     * yielding the correct resolution:
     * <pre>{@code
     *   method(wrapped_foo) -> nativeImplementation(foo)
     * }</pre>
     * <p>
     * Note that since the prefix is only used when standard
     * resolution fails, native methods can be wrapped selectively.
     * <p>
     * Since each <code>ClassFileTransformer</code>
     * can do its own transformation of the bytecodes, more
     * than one layer of wrappers may be applied. Thus each
     * transformer needs its own prefix.  Since transformations
     * are applied in order, the prefixes, if applied, will
     * be applied in the same order
     * (see {@link #addTransformer(ClassFileTransformer,boolean) addTransformer}).
     * Thus if three transformers applied
     * wrappers, <code>foo</code> might become
     * <code>$trans3_$trans2_$trans1_foo</code>.  But if, say,
     * the second transformer did not apply a wrapper to
     * <code>foo</code> it would be just
     * <code>$trans3_$trans1_foo</code>.  To be able to
     * efficiently determine the sequence of prefixes,
     * an intermediate prefix is only applied if its non-native
     * wrapper exists.  Thus, in the last example, even though
     * <code>$trans1_foo</code> is not a native method, the
     * <code>$trans1_</code> prefix is applied since
     * <code>$trans1_foo</code> exists.
     *
     * @param   transformer
     *          The ClassFileTransformer which wraps using this prefix.
     * @param   prefix
     *          The prefix to apply to wrapped native methods when
     *          retrying a failed native method resolution. If prefix
     *          is either <code>null</code> or the empty string, then
     *          failed native method resolutions are not retried for
     *          this transformer.
     * @throws java.lang.NullPointerException if passed a <code>null</code> transformer.
     * @throws java.lang.UnsupportedOperationException if the current configuration of
     *           the JVM does not allow setting a native method prefix
     *           ({@link #isNativeMethodPrefixSupported} is false).
     * @throws java.lang.IllegalArgumentException if the transformer is not registered
     *           (see {@link #addTransformer(ClassFileTransformer,boolean) addTransformer}).
     *
     * @since 1.6
     */
    void
    setNativeMethodPrefix(ClassFileTransformer transformer, String prefix);
}
