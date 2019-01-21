package test;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HastebinAPI {

    private static final JsonParser JSON_PARSER = new JsonParser();

    public String upload(String content) {
        String result;
        try {
            byte[] postData = content.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            String request = "https://hastebin.com/documents";
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            wr.write(postData);
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String json = ((BufferedReader) in).readLine();

            JsonElement jsonElement = JSON_PARSER.parse(json);
            if (!jsonElement.getAsJsonObject().has("key")) {
                System.out.println("Cannot uploud text!");
                return null;
            }

            String key = jsonElement.getAsJsonObject().get("key").getAsString();
            result = "https://hastebin.com/" + key;

        } catch (Exception ex) {
            System.out.println("Cannot uploud text: " + ex.getMessage());
            return null;
        }

        return result;
    }

}
