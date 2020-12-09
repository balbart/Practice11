package mireahtml;

import org.jsoup.Jsoup;

import java.io.File;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scraper scraper = new Scraper("https://www.mirea.ru");
        ArrayList<String> urls = scraper.getImgAbsUrls();
        for (String s : urls) {
            scraper.download(s,"images/");
        }
        File path = new File("images\\");
        String[] files = path.list();
        for (String s : files) {
            System.out.println(s);
        }
    }
}
