package com.areastudio.nwtpdfanalyser;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

@MultipartConfig
public final class Hello extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {
        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println(request.getParameter("color"));
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {

        Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
        String fileName = filePart.getName();

        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();
        writer.println(request.getParameter("color"));
        writer.println(fileName);

        PDDocument pdDoc = null;
        UtilsBible.getBooks(15);


        PDFParser parser = new PDFParser(filePart.getInputStream());

        parser.parse();
        pdDoc = new PDDocument(parser.getDocument());

        PDFTextAnnotator pdfAnnotator = new PDFTextAnnotator("UTF-8"); // create
        // new
        // annotator
        pdfAnnotator.setLineSeparator(" "); // kinda depends on what you want to
        // match
        pdfAnnotator.initialize(pdDoc);
        try {
            pdfAnnotator
                    .highlight(
                            pdDoc,
                            "(\\d{0,1})\\s*([\\wÀ-ú\\p{L}]{2,})\\.?\\s*(\\d{1,3})(?::(\\d{1,3}))((?:(?:,\\s?|-)\\d{1,3})*)");
        }
        catch (Exception e){
            throw new ServletException(e);
        }

        pdDoc.save("new_" + fileName);
        try {
            if (parser.getDocument() != null) {
                parser.getDocument().close();
            }
            if (pdDoc != null) {
                pdDoc.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
