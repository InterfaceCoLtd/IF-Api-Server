package xyz.interfacesejong.interfaceapi.sejongAuth;

import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.interfacesejong.interfaceapi.sejongAuth.dto.StudentDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class sejongAuth {

    private final Logger LOGGER = LoggerFactory.getLogger(sejongAuth.class);

    public StudentDTO getUserAuthInfos(String studentId, String password) throws IOException {

        String jsessionId = setJsessionId();
        sendPost(studentId, password, jsessionId);

        return sendGet(jsessionId);
    }

    public String setJsessionId() throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        Request request = new Request.Builder()
                .url("http://classic.sejong.ac.kr")
                .build();

        Response response = client.newCall(request).execute();

        LOGGER.info("[setJsessionId] " + response);

        Headers headers = response.headers();

        for (String name : headers.names()) {
            List<String> values = headers.values(name);
            if ("Set-Cookie".equalsIgnoreCase(name)) {
                for (String value : values) {
                    if (value.contains("JSESSIONID")) {
                        String jsessionId = extractJSessionID(value);
                        System.out.println("JSESSIONID: " + jsessionId);
                        return jsessionId;
                    }
                }
            }
        }


        return null;
    }

    public String extractJSessionID(String cookieValue) {

        String[] parts = cookieValue.split(";");

        for (String part : parts) {
            part = part.trim();
            if (part.startsWith("JSESSIONID=")) {
                return part.substring("JSESSIONID=".length());
            }
        }

        return null;
    }

    public void sendPost(String studentId, String password, String jsessionId) throws IOException{

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", studentId)
                .addFormDataPart("password", password)
                .build();

        Request request = new Request.Builder()
                .url("https://classic.sejong.ac.kr/userLogin.do?userId=" + studentId + "&password=" + password)
                .method("POST", body)
                .addHeader("Cookie", "JSESSIONID=" + jsessionId)
                .build();

        try (Response response = client.newCall(request).execute()) {
            LOGGER.info("[sendPost] " + response);
        }

    }

    public StudentDTO sendGet(String jsessionId) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();

        Request request = new Request.Builder()
                .url("http://classic.sejong.ac.kr/userCertStatus.do?menuInfoId=MAIN_02_05")
                .addHeader("Cookie", "JSESSIONID=" + jsessionId)
                .build();

        try (Response response = client.newCall(request).execute()) {

            LOGGER.info("[sendGet] " + response);

            if (response.body() != null) {
                return extractDataFromHtml(response.body().string());
            } else
                return null;
        }
    }

    public StudentDTO extractDataFromHtml(String html) {
        List<String> dataList = new ArrayList<>();
        Document doc = Jsoup.parse(html);

        Elements elements = doc.select("div.contentWrap li dl dd");

        for (Element element : elements) {
            dataList.add(element.text());
        }

        return StudentDTO.builder()
                .major(dataList.get(0))
                .studentId(dataList.get(1))
                .studentName(dataList.get(2))
                .grade(dataList.get(3))
                .enrolled(dataList.get(4))
                .build();
    }
}
