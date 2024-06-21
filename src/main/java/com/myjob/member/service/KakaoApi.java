package com.myjob.member.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.myjob.member.vo.MemberVo;

@Service
public class KakaoApi {
    
    public String getAccessToken (String code){

        String accessToken = "";
        String refreshToken = "";
        String idToken = "";

        //토큰발급 요청을 보낼 주소
        String reqURL = "https://kauth.kakao.com/oauth/token";

        try{
            //url객체 생성
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            // POST 요청을 위해 기본값이 false인 setDoOutput을 true
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // POST 용청 본문 작성
            String params = "grant_type=authorization_code" +
                            "&client_id=e8852b21d1a31c360360d0231f16746b" +
                            "&redirect_uri=http://localhost:8989/kakao/callback" +
                            "&code=" + code ;
                            
            
            // 요청 본문 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(params);
            bw.flush();
            bw.close();

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode: " + responseCode);

            // 응답데이터 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while((line=br.readLine())!=null){
                response.append(line);
            }
            br.close();

            String result = response.toString();
            System.out.println("response body: " + result);

            // Gson 라이브러리를 사용하여 JSON 파싱
            JsonParser parser = new JsonParser();
            JsonElement je = parser.parse(result);
            if(je.isJsonObject()){
                JsonObject jsonObject = je.getAsJsonObject();
                if(jsonObject.has("access_token")){
                    accessToken = jsonObject.get("access_token").getAsString();
                    System.out.println("access_token:" + accessToken);

                }
                if(jsonObject.has("refresh_token")){
                    refreshToken = jsonObject.get("refresh_token").getAsString();
                    System.out.println("refresh_token: " +refreshToken);
                }
                if(jsonObject.has("id_token")){
                    idToken = jsonObject.get("id_token").getAsString();
                    System.out.println("id_token: " + idToken);
                    
                }
            }
        }catch(IOException e){
            e.getStackTrace();
        }
        return accessToken;
    }

    public MemberVo userInfo(String accessToken) throws IOException{
        MemberVo vo= new MemberVo();
        String reqUrl ="https://kapi.kakao.com/v2/user/me";
        String token = accessToken;
        String header = "Bearer " + token;
        String id = "";
        String nickname = "";
        String name = "";
        String email = "";

        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", header);
            //응답확인
            int responseCode = conn.getResponseCode();
            System.out.println("responseCode:" + responseCode);

            //입력스트림을 가지고 데이터 읽기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            if((line=br.readLine())!=null){
                sb.append(line);
            }
            br.close();
            System.out.println("response body: " +sb.toString() );
            //json파싱

            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(sb.toString());
            if (je.isJsonObject()) {
                JsonObject jo = je.getAsJsonObject();
    
                if (jo.has("id")) {
                    id = jo.get("id").getAsString();
                }
    
                if (jo.has("kakao_account")) {
                    JsonObject kakaoAccount = jo.getAsJsonObject("kakao_account");
    
                    if (kakaoAccount.has("profile")) {
                        JsonObject profile = kakaoAccount.getAsJsonObject("profile");
                        nickname = profile.get("nickname").getAsString();

                    }
                }
            }
            vo.setId(id);
            vo.setNickname(nickname); // vo에 닉네임 설정
            System.out.println("nickname: " + vo.getNickname());
            System.out.println("id: " + id);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("API 요청과 응답 실패", e);
        }
    
        return vo;
    }
}
