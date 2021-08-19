package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Article;
import com.megait.nocoronazone.thread.ProcessOutputThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Validated
@Slf4j
@RequiredArgsConstructor
public class ArticleService {

    private final String localCsvPath = "D:\\2021_15_webdev_hsa\\project_corona_before\\src\\main\\resources\\csv\\local_article.csv";
    private final String localBatPath = "D:\\2021_15_webdev_hsa\\project_corona_before\\src\\main\\resources\\bat\\local_article.bat";
    private final String vaccineCsvPath = "D:\\2021_15_webdev_hsa\\project_corona_before\\src\\main\\resources\\csv\\vaccine_article.csv";
    private final String vaccineBatPath = "D:\\2021_15_webdev_hsa\\project_corona_before\\src\\main\\resources\\bat\\vaccine_article.bat";


    public String getCommendLineArg(List<String> list){
        return String.join(" ", list);
    }

    public void setArticleFile(String LineArg, String batPath, String csvPath) throws InterruptedException, IOException {

        new FileOutputStream(csvPath).close();

        Runtime runtime = Runtime.getRuntime();
        Process process = null;

        try {
            System.out.println("명령 keyword : "+ LineArg);
            process = runtime.exec(batPath + " " +  LineArg);

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


    public List<Article> getArticleList(String LineArg, String batPath, String csvPath) throws IOException {

        try {
            setArticleFile(LineArg, batPath, csvPath);
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

    public List<Article> getLocalArticleList(String mainCityName, String subCityName) throws IOException {

        String LineArg = getCommendLineArg(Arrays.asList(new String[]{mainCityName,subCityName, localCsvPath }));
        return getArticleList(LineArg, localBatPath, localCsvPath);

    }

    public List<Article> getVaccineArticleList() throws IOException {

        String LineArg = getCommendLineArg(Arrays.asList(new String[]{vaccineCsvPath}));
        return getArticleList(LineArg, vaccineBatPath, vaccineCsvPath);

    }


}
