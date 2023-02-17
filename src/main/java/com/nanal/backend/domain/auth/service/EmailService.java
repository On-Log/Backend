package com.nanal.backend.domain.auth.service;

import com.nanal.backend.domain.auth.dto.resp.RespEmailConfirmDto;
import com.nanal.backend.domain.auth.enumerate.MemberProvider;
import com.nanal.backend.domain.auth.exception.EmailAlreadyExistException;
import com.nanal.backend.domain.auth.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    @Value("${AdminMail.id}")
    private String senderEmail;
    @Value("${mail.smtp.timeout}")
    private Long timeout;

    private final MemberRepository memberRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender emailSender;

    private MimeMessage createMessage(String receiverEmail, String ePw)throws Exception{


        System.out.println("보내는 대상 : "+ receiverEmail);
        System.out.println("인증 번호 : "+ePw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, receiverEmail);//보내는 대상
        message.setSubject("Nanal 이메일 인증");//제목

        String msgg="";
        msgg+= "<div style='margin:20px;'>";
        msgg+= "<h1> 안녕하세요 Nanal 입니다. </h1>";
        msgg+= "<br>";
        msgg+= "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg+= "<br>";
        msgg+= "<p>감사합니다.<p>";
        msgg+= "<br>";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress(senderEmail, "onlog"));//보내는 사람

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    public RespEmailConfirmDto sendSimpleMessage(String receiverEmail)throws Exception {
        // 이미 가입된 이메일인지 체크
        memberRepository.findByEmail(MemberProvider.GENERAL + "#" + receiverEmail)
                .ifPresent(m -> { throw EmailAlreadyExistException.EXCEPTION; });

        String ePw = createKey();
        // TODO Auto-generated method stub
        MimeMessage message = createMessage(receiverEmail, ePw);
        try{//예외처리
            emailSender.send(message);

            redisTemplate.opsForValue().set(
                    receiverEmail,
                    ePw,
                    timeout,
                    TimeUnit.SECONDS
            );
            log.info("이메일 인증값 저장 완료");
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
        return new RespEmailConfirmDto(ePw);
    }
}