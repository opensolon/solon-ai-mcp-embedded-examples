package webapp;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author noear 2025/10/21 created
 *
 */
@RestController
public class HelloController {
    @RequestMapping("/")
    public String hello() {
        return "Hello World!";
    }
}
