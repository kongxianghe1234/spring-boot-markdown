package psylinse;

import de.neuland.jade4j.JadeConfiguration;
import de.neuland.jade4j.spring.template.SpringTemplateLoader;
import de.neuland.jade4j.spring.view.JadeViewResolver;
import org.pegdown.PegDownProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ViewResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
@EnableAutoConfiguration
public class Main {

    @Bean
    public SpringTemplateLoader templateLoader() {
        SpringTemplateLoader templateLoader = new SpringTemplateLoader();
        templateLoader.setResourceLoader(new DefaultResourceLoader());
        templateLoader.setBasePath("/templates/");
        templateLoader.setEncoding("UTF-8");
        templateLoader.setSuffix(".jade");
        return templateLoader;
    }

    @Bean
    public JadeConfiguration jadeConfiguration() {
        JadeConfiguration configuration = new JadeConfiguration();
        configuration.setCaching(false);
        configuration.setTemplateLoader(templateLoader());
        return configuration;
    }

    @Bean
    public ViewResolver viewResolver() {
        JadeViewResolver viewResolver = new JadeViewResolver();
        viewResolver.setConfiguration(jadeConfiguration());
        return viewResolver;
    }

    @RequestMapping("/hello")
    String home() {
        return "index";
    }

    @RequestMapping("/")
    @ResponseBody
    String another() {
        return "index";
    }


    @RequestMapping(value = "/p/{page}", method = RequestMethod.GET)
    @ResponseBody
    String page(@PathVariable("page") String page) throws IOException {
        String markdownFile = new ClassPathResource("/" + page + ".md", getClass())
                                  .getFile()
                                  .getAbsolutePath();

        String fileContents = new String(Files.readAllBytes(Paths.get(markdownFile)));

        return new PegDownProcessor().markdownToHtml(fileContents);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Main.class, args);
    }
}
