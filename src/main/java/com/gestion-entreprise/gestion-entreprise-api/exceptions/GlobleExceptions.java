package com.example.springbootoauthjwt.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobleExceptions {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetails> UserExceptionHandler(UserException ue,WebRequest req){

        ErrorDetails error=new ErrorDetails(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error,HttpStatus.BAD_REQUEST);
    }

    

    @ExceptionHandler(CompanyException.class)
    public ResponseEntity<ErrorDetails> CommentExceptionHandler(CompanyException ce, WebRequest req) {

        ErrorDetails error = new ErrorDetails(ce.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    } 
    
    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorDetails> ChatExceptionHandler(ChatException ce, WebRequest req) {

        ErrorDetails error = new ErrorDetails(ce.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }
   

    @ExceptionHandler(CustomerException.class)
    public ResponseEntity<ErrorDetails> CustomerExceptionHandler(CustomerException me, WebRequest req) {

        ErrorDetails error = new ErrorDetails(me.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvoiceException.class)
    public ResponseEntity<ErrorDetails> InvoiceExceptionHandler(InvoiceException ie, WebRequest req) {

        ErrorDetails error = new ErrorDetails(ie.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ItemException.class)
    public ResponseEntity<ErrorDetails> ItemExceptionHandler(ItemException ie, WebRequest req) {

        ErrorDetails error = new ErrorDetails(ie.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(RoleException.class)
    public ResponseEntity<ErrorDetails> RoleExceptionHandler(RoleException re, WebRequest req) {

        ErrorDetails error = new ErrorDetails(re.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(TaxException.class)
    public ResponseEntity<ErrorDetails> TaxExceptionHandler(TaxException te, WebRequest req) {

        ErrorDetails error = new ErrorDetails(te.getMessage(), req.getDescription(false), LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetails> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException me) {

        ErrorDetails error = new ErrorDetails(me.getBindingResult().getFieldError().getDefaultMessage(),"validation error", LocalDateTime.now());
        return new ResponseEntity<ErrorDetails>(error, HttpStatus.BAD_REQUEST);
    }
}
