package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Article;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class NewsService {


    private void initArticleRunFile(String frameFilePath, String runFilePath){

        try (FileInputStream fIn = new FileInputStream(new File(frameFilePath));
             FileOutputStream fOut = new FileOutputStream(new File(runFilePath))){

            File file = new File(frameFilePath);
            long size = file.length();

            byte[] arr = new byte[(int)size];

            fIn.read(arr);
            fOut.write(arr);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public void setArticleFile(String keyword) throws InterruptedException, IOException {

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
//        String frameFilePath = new ClassPathResource("bat/article_frame.txt").getFile().getAbsolutePath();
//        String runFilePath = new ClassPathResource("bat/article.txt").getFile().getAbsolutePath();

        String frameFilePath = "D:\\2021_15_webdev_hsa\\project_corona_before\\src\\main\\resources\\bat\\article_frame.bat";
        String runFilePath = "D:\\2021_15_webdev_hsa\\project_corona_before\\src\\main\\resources\\bat\\article.bat";

        initArticleRunFile(frameFilePath, runFilePath);

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(runFilePath, true));

        try{
            bufferedWriter.write(" "+keyword);
            bufferedWriter.flush();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bufferedWriter.close();
        }

        try {
            process = runtime.exec(runFilePath);
            System.out.println("실행");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
                System.out.println("tasklist: " + line);


//            StringBuffer stdMsgT = new StringBuffer();
//            stdMsgT = new ProcessOutputThread(process.getInputStream(), stdMsg);
//            stdMsgT.start();
//
//            StringBuffer errMsg = new StringBuffer();
//            // 스레드로 errorStream 버퍼 비우기
//            errMsgT = new ProcessOutputThread(process.getErrorStream(), errMsg);
//            errMsgT.start();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //process.getErrorStream().close();
            //process.getInputStream().close();
           // process.getOutputStream().close();

            process.waitFor();
            System.out.println("완료");
            process.destroy();

        }

    }


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
