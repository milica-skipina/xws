package com.example.tim2.datavalidation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpressions {

    public boolean idIdValid(Long id){
        String number = Long.toString(id);
        String nameRegex = "^[0-9]{1,6}$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(number);
        if(matcher.find()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isValidMileage(Double a){
        String number = Double.toString(a);
        String nameRegex = "[0-9]*\\\\.[0-9]+|[0-9]+";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(number);
        if(matcher.find()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean idValidRaiting(Double a){
        String raiting = Double.toString(a);
        String nameRegex = "[0-9]*\\\\.[0-9]+|[0-9]+";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(raiting);
        if(matcher.find() && a>=0 && a<=10){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean idValidKidsSeats(Integer a){
        String raiting = Integer.toString(a);
        String nameRegex = "[0-9]*";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(raiting);
        if(matcher.find() && a>=0 && a<=7){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isTextCommentOrMessageValid (String name) {
        String nameRegex = "^.{1,100}$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);

        if(matcher.find()){
            return true;
        }
        else{
            return  false;
        }
    }

    public boolean isValidDiscount(Double discount){
        String raiting = Double.toString(discount);
        String nameRegex = "[0-9]*\\\\.[0-9]+|[0-9]+";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(raiting);
        if(matcher.find() && discount>=0 && discount<=100){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isValidSomeName(String name){
        String nameRegex = "^[a-zA-Z0-9]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);
        if(matcher.find()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isValidCodeType(String name){
        String nameRegex = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);
        if(matcher.find()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isValidCity(String name){
        String nameRegex = "^[a-zA-Z]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        System.out.println(name);
        Matcher matcher = pattern.matcher(name);
        if(matcher.find()){
            return true;
        }
        else{
            return false;
        }
    }
}
