package com.myjob.member.passHash;

import java.security.MessageDigest;


public class PasswordHash {
    
    public static String hashPassword(String password){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
             // Convert byte array to a hexadecimal string
             StringBuilder sb = new StringBuilder();
             for(byte b : hashedBytes){
                sb.append(String.format("%02x", b));

             }
             return sb.toString();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
