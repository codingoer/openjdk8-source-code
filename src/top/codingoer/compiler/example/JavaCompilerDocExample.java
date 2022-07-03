package top.codingoer.compiler.example;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * JavaCompiler文档中的代码示例
 */
public class JavaCompilerDocExample {

    public static void main(String[] args) {
        standardJavaFileManagerTest();
    }

    public static void standardJavaFileManagerTest() {
        File[] files1 = new File[1];
        files1[0] = new File("/Users/lionel/IdeaProjects/codingoer/openjdk8-source-code/java/HelloWorld.java");

        File[] files2 = new File[1];
        files2[0] = new File("/Users/lionel/IdeaProjects/codingoer/openjdk8-source-code/java/HelloWorld.java");

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);

        Iterable<? extends JavaFileObject> compilationUnits1 = fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files1));
        compiler.getTask(null, fileManager, null, null, null, compilationUnits1).call();

        Iterable<? extends JavaFileObject> compilationUnits2 = fileManager.getJavaFileObjects(files2); // use alternative method
        // reuse the same file manager to allow caching of jar files
        compiler.getTask(null, fileManager, null, null, null, compilationUnits2).call();

        try {
            fileManager.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
