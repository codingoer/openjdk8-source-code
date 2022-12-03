/*
 * Copyright (c) 2003, 2011, Oracle and/or its affiliates. All rights reserved.
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

import  java.security.ProtectionDomain;

/*
 * Copyright 2003 Wily Technology, Inc.
 */

/**
 * An agent provides an implementation of this interface in order to transform class files.
 * The transformation occurs before the class is defined by the JVM.
 * 代理提供这个接口的实现，以便转换类文件。转换发生在JVM定义类之前。
 * <P>
 * Note the term <i>class file</i> is used as defined in section 3.1 of <cite>The Java&trade; Virtual Machine Specification</cite>,
 * to mean a sequence of bytes in class file format, whether or not they reside in a file.
 * 注意，使用Java™虚拟机规范第3.1节中定义的术语类文件，表示类文件格式的字节序列，无论它们是否驻留在文件中。
 *
 *
 * @see     java.lang.instrument.Instrumentation
 * @see     java.lang.instrument.Instrumentation#addTransformer
 * @see     java.lang.instrument.Instrumentation#removeTransformer
 * @since   1.5
 */

public interface ClassFileTransformer {
    /**
     * The implementation of this method may transform the supplied class file and
     * return a new replacement class file.此方法的实现可以转换所提供的类文件并返回一个新的替换类文件。
     *
     * <P>
     * There are two kinds of transformers, determined by the <code>canRetransform</code> parameter of
     * {@link java.lang.instrument.Instrumentation#addTransformer(ClassFileTransformer,boolean)}:
     * 有两种类型的转换器，由 Instrumentation#addtransformer(ClassFileTransformer,boolean)的canRetransform参数确定:
     *  <ul>
     *    <li><i>retransformation capable</i> transformers that were added with <code>canRetransform</code> as true
     *        添加了canRetransform为true的能重新转换的转换器。
     *    </li>
     *    <li><i>retransformation incapable</i> transformers that were added with
     *        <code>canRetransform</code> as false or where added with {@link java.lang.instrument.Instrumentation#addTransformer(ClassFileTransformer)}
     *        添加了canRetransform为false或添加了 Instrumentation#addtransformer(ClassFileTransformer)的不能重新转换的转换器
     *    </li>
     *  </ul>
     *
     * <P>
     * Once a transformer has been registered with
     * {@link java.lang.instrument.Instrumentation#addTransformer(ClassFileTransformer,boolean) addTransformer},
     * the transformer will be called for every new class definition and every class redefinition.
     * 一旦向addTransformer注册了一个转换器，就会在每次新的类定义和每次类重定义时调用该转换器。
     * Retransformation capable transformers will also be called on every class retransformation.
     * 能够进行重新转换的转换器也将在每个类的重新转换中被调用。
     * The request for a new class definition is made with {@link java.lang.ClassLoader#defineClass ClassLoader.defineClass}
     * or its native equivalents. 对定义新类的请求是通过ClassLoader.defineClass或原生的等价类实现的。
     * The request for a class redefinition is made with
     * {@link java.lang.instrument.Instrumentation#redefineClasses Instrumentation.redefineClasses}
     * or its native equivalents.对类重新定义的请求是通过Instrumentation发出的。redefineclass或它的原生等等物。
     * The request for a class retransformation is made with
     * {@link java.lang.instrument.Instrumentation#retransformClasses Instrumentation.retransformClasses}
     * or its native equivalents.对类重新转换的请求是通过Instrumentation发出的。Instrumentation.retransformClasses或其原生等价物。
     * The transformer is called during the processing of the request, before the class file bytes
     * have been verified or applied.转换器在请求处理过程中，在验证或应用类文件字节之前被调用。
     * When there are multiple transformers, transformations are composed by chaining the
     * <code>transform</code> calls.当有多个转换器时，转换器通过链接调用transform来组成。
     * That is, the byte array returned by one call to <code>transform</code> becomes the input
     * (via the <code>classfileBuffer</code> parameter) to the next call.
     * 也就是说，一个转换调用返回的字节数组成为下一个调用的输入(通过classfileBuffer参数)。
     * <P>
     * Transformations are applied in the following order:转换按以下顺序应用:
     *  <ul>
     *    <li>Retransformation incapable transformers
     *    </li>
     *    <li>Retransformation incapable native transformers
     *    </li>
     *    <li>Retransformation capable transformers
     *    </li>
     *    <li>Retransformation capable native transformers
     *    </li>
     *  </ul>
     *
     * <P>
     * For retransformations, the retransformation incapable transformers are not called,
     * instead the result of the previous transformation is reused.
     * 对于重新转换，不会调用重新转换能力的转换器，而是重用前一个转换的结果。
     * In all other cases, this method is called. Within each of these groupings, transformers are called in the order registered.
     * Native transformers are provided by the <code>ClassFileLoadHook</code> event in the Java Virtual Machine Tool Interface).
     * 在所有其他情况下，都会调用此方法。在每一个组中，按注册的顺序调用变压器。本机转换器由Java虚拟机工具接口中的ClassFileLoadHook事件提供)。
     *
     * <P>
     * The input (via the <code>classfileBuffer</code> parameter) to the first transformer is:
     * 第一个转换器的输入(通过classfileBuffer参数)是:
     *  <ul>
     *    <li>for new class definition, the bytes passed to <code>ClassLoader.defineClass</code>
     *        对于新的类定义，传递给ClassLoader.defineClass的字节
     *    </li>
     *    <li>for class redefinition,
     *        <code>definitions.getDefinitionClassFile()</code> where
     *        <code>definitions</code> is the parameter to {@link java.lang.instrument.Instrumentation#redefineClasses
     *        Instrumentation.redefineClasses}
     *        对于类重定义，可以使用definitions.getdefinitionclassfile()，其中definitions是Instrumentation.redefineclass的参数
     *    </li>
     *    <li>for class retransformation,
     *         the bytes passed to the new class definition or, if redefined,
     *         the last redefinition, with all transformations made by retransformation
     *         incapable transformers reapplied automatically and unaltered;
     *         for details see {@link java.lang.instrument.Instrumentation#retransformClasses Instrumentation.retransformClasses}
     *         对于类重转换，传递给新类定义的字节，或者如果被重定义，则传递给最后一个重定义的字节，并且由重转换所做的所有转换都不能被自动重新应用且不被更改;
     *
     *    </li>
     *  </ul>
     *
     * <P>
     * If the implementing method determines that no transformations are needed, it should return <code>null</code>.
     * 如果实现方法确定不需要转换，就应该返回null。
     * Otherwise, it should create a new <code>byte[]</code> array,
     * copy the input <code>classfileBuffer</code> into it,
     * along with all desired transformations, and return the new array. The input <code>classfileBuffer</code> must not be modified.
     * 否则，它应该创建一个新的byte[]数组，将输入classfileBuffer复制到其中，以及所有所需的转换，然后返回新的数组。输入的classfileBuffer不能被修改。
     *
     * <P>
     * In the retransform and redefine cases, the transformer must support the redefinition semantics:
     * 在重新转换和重新定义的情况下，转换器必须支持重新定义语义:
     * if a class that the transformer changed during initial definition is later retransformed or redefined,
     * the transformer must insure that the second class output class file is a legal
     * redefinition of the first output class file.
     * 如果在初始定义期间被转换器更改的类稍后被重新转换或重新定义，转换器必须确保第二个类输出类文件是第一个输出类文件的合法重定义。
     *
     * <P>
     * If the transformer throws an exception (which it doesn't catch),
     * subsequent transformers will still be called and the load, redefine or retransform will still be attempted.
     * 如果转换器抛出异常(它没有捕获)，后续的转换器仍然会被调用，并且仍然会尝试加载、重新定义或重新转换。因此，抛出异常与返回null具有相同的效果。
     * Thus, throwing an exception has the same effect as returning <code>null</code>.
     * To prevent unexpected behavior when unchecked exceptions are generated in transformer code, a transformer can catch <code>Throwable</code>.
     * 为了防止transformer代码中生成未检异常时的意外行为，transformer可以捕获Throwable异常。
     * If the transformer believes the <code>classFileBuffer</code> does not
     * represent a validly formatted class file, it should throw an <code>IllegalClassFormatException</code>;
     * 如果转换器认为classFileBuffer不表示有效格式化的类文件，它应该抛出一个IllegalClassFormatException;
     * while this has the same effect as returning null. it facilitates the logging or debugging of format corruptions.
     * 这与返回null具有相同的效果。它有助于记录或调试格式损坏。
     *
     * @param loader                the defining loader of the class to be transformed, may be <code>null</code> if the bootstrap loader
     *                              要转换的类的定义装入器，如果是引导装入器，则可能为空
     * @param className             the name of the class in the internal form of fully qualified class and interface names as defined in
     *                              <i>The Java Virtual Machine Specification</i>. For example, <code>"java/util/List"</code>.
     *                              在Java虚拟机规范中定义的完全限定类名和接口名的内部形式的类名。例如，“java/util/List”。
     *
     * @param classBeingRedefined   if this is triggered by a redefine or retransform, the class being redefined or retransformed;
     *                              if this is a class load, <code>null</code>
     *                              如果这是由重定义或重新转换触发的，则类将被重新定义或重新转换; 如果这是一个类加载，则为空
     * @param protectionDomain      the protection domain of the class being defined or redefined
     * @param classfileBuffer       the input byte buffer in class file format - must not be modified 类文件格式的输入字节缓冲区 - 不能被修改
     *
     * @throws IllegalClassFormatException if the input does not represent a well-formed class file
     * @return  a well-formed class file buffer (the result of the transform),
     *          or <code>null</code> if no transform is performed.格式良好的类文件缓冲区(转换的结果)，如果没有执行转换则为空。
     * @see Instrumentation#redefineClasses
     */
    byte[]
    transform(  ClassLoader         loader,
                String              className,
                Class<?>            classBeingRedefined,
                ProtectionDomain    protectionDomain,
                byte[]              classfileBuffer)
        throws IllegalClassFormatException;
}
