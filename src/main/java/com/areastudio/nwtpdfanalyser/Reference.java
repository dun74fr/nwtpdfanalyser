package com.areastudio.nwtpdfanalyser;

import java.util.ArrayList;
import java.util.List;


public class Reference {

    public Reference(int lang, String book, int chapter, int verse) {
        List<Integer> verses = new ArrayList();
        verses.add(verse);
        this.book = book;
        this.chapter = chapter;
        this.verses = verses;
        if (verses != null && verses.size() > 0) {
            this.verseId = computeVerseId(lang, book, chapter, verses.get(0));
        }
    }

    public Reference(int lang, String book, int chapter, List<Integer> verses) {
        this.book = book;
        this.chapter = chapter;
        this.verses = verses;
        if (verses != null && verses.size() > 0) {
            this.verseId = computeVerseId(lang, book, chapter, verses.get(0));
        }
    }

    private String computeVerseId(int lang, String book, int chapter, int verse) {
        UtilsBible bible = new UtilsBible(lang);
        return "v" + (bible.getBookNum(book) + 1) + String.format("%03d", chapter) + String.format("%03d", verse);
    }

    public String content = "";
    public String book;
    public int chapter;
    public List<Integer> verses;
    public String verseId;

    @Override
    public String toString() {
        String ref = book + " " + chapter + ":";
        if (verses != null && verses.size() > 0) {
            boolean inList = false;
            int lastId = 0;
            for (int i = 0; i < verses.size(); i++) {
                if (i == 0) {
                    ref += verses.get(0);
                } else {
                    if (verses.get(i) == verses.get(i - 1) + 1) {
                        inList = true;
                        lastId = verses.get(i);
                        if (i == verses.size() - 1) {
                            ref += "-" + lastId;
                        }
                    } else {
                        if (inList) {
                            ref += "-" + lastId;
                        }
                        ref += "," + verses.get(i);
                    }
                }

            }
        }
        return ref;
    }

    public static Reference fromVerseId(int lang, String verseId) {
        UtilsBible bible = new UtilsBible(lang);
        if (verseId.contains("-")) {
            verseId = verseId.substring(0, verseId.indexOf("-"));
        }

        int increment = 0;
        if (verseId.length() == 9) {
            increment = 1;
        }
        int bookNum = Integer.parseInt(verseId.substring(1, 2 + increment)) - 1;
        if (bible.getBooks().size() < 66) {
            bookNum = bookNum - 39;
        }

        int chap = Integer.parseInt(verseId.substring(2 + increment, 5 + increment));
        int verse = Integer.parseInt(verseId.substring(5 + increment, 8 + increment));

        ArrayList<Integer> verses = new ArrayList();
        verses.add(verse);
        return new Reference(lang, bible.getBook(bookNum), chap, verses);
    }
}
