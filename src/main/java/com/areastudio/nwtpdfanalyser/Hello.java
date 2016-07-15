package com.areastudio.nwtpdfanalyser;

import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;

@MultipartConfig
public final class Hello extends HttpServlet {
    public static String LANG_JSON;
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

        Part filePart = request.getPart("pdf"); // Retrieves <input type="file" name="file">
        String fileName = filePart.getName();

        response.setContentType("application/pdf");
//        PrintWriter writer = response.getWriter();
//        writer.println(request.getParameter("color"));
//        writer.println(fileName);

        PDDocument pdDoc = null;
//        UtilsBible.getBooks(2);
        try {
            System.out.println("RealPath : " +getServletContext().getRealPath("/langs.xml"));
            LANG_JSON = LanguageXml.importFile(new FileInputStream(getServletContext().getRealPath("/langs.xml")), Integer.parseInt(request.getParameter("lang_id")));
            System.out.println("ImportLang :" +LANG_JSON);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("ImportLang failed :" +e.getMessage());
        }

        PDFParser parser = new PDFParser(new RandomAccessBufferedFileInputStream(filePart.getInputStream()));

        parser.parse();
        pdDoc = new PDDocument(parser.getDocument());

        PDFTextAnnotator pdfAnnotator = new PDFTextAnnotator("UTF-8"); // create
        // new
        // annotator
        pdfAnnotator.setLineSeparator(" "); // kinda depends on what you want to
        // match
        pdfAnnotator.initialize(pdDoc);
        try {
            pdfAnnotator.highlight(pdDoc, "((?:\\d\\.?)?)\\s?([\\wÀ-ú]\\p{L}{1,})\\.?\\s*(\\d{1,3})(?::\\s?(\\d{1,3}))((?:(?:,\\s?|-\\s?)\\d{1,3})*)(?:\\s?;\\s?(\\d{1,3})(?::\\s?(\\d{1,3}))((?:(?:,\\s?|-\\s?)\\d{1,3})*))*");
        }
        catch (Exception e){
            throw new ServletException(e);
        }

        pdDoc.save(response.getOutputStream());
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
