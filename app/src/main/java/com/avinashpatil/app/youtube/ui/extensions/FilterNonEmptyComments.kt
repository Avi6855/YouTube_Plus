package com.avinashpatil.app.youtube.ui.extensions

import com.avinashpatil.app.youtube.api.obj.Comment

fun List<Comment>.filterNonEmptyComments(): List<Comment> {
    return filter { !it.commentText.isNullOrEmpty() }
}
