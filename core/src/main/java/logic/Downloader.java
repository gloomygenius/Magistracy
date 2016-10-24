package logic;

import com.sun.org.apache.xml.internal.security.utils.Base64;
import logic.errors.LinkParseException;
import lombok.SneakyThrows;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Downloader {
    static boolean frameControl=false;
    static JTextArea status;
    public static String getData(String link, JTextArea linkField) {
        status=linkField;
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        if (frameControl==true){

        }
        return (
                getResponseFromURL(
                        parseLink(
                                getResponseFromURL(
                                        getResponseFromURL(link)))));
    }

    @SneakyThrows
    private static String getAuthResponce(String address) {
        status.setText("Открываем авторизованное соединение: " + address);
        URL object = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) object.openConnection();
        connection.setReadTimeout(60 * 1000);
        connection.setConnectTimeout(60 * 1000);
        String authorization = "master2017:Magistracy2017";
        String encodedAuth = "Basic " + Base64.encode(authorization.getBytes());
        connection.setRequestProperty("Authorization", encodedAuth);
        return getTextResponse(connection);
    }

    private static String parseLink(String responce) {
        status.setText("Парсим ответ");
        Pattern pattern = Pattern.compile("a href=\\\"(.+)\\\"");
        Matcher matcher = pattern.matcher(responce);
        String link;
        if (matcher.find()) {
            link = matcher.group(1);
        } else {
            throw new LinkParseException();
        }
        return link;
    }

    @SneakyThrows
    private static String getResponseFromURL(String url) {

        status.setText("Открываем соединение: " + url);
        URL object = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) object.openConnection();
        connection.setReadTimeout(300 * 1000);
        connection.setConnectTimeout(300 * 1000);

        int responseCode = connection.getResponseCode();
        switch (responseCode) {
            case 200:
                status.setText("Ответ сервера 200");
                status.setText("Файл успешно загружен");
                return getTextResponse(connection);
            case 302:
                status.setText("Ответ сервера 302");
                return (parseLink(getTextResponse(connection)));
            case 401:
                status.setText("Ответ сервера 401");
                return getAuthResponce(url);
            default:
                status.setText("Ответ сервера "+responseCode);
                return String.valueOf(responseCode);

        }
    }

    private static String getTextResponse(HttpURLConnection connection) {
        StringBuilder builder = new StringBuilder();

        String encoding = connection.getContentEncoding() == null ? "UTF-8"
                : connection.getContentEncoding();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream(), encoding))) {
            String nextString;
            while ((nextString = reader.readLine()) != null) {
                builder.append(nextString + "\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}