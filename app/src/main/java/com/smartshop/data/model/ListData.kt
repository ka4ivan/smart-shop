package com.smartshop.data.model

import com.google.firebase.database.ServerValue

data class ListData(
    val id: String,
    val name: String,
    val userId: String,
    val delete: Boolean,
    val createdAt: Any?,
    val updatedAt: Any?
) {
    constructor() : this("","", "", false, ServerValue.TIMESTAMP, ServerValue.TIMESTAMP)
}