package com.pablichj.incubator.amadeus.endpoint.offers.flight.model

@kotlinx.serialization.Serializable
data class Departure (
    val iataCode: String,
    val terminal: String = "",
    val at: String
)