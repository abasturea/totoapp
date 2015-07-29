package com.github.kylarme.totoapp.totoparsers;

import android.util.Log;

import com.github.kylarme.totoapp.totoitems.FeedItem;
import com.github.kylarme.totoapp.totoitems.RssItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RssParser {

    private static final String TAG = "RssParser";

    private static final String IMAGE_GET_REGEX = "img[src~=([^\\s]+(\\.(?i)(jpg|png))$)";
    private static final int DESCRIPTION_FORMAT_DOT_COUNT = 5;

    private Document mDoc;

    private ArrayList<RssItem> mRssItems;

    private FeedItem mFeedItem;

    private OnRetrieveListener mOnRetrieveListener;

    public RssParser(final FeedItem feedItem) {
        mFeedItem = feedItem;
        mRssItems = new ArrayList<RssItem>();
    }

    public void retrieve(OnRetrieveListener onRetrieveListener) throws IOException {

        if (onRetrieveListener == null) {
            return;
        }

        mOnRetrieveListener = onRetrieveListener;

        connect();

        ArrayList<RssItem> rssItems;

        rssItems = getItems();

        if (rssItems.isEmpty()) {
            rssItems = getEntries();
        }

        mOnRetrieveListener.OnRetrieve(rssItems);
    }

    private ArrayList<RssItem> getItems() {

        Elements elements = mDoc.select("item");

        for (Element element : elements) {

            mRssItems.add(getItem(element));

        }

        return mRssItems;
    }

    private ArrayList<RssItem> getEntries() {

        Elements elements = mDoc.select("entry");

        for (Element element : elements) {

            mRssItems.add(getItem(element));

        }

        return mRssItems;
    }

    private void connect() throws IOException {

        String xml = Jsoup.connect(mFeedItem.getLink())
                .followRedirects(true)
                .ignoreHttpErrors(true)
                .userAgent("Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)")
                .referrer("http://www.google.com")
                .timeout(1000 * 5)
                .execute()
                .body();

        mDoc = createCleanDocument(Parser.xmlParser().parseInput(xml, ""));
    }

    private RssItem getItem(Element element) {

        final RssItem rssItem = new RssItem();

        rssItem.setHeadline(getTitle(element));
        rssItem.setLink(getLink(element));
        rssItem.setImageLink(getImageLink(element));
        rssItem.setDescription(getDescription(element));
        rssItem.setFeedId(mFeedItem.getId());

        return rssItem;
    }

    private String getTitle(Element element) {

        String title;

        title = element.select("title").text();

        return title;
    }

    private String getLink(Element element) {

        String link;

        link = element.select("link").text();
        if (isEmpty(link)) {
            link = element.select("link").attr("href");
        }

        return link;
    }

    private String getImageLink(Element element) {

        String imageUrl = "";

        Elements imageElements = element.select(IMAGE_GET_REGEX);
        Element imageElement = imageElements.first();

        if (imageElement != null) {
            imageUrl = imageElement.absUrl("src").trim();
        }

        if (isEmpty(imageUrl)) {
            imageUrl = element.select("enclosure").attr("url");
        }

        return imageUrl;
    }

    private String getDescription(Element element) {

        Elements description;

        description = element.select("description");

        if (description.size() == 0) {
            description = element.select("content");
        }

        description.select("a.href,script,img,image,iframe").remove();

        Log.i("RssParser", description.html());

        return description.html();
    }

//    private String getDescription(Element element) {
//
//        Elements description;
//
//        StringBuilder buffer = new StringBuilder();
//
//        description = element.select("description");
//
//        if (description.size() == 0) {
//            description = element.select("content");
//        }
//
//        Elements descElements = description.select("p");
//
//        if (descElements.size() == 0) {
//            buffer.append("   ").append(description.text()).append("\n\n");
//        }
//
//        for (Element el : descElements) {
//            el.select("a").remove();
//
//            buffer.append("   ").append(el.text()).append("\n\n");
//        }
//
//        return buffer.toString();
//    }

    private String formatDescription(String description) {

        // the pattern we want to search for
        Pattern p = Pattern.compile("(?i)(<p.*?>)(.+?)(</p>)");
        Matcher m = p.matcher(description);

        // if we find a match, get the group
        if (m.find()) {
            // get the matching group
            String codeGroup = m.group(1);

            // print the group
            System.out.format("'%s'\n", codeGroup);
        }

        return description;
    }

/*    private String formatDescription(String description) {

        int count = 0;
        StringBuilder buffer = new StringBuilder();

        buffer.append("   ");

        for (int i = 0; i < description.length(); i++) {
            char character = description.charAt(i);
            char nextCharacter = ' ';

            if (i != description.length() - 1) {
                nextCharacter = description.charAt(i + 1);
            }

            if (nextCharacter == ' ') {
                switch (character) {
                    case '.':
                        count++;
                        break;

                    case '?':
                        count++;
                        break;

                    case '!':
                        count++;
                        break;
                }
            }

            buffer.append(character);

            if (count == DESCRIPTION_FORMAT_DOT_COUNT) {
                buffer.append("\n\n");
                buffer.append("   ");

                count = 0;
            }
        }

        return buffer.toString();
    }*/

    private String removeTags(String description) {

        description = description.replaceAll("[\\[<](.*?)[>\\]]", " ");

        return description;
    }

    private Document createCleanDocument(Document document) {

        String docString = clean(document.toString());

        return Parser.xmlParser().parseInput(docString, "");
    }

    private String clean(String xml) {

        xml = xml.replaceAll("&lt;", "<");
        xml = xml.replaceAll("&gt;", ">");
        xml = xml.replaceAll("&amp;", "&");
        xml = xml.replaceAll("&quot;", "\"");
        xml = xml.replaceAll("&nbsp;", " ");

        return xml;
    }

    private boolean isEmpty(String value) {

        return value.trim().equals("");
    }

    public interface OnRetrieveListener {
        void OnRetrieve(ArrayList<RssItem> rssItems);
    }
}