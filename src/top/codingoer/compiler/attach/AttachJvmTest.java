package top.codingoer.compiler.attach;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * Description：
 *
 * @author LiuQiang
 * @date Created in 2022/12/7 1:37 上午
 */
public class AttachJvmTest {

    public static void main(String[] args) {
        VirtualMachine virtualMachine = null;

        try {
            virtualMachine = VirtualMachine.attach("34999");
            virtualMachine.loadAgent("/Users/lionel/IdeaProjects/codingoer/java-agent/agent-redefine-class/target/redefine-class-agent-jar-with-dependencies.jar");
        } catch (AttachNotSupportedException e) {
            e.printStackTrace();
        } catch (AgentLoadException e) {
            e.printStackTrace();
        } catch (AgentInitializationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
