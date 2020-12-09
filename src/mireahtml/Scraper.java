package mireahtml;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Scraper {

    private ArrayList<String> imgAbsUrls;
    private String url;

    Scraper(String url) {
        this.url = url;
    }

    public ArrayList<String> getImgAbsUrls() {
        imgAbsUrls = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Elements imgElements = doc.select("img");
            for (int i = 0; i < imgElements.size()-2; i++) {
                Element e = imgElements.get(i);
                if(e.hasAttr("data-src")){
                    imgAbsUrls.add(e.absUrl("data-src"));
                }
                else if(e.hasAttr("src")) {
                    imgAbsUrls.add(e.absUrl("src"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgAbsUrls;
    }

    public String parseName(String url) {
        String[] buff = url.split("/");
        if (buff.length > 0) {
            return buff[buff.length - 1];
        }
        else return null;
    }

    public void download(String url, String filePath) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(filePath + parseName(url))) {
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
