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

    /**
     * slova i brojevi
     * @param name
     * @return
     */
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

    /**
     * generalno za proveru da li input sadrzi samo slova, treba na vise mesta pa sam promenila naziv
     * @param name
     * @return
     */
    public boolean isValidInput(String name){
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

    /**
     * sadrzi slova, brojeve i znak +
     * @param name
     * @return
     */
    public boolean charNumPlusUnlimited(String name){
        String nameRegex = "^[a-zA-Z0-9 +]+$";
        Pattern pattern = Pattern.compile(nameRegex);
        Matcher matcher = pattern.matcher(name);
        if(matcher.find()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isValidPassword(String pass){
        String passRegex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!_@$%^&*-]).{8,}$";
        Pattern pattern = Pattern.compile(passRegex);
        Matcher matcher = pattern.matcher(pass);
        if(matcher.find()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isValidEmail(String email){
        String emailRegex = "/\\S+@\\S+\\.\\S+/";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(emailRegex);
        if(matcher.find()){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean isValidCharNumSpace(String name){
        String nameRegex = "^[a-z A-Z 0-9]+$";
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
