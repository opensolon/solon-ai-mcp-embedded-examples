package webapp.llm.tool;

import org.noear.solon.ai.annotation.ToolMapping;
import org.noear.solon.annotation.Param;

public class CalculatorTools {
    @ToolMapping(description = "将两个数字相加")
    public int add(@Param int a, @Param int b) {
        return a + b;
    }

    @ToolMapping(description = "从第一个数中减去第二个数")
    public int subtract(@Param int a, @Param int b) {
        return a - b;
    }

    @ToolMapping(description = "将两个数相乘")
    public int multiply(@Param int a, @Param int b) {
        return a * b;
    }

    @ToolMapping(description = "将第一个数除以第二个数")
    public float divide(@Param float a, @Param float b) {
        return a / b;
    }
}
