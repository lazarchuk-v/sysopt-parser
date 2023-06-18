package Parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static Elements parseUrl(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        return document.select(".product-variant select option");
    }

    public static void main(String[] args) {
        String sitemapUrl = "https://www.systopt.com.ua/sitemap-0.xml";
        String csvFilePath = "items.csv";

        try {
            Document sitemap = Jsoup.connect(sitemapUrl).get();
            Elements urlElements = sitemap.select("url loc:contains(/item-)");

            FileWriter csvWriter = new FileWriter(csvFilePath, true);

            int count = 0;

            for (Element urlElement : urlElements) {
                String url = urlElement.text();
                System.out.println(url);
                Elements productElements = parseUrl(url);

                for (Element productElement : productElements) {
                    String productInfo = url + "///" + productElement.selectFirst("#product-title").text() + "///" +
                            productElement.selectFirst("option").text() + "///" +
                            productElement.selectFirst("#product_price").text() + "///SKU" +
                            productElement.selectFirst(".product-sku label").text();
                    System.out.println(productInfo + "info");
                    csvWriter.append(productInfo).append("\n");
                }

                count++;

                if (count >= 10) {
                    break;
                }
            }

            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
