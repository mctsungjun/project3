package com.myjob.member.controller;




import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.myjob.member.dao.MemberDao;
import com.myjob.member.passHash.PasswordHash;
import com.myjob.member.service.KakaoApi;
import com.myjob.member.service.NaverAPI;
import com.myjob.member.vo.MemberVo;

import jakarta.servlet.http.HttpServletRequest;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
@RestController

public class MemberController {
    
   @Autowired
   MemberDao dao; 
   @Autowired
   NaverAPI nApi;

   @Autowired
   KakaoApi kakaoApi;
   
    
    // 메인화면 보이기
    @RequestMapping(path="/")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }

    //로그인폼
    @RequestMapping(path="/sung/loginF")
    public ModelAndView loginF(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sung/login");
        return mv;
    }
    //로그인
    @RequestMapping(path="/sung/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam("id") String id,@RequestParam("password") String password, HttpSession session){
        Map<String, Object> response = new HashMap<>();
        MemberVo vo = dao.login(id,password);
        if(vo !=null){
            session.setAttribute("id", vo.getId());
            response.put("message", "success");
            // response.put("htmlFile","sung/detail.html");
            response.put("vo",vo);
            System.out.println("로그인 되었습니다.");
        }else{
            response.put("message","false");
            System.out.println("아이디, 비번이 틀립니다.");
        }
        return ResponseEntity.ok(response);
    }
    
    // @RequestMapping(path="/sung/login")
    // public ModelAndView login(@RequestParam("id") String id,@RequestParam("password") String password, HttpSession session){
    //     ModelAndView mv = new ModelAndView();
    //     System.out.println(id);
    //     System.out.println(password);
    //     String responseId = dao.login(id,password);
        
    //     if(responseId !=null){
    //         session.setAttribute("id", responseId);
    //         mv.setViewName("sung/list");
    //         System.out.println("로그인 되었습니다.");
    //         return mv;
    //     }else{
    //         mv.addObject("message", "false");
    //         System.out.println("아이디,비번이 틀립니다.");
    //         return mv;
    //     }

    // }
    //로그인 완료시 상세페이지로
    @RequestMapping(path="/sung/detail")
    public ModelAndView detail(MemberVo vo){
        ModelAndView mv = new ModelAndView();
        mv.addObject("vo", vo);
        mv.setViewName("sung/detail");
        return mv;
    }
    //회원등록폼
    @RequestMapping(path="/sung/registerF")
    public ModelAndView registerF(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sung/register");
        return mv;
    }
    //회원등록
    @RequestMapping(path="/sung/registerR")
    @ResponseBody
    public String registerR(@ModelAttribute MemberVo vo){
        ModelAndView mv = new ModelAndView();
        System.out.println("컨트롤러입니다 :"+vo);
        String passwordHash = PasswordHash.hashPassword(vo.getPassword());
        
        boolean b= false;
        b= dao.registerR(vo,passwordHash);
        mv.addObject("b", b);
        // mv.setViewName("sung/list");
        return "hi";
    }
    //아이디중복확인
    @RequestMapping(path="/memberId/chk")
    public int userIdchk(@RequestParam("userId") String id ){
        System.out.println("usedId:  "+id);
        if(!id.isEmpty()){
            return 0;
        }else{
            return 1;

        }

    }

    //아이디/비번 찿기form
    @RequestMapping(path="/sung/findIdPwd")
    public ModelAndView findIdPwdForm(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("sung/findIdPw");
        
        return mv;
    }
    //리스트폼


    //네이버 로그인
    @RequestMapping(path="/naver/callback")
    public String login(@RequestParam("code") String code,@RequestParam("state") String state, HttpSession session, HttpServletRequest request) throws IOException {
      String accessToken = nApi.gettoken(code,state);  
      session = request.getSession();

      
      MemberVo vo =null;
      vo = nApi.userInfo(accessToken);
      // 세션에 담기
      session.setAttribute("nickname", vo.getNickname());
      session.setAttribute("id", vo.getId());
      session.setAttribute("name", vo.getName());
     
    return "ok";
        
}



//카카오 로그인
@RequestMapping(path="kakao/callback")
public String kakaoLogin(@RequestParam("code") String code, HttpSession session) throws IOException{
    MemberVo vo=null;
    String accessToken = kakaoApi.getAccessToken(code);
    System.out.println(code);

    vo = kakaoApi.userInfo(accessToken);
    session.setAttribute("nickname", vo.getNickname());
    session.setAttribute("id", vo.getId());
    return vo.getNickname();
    }
}
