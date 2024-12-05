package com.smartshop.data.model

import com.google.firebase.database.ServerValue

data class ListitemData(
    val id: String,
    val name: String,
    val qty: Double,
    val unit: String,
    val delete: Boolean,
    val isCheck: Boolean,
    val listId: String,
    val createdAt: Any?,
    val updatedAt: Any?
) {
    constructor() : this("","", 1.0, "", false, false, "", ServerValue.TIMESTAMP, ServerValue.TIMESTAMP)
}