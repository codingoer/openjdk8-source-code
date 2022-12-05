/*
 * Copyright (c) 2005, 2013, Oracle and/or its affiliates. All rights reserved.
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

/**
 * Provides the API to attach to a Java<sup><font size=-2>TM</font></sup> virtual machine.
 * 提供附加到JavaTM虚拟机的API。
 * <p>
 * A tool, written in the Java Language, uses this API to attach to a target virtual machine (VM)
 * and load its tool agent into the target VM. 用Java语言编写的工具，使用这个API附加到目标虚拟机(VM)，并将其工具代理加载到目标VM中。
 * For example, a management console might have a management agent which it uses
 * to  obtain management information from instrumented objects in a Java virtual machine.
 * 例如，管理控制台可能有一个管理代理，用于从Java虚拟机中的仪器化对象中获取管理信息。
 * If the management console is required to manage an application that is running in a virtual machine that does not include
 * the management agent, then this API can be used to attach to the target VM and load the agent.
 * 如果需要管理控制台来管理在不包含管理代理的虚拟机中运行的应用程序，则可以使用此API附加到目标VM并加载代理。
 *
 * @since 1.6
 */

@jdk.Exported
package com.sun.tools.attach;
