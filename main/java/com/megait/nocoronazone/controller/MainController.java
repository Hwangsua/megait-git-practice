package com.megait.nocoronazone.controller;

import com.megait.nocoronazone.domain.User;
import com.megait.nocoronazone.form.SignUpForm;
import com.megait.nocoronazone.repository.UserRepository;
import com.megait.nocoronazone.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    @RequestMapping("/")
    public String index() {

        return "index";
    }

    @GetMapping("/signup")
    public String signUpForm(Model model) {
        model.addAttribute("signUpForm", new SignUpForm());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signUpSubmit(@Valid SignUpForm signUpForm, Errors errors) {

        if(errors.hasErrors()){

            return "/user/signup";
        }

        User user = userService.processNewUser(signUpForm);

        //TODO - 0808 email_check 구현하기
        //return "/user/email_check";
        return "redirect:/";
    }

//    @ResponseBody
//    @PostMapping("/nicknameCK/{nickname}")
//    public String checkNickname(){
//      //TODO - 0811 중복확인 요청 구현하기
//    }

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

//    @PostMapping("/login")
//    public String loginSubmit(@Valid LoginForm loginForm, Errors errors){
//        //TODO - 0808 LoginForm 구현하기
//        if(errors.hasErrors()){
//            return "/user/login";
//        }
//        return "redirect:/";
//    }


    @GetMapping("/logout")
    public String logout(){
        return "redirect:/";
    }


    @GetMapping("/settings")
    public String setUpForm(){
        return "user/settings";
    }

    @PostMapping("/settings")
    public String setUpSubmit(){
        return "user/settings";
    }

    @GetMapping("/cosns")
    public String followTimeline() {
        return "co_sns/timeline_follow";
    }

    @GetMapping("/timeline_location")
    public String locationTimeline(){
        return "co_sns/timeline_location";
    }

    @GetMapping("/mention/write")
    public String writeTimeline(){
        return "success";
    }


    @GetMapping("/mention_datail")
    public String mentionDetail(){
        return "co_sns/mention_detail";
    }

    @PostMapping("/remention")
    public String remention(){
        return "success";
    }

    @GetMapping("/search")
    public String searchCoSns(){
        return "co_sns/search";
    }
    
    //TODO - 트렌드, 강사님이 도와주신다고 하심

    @GetMapping("/following")
    public String followingList(){
        return "co_sns/following";
    }

    @GetMapping("/follower")
    public String followerList(){
        return "co_sns/follower";
    }

    @GetMapping("/{nickname}")
    public String profile(@PathVariable String nickname){
        return "co_sns/profile";
    }


}
