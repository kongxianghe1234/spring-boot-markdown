package psylinse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
@EnableAutoConfiguration
@ComponentScan("psylinse")
public class Main {

    @Autowired
    private PostsRepository postsRepository;

    @RequestMapping("/")
    ModelAndView another() throws IOException {
        ModelMap model = new ModelMap();

        model.addAttribute("posts", postsRepository.all());

        return new ModelAndView("index", model);
    }

    @RequestMapping(value = "/{post}", method = RequestMethod.GET)
    @ResponseBody
    String post(@PathVariable("post") String post) throws IOException {
        return postsRepository.get(post);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
