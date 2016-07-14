package com.areastudio.nwtpdfanalyser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public final class Hello extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println("Sup?");
    }
    
}
