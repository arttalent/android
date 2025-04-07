package com.example.talenta.data.model


data class Expert(
    val id: String = "",
    val person: Person = Person(),
    val reviews: Int = 0,
    val location: String = "",
    val rating: Int = 0,
    val profession: String = "",
    val followers: Long = 0
)