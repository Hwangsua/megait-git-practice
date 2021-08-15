package com.megait.nocoronazone.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.JsonObject;
import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.form.SignUpForm;
import com.megait.nocoronazone.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;

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

        System.out.println("회원가입 요청");

        if(errors.hasErrors()){
            System.out.println("에러발생");
            return "member/signup_test";
        }

        Member member = memberService.processNewUser(signUpForm);

        //TODO - 0808 email_check 구현하기
        //return "/user/email_check";
        return "redirect:/";
    }


    @ResponseBody
    @RequestMapping("/nicknameCk")
    public String checkNickname(@RequestParam String nickname){

        System.out.println(nickname+"닉네임 확인");

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


    @GetMapping("/news/article")
    public String article(){
        return "co_info/article";
    }

    @GetMapping("/news")
    public String news(){
        return "co_info/article";
    }


    @GetMapping("/test")
    public String test(Model model) throws IOException {

        ClassPathResource resource = new ClassPathResource("csv/article.csv");
        List<String> stringList = Files.readAllLines(resource.getFile().toPath(), StandardCharsets.UTF_8);
        int i = 0;

        for(String s : stringList){
            i++;

            String[] arr = s.replaceAll("^\"|\"$", "").split("\\|");
            model.addAttribute("pressName", arr[0]);
            model.addAttribute("pressImgUrl", arr[1]);
            model.addAttribute("articleTitle", arr[2]);
            model.addAttribute("articleContent", arr[3]);
            model.addAttribute("articleLink", arr[4]);
            model.addAttribute("articleImgUrl", arr[5]);
            System.out.println(arr[5]);
            if (i == 1){
                break;
            }
        }

        return "co_info/article";
    }


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
    @GetMapping("/{nickname}")
    public String profile(@PathVariable String nickname){
        return "co_info/article";
    }


}
