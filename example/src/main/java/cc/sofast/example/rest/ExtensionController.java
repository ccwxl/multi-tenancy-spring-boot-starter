package cc.sofast.example.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/extension")
public class ExtensionController {

    @GetMapping
    public String extension() {

        return "";
    }
}
