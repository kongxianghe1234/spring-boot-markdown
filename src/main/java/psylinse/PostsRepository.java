package psylinse;

import org.pegdown.PegDownProcessor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Repository
public class PostsRepository {
    private PegDownProcessor pegDownProcessor = new PegDownProcessor();

    public List<String> all() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("/posts/", getClass());

        List<String> posts = new ArrayList<>();
        File[] files = classPathResource.getFile().listFiles();

        if (files == null) {
            throw new RuntimeException("Something went really wrong");
        }

        Arrays.asList(files).forEach(
            (post) -> posts.add(post.getName().split(".md")[0])
        );
        return posts;
    }

    public String get(String name) throws IOException {
        String markdownFile = new ClassPathResource("/posts/" + name + ".md", getClass())
            .getFile()
            .getAbsolutePath();

        String fileContents = new String(Files.readAllBytes(Paths.get(markdownFile)));
        return pegDownProcessor.markdownToHtml(fileContents);
    }
}
