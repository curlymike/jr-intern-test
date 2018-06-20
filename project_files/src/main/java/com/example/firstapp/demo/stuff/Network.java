package com.example.firstapp.demo.stuff;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class Network {

  //private static final String UserAgent = "Mozilla/5.0";
  private static final String UserAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:60.0) Gecko/20100101 Firefox/60.0";

  private static final CookieManager cookieManager = new CookieManager();
  private static final AtomicBoolean isInit = new AtomicBoolean(false);

  private static String UserAgent() {
    return UserAgent;
  }

  //----------------------------------
  // This one supports gzip and deflate

  public static String fetchURL(URL url) throws IOException {
    //URL obj = new URL(url);

    synchronized (Network.class) {
      cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
      CookieHandler.setDefault(cookieManager);
    }

    System.out.println("---------------------------");
    System.out.println("Cookies before:");
    for (HttpCookie cookie: cookieManager.getCookieStore().getCookies()) {
      System.out.println("CookieHandler retrieved cookie: " + cookie);
    }
    System.out.println("---------------------------");

    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    // optional default is GET
    // con.setRequestMethod("GET");

    //add request header
    con.setRequestProperty("User-Agent", UserAgent());
    con.setRequestProperty("Accept", "text/html");
    con.setRequestProperty("Accept-Encoding", "gzip, deflate");
    con.setRequestProperty("Accept-Language", "ru");


    int code = con.getResponseCode();

    System.out.println("Network: " + code + " " + con.getResponseMessage());

    if (code != 200) {
      //System.out.println(code + " " + con.getResponseMessage());
      return null;
    }

    //System.out.println(code + " " + con.getResponseMessage());
    //System.out.println(con.getContentEncoding());

    // TODO: Should I take into account content charset?
    // Content-Type: application/json; charset=utf-8
    // Response header

    String contentEncoding = con.getContentEncoding();
    InputStream is = con.getInputStream();
    InputStream cis = null;

    if (contentEncoding == null) {
      cis = is;
    } else if (contentEncoding.equals("gzip")) {
      cis = new GZIPInputStream(is);
    } else if (contentEncoding.equals("deflate")) {
      cis = new InflaterInputStream(is);
    } else {
      //throw new IOException();
      throw new UnsupportedEncodingException();
    }

    StringBuilder sb = new StringBuilder();
    BufferedReader in = new BufferedReader(new InputStreamReader(cis));

    String inputLine;

    while ((inputLine = in.readLine()) != null) {
      //System.out.println(inputLine);
      sb.append(inputLine);
    }

    in.close();

    return sb.toString();

  }

  //----
  // This is th initial version

  public static String fetchURLSimple(URL url) throws IOException {
    //URL obj = new URL(url);

    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    // optional default is GET
    // con.setRequestMethod("GET");

    //add request header
    //con.setRequestProperty("User-Agent", USER_AGENT);
    con.setRequestProperty("User-Agent", UserAgent());

    StringBuilder sb = new StringBuilder();
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

    String inputLine;

    while ((inputLine = in.readLine()) != null) {
      //System.out.println(inputLine);
      sb.append(inputLine);
    }

    in.close();

    return sb.toString();

  }

  //----------------------------------

  public static int downloadFile(String sourceUrl, Path target) throws MalformedURLException, IOException {
    URL url = new URL(sourceUrl);
    HttpURLConnection con = (HttpURLConnection) url.openConnection();

    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    con.setRequestProperty("Accept-Encoding", "gzip, deflate");
    // Accept-Encoding: gzip, deflate

    System.out.println(con.getResponseCode() + " " + con.getResponseMessage());
    //System.out.println(con.getResponseMessage());
    System.out.println(con.getContentEncoding());
    //System.out.println(con.getHeaderField("test"));

    String contentEncoding = con.getContentEncoding();
    InputStream cis = null;

    int m = 0;
    try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(target));
         InputStream is = con.getInputStream()) {

      //ByteArrayInputStream bais = new ByteArrayInputStream(is);
      //InflaterInputStream iis = new InflaterInputStream(is);
      //GZIPInputStream cis = new GZIPInputStream(is);

      if (contentEncoding == null) {
        cis = is;
      } else if (contentEncoding.equals("gzip")) {
        cis = new GZIPInputStream(is);
      } else if (contentEncoding.equals("deflate")) {
        cis = new InflaterInputStream(is);
      } else {
        //throw new IOException();
        throw new UnsupportedEncodingException();
      }

      //InflaterOutputStream ios = new InflaterOutputStream();

      long len = con.getContentLengthLong();
      byte[] buffer = new byte[1024 * 64];
      //is.available();
      int n;
      while ((n = cis.read(buffer)) > -1) {
        bos.write(buffer, 0, n);
        m += n;
      }

      System.out.println(m + "/" + len);

      //cis.close();
    } finally {
      // This block executed no matter what.
      if (cis != null) {
        //System.out.println("Finally!");
        cis.close();
      }
    }

    return m;
  }

}
