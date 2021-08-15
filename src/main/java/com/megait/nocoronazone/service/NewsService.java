package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Article;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class NewsService {




    @PostConstruct
    public List<Article> getArticleList() throws IOException {

        ClassPathResource resource = new ClassPathResource("csv/article.csv");
        List<String> stringList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);

        List<Article> articleList = new ArrayList<>();

        for(String s : stringList){

            String[] arr = s.replaceAll("^\"|\"$", "").split("\\|");
            Article article = Article.builder()
                    .pressName(arr[0])
                    .pressImgUrl(arr[1])
                    .articleTitle(arr[2])
                    .articleContent(arr[3])
                    .articleLink(arr[4])
                    .articleImgUrl(arr[5])
                    .build();

            articleList.add(article);

        }

        return articleList;

    }
}
