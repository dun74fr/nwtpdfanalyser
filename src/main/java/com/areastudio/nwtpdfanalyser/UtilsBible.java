package com.areastudio.nwtpdfanalyser;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsBible {

    private final ServletContext context;
    private Map<String, Integer> books = new LinkedHashMap();
    private int currentLang;
    private final Pattern pattern = Pattern.compile("((?:\\d\\.?)?)\\s?([\\wÀ-ú]\\p{L}{1,})\\.?\\s*(\\d{1,3})(?::\\s?(\\d{1,3}))((?:(?:,\\s?|-\\s?)\\d{1,3})*)(?:\\s?;\\s?(\\d{1,3})(?::\\s?(\\d{1,3}))((?:(?:,\\s?|-\\s?)\\d{1,3})*))*");
//    private HashMap<Integer, HashMap<Integer, String>> audioList;

    private static String bibleFileName = "bi12_%1.epub";
    private static String bibleFileNameNew = "nwt_%1.epub";

    public static String bibleUrl = "http://www.jw.org/apps/TRGCHlZRQVNYVrXF?output=json&pub=bi12&fileformat=EPUB&alllangs=0&langwritten=%1";
    public static String bibleUrlNew = "http://www.jw.org/apps/TRGCHlZRQVNYVrXF?output=json&pub=nwt&fileformat=EPUB&alllangs=0&langwritten=%1";
    private Map<String, String> epubBooks;

    public UtilsBible(int lang, ServletContext context) {
        this.context = context;
        currentLang = lang;
        initBooks(currentLang);
    }

    public static String normalizeBook(String book) {

        return normalize(book, true);

    }

    public static String normalize(String book, boolean removeSpace) {

        String cleanedBook = book.toLowerCase();

        if (removeSpace) {
            cleanedBook = cleanedBook.replace(" ", "-");
        }

        cleanedBook = cleanedBook.replace("·", "");
        cleanedBook = cleanedBook.replace("′", "");
        cleanedBook = cleanedBook.replace("́", "");
        cleanedBook = cleanedBook.replace("á", "a");
        cleanedBook = cleanedBook.replace("á", "a");
        cleanedBook = cleanedBook.replace("ä", "a");
        cleanedBook = cleanedBook.replace("ä", "a");
        cleanedBook = cleanedBook.replace("â", "a");
        cleanedBook = cleanedBook.replace("ã", "a");
        cleanedBook = cleanedBook.replace("ạ", "a");


        cleanedBook = cleanedBook.replace("é", "e");
        cleanedBook = cleanedBook.replace("é", "e");
        cleanedBook = cleanedBook.replace("é", "e");
        cleanedBook = cleanedBook.replace("è", "e");
        cleanedBook = cleanedBook.replace("ë", "e");
        cleanedBook = cleanedBook.replace("ê", "e");
        cleanedBook = cleanedBook.replace("ẹ", "e");

        cleanedBook = cleanedBook.replace("í", "i");
        cleanedBook = cleanedBook.replace("ı́", "i");
        cleanedBook = cleanedBook.replace("ıı ́", "i");
        cleanedBook = cleanedBook.replace("ï", "i");
        cleanedBook = cleanedBook.replace("î", "i");
        cleanedBook = cleanedBook.replace("ị", "i");


        cleanedBook = cleanedBook.replace("ó", "o");
        cleanedBook = cleanedBook.replace("ó", "o");
        cleanedBook = cleanedBook.replace("ö", "o");
        cleanedBook = cleanedBook.replace("ö", "o");
        cleanedBook = cleanedBook.replace("ô", "o");
        cleanedBook = cleanedBook.replace("õ", "o");
        cleanedBook = cleanedBook.replace("ọ", "o");


        cleanedBook = cleanedBook.replace("ú", "u");
        cleanedBook = cleanedBook.replace("ú", "u");
        cleanedBook = cleanedBook.replace("ü", "u");
        cleanedBook = cleanedBook.replace("ü", "u");
        cleanedBook = cleanedBook.replace("u ̈", "u");

        cleanedBook = cleanedBook.replace("û", "o");
        cleanedBook = cleanedBook.replace("ç", "c");
        cleanedBook = cleanedBook.replace("ỵ", "y");

        cleanedBook = cleanedBook.replace("sprüche", "spruche");
        return cleanedBook;

    }

    public String normalizeBookKorean(String book) {
        book = book.replace("Fë!", "창세기");
        book = book.replace("fXŠ", "출애굽기");
        book = book.replace("Ô´!", "레위기");
        book = book.replace("W!!", "민수기");
        book = book.replace("!è", "신명기");
        book = book.replace("~B!K ", "여호수아");
        book = book.replace("ÌÌ", "사사기");
        book = book.replace("õ!", "룻기");
        book = book.replace("ÌBzÕ", "사무엘상");
        book = book.replace("ÌBz!", "사무엘하");
        book = book.replace("‚œ!Õ", "열왕기상");
        book = book.replace("‚œ!!", "열왕기하");
        book = book.replace("\"H!Õ", "역대기상");
        book = book.replace("\"H!!", "역대기하");
        book = book.replace("w# ́", "에스라");
        book = book.replace("(5U^", "느헤미야");
        book = book.replace("w#O", "에스더");
        book = book.replace("_!", "욥기");
        book = book.replace("þ", "시편");
        book = book.replace("Øj", "잠언");
        book = book.replace("a^á", "전도서");
        book = book.replace("øa;Å!¾", "솔로몬의 노래");
        book = book.replace("ÆÌ^", "이사야");
        book = book.replace("ŠÔU^", "예레미야");
        book = book.replace("ŠÔU^ X#", "예레미야 애가");
        book = book.replace("w##", "에스겔");
        book = book.replace("92z", "다니엘");
        book = book.replace("BëK", "호세아");
        book = book.replace(" z", "요엘");
        book = book.replace("K8#", "아모스");
        book = book.replace("Œ_!", "오바댜");
        book = book.replace(" Û", "요나");
        book = book.replace("U#", "미가");
        book = book.replace("ÛX", "나훔");
        book = book.replace("!`p", "하박국");
        book = book.replace("#_ò", "스바냐");
        book = book.replace("\"$", "학개");
        book = book.replace("##Ç", "스가랴");
        book = book.replace("!´!", "말라기");

        book = book.replace("·ŠÂ", "마태복음");
        book = book.replace("#ŠÂ", "마가복음");
        book = book.replace("\u0002\u0002", "마가복음");
        book = book.replace("!#ŠÂ", "누가복음");
        book = book.replace(" !ŠÂ", "요한복음");
        book = book.replace(" !", "요한복음");
        book = book.replace("Ì^-a", "사도행전");
        book = book.replace("a\"á", "로마서");
        book = book.replace("1˝^ aá", "고린도 전서");
        book = book.replace("1˝^ Tá", "고린도 후서");
        book = book.replace("˘´‚Ká", "갈라디아서");
        book = book.replace("w}õá", "에베소서");
        book = book.replace("¦&‰á", "빌립보서");
        book = book.replace("]axá", "골로새서");
        book = book.replace("YÏa2# aá", "데살로니가 전서");
        book = book.replace("YÏa2# Tá", "데살로니가 후서");
        book = book.replace("‚8Y aá", "디모데 전서");
        book = book.replace("‚8Y Tá", "디모데 후서");
        book = book.replace("‚^á", "디도서");
        book = book.replace("¦Ô;á", "빌레몬서");
        book = book.replace("m ˚á", "히브리서");
        book = book.replace("^1‰á", "야고보서");
        book = book.replace("}ya aá", "베드로 전서");
        book = book.replace("}ya Tá", "베드로 후서");
        book = book.replace(" ! 1á", "요한 1서");
        book = book.replace(" ! 2á", "요한 2서");
        book = book.replace(" ! 3á", "요한 3서");
        book = book.replace("¹9á", "유다서");
        book = book.replace("0\u0005å", "요한 계시록");
        return book;
    }

    public Map<String, Integer> getBooks() {
//        if (books.isEmpty() || lang != currentLang) {
//            initBooks(lang);
//        }
        return books;
    }

    public String getBook(int bookNum) {
        if (bookNum < books.size() && bookNum >= 0) {
            return books.keySet().toArray(new String[books.keySet().size()])[bookNum];
        } else if (books.size() > 0) {
            return books.keySet().toArray(new String[books.keySet().size()])[0];
        } else {
            return "";
        }
    }

//    public String getFirstBook() {
//        return books.keySet().toArray(new String[books.keySet().size()])[0];
//    }

    public int getBookChap(String book) {
        return books.get(book) != null ? books.get(book) : 0;
    }

    public String getBook(String book) {
        book = book.trim();
        book = book.replace("-", " ");
//        book = book.replace(".", "");
        book = normalizeBook(book);
//        if (currentLang == 15) {
//            book = normalizeBookKorean(book);
//        }
        if (book.length() < 2) {
            return "";
        }
        if (book.startsWith("apocal") || book.startsWith("offb")) {
            return books.keySet().toArray(new String[1])[books.keySet().size() - 1];
        } else if (book.startsWith("salm")) {
            return getBook(18);
        }
        for (String b : books.keySet()) {
            String nb = normalizeBook(b);

            if (nb.equals(book) || nb.startsWith(book)) {
                return b;
            }
        }
        int distance = 1000;
        String returnBook = "";
        for (String b : books.keySet()) {
            if (book.startsWith(b.substring(0, 1).toLowerCase())) {
                int d = distance(b, book);
                if (d <= distance && containAll(book, b)) {
                    distance = d;
                    returnBook = b;
                }
            }
        }
        return returnBook;
    }

    public static boolean containAll(String subset, String full) {
        for (String c : subset.split("(?<=.)")) {
            if (!full.toLowerCase().contains(c)) {
                return false;
            }
        }
        return true;
    }

    public static int distance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    public int getBookNum(String book) {
        book = book.replace("-", " ");
        int i = 0;
        for (String b : books.keySet()) {
            if (b.replace("-", " ").equals(book)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void initBooks(int lang) {
        books.clear();
        Gson gson = new Gson();
//        String json = "";
//        FullBibleActivity.settings.getString("LANG_" + lang, "");
        LinkedHashMap<String,Integer> fullbook = gson.fromJson(Hello.LANG_JSON,new TypeToken<LinkedHashMap<String, Integer>>() {
        }.getType());
        books = new LinkedHashMap();
        epubBooks = new LinkedHashMap();
        for(String key : fullbook.keySet()) {
            if (key.contains("|")){
                String[] sp = key.split("\\|");
                books.put(sp[0],fullbook.get(key));
                epubBooks.put(sp[0],sp[1]);
            }
            else {
                books.put(key,fullbook.get(key));
            }
        }
        //books = gson.fromJson(json, new TypeToken<LinkedHashMap<String, Integer>>() {
//        }.getType());
        currentLang = lang;
    }


    public static boolean isEpubMode(int selectedLang, int versionNumber){
//        return !(new File(Environment.getExternalStorageDirectory()
//                + "/FloatingBible/lang_" + selectedLang + "_"
//                + versionNumber + "/bible/b17c10.html").isFile() || new File(Environment.getExternalStorageDirectory()
//                + "/FloatingBible/lang_" + selectedLang + "_"
//                + versionNumber + "/bible/b27c22.html").isFile());
        return true;
    }

    public List<Reference> getReferences(String refText) {
        List<Reference> results = new ArrayList();

        Matcher matcher;

        matcher = pattern.matcher(refText);

        while (matcher.find()) {

            if (isBibleReference(matcher.group())) {
                Reference ref = getReference(matcher.group());
                if (ref != null) {
                    results.add(ref);
                    if (matcher.group().contains(";")) {
                        String[] parts = matcher.group().split(";");
                        for (int i = 1; i < parts.length; i++) {
                            parts[i] = ref.book + " " + parts[i];
                            Reference newreference = getReference(parts[i]);
                            if (newreference != null) {
                                results.add(newreference);
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    public Reference getReference(String refText) {
        if (refText != null) {
            String prefix = "", book = "";
            int chapter = 1, verse;
            List<Integer> verses = new ArrayList();

            Matcher m = pattern.matcher(refText);
            if (m.find()) {
                int count = m.groupCount();
                if (count > 0) {
                    prefix = m.group(1) == null ? "" : m.group(1).concat(" ");
                }
                // must at least have a book or things will eventually fail
                if (count > 1) {
                    book = m.group(2) == null ? "" : m.group(2).trim();
                }
                // default chapter to 1
                if (count > 2) {
                    chapter = Integer.parseInt(m.group(3) == null ? "1" : m
                            .group(3));
                }
                // default verse to 1
                if (count > 3) {
                    verse = Integer.parseInt(m.group(4) == null ? "1" : m
                            .group(4));
                    verses.add(verse);
                }
                if (count > 4) {
                    // verse = Integer.parseInt(m.group(4) == null ? "1" :
                    // m.group(4));
                    String[] versesgp = m.group(5).replaceAll(" ", "")
                            .split("((?<=[-,])|(?=[-,]))");
                    for (int i = 0; i < versesgp.length; i++) {
                        if (versesgp[i].equals("-")) {
                            for (int y = verses.get(verses.size() - 1) + 1; y <= Integer
                                    .parseInt(versesgp[i + 1]); y++) {
                                verses.add(y);
                            }
                            i++;
                        } else if (versesgp[i].equals(",")) {
                            verses.add(Integer.parseInt(versesgp[i + 1]));
                            i++;
                        }
                    }
                }
            }
            //book = UtilsBible.getBook(prefix + book);
            if (book.length() > 1) {
                book = getBook(prefix + book);
                if (book.length() > 0 && chapter > 0 && verses.size() > 0) {
                    System.out.println("GETBOOK " + refText + " --> OK");
                    return new Reference(this.currentLang, book, chapter, verses);
                } else {
                    System.out.println("GETBOOK " + refText + " --> Not OK");
                }
            } else {
                System.out.println("GETBOOK " + refText + " --> Not found");
            }


        }
        return null;
    }

    public String handleSendText(String sharedText, int versionNum) {
        boolean multiple = false;
        if (sharedText.contains(";")) {
            multiple = true;
        }
        Reference reference = getReference(sharedText);
        if (reference != null) {
            String returnString = openFileAndFind(reference, versionNum);
            if (multiple) {
                String[] parts = sharedText.split(";");
                for (int i = 1; i < parts.length; i++) {
                    parts[i] = reference.book + " " + parts[i];
                    Reference newreference = getReference(parts[i]);
                    if (newreference != null) {
                        returnString += " " + newreference.toString() + " " + openFileAndFind(newreference, versionNum);
                    }
                }
            }
            return returnString;
        }
        return "";
    }

    public List<String[]> handleSendTextMultiple(String sharedText, int versionNum) {
        List<String[]> results = new ArrayList();

        List<Reference> references = getReferences(sharedText);
        for (Reference ref : references) {
            String text = openFileAndFind(ref, versionNum);
            if (text != null && !text.startsWith("Error")) {
                String[] verse = {ref.verseId, text};
                results.add(verse);
            }
        }

        return results;
    }

    public String openFileAndFind(Reference reference, int versionNum) {
        String html = getStringForBookAndChap(reference, versionNum);
        if (html.startsWith("Error")) {
            return html;
        }
        Document doc = Jsoup.parse(html);
        String finalText = "";
        for (Integer i : reference.verses) {
            int book = getBookNum(reference.book) + 1;
            if (getBooks().size() < 66) {
                book += 39;
            }
            String id = "v" + (book)
                    + String.format("%03d", reference.chapter) + String.format("%03d", i);
            Element content = doc.getElementById(id);
            if (content != null) {
                if (finalText.length() > 0) {
                    finalText += " ";
                }
                finalText += content.text().replaceAll("\\+", "")
                        .replaceAll(" ", " ");

            }
        }
        finalText = finalText.replaceAll("(.*?)(\\D)(\\d)(.*?)", "$1$2 $3$4");
        if (finalText.length() > 0) {
            return finalText;
        }
        return "Error can't extract verses from file.";
    }

    public int getVerseNumber(String html) {
        Document doc = Jsoup.parse(html);
        Elements content = doc.getElementsByAttributeValue("class", "verse");
        if (content != null) {
            return content.size();
        }
        return -1;
    }

    public int getVerseNumberFromReference(Reference reference) {
        String html = getStringForBookAndChap(reference, 400);
        Document doc = Jsoup.parse(html);
        Elements content = doc.getElementsByAttributeValue("class", "verse");
        if (content != null) {
            return content.size();
        }
        return -1;
    }

    public String getStringForBookAndChap(Reference reference, int versionNum) {

        if (isEpubMode(currentLang,versionNum)) {
            String file = getBibleFileName(currentLang).replace(".epub", "")
                    + "/OEBPS/" + getFileForReference(true, reference);
            String html = getStringForFile(new File(file));

            html = html.replaceAll("<span id=\"(chapter\\d+_verse\\d+)\"></span>", "</span><span id=\"$1\">");
            html = html.replaceAll("<a id=\"(chapter\\d+_verse\\d+)\"></a>", "</span><span id=\"$1\">");

            html = html.replaceAll("chapter(\\d{3})_", "v" + (getBookNum(reference.book) + 1) + "$1_");
            html = html.replaceAll("chapter(\\d{2})_", "v" + (getBookNum(reference.book) + 1) + "0$1_");
            html = html.replaceAll("chapter(\\d)_", "v" + (getBookNum(reference.book) + 1) + "00$1_");
            html = html.replaceAll("_verse(\\d{3})", "$1");
            html = html.replaceAll("_verse(\\d{2})", "0$1");
            html = html.replaceAll("_verse(\\d{1})", "00$1");
            html = html.replaceAll("<span id=\"v", "<span class=\"verse\" id=\"v");
            return html;
        }
        else {
            String file = "lang_" + currentLang + "_" + versionNum
                    + "/bible/" + getFileForReference(false,reference);
            String html = getStringForFile(new File(file));
            return html;
        }

    }


    public String getStringForFile(File html) {
        Date start = new Date();
//        System.out.println("GETBOOK " + "getStringForFile : " + start);
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    context.getResourceAsStream( html.toString() )));
            String y = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                y = y.concat(inputLine);
            in.close();
//            Log.d("TIMERS", "getStringForFile end :" + (new Date().getTime() - start.getTime()) + "ms");



            return y;
        } catch (IOException e) {
            System.out.println("getStringForFile " + e.getMessage());
            return "Error " + e.getMessage();
        }

    }

    public String getFileForReference(Reference reference) {
        return getFileForReference(isEpubMode(currentLang,400),reference);
    }

    public String getFileForReference(boolean epubMode, Reference reference) {
        if (epubMode)
        {
            String bookHtml = epubBooks.get(reference.book);
            if (reference.chapter > 1) {
                bookHtml += "-split" + (reference.chapter);
            }
            bookHtml += ".xhtml";
            return bookHtml;
        }
        else {
            if (currentLang < 7) {
                return UtilsBible.normalizeBook(reference.book) + "_" + reference.chapter
                        + "_.html";
            } else {
                return "b" + (getBookNum(reference.book) + 1) + "c" + reference.chapter + ".html";
            }
        }
    }

    public boolean isBibleReference(String ref) {
        return getReference(ref) != null;
    }


//    public static int getIndexOfLangId(int id) {
//        String[] langs = getLangArray();
//
//        ArrayList<Lang> langArray = getLangs(context);
//
//        for (int i = 0; i < langs.length; i++) {
//            if (getLangIdFromString(context, langs[i]) == id) {
//                return i;
//            }
//        }
//        return -1;
//    }
//
//    public static int getIdOfLangIndex(Context context, int index) {
//        String[] langs = getLangArray(context);
//
//        return getLangIdFromString(context, langs[index]);
//
//    }
//
//    public static List<String> filterAvailableLanguages(Context context) {
//        int versionNumber = context.getResources().getInteger(R.integer.data_version);
//
//        ArrayList<Lang> langArray = getLangs(context);
//        List<String> availableLangs = new ArrayList<>();
//        for (Lang lang : langArray) {
//            if (checkInstallation(lang.getId(), isEpubMode(lang.getId(),versionNumber),versionNumber)) {
//                availableLangs.add(lang.getText());
//            }
//        }
//        return availableLangs;
//    }

//    public static int getCurrentSelectedPosition(Context context, int currentLangId) {
//        int versionNumber = context.getResources().getInteger(R.integer.data_version);
//        SharedPreferences settings = context.getSharedPreferences(FullBibleActivity.PREFS_FLOATINGBIBLE, 0);
//
//        ArrayList<Lang> langArray;
//        Gson gson = new Gson();
//        String json = settings.getString("LANGS", "");
//        langArray = gson.fromJson(json, new TypeToken<ArrayList<Lang>>() {
//        }.getType());
//
//        List<String> availableLangs = new ArrayList<>();
//        for (int i = 0; i < langArray.size(); i++) {
//            if (checkInstallation(langArray.get(i).getId(),isEpubMode(langArray.get(i).getId(),versionNumber), versionNumber)) {
//                availableLangs.add(langArray.get(i).getText());
//                if (langArray.get(i).getId() == currentLangId) {
//                    return availableLangs.size() - 1;
//                }
//            }
//        }
//        return 0;
//    }

//    public static ArrayList<Lang> getLangs(Context context) {
//        SharedPreferences settings = context.getSharedPreferences(FullBibleActivity.PREFS_FLOATINGBIBLE, 0);
//
//        ArrayList<Lang> langArray;
//        Gson gson = new Gson();
//        String json = settings.getString("LANGS", "");
//        return gson.fromJson(json, new TypeToken<ArrayList<Lang>>() {
//        }.getType());
//    }
//
//    public static int getLangIdFromString(Context context, String text) {
//        ArrayList<Lang> langArray = getLangs(context);
//        for (Lang lang : langArray) {
//            if (lang.getText().equals(text)) {
//                return lang.getId();
//            }
//        }
//        return 1;
//    }
//
//    public static String getLangStringFromId(Context context, int id) {
//        ArrayList<Lang> langArray = getLangs(context);
//        for (Lang lang : langArray) {
//            if (lang.getId() == id) {
//                return lang.getText();
//            }
//        }
//        return "";
//    }
//
//    public static Lang getLangFromId(Context context, int id) {
//        ArrayList<Lang> langArray = getLangs(context);
//        for (Lang lang : langArray) {
//            if (lang.getId() == id) {
//                return lang;
//            }
//        }
//        return null;
//    }
//
//    public static String[] getLangArray(Context context) {
//        ArrayList<Lang> langArray = getLangs(context);
//        String[] result = new String[langArray.size()];
//        Collections.sort(langArray, new Comparator<Lang>() {
//            @Override
//            public int compare(Lang lhs, Lang rhs) {
//                return lhs.getText().compareTo(rhs.getText());
//            }
//        });
//        for (int i = 0; i < langArray.size(); i++) {
//            result[i] = langArray.get(i).getText();
//        }
//        return result;
//    }

//    public String getAudioUrl(Context context, String selectedBook, int selectedChap) {
//        if (audioList == null || audioList.isEmpty()) {
//            audioList = new HashMap<>();
//            SharedPreferences settings = context.getSharedPreferences(
//                    FullBibleActivity.PREFS_FLOATINGBIBLE, 0);
//            final String letter = getJWLangFromInt(currentLang);
//            String audioListStr = settings.getString("AUDIO_LIST_" + letter, null);
//            if (audioListStr == null) {
//                Log.d("AUDIO", "getAudioUrl str : null");
//                return null;
//            }
//            String[] lines = audioListStr.split("\\n");
//            if (lines != null && lines.length > 0) {
//                for (int i = 0; i < lines.length; i++) {
//                    String[] row = lines[i].split("\\|");
//                    if (row[0].equals(getJWLangFromInt(currentLang))) {
//                        if (audioList.get(Integer.parseInt(row[1])) == null) {
//                            audioList.put(Integer.parseInt(row[1]), new HashMap<Integer, String>());
//                        }
//                        audioList.get(Integer.parseInt(row[1])).put(Integer.parseInt(row[2]), row[3]);
//                    }
//                }
//            }
//        }
//        if (audioList != null && audioList.get(getBookNum(selectedBook) + 1) != null) {
//            Log.d("AUDIO", "getAudioUrl : " + audioList.get(getBookNum(selectedBook) + 1).get(selectedChap + 1));
//            return audioList.get(getBookNum(selectedBook) + 1).get(selectedChap + 1);
//        }
//        Log.d("AUDIO", "getAudioUrl : null");
//        return null;
//    }

    public static String getJWLangFromInt(int lang) {
        switch (lang) {
            case 1:
                return "F";
            case 2:
                return "E";
            case 3:
                return "S";
            case 4:
                return "X";
            case 5:
                return "T";
            case 6:
                return "I";
            case 7:
                return "G";
            case 8:
                return "TG";
            case 9:
                return "M";
            case 10:
                return "H";
            case 11:
                return "K";
            case 12:
                return "U";
            case 13:
                return "CHS";
            case 14:
                return "SV";
            case 15:
                return "KO";
            case 16:
                return "P";
            case 17:
                return "V";
            case 18:
                return "CR";
            case 19:
                return "CV";
            case 20:
                return "SB";
            case 21:
                return "BL";
            case 22:
                return "J";
            case 23:
                return "B";
            case 24:
                return "A";
            case 25:
                return "REA";
            case 26:
                return "T";
            case 27:
                return "AL";
            case 28:
                return "O";
            case 29:
                return "IN";
            case 30:
                return "N";
            case 31:
                return "Z";
            case 32:
                return "VT";
            case 33:
                return "TK";
            case 34:
                return "CB";
            case 35:
                return "AZ";
            case 36:
                return "AF";
            case 37:
                return "ST";
            case 38:
                return "LI";
            case 39:
                return "MG";
            case 40:
                return "AM";
            case 41:
                return "CHM-CHS";
            case 42:
                return "C";
            case 43:
                return "KZ";
            case 44:
                return "IL";
            case 45:
                return "AN";
            case 46:
                return "CH";
            case 47:
                return "HI";
            case 48:
                return "TW";
            case 49:
                return "YR";
            case 50:
                return "D";
            case 51:
                return "SW";
            default:
                return "-1";
        }
    }
//    public boolean isAudioAvailable(final Context context) {
//
//        final String letter = getJWLangFromInt(currentLang);
//        if (!((FullBibleActivity) context).isOnline()) {
//            Toast.makeText(context, R.string.audio_need_internet_connection, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//
//        if (audioList != null) {
//            String[] lines = audioList.split("\\n");
//            if (lines != null && lines.length > 0) {
//                for (int i = 0; i < lines.length; i++) {
//                    String[] row = lines[i].split("\\|");
//                    if (row[0].equals(getJWLangFromInt(currentLang))) {
//                        Log.d("AUDIO", "isAudioAvailable : true");
//                        return true;
//                    }
//                }
//            }
//        }
//        Toast.makeText(context, R.string.no_audio_files, Toast.LENGTH_SHORT).show();
//        Log.d("AUDIO", "isAudioAvailable : false");
//        return false;
//
//    }
//    public boolean isAudioAvailable(final Context context) {
//        final SharedPreferences settings = context.getSharedPreferences(
//                FullBibleActivity.PREFS_FLOATINGBIBLE, 0);
//        final String letter = getJWLangFromInt(currentLang);
//        String audioList = settings.getString("AUDIO_LIST_" + letter, null);
//        if (!((FullBibleActivity) context).isOnline()) {
//            Toast.makeText(context, R.string.audio_need_internet_connection, Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        long date = settings.getLong("LAST_AUDIO_LIST_CHECK_" + letter, 0);
//        long period;
//        if (audioList != null) {
//            period = (7 * 24 * 3600 * 1000);
//        } else {
//            period = (1 * 24 * 3600 * 1000);
//        }
//        if (new Date().getTime() - date > period) {
//            new AudioListDownloader() {
//                @Override
//                protected void onPostExecute(String s) {
//                    if (s == null) {
//                        Log.d("AUDIO", "second try download");
//                        new AudioListDownloader() {
//                            @Override
//                            protected void onPostExecute(String s) {
//                                settings.edit().putLong("LAST_AUDIO_LIST_CHECK_" + letter, new Date().getTime()).apply();
//                                settings.edit().putString("AUDIO_LIST_" + letter, s).commit();
//                                ((FullBibleActivity) context).createAudioPlayer();
//                            }
//                        }.execute(letter);
//                    } else {
//                        settings.edit().putLong("LAST_AUDIO_LIST_CHECK_" + letter, new Date().getTime()).apply();
//                        settings.edit().putString("AUDIO_LIST_" + letter, s).commit();
//                        ((FullBibleActivity) context).createAudioPlayer();
//                    }
//
//                }
//            }.execute(letter);
////            Log.d("AUDIO", "isAudioAvailable : " + (audioList != null && audioList.length() > 0));
////            return audioList != null && audioList.length() > 0;
//        }
//        if (audioList != null) {
//            String[] lines = audioList.split("\\n");
//            if (lines != null && lines.length > 0) {
//                for (int i = 0; i < lines.length; i++) {
//                    String[] row = lines[i].split("\\|");
//                    if (row[0].equals(getJWLangFromInt(currentLang))) {
//                        Log.d("AUDIO", "isAudioAvailable : true");
//                        return true;
//                    }
//                }
//            }
//        }
//        Toast.makeText(context, R.string.no_audio_files, Toast.LENGTH_SHORT).show();
//        Log.d("AUDIO", "isAudioAvailable : false");
//        return false;
//
//    }

    public static String getBibleFileName(String code) {
        if (UtilsBible.isNewNwt(code)){
            return bibleFileNameNew.replace("%1", code);
        }
        else {
            return bibleFileName.replace("%1", code);
        }

    }
    public static String getBibleFileName(int selectedLang) {
        return getBibleFileName(getJWLangFromInt(selectedLang));

    }

//    public void processBibleEpub(int selectedLang, String epubfile) {
//        try {
//            new Decompress(new File(Environment.getExternalStorageDirectory(),
//                    "FloatingBible/"+epubfile).getCanonicalPath(),
//                    new File(Environment.getExternalStorageDirectory(),
//                            "FloatingBible").getAbsolutePath() + "/" + epubfile.replace(".epub", ""))
//                    .unzip();
//            new File(Environment.getExternalStorageDirectory(),
//                    "FloatingBible/"+epubfile).delete();
//        } catch (IOException e) {
//
//        }
//    }

    public String getBibleUrl(String code) {
        if (UtilsBible.isNewNwt(code)){
            return bibleUrlNew.replace("%1", code);
        }
        else {
            return bibleUrl.replace("%1", code);
        }
    }

    public static boolean isNewNwt(String code){
        return ("E".equals(code) || "KO".equals(code) || "K".equals(code)) || "CR".equals(code) || "T".equals(code)|| "AZ".equals(code)|| "ST".equals(code)|| "AM".equals(code);
    }
}
