package com.tejas.bookhub.model

import androidx.annotation.VisibleForTesting

data class Book(
    val bookId:String,
    val bookName:String,
    val bookAuthor:String,
    val bookRating:String,
    val bookPrice:String,
    val bookImage:String
)