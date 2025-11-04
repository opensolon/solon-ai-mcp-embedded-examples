package client;

import org.junit.jupiter.api.Test;
import org.noear.solon.test.HttpTester;
import org.noear.solon.test.SolonTest;
import webapp.HelloApp;

/**
 * 运行单测前，先手动运行 HelloApp
 * */
@SolonTest(HelloApp.class)
public class LlmRagTest extends HttpTester {
    @Test
    public void rag_demo() throws Exception {
        String rst = path("/rag/demo").data("prompt", "solon 是谁开发的？").post();
        System.out.println(rst);

        assert rst != null && rst.length() > 0;
    }
}
