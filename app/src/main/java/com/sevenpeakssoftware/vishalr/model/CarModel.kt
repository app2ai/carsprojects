package com.sevenpeakssoftware.vishalr.model

data class CarModel(
    val content: List<Content>,
    val serverTime: Int,
    val status: String
)

data class Content(
    val changed: Int,
    val content: List<ContentX>,
    val created: Int,
    val dateTime: String,
    val id: Int,
    val image: String,
    val ingress: String,
    val tags: List<Any>,
    val title: String
)

data class ContentX(
    val description: String,
    val subject: String,
    val type: String
)