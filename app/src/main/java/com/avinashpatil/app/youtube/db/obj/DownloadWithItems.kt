package com.avinashpatil.app.youtube.db.obj

import androidx.room.Embedded
import androidx.room.Relation
import com.avinashpatil.app.youtube.enums.FileType
import com.avinashpatil.app.youtube.ui.fragments.DownloadTab

data class DownloadWithItems(
    @Embedded val download: Download,
    @Relation(
        parentColumn = "videoId",
        entityColumn = "videoId"
    )
    val downloadItems: List<DownloadItem>,
    @Relation(
        parentColumn = "videoId",
        entityColumn = "videoId"
    )
    val downloadChapters: List<DownloadChapter> = emptyList()
)

fun List<DownloadWithItems>.filterByTab(tab: DownloadTab) = filter { dl ->
    when (tab) {
        DownloadTab.AUDIO -> {
            dl.downloadItems.any { it.type == FileType.AUDIO } && dl.downloadItems.none { it.type == FileType.VIDEO }
        }

        DownloadTab.VIDEO -> {
            dl.downloadItems.any { it.type == FileType.VIDEO }
        }
    }
}