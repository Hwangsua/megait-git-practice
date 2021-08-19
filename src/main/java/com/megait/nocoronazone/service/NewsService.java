package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Article;
import com.megait.nocoronazone.thread.ProcessOutputThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class NewsService {

    private final String csvPath = "D:\\2021_15_webdev_hsa\\project_corona_before\\src\\main\\resources\\csv\\article.csv";
    private final String batPath = "D:\\2021_15_webdev_hsa\\project_corona_before\\src\\main\\resources\\bat\\article.bat";


    public void setArticleFile(String keyword1, String keyword2) throws InterruptedException, IOException {

        new FileOutputStream(csvPath).close();

        Runtime runtime = Runtime.getRuntime();
        Process process = null;

        try {
            process = runtime.exec(batPath + " " + keyword1 + " " + keyword2 );

            StringBuffer stdMsg = new StringBuffer();

            ProcessOutputThread outputThread = new ProcessOutputThread(process.getInputStream(), stdMsg);
            outputThread.start();

            StringBuffer errMsg = new StringBuffer();

            outputThread = new ProcessOutputThread(process.getErrorStream(),errMsg);
            outputThread.start();

            process.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            process.destroy();

        }

    }


    public List<Article> getArticleList(String keyword1, String keyword2) throws IOException {

        try {
            setArticleFile(keyword1,keyword2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<String> stringList = Files.readAllLines(Path.of(csvPath), StandardCharsets.UTF_8);

        while (stringList.size() <= 1){
            stringList = Files.readAllLines(Path.of(csvPath), StandardCharsets.UTF_8);
            System.out.println(stringList.size());
        }


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
            System.out.println(arr[0]);
            articleList.add(article);

        }

        return articleList;

    }
}
