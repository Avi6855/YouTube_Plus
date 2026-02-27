package com.avinashpatil.app.youtube.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.avinashpatil.app.youtube.db.dao.CustomInstanceDao
import com.avinashpatil.app.youtube.db.dao.DownloadDao
import com.avinashpatil.app.youtube.db.dao.LocalPlaylistsDao
import com.avinashpatil.app.youtube.db.dao.LocalSubscriptionDao
import com.avinashpatil.app.youtube.db.dao.PlaylistBookmarkDao
import com.avinashpatil.app.youtube.db.dao.SearchHistoryDao
import com.avinashpatil.app.youtube.db.dao.SubscriptionGroupsDao
import com.avinashpatil.app.youtube.db.dao.SubscriptionsFeedDao
import com.avinashpatil.app.youtube.db.dao.WatchHistoryDao
import com.avinashpatil.app.youtube.db.dao.WatchPositionDao
import com.avinashpatil.app.youtube.db.obj.CustomInstance
import com.avinashpatil.app.youtube.db.obj.Download
import com.avinashpatil.app.youtube.db.obj.DownloadChapter
import com.avinashpatil.app.youtube.db.obj.DownloadItem
import com.avinashpatil.app.youtube.db.obj.DownloadPlaylist
import com.avinashpatil.app.youtube.db.obj.DownloadPlaylistVideosCrossRef
import com.avinashpatil.app.youtube.db.obj.DownloadSponsorBlockSegment
import com.avinashpatil.app.youtube.db.obj.LocalPlaylist
import com.avinashpatil.app.youtube.db.obj.LocalPlaylistItem
import com.avinashpatil.app.youtube.db.obj.LocalSubscription
import com.avinashpatil.app.youtube.db.obj.PlaylistBookmark
import com.avinashpatil.app.youtube.db.obj.SearchHistoryItem
import com.avinashpatil.app.youtube.db.obj.SubscriptionGroup
import com.avinashpatil.app.youtube.db.obj.SubscriptionsFeedItem
import com.avinashpatil.app.youtube.db.obj.WatchHistoryItem
import com.avinashpatil.app.youtube.db.obj.WatchPosition

@Database(
    entities = [
        WatchHistoryItem::class,
        WatchPosition::class,
        SearchHistoryItem::class,
        CustomInstance::class,
        LocalSubscription::class,
        PlaylistBookmark::class,
        LocalPlaylist::class,
        LocalPlaylistItem::class,
        Download::class,
        DownloadItem::class,
        DownloadChapter::class,
        DownloadSponsorBlockSegment::class,
        DownloadPlaylist::class,
        DownloadPlaylistVideosCrossRef::class,
        SubscriptionGroup::class,
        SubscriptionsFeedItem::class
    ],
    version = 22,
    autoMigrations = [
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11),
        AutoMigration(from = 16, to = 17),
        AutoMigration(from = 18, to = 19),
        AutoMigration(from = 19, to = 20),
        AutoMigration(from = 20, to = 21)
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Watch History
     */
    abstract fun watchHistoryDao(): WatchHistoryDao

    /**
     * Watch Positions
     */
    abstract fun watchPositionDao(): WatchPositionDao

    /**
     * Search History
     */
    abstract fun searchHistoryDao(): SearchHistoryDao

    /**
     * Custom Instances
     */
    abstract fun customInstanceDao(): CustomInstanceDao

    /**
     * Local Subscriptions
     */
    abstract fun localSubscriptionDao(): LocalSubscriptionDao

    /**
     * Bookmarked Playlists
     */
    abstract fun playlistBookmarkDao(): PlaylistBookmarkDao

    /**
     * Local playlists
     */
    abstract fun localPlaylistsDao(): LocalPlaylistsDao

    /**
     * Downloads
     */
    abstract fun downloadDao(): DownloadDao

    /**
     * Subscription groups
     */
    abstract fun subscriptionGroupsDao(): SubscriptionGroupsDao

    /**
     * Locally cached subscription feed
     */
    abstract fun feedDao(): SubscriptionsFeedDao
}