package com.github.kylarme.totoapp.totoapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.github.kylarme.totoapp.totoitems.FeedItem;
import com.github.kylarme.totoapp.totoitems.RssItem;
import com.github.kylarme.totoapp.totoparsers.RssParser;
import com.github.kylarme.totoapp.totoproviders.FeedItemsHandler;
import com.github.kylarme.totoapp.totoproviders.RssItemsHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class UpdateRssItemsService extends Service {

    private final static int THREAD_POOL_COUNT = 2;

    private static RssItemsHandler sRssItemsHandler;
    private static FeedItemsHandler sFeedItemsHandler;

    private static ScheduledExecutorService sScheduledExecutorService;

    private static Context sContext;

    private static ScheduledFuture<?> sFutureTask;

    private static int sUpdateTime;
    private static Runnable sUpdateRunnable;

    public static void updateRssItems(final FeedItem feedItem, final ItemUpdateCallback onUpdateItem) {

        if (sScheduledExecutorService != null) {
            if (Utils.isNetworkAvailable(sContext)) {
                sScheduledExecutorService.execute(new UpdateRssItem(feedItem, onUpdateItem));
            }
        }
    }

    public static void setUpdateTime(final int updateTime) {

        if (updateTime != -1) {
            sUpdateTime = updateTime;
        }

        if (sFutureTask != null) {
            sFutureTask.cancel(false);

            sFutureTask = sScheduledExecutorService.scheduleAtFixedRate(sUpdateRunnable, 0, sUpdateTime, TimeUnit.MILLISECONDS);
        }
    }

    private static void updateAllRssItems() {

        ArrayList<FeedItem> feedItems = sFeedItemsHandler.getFeedItems();

        for (FeedItem feedItem : feedItems) {
            updateRssItems(feedItem, new ItemUpdateCallback() {
                @Override
                public void onItemUpdate(boolean success) {

                }
            });

            //TODO: set wait time
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();

        sUpdateTime = Utils.UpdateService.getCurrentUpdateTime(sContext);

        sScheduledExecutorService = Executors.newScheduledThreadPool(THREAD_POOL_COUNT);

        sRssItemsHandler = new RssItemsHandler(this);
        sFeedItemsHandler = new FeedItemsHandler(this);

        sUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                updateAllRssItems();
            }
        };

        sFutureTask = sScheduledExecutorService.scheduleAtFixedRate(sUpdateRunnable, 0, sUpdateTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

//        stopExecutorService();
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    private static class UpdateRssItem implements Runnable {

        private FeedItem mFeedItem;
        private ItemUpdateCallback mOnItemUpdate;

        public UpdateRssItem(FeedItem feedItem, ItemUpdateCallback onItemUpdate) {
            mFeedItem = feedItem;
            mOnItemUpdate = onItemUpdate;
        }

        @Override
        public void run() {
            try {
                RssParser rssParser = new RssParser(mFeedItem);

                rssParser.retrieve(new RssParser.OnRetrieveListener() {
                    @Override
                    public void OnRetrieve(ArrayList<RssItem> rssItems) {

                        if (!rssItems.isEmpty()) {
                            Log.i("UpdateRssItemsService", "Update feed item: " + mFeedItem.getTitle());

                            sRssItemsHandler.deleteRssItems(mFeedItem.getId());

                            if (sRssItemsHandler.addRssItems(rssItems) > 0) {
                                mOnItemUpdate.onItemUpdate(true);
                            } else {
                                mOnItemUpdate.onItemUpdate(false);
                            }
                        } else {
                            mOnItemUpdate.onItemUpdate(false);
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public interface ItemUpdateCallback {
        public void onItemUpdate(boolean success);
    }
}
