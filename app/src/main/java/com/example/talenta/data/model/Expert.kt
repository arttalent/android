package com.example.talenta.data.model


data class Expert(
    val id: String = "",
    val person: Person = Person(),
    val reviews: Int = 0,
    val location: String = "",
    val rating: Int = 0,
    val profession: String = "",
    val followers: Long = 0

) {
    constructor() : this(
        id = "",
        person = Person(),
        reviews = 0,
        location = "",
        rating = 0,
        profession = "",
        followers = 0
    )
}