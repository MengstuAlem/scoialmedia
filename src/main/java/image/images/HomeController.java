package image.images;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping
    public String greeting(@RequestParam(required = false,value = "") String name){
     return name.equalsIgnoreCase("")?"hey" :"hey " +name + "!";
    }
}
