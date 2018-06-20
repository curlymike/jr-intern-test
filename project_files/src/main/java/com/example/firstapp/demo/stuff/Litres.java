package com.example.firstapp.demo.stuff;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*******
 * - title
 * - description
 * - Author
 * - isbn (no dashes)
 * - print year
 */

public class Litres {

  private boolean status = false;
  private String pageContent;

  private int contentLength = -1;

  private String title = "";
  private String description = "";
  private List<String> authors = new ArrayList<>();
  private String isbn = "";
  private Integer year = 0;

  public Litres(URL url) {
    //loadContent();
    if (fetchContent(url)) {
      extactValues();
    }
  }

  private boolean fetchContent(URL url) {
    try {
      pageContent = Network.fetchURL(url);
      if (pageContent != null) {
        status = true;
        contentLength = pageContent.length();
      }
    } catch (IOException e) {
    }
    return status;
  }

  private void extactValues() {

    Matcher mtch;
    int opts = Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.DOTALL;

    mtch = Pattern.compile("<(\\w+)>(.+?)</\\1>", opts).matcher(pageContent);

    while (mtch.find()) {
      Matcher mtch2 = Pattern.compile("Дата\\s+написания:.*?(\\d{4})", opts).matcher(mtch.group(2));
      while (mtch2.find()) {
        try {
          year = Integer.parseInt(mtch2.group(1));
          break;
        } catch (NumberFormatException e) {
          // Whatever
        }
      }
    }

    //---

    mtch = Pattern.compile("<a[^>]+class=\"biblio_book_author__link\"[^>]*>(.+?)</a>", opts).matcher(pageContent);

    while (mtch.find()) {
      String author = cleanup(Jsoup.parse(mtch.group(1)).text()).trim();
      if (!authors.contains(author)) {
        authors.add(author);
      }
    }

    //---

    mtch = Pattern.compile("<(\\w+)[^>]+itemprop=\"([^\"]+)\"[^>]*>(.+?)</\\1>", opts).matcher(pageContent);

    while (mtch.find()) {
      System.out.println("- " + mtch.group(2) + " - " + Jsoup.parse(mtch.group(3)).text());

      String prop = mtch.group(2);
      if (prop.equals("name") && title.length() == 0) {
        title = cleanup(mtch.group(3)).trim();
      }
      if (prop.equals("description") && description.length() == 0) {
        description = cleanup(mtch.group(3));
      }
      if (prop.equals("isbn") && isbn.length() == 0) {
        Matcher mtch2 = Pattern.compile("[0-9-]+", opts).matcher(mtch.group(3));
        if (mtch2.find()) {
          isbn = mtch2.group(0).replaceAll("[^\\d]", "");
        }
      }
    }

    if (title.length() == 0) {
      mtch = Pattern.compile("<h1[^>]*>(.+?)</h1>", opts).matcher(pageContent);
      if (mtch.find()) {
        title = cleanup(Jsoup.parse(mtch.group(1)).text()).trim();
      }
    }

  }

  //---

  private String cleanup(String str) {
    return Jsoup.parse(str).text().replaceAll("(\\r\\n|\\r|\\n)", " ").replaceAll("\\s+", " ");
  }

  //---

  public boolean status() {
    return status;
  }

  public int getContentLength() {
    return contentLength;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public String getAuthor() {
    if (authors.size() > 0) {
      return authors.get(0);
    }
    return "";
  }

  public String getIsbn() {
    return isbn;
  }

  public Integer getYear() {
    return year;
  }

  //---

  public String toString() {
    return
        "title: " + title + System.lineSeparator() +
        "description: " + description + System.lineSeparator() +
        "author: " + getAuthor() + System.lineSeparator() +
        "authors: " + String.join(", ", authors) + System.lineSeparator() +
        "isbn: " + isbn + System.lineSeparator() +
        "year: " + year + System.lineSeparator();
  }

}
