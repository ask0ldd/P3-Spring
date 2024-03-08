package com.example.immo.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.example.immo.services.interfaces.IValidationService;

@Service
public class ValidationService implements IValidationService {

    public boolean isName(String string) {
        Pattern p = Pattern.compile(
                "^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]{2,}$");
        Matcher m = p.matcher(string);
        return m.matches();
    }

    public boolean isEmail(String string) {
        Pattern p = Pattern.compile(
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher m = p.matcher(string);
        return m.matches();
    }

}
