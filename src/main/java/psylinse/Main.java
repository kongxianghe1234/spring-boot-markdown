package psylinse;

import org.pegdown.PegDownProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@EnableAutoConfiguration
@ComponentScan("psylinse")
public class Main {
    private PegDownProcessor pegDownProcessor = new PegDownProcessor();

    @RequestMapping("/")
    ModelAndView another() throws IOException {
        ModelMap model = new ModelMap();
        ClassPathResource classPathResource = new ClassPathResource("/posts/", getClass());

        List<String> posts = new ArrayList<>();
        File[] files = classPathResource.getFile().listFiles();

        if (files == null) {
            throw new RuntimeException("Something went really wrong");
        }

        Arrays.asList(files).forEach(
            (post) -> posts.add(post.getName().split(".md")[0])
        );

        model.addAttribute("posts", posts);

        return new ModelAndView("index", model);
    }

    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    @ResponseBody
    String page(@PathVariable("page") String page) throws IOException {
        String markdownFile = new ClassPathResource("/posts/" + page + ".md", getClass())
            .getFile()
            .getAbsolutePath();

        String fileContents = new String(Files.readAllBytes(Paths.get(markdownFile)));

        return pegDownProcessor.markdownToHtml(fileContents);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
