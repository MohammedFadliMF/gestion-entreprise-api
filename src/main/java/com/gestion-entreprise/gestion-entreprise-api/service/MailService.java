package com.example.springbootoauthjwt.service;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
// import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import com.example.springbootoauthjwt.exceptions.InvoiceException;
import com.example.springbootoauthjwt.model.Invoice;
import com.example.springbootoauthjwt.model.InvoiceGenarator;
import com.example.springbootoauthjwt.model.InvoiceItem;
import com.example.springbootoauthjwt.model.MailStructure;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;

@Service
public class MailService {
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private InvoiceService invoiceService;

    @Value("${spring.mail.username}")
    private String fromMail;
    public void sendMail(MailStructure mailStructure, byte[] pdfBytes){
        MimeMessagePreparator preparator=mimeMessage->{
        mimeMessage.setFrom(fromMail);
        mimeMessage.setSubject(mailStructure.getSubject());

        String message="From: "+mailStructure.getSender()+"\n"+mailStructure.getMessage();

        mimeMessage.setText(message);
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailStructure.getTo()));

        String attachment = "invoices/invoice.pdf";
        try{

            // FileSystemResource file = new FileSystemResource(new File(attachment));
            // if (file.exists()){
                MimeMessageHelper helper= new MimeMessageHelper(mimeMessage,true);
                // helper.addAttachment(file.getFilename(), file);
                        helper.addAttachment("invoice.pdf", new ByteArrayResource(pdfBytes));

                helper.setText("",true);
            // } else {
                // System.out.println("File is not Found");
            // }
        } catch (Exception ex){
            ex.printStackTrace();
        }
        };
        javaMailSender.send(preparator);
    }
    
    public void sendMailToUser(MailStructure mailStructure) {
        MimeMessagePreparator preparator = mimeMessage -> {
            mimeMessage.setFrom(fromMail);
            mimeMessage.setSubject(mailStructure.getSubject());

            String message = "From: " + mailStructure.getSender() + "\n" + mailStructure.getMessage();

            mimeMessage.setText(message);
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(mailStructure.getTo()));
        };
        javaMailSender.send(preparator);
    }

    
    public byte[] generateInvoice(String companyName,
            String companyEmail,String invoiceNumber, String invoiceDate, String customerName, String customerAddress,Set<InvoiceItem> items,String totalAmount) throws IOException, InvoiceException {
    //   Invoice invoice=invoiceService.findInvoiceById(12L);
        InvoiceGenarator generator = new InvoiceGenarator();
        return generator.generateInvoice1(companyName,companyEmail,invoiceNumber, invoiceDate, customerName, customerAddress,  
                items,totalAmount);
    }

}
