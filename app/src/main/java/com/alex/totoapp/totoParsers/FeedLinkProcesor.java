package com.alex.totoapp.totoParsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public abstract class FeedLinkProcesor {

    public static String getRssLinkFromUrl(String url) {
        String rssUrl = null;

        url = resolveMalformedUrl(url);

        if (url == null)
            return null;

        try {
            Document doc = Jsoup.connect(url).get();

            Elements linksRss = doc.select("link[type=application/rss+xml]");
            if (linksRss.size() > 0) {
                rssUrl = linksRss.get(0).attr("href").toString();
            } else {
                Elements linksAtom = doc.select("link[type=application/atom+xml]");
                if (linksAtom.size() > 0) {
                    rssUrl = linksAtom.get(0).attr("href").toString();
                } else {
                    // worst case scenario
                    rssUrl = getFeedByLinks(doc);
                }
            }

            return rssUrl;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static boolean isRssLink(String url) {

        if (url == null || url.equals(""))
            return false;



        return false;
    }

    private static String resolveMalformedUrl(String url) {

        if (url == null || url.equals(""))
            return null;

        if (url.startsWith("http://"))
            return url;

        return "http://" + url;
    }

    private static String getFeedByLinks(Document doc) {
        String rssUrl = null;

        Elements links = doc.select("a[href]");

        if(links.size() > 0) {
            for(Element link: links) {
                String href =link.attr("abs:href");
                if(href.contains("feed")) {
                    rssUrl = href.trim();
                } else {
                    if(href.contains("rss")) {
                        rssUrl = href.trim();
                    }
                }
            }
        } else {
            return rssUrl;
        }

        return rssUrl;
    }
}
