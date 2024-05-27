package id.co.map.spk.utils;

import id.co.map.spk.entities.AppUserEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class EncryptedPasswordUtil {

    //Encrypt Password with BCryptPasswordEncoder
    public static String encryptPassword(String password){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    public static void main(String... args){
        String password = "Password01";
        String encryptedPassword = encryptPassword(password);

        System.out.println("%%% Encrypted Password Test: " + encryptedPassword);
        List<Integer> listNumbers = new ArrayList<>();

        for(int ln:listNumbers) {
            System.out.println(listNumbers.size());
        }
        System.out.println("Done Testing");
    }

}
