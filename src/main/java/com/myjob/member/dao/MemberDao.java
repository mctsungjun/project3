package com.myjob.member.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import com.myjob.member.mybatis.MyFactory;
import com.myjob.member.passHash.PasswordHash;
import com.myjob.member.vo.MemberVo;

@Component
public class MemberDao {
    
    SqlSession session; 
    
    
    public boolean registerR(MemberVo vo, String passwordHash){
        boolean b = false;
        System.out.println("dao입나다:"+vo);
        session = new MyFactory().getSession();
        vo.setPassword(passwordHash);

        int checkNum = session.insert("member.register", vo);
        if (checkNum >0){
            b= true;
            session.commit();
        }else{
            session.rollback();
        }
        return b;
    }

    public MemberVo login(String id,String password){
        session = new MyFactory().getSession();
       
        String hashPassword = PasswordHash.hashPassword(password);
        MemberVo vo = new MemberVo();
        Object ob =(Object)vo;
        ob = session.selectOne("member.login",id);
        if(ob !=null){
            vo =(MemberVo)ob;
            String pwd = vo.getPassword(); //해쉬되어진 비번 가져옴
            
            if(hashPassword.equals(pwd)){
               
                return vo;

        }else{
            System.out.println("비번이 틀렸습니다.");
            return null;
            }
        }else{
            System.out.println("해당 아이디가 존재하지 않습니다.");
            return null;
        }
       

        
    }
}
