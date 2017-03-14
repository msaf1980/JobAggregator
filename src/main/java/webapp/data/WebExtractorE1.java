/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webapp.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

import java.util.HashMap;

//import java.io.File;
//import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
*/

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;

public class WebExtractorE1 {

    public static final String TMP_DIR = System.getProperty("java.io.tmpdir");
            
    private static final String codepage = "UTF-8";
    private static final String site = "http://zarplata.ru";
    //private final String vacanciesURL = "http://zarplata.ru/api/v1/vacancies";
    private static final String resumesURL = site + "/api/v1/resumes";
    
    private int last = 0;
    private int limit = 25;
    private int count = 0;
    
    //private final String vacanciesRubricURL = "vacancy";
    //----------------------------
    public WebExtractorE1() {
        
    }
    //----------------------------
    public WebExtractorE1(int limit) {
        this.limit = limit;
    }
    //----------------------------
    private String JavaToASCII(String javaStr) {
        StringBuilder output = new StringBuilder();
        int size = javaStr.length();
        int i = 0;
        char[] input = javaStr.toCharArray();
        while (i < size) {
            if (input[i] == '\\' && input[i+1] == 'u' && size >= i + 6) {
                String hex = javaStr.substring(i+2, i+6);
                output.append((char) Integer.parseInt(hex, 16));
                i += 6;
            } else {
                output.append(input[i]);
                i++;
            }
        }
        return output.toString();
    }
    //----------------------------
    public URLConnection openConnection(String url) throws MalformedURLException, IOException {
        URL furl = new URL(url);
        URLConnection conn = furl.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
        conn.setRequestProperty("Accept-Charset", codepage);
        return conn;
    }
    //---------------------------
    public String getURL(String url) throws MalformedURLException, IOException {
        URLConnection conn = openConnection(url);
        BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), codepage));

        String inputLine;
        //save to this filename
        StringBuilder sb = new StringBuilder();
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
        br.close();
   
        return sb.toString();
    }

    //----------------------------
    /*
    public void GetVacancies() throws IOException {
        Integer last = 0;
        Integer limit = 25;
        while (true) {
            Integer count = -1;
            Integer fetch = -1;
            Integer offset = -1;

            String content = getURL(String.format("%s/?limit=%d&offset=%d", vacanciesURL, limit, last));

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(content);

            JsonNode metadataNode = root.path("metadata");
            if (!metadataNode.isMissingNode()) {
                JsonNode queryNode = metadataNode.path("query");
                if (!queryNode.isMissingNode()) {
                    JsonNode resultNode = metadataNode.path("resultset");
                    if (!resultNode.isMissingNode()) {
                        count = resultNode.path("count").asInt();
                        limit = resultNode.path("limit").asInt();
                        offset = resultNode.path("offset").asInt();
                    }
                }
            }
            JsonNode vacanciesNode = root.path("vacancies");
            if (vacanciesNode.isArray()) {
                for (JsonNode vacanciesEntry : vacanciesNode) {
                    Vacancy vacancy = new Vacancy(vacanciesEntry);

                    
                }
            }
            if (fetch <= 0) {
                break;
            }
            last += fetch;
            if (last >= count) {
                break;
            }
            break;
        }
    }
    */
    //----------------------------
    public Resumes getResumes(boolean debug) throws IOException {
        //if (last > 0 && last >= count)
        //    return null;
        
        Resumes resumes = new Resumes(site);
        String content = null;
        //int offset = -1;
        try {
            content = getURL(String.format("%s/?limit=%d&offset=%d", resumesURL, limit, last));
            if (debug) {
                try ( BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(TMP_DIR + File.separator + "resume-" + last + ".txt"), StandardCharsets.UTF_8)) 
                        ) {
                    out.write(JavaToASCII(content));
                } catch  (IOException e) { }
            }
        } catch (IOException e) {
            String error = e.getMessage();
            if (error.contains("response code: 400 for URL"))
                return null;
            else
                throw e;
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(content);
        JsonNode metadataNode = root.path("metadata");
        if (!metadataNode.isMissingNode()) {
            JsonNode queryNode = metadataNode.path("query");
            if (!queryNode.isMissingNode()) {
                JsonNode resultNode = metadataNode.path("resultset");
                if (!resultNode.isMissingNode()) {
                    //count = resultNode.path("count").asInt();
                    //limit = resultNode.path("limit").asInt();
                    //offset = resultNode.path("offset").asInt();
                }
            }
        }

        //resumes.clear();
        resumes.set(root.path("resumes"));

        int fetch = resumes.getResumes().size();
        if (fetch == 0)
            return null;
    
        last += fetch;
        return resumes ;
    }
    //----------------------------
}