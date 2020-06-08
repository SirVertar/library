package com.jakuszko.mateusz.library.expectionshandlers;

import com.jakuszko.mateusz.library.exceptions.CopyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class CopyNotFoundHandler {

    @ExceptionHandler(value = CopyNotFoundException.class)
    @ResponseBody
    public String handleMethod(HttpServletRequest request, CopyNotFoundException ex) {
        log.error("Request" + request.getRequestURL() + "Threw an Exception ", ex);
        return "There is no such copy in a database, please contact with developers...";
    }
}
