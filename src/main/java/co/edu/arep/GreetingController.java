package co.edu.arep;

import co.edu.arep.Annotations.RestController;
import co.edu.arep.Annotations.GetMapping;
import co.edu.arep.Annotations.RequestParam;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }
}
