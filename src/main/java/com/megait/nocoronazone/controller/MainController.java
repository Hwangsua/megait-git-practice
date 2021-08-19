package com.megait.nocoronazone.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.megait.nocoronazone.domain.Article;
import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.form.SignUpForm;
import com.megait.nocoronazone.service.ArticleService;
import com.megait.nocoronazone.service.MemberService;
import com.megait.nocoronazone.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final NewsService newsService;
    private final ArticleService articleService;
    

    @RequestMapping("/")
    public String index() {

        return "index";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "member/signup_test";
    }

    @PostMapping("/signup")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {

        if(errors.hasErrors()){
            System.out.println("에러발생");
            return "member/signup_test";
        }

        Member member = memberService.processNewUser(signUpForm);

        //memberService.login(member);

        return "/member/checked-email";
    }


    @ResponseBody
    @RequestMapping("/nicknameCk")
    public String checkNickname(@RequestParam String nickname){

        JsonObject object = new JsonObject();

        try{
            memberService.checkNickname(nickname);
            object.addProperty("result", false);
            object.addProperty("message", "사용 불가능한 닉네임입니다.");
        }catch (IllegalArgumentException e){
            object.addProperty("result", true);
            object.addProperty("message","사용 가능한 닉네임입니다.");
        }

        return object.toString();
    }

    @GetMapping ("/email-check-token")
    public String emailCheckToken(String token, String email, Model model){

        try{
            memberService.checkEmailToken(token, email);
        }catch (IllegalArgumentException e){
            model.addAttribute("error","wrong");
            return "member/email_check_result";
        }

        model.addAttribute("success","사용자님");
        return "member/email_check_result";

    }

    @GetMapping("/login")
    public String loginForm() {
        return "member/login_test";
    }




//    @PostMapping("/login")
//    public String loginSubmit(@Valid LoginForm loginForm, Errors errors){
//        //TODO - 0808 LoginForm 구현하기
//        if(errors.hasErrors()){
//            return "/member/login_test";
//        }
//        return "redirect:/";
//    }


    @GetMapping("/logout")
    public String logout(){
        return "redirect:/";
    }


    @RequestMapping("/article")
    public String article(Model model) throws IOException {
      
        try {
            model.addAttribute("articleList",articleService.getLocalArticleList("서울", "전체"));
        }catch (IOException  e){
            e.printStackTrace();
        }

        return "co_info/article";
    }

    @GetMapping("/vaccine")
    public String vaccine(Model model) throws IOException {

        try {
            model.addAttribute("articleList",articleService.getVaccineArticleList());
        }catch (IOException  e){
            e.printStackTrace();
        }

        return "co_info/vaccine";
    }

    @GetMapping("/local_article")
    public String article(@RequestParam String mainCityName, @RequestParam String subCityName, Model model){
        System.out.println("신호옴");

        try {
            model.addAttribute("articleList",articleService.getLocalArticleList(mainCityName, subCityName));
        }catch (IOException e){
            e.printStackTrace();
        }

        return "/co_info/article :: #article-list";
    }




//    @GetMapping("/test")
//    public String test(Model model) throws IOException, InterruptedException {
//        try {
//            newsService.setArticleFile("서울","구로구");
//            model.addAttribute("articleList",newsService.getArticleList("서울","서울전체"));
//        }catch (InterruptedException e){
//            e.printStackTrace();
//        }
//        return "co_info/article";
//    }



//    @GetMapping("/news")
//    public String news(){
//
//        return "co_info/main";
//    }



//    @GetMapping("/settings")
//    public String setUpForm(){
//        return "member/settings";
//    }
//
//    @PostMapping("/settings")
//    public String setUpSubmit(){
//        return "member/settings";
//    }
//
//    @GetMapping("/cosns")
//    public String followTimeline() {
//        return "co_sns/timeline_follow";
//    }
//
//    @GetMapping("/timeline_location")
//    public String locationTimeline(){
//        return "co_sns/timeline_location";
//    }
//
//    @GetMapping("/mention/write")
//    public String writeTimeline(){
//        // ajax
//        return "success";
//    }
//
//
//    @GetMapping("/mention_datail")
//    public String mentionDetail(){
//        return "co_sns/mention_detail";
//    }
//
//    @PostMapping("/remention")
//    public String remention(){
//        // ajax
//        return "success";
//    }
//
//    @GetMapping("/search")
//    public String searchCoSns(){
//        return "co_sns/search";
//    }
//
//    //TODO - 트렌드, 강사님이 도와주신다고 하심
//
//    @GetMapping("/following")
//    public String followingList(){
//        return "co_sns/following";
//    }
//
//    @GetMapping("/follower")
//    public String followerList(){
//        return "co_sns/follower";
//    }
//
//    @GetMapping("/{nickname}")
//    public String profile(@PathVariable String nickname){
//        return "co_sns/profile";
//    }


}
