package com.areastudio.nwtpdfanalyser;

import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.color.PDDeviceRGB;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
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

    public InputStream getResource(String resourcePath) {
        ServletContext servletContext = getServletContext();
        InputStream openStream = servletContext.getResourceAsStream( resourcePath );
        return openStream;
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException, ServletException {

        Part filePart = request.getPart("pdf"); // Retrieves <input type="file" name="file">
        String disposition = filePart.getHeader("Content-Disposition");
        String fileName = disposition.replaceFirst("(?i)^.*filename=\"([^\"]+)\".*$", "$1");


        response.setContentType("application/pdf");
        System.out.println("content-disposition :" + "attachment; filename=\"" + fileName +"\"");
        response.addHeader("content-disposition", "attachment; filename=\"" + fileName +"\"");
//        PrintWriter writer = response.getWriter();
//        writer.println(request.getParameter("color"));
//        writer.println(fileName);

        PDDocument pdDoc = null;
//        UtilsBible.getBooks(2);
        try {
            System.out.println("Import lang : " + Integer.parseInt(request.getParameter("lang_id")));
            LANG_JSON = LanguageXml.importFile(getResource("langs.xml"), Integer.parseInt(request.getParameter("lang_id")));
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

        int color = Integer.parseInt(request.getParameter("color"));

        pdfAnnotator.setDefaultColor(new PDColor(new float[] { red(color) / 100, green(color) / 100, blue(color)/100 },PDDeviceRGB.INSTANCE));

        if (color == -1){
            pdfAnnotator.setOpacity(0f);
        }

        pdfAnnotator.setCurrentLang(Integer.parseInt(request.getParameter("lang_id")));
        // new
        // annotator
        pdfAnnotator.setLineSeparator(" "); // kinda depends on what you want to
        // match
        pdfAnnotator.initialize(pdDoc);
        try {
            pdfAnnotator.highlight(pdDoc, "((?:\\d\\.?)?)\\s?([\\wÀ-ú]\\p{L}{1,})\\.?\\s*(\\d{1,3})(?::\\s?(\\d{1,3}))((?:(?:,\\s?|-\\s?)\\d{1,3})*)(?:\\s?;\\s?(\\d{1,3})(?::\\s?(\\d{1,3}))((?:(?:,\\s?|-\\s?)\\d{1,3})*))*", getServletContext());
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

    public static int red(int color) {
        return (color >> 16) & 0xFF;
    }

    public static int green(int color) {
        return (color >> 8) & 0xFF;
    }
    public static int blue(int color) {
        return color & 0xFF;
    }

}
