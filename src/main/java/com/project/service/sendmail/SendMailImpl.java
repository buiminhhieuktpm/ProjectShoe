package com.project.service.sendmail;

import lombok.var;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class SendMailImpl implements SendMail {

    private static final Logger logger = LoggerFactory.getLogger(SendMailImpl.class);

    @Qualifier("getMailSender")
    @Autowired
    private JavaMailSender javaMailSender;

    //send mail to contact
    @Override
    public void sendMail(String toEmail, String subject, String fullname) {

        var mailMessage = new SimpleMailMessage();

        StringBuilder mailContent = new StringBuilder();
        mailContent.append("Xin chào, " + fullname + "\n");
        mailContent.append("Người nhận: " + toEmail + "\n");
        mailContent.append("Xin chào quý khách hàng thân thiện đã quan tâm đến Shoe Store \n");
        mailContent.append("Chúc quý khách một ngày tốt lành !");

        mailMessage.setTo(toEmail);
        mailMessage.setSubject(subject);
        mailMessage.setText(mailContent.toString());

        try {
            javaMailSender.send(mailMessage);
        } catch (MailException ex) {
            ex.printStackTrace();
            logger.error("Cannot send email ", ex.getMessage());
        }
    }

    @Override
    public void confirmMail(String toEmail, String url, String username) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setFrom("contact@shoestore.com", "Shoe Store");
            helper.setTo(toEmail);

            String subject = "Kích hoạt tài khoản đăng kí";

            String content = "<p>Xin chào, " + username + "</p>"
                    + "<p>Bạn có yêu cầu kích hoạt tài khoản của bạn.</p>"
                    + "<p>Click vào đường dẫn bên dưới để kích hoạt tài khoản tài khoản: </p>"
                    + "<p>" + url + "</p>"
                    + "<br>"
                    + "<p>Bỏ qua mail này nếu bạn không muốn đăng kí tài khoản, "
                    + "và đường dẫn này sẽ hết hiệu trong 15 phút.</p>";

            //message.setContent(content,"text/html");
            helper.setSubject(subject);
            helper.setText(content, true);

            this.javaMailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendOrders(String toEmail, String body, String name) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

            helper.setFrom("contact@shoestore.com", "Shoe Store");
            helper.setTo(toEmail);

            String subject = "Đơn hàng";

            String content = "<p>Xin chào, " + name + "</p>"
                    + "<p>Cảm ơn bạn đã đặt hàng của chúng tôi.</p>"
                    + "<p>Đây là thông tin đơn hàng của bạn : </p>"
                    + body;

            message.setContent(content,"text/html;charset=utf-8");
            helper.setSubject(subject);
            helper.setText(content, true);

            this.javaMailSender.send(message);

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
