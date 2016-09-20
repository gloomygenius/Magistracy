package logic;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class Downloader {

    public static void main(String[] args) {
        String url = "https://urs.earthdata.nasa.gov/oauth/authorize/?scope=uid&app_type=401&client_id=e2WVk8Pw6weeLUKZYOxvTQ&response_type=code&redirect_uri=http%3A%2F%2Fhydro1.gesdisc.eosdis.nasa.gov%2Fdata-redirect&state=aHR0cDovL2h5ZHJvMS5zY2kuZ3NmYy5uYXNhLmdvdi9kb2RzL0dMREFTX05PQUgwMjVTVUJQXzNILmFzY2lpP2V2YXBbMDoyXVszODk6MzkyXVs0MTc6NDE4XQ";
        try {
            downloadUsingNIO(url, "C:\\Java\\response.html");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void downloadUsingNIO(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }
}