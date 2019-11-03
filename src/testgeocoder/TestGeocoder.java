package testgeocoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestGeocoder {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String address = sc.next();
        try {
            String URL = getURL(address);
            System.out.println("URL: " + URL);
            String JSON = getGeocode(URL);
            System.out.println("JSON: " + JSON);
        } catch (Exception ex) {
            Logger.getLogger(TestGeocoder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static String getKey(){
        return "a880cf10-7c51-4526-9e32-517f6e45c777";
    }
    
    public static String getURL(String address) throws Exception{
        String server = "https://geocode-maps.yandex.ru/1.x/";
        String params = "format=json" +
                       "&geocode=" + URLEncoder.encode(address, "UTF-8") +
                       "&apikey=" + getKey();
        return server + "?" + params;
    }
    
    public static String getGeocode(String url) throws Exception{
        URL object = new URL(url);
        HttpURLConnection con = (HttpURLConnection)object.openConnection();
        con.setRequestMethod("GET");
        InputStreamReader stream = new InputStreamReader(con.getInputStream());
        BufferedReader in = new BufferedReader(stream);
        
        String tmp;
        StringBuffer response = new StringBuffer();
        while ((tmp = in.readLine()) != null){
            response.append(tmp);
        }
        in.close();
        stream.close();
        return response.toString();
    }
    
}
