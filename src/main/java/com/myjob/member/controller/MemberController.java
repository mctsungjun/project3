package com.myjob.member.controller;




import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.myjob.member.dao.MemberDao;
import com.myjob.member.passHash.PasswordHash;
import com.myjob.member.service.KakaoApi;
import com.myjob.member.service.NaverAPI;
import com.myjob.member.vo.MemberVo;
import com.myjob.member.vo.PhotoVo;

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
   
   static String uploadPath= "C:/Users/sung/Desktop/projectSet/3차/2cha/project_v1/member/src/main/resources/static/upload/";
    
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
        // 주석 처리 표시 는 필용없음
        //System.out.println(id+"   "+password);
        Map<String, Object> response = new HashMap<>();
        MemberVo vo = dao.login(id,password);
        if(vo !=null){
            session.setAttribute("id", vo.getId());
            session.setAttribute("name", vo.getName());
            response.put("message", "success");
            // response.put("htmlFile","sung/detail.html");
           
            //response.put("vo",vo);
            System.out.println("로그인 되었습니다.");
        }else{
            response.put("message","false");
            System.out.println("아이디, 비번이 틀립니다.");
        }
        return ResponseEntity.ok(response);
    }
    //로그아웃
    @RequestMapping(path="/sung/logout")
    public void logout(HttpSession session){
        session.setAttribute("id", null);
        session.setAttribute("name", null);

    }
      //상세페이지로
      @RequestMapping(path="/sung/detail")
      public ModelAndView detail(HttpSession sesssion){
          ModelAndView mv = new ModelAndView();
          String id = (String)sesssion.getAttribute("id");
          System.out.println("id:"  +id);
          MemberVo vo = dao.detail(id);
          if(vo.getPhoto() !=null && !vo.getPhoto().equals("vo")){
            for(PhotoVo pv:vo.getPhotos()){
                if(pv.photo.contains(vo.getPhoto())){
                    vo.setPhoto(pv.photo);;
                }
            }
          }
        System.out.println(vo);
          mv.addObject("vo", vo);
          mv.setViewName("sung/detail");
          return mv;
      }
      //이미지/파일 업로드
      @RequestMapping(path="/sung/upload")
      public String fileUpload(@RequestParam("files") MultipartFile[] photo, HttpSession session, @RequestParam("reprePhoto") String reprePhoto){
        //ModelAndView mv = new ModelAndView();
        List<PhotoVo> photos = new ArrayList<>();
        MemberVo vo = new MemberVo();
        if (photo != null){
            UUID uuid = null;
            String sysFile = "";
            for(MultipartFile f : photo){
                if(f.getOriginalFilename().equals("")){
                    continue;
                }
                // 파일업로드
                uuid = UUID.randomUUID();
                sysFile = String.format("%s-%s",uuid,f.getOriginalFilename());
                File savefile = new File(uploadPath+sysFile);
                try{
                    f.transferTo(savefile);
                }catch(Exception e){
                    e.printStackTrace();
                }
                PhotoVo pv = new PhotoVo();
               
                pv.setOriPhoto(f.getOriginalFilename());
                pv.setPhoto(sysFile);
                photos.add(pv);
                System.out.println(f.getOriginalFilename());

            }
            if(photos.size()>0){
                vo.setPhotos(photos);
                vo.setPhoto(reprePhoto);
                vo.setId((String)session.getAttribute("id"));
                System.out.println("vo: " +vo.getPhoto());
            }
        }
        String msg = dao.fileUpload(vo);
        return msg;
      }
// 수정폼/정보가져오기
@RequestMapping(path="/sung/modify" )
public ModelAndView updateFrom(@RequestParam("id") String id){
    ModelAndView mv = new ModelAndView();
    MemberVo vo = dao.updateFrom(id);
    mv.addObject("vo", vo);
    mv.setViewName("sung/update");
    return mv;

}
//대표이미지 수정폼
@RequestMapping(path="/sung/repreChangeForm")
public ModelAndView repreChangForm(HttpSession session){
    ModelAndView mv = new ModelAndView();
    String id = (String)session.getAttribute("id");
    MemberVo vo =dao.detail(id);
    mv.addObject("vo", vo);
    mv.setViewName("sung/reprePhoto");
    return mv;
}
//대표이미지 바꾸기
    @RequestMapping(path="/sung/changePhoto")
    public String changePhoto(String id, String photo){
        String msg = dao.changePhoto(id, photo);
        return msg;
    }
//리스트 폼으로 가기
    @RequestMapping(path="/sung/list")
    public ModelAndView list(String code){
        ModelAndView mv = new ModelAndView();
        System.out.println(code);
        List<MemberVo> list = new ArrayList<>();
        list = dao.list(code);
        System.out.println(list);
        mv.addObject("list", list);
        mv.setViewName("sung/list");
        return mv;
    }

    // 로그인 완료시 메인페이지 사용자 이름과 로그아웃 활성화


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
