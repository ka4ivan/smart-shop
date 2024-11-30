package com.smartshop.data.model

//import com.google.firebase.Timestamp

data class ListitemData(
    val name: String,
    val qty: Float,
    val unit: String,
    val isDelete: Boolean,
    val isCheck: Boolean,
//    val createdAt: Timestamp,
)