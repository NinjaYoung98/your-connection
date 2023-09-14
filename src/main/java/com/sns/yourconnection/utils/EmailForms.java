package com.sns.yourconnection.utils;


import com.sns.yourconnection.exception.AppException;
import com.sns.yourconnection.exception.ErrorCode;
import java.util.regex.Pattern;
import org.springframework.mail.SimpleMailMessage;

public class EmailForms {

    public static SimpleMailMessage createEmailForm(String toEmail, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(text);
        return message;
    }

    public static void validateEmailForm(String email) {
        // 이메일 형식에 대한 정규식 검증  ex) username@domaim.com
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        if (!pattern.matcher(email).matches()) {
            throw new AppException(ErrorCode.NOT_SUPPORT_FORMAT);
        }
    }
}
