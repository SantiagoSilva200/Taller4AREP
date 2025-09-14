package co.edu.arep;

import co.edu.arep.Annotations.GetMapping;
import co.edu.arep.Annotations.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String index(){
        return "Hello Docker!";
    }
}

