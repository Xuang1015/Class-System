package util;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class URLCommunicate {

    public static String postFile(String address, Map<String, Object> param, File file) throws IOException {
        // TODO:研究okhttp和这个代码的意思
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(5, TimeUnit.SECONDS).readTimeout(5, TimeUnit.SECONDS).build();
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        param.forEach((k, v) -> multipartBodyBuilder.addFormDataPart(k, String.valueOf(v)));
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        multipartBodyBuilder.addFormDataPart("file", randomString(), requestBody);
        MultipartBody multipartBody = multipartBodyBuilder.build();
        Request request = new Request.Builder().url(address).post(multipartBody).build();
        Call call = client.newCall(request);
        return call.execute().body().string();
    }

    private static String randomString() {
        Random random = new Random();
        return Long.toHexString(random.nextLong()) + Long.toHexString(random.nextLong());
    }

    public static JSONObject post(String address, Map<String, Object> param) throws IOException, JSONException {
        // link
        URL url = new URL(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type","");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        // output
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(DataTypeChange.map2String(param).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        System.out.printf("URL post:%s\tparams:%s\tresponse code:%d%n", address, param, connection.getResponseCode());
        // input
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder input = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            input.append(line).append("\n");
        }
        // close
        outputStream.close();
        bufferedReader.close();
        JSONObject jsonObject = new JSONObject(input.toString());
        System.out.println(jsonObject);
        return jsonObject;
    }

    public static JSONObject get(String address, Map<String, Object> param) throws IOException, JSONException {
        // link
        URL url = new URL(address + "?" + DataTypeChange.map2String(param));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        // input
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder input = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            input.append(line).append("\n");
        }
        // close
        bufferedReader.close();
        JSONObject jsonObject = new JSONObject(input.toString());
        System.out.println(jsonObject);
        return jsonObject;
    }

    public static String getFile(String address, Map<String, Object> param) throws IOException, JSONException {
        // link
        URL url = new URL(address + "?" + DataTypeChange.map2String(param));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
        // input
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder input = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            input.append(line).append("\n");
        }
        // close
        bufferedReader.close();
        return input.toString();
    }
}
