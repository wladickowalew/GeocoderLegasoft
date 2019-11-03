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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TestGeocoder {

    public static void main(String[] args) {
        MyWindow window = new MyWindow();
        window.setVisible(true);
        //Scanner sc = new Scanner(System.in);
        //String address = sc.next();
    }
    
    public static String[] getArrayForAddress(String address){
        try {
            String URL = getURL(address);
            System.out.println("URL: " + URL);
            String JSON = getGeocode(URL);
            System.out.println("JSON: " + JSON);
            JSONArray jarr = getArrayFromJSON(JSON);
            String[] arr = parse2Array(jarr);
            return arr;
        } catch (Exception ex) {
            Logger.getLogger(TestGeocoder.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static String getKey(){
        return "a880cf10-7c51-4526-9e32-517f6e45c777";
    }
    
    public static String getURL(String address) throws Exception{
        String server = "https://geocode-maps.yandex.ru/1.x/";
        String params = "format=json" +
                       "&results=100"+
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
    
    public static JSONArray getArrayFromJSON(String jsonString) throws JSONException{
          JSONObject obj = new JSONObject(jsonString);
          JSONObject res = obj.getJSONObject("response");
          JSONObject GeoCol = res.getJSONObject("GeoObjectCollection");
          JSONArray ans = GeoCol.getJSONArray("featureMember");
          return ans;
    }
    
    public static String[] parse2Array(JSONArray j_array) throws JSONException{
        int n = j_array.length();
        String[] ans = new String[n];
        for (int i = 0; i < n; i++){
            JSONObject element = j_array.getJSONObject(i);
            JSONObject GO = element.getJSONObject("GeoObject");
            String name = GO.getString("name");
            String descr = "";
            try{
                descr = GO.getString("description");
            }catch(JSONException ex){
                System.out.println("Object " + name + " has no attribute desription");
            }
            JSONObject point = GO.getJSONObject("Point");
            String pos = point.getString("pos");
            String line = name + " " + descr + " coords:" + pos;
            ans[i] = line;
        } 
        return ans;
    }
    
}
