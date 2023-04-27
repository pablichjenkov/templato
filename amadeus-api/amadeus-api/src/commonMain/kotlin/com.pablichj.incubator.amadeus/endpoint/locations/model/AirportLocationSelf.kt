package com.pablichj.incubator.amadeus.endpoint.locations.model

@kotlinx.serialization.Serializable
data class AirportLocationSelf (
    val href: String,
    val methods: List<String> // Only Http Methods Allowed
)