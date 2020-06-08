package com.jakuszko.mateusz.library.expectionshandlers;

import com.jakuszko.mateusz.library.exceptions.ReaderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ReaderNotFoundHandler {

    @ExceptionHandler(value = ReaderNotFoundException.class)
    @ResponseBody
    public String handleMethod(HttpServletRequest request, ReaderNotFoundException ex) {
        log.error("Request" + request.getRequestURL() + "Threw an Exception ", ex);
        return "There is no such reader in a database, please contact with developers...";
    }
}
