# TotoApp 
  This app is intended to those who like to follow blogs, news websites, pretty much everything with support for rss. They can have all the data structured as they please and access it in a more simple and less time consuming manor, than accessing every website one by one. 
The app will allow the user, to add a feed or create his on personalized feeds by combining different websites posts into one feed, and categorize them as he wishes. 

  This is a Android rss feed reader application, made to support mobile devices with Android 4.0.3 to the latest one, at the moment of creating this readme, 5.0. At this point, the app is in a crude, unfinished state, allowing anyone who uses it to add a feed, and access the posts provided. The feed url has to be exactly the one on wich the feed is shown (example: http://rss.cnn.com/rss/edition.rss). All added feeds can be seen by swapping to the right, or tapping the upper left shown name. The posts show up as the title of the post to wich it points to. By tapping it the app will open the phones browser and show the content on the targeted website.

  The app, has two different item types, a FeedItem for the websites from wich the posts are pulled, and a RssItem for the post provided. Adding a feed is made through a fragment, by insertting the feed details in a specialized content provider, 
  
    ContentValues cv = new ContentValues();
  
    cv.put(FeedContentProvider.FEED_TITLE, ((EditText)rootView.findViewById(R.id.feed_title_edit_text)).getText().toString());
    cv.put(FeedContentProvider.FEED_URL, ((EditText)rootView.findViewById(R.id.feed_url_edit_text)).getText().toString());
  
    Uri insertedUri = getActivity().getContentResolver().insert(FeedContentProvider.CONTENT_URI, cv);
 
  and pulled by a loader when requested.
  
  The same is done with the RssItem types. When requested, the items will be provided by the getItems function call:

    boolean insideItem = false;
    int eventType = xpp.getEventType();
  
    String headline = "";
    String link = "";
  
    while (eventType != XmlPullParser.END_DOCUMENT) {
        if (eventType == XmlPullParser.START_TAG) {
  
            if (xpp.getName().equalsIgnoreCase("item")) {
                insideItem = true;
            } else if (xpp.getName().equalsIgnoreCase("title")) {
                if (insideItem)
                    headline = xpp.nextText();
            } else if (xpp.getName().equalsIgnoreCase("link")) {
                if (insideItem)
                    link = xpp.nextText();
            }
        } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {
            RssItem item = new RssItem(headline, link);
            rssItems.add(item);
            Log.i(TAG, "Item loaded:" + item);
            insideItem = false;
        }
        eventType = xpp.next();
    }
    
  There is only one Activity, a main activity, that contains a app drawer for displaying all the feeds in the content provider. The other elements are stored in fragments, and shown by the displayFragment function:
  
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        
  
  
