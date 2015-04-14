package com.alex.totoapp.totoparsers;

import com.alex.totoapp.totoitems.FeedLinkItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FeedLinkParser {

    private Document mDoc = null;
    private String mFeedLink = null;

    private static String resolveMalformedUrl(String url) {

        if (url == null)
            return null;

        if (url.equals(""))
            return null;

        url = url.toLowerCase();

        if (url.startsWith("http://"))
            return url;

        return "http://" + url;
    }

    public boolean connectToUrl(String feedLink) {

        if (feedLink == null) {
            return false;
        }

        if (feedLink.equals("")) {
            return false;
        }

        mFeedLink = resolveMalformedUrl(feedLink.trim());

        try {
            mDoc = Jsoup.connect(mFeedLink)
                    .followRedirects(true)
                    .ignoreHttpErrors(true)
                    .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                    .referrer("http://www.google.com")
                    .timeout(1000 * 5)
                    .get();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<String> getAllLinks() {

        if (mDoc == null) {
            return null;
        }

        ArrayList<String> links = new ArrayList<String>();

        Elements elementsLinks = mDoc.select("a[href]");

        if (elementsLinks.size() > 0) {
            for (Element elementsLink : elementsLinks) {
                String href = elementsLink.attr("abs:href");
                links.add(href);
            }
            return links;
        }

        return null;
    }

    public ArrayList<FeedLinkItem> getAllRssLinks(ArrayList<String> links) {

        if (links == null)
            return null;

        if (links.size() == 0)
            return null;

        ArrayList<FeedLinkItem> rssLinks = new ArrayList<FeedLinkItem>();

        for (String link : filterLinks(links)) {
            if (isFeedLink(link)) {
                rssLinks.add(new FeedLinkItem(link, link));
            }
        }

        return rssLinks;
    }

    public ArrayList<FeedLinkItem> getAllRssLinks() {

        if (mDoc == null)
            return null;

        ArrayList<FeedLinkItem> rssLinks = new ArrayList<FeedLinkItem>();

        Elements linksRss = mDoc.select("link[type=application/rss+xml]");
        if (linksRss.size() > 0) {
            for (Element link : linksRss) {
                rssLinks.add(new FeedLinkItem(link.attr("href"), link.attr("href")));
            }
        } else {
            Elements linksAtom = mDoc.select("link[type=application/atom+xml]");
            if (linksAtom.size() > 0) {
                for (Element link : linksAtom) {
                    rssLinks.add(new FeedLinkItem(link.attr("href"), link.attr("href")));
                }
            }
        }

        return rssLinks;
    }

    public boolean isFeedLink(String link) {

        if (link == null)
            return false;

        if (link.equals(""))
            return false;

        link = resolveMalformedUrl(link);

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
            String contentType = connection.getContentType();

            if (contentType == null) {
                return false;
            }

            contentType = contentType.trim().toLowerCase();

            if (contentType.equals("text/xml; charset=utf-8")) {
                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private ArrayList<String> filterLinks(ArrayList<String> links) {

        if (links == null)
            return null;

        if (links.size() == 0)
            return null;

        ArrayList<String> rssLinks = new ArrayList<String>();

        for (String link : links) {
            if (link.contains("feed")) {
                rssLinks.add(link.trim());
            } else {
                if (link.contains("rss")) {
                    rssLinks.add(link.trim());
                } else {
                    if (link.contains("atom")) {
                        rssLinks.add(link.trim());
                    }
                }
            }
        }

        return rssLinks;
    }
}
