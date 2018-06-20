package com.example.firstapp.demo;

import com.example.firstapp.demo.model.Book;

import java.util.List;

public class Utility {
    public static String chop(String str, int maxLength) {
        if (str.length() > maxLength) {
            return str.substring(0, maxLength - 3) + "...";
        }
        return str;
    }

    public static void chopDescription(List<Book> books, int maxLength) {
        for (Book book : books) {
            book.setDescription(Utility.chop(book.getDescription(), 80));
        }
    }

}
