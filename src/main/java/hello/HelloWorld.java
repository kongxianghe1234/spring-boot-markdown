package hello;

import org.pegdown.PegDownProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@EnableAutoConfiguration
public class HelloWorld {

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Hello World!";
    }

    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    @ResponseBody
    String page(@PathVariable("page") String page) throws IOException {
        String markdownFile = new ClassPathResource("/" + page + ".md", getClass())
                                  .getFile()
                                  .getAbsolutePath();

        String fileContents = new String(Files.readAllBytes(Paths.get(markdownFile)));

        return new PegDownProcessor().markdownToHtml(fileContents);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HelloWorld.class, args);
    }
}
