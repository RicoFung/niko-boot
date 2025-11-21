package com.niko.boot.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 基础Controller类
 * 封装chok2-devwork-controller-light的BaseRestController
 * 提供HttpServletRequest和HttpServletResponse的注入
 */
public class BaseRestController {
    
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    
    @ModelAttribute
    public void baseInitialization(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}

