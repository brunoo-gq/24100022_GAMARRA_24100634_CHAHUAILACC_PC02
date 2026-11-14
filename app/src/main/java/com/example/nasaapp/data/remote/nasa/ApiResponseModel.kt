
package com.example.nasaapp.data.remote.nasa

import com.google.gson.annotations.SerializedName

data class ApodDto(
    val date: String?,
    val title: String?,
    val explanation: String?,
    val url: String?,
    val hdurl: String?,
    @SerializedName("media_type")
    val mediaType: String?,
    val copyright: String?
)

data class NeoFeedDto(
    @SerializedName("near_earth_objects")
    val nearEarthObjects: Map<String, List<AsteroideDto>>?
)

data class AsteroideDto(
    val id: String?,
    val name: String?,
    @SerializedName("is_potentially_hazardous_asteroid")
    val isPotentiallyHazardousAsteroid: Boolean?,
    @SerializedName("estimated_diameter")
    val estimatedDiameter: EstimatedDiameterDto?,
    @SerializedName("close_approach_data")
    val closeApproachData: List<CloseApproachDto>?
)

data class EstimatedDiameterDto(
    val meters: DiameterRangeDto?
)

data class DiameterRangeDto(
    @SerializedName("estimated_diameter_min")
    val min: Double?,
    @SerializedName("estimated_diameter_max")
    val max: Double?
)

data class CloseApproachDto(
    @SerializedName("close_approach_date")
    val closeApproachDate: String?,
    @SerializedName("relative_velocity")
    val relativeVelocity: RelativeVelocityDto?,
    @SerializedName("miss_distance")
    val missDistance: MissDistanceDto?
)

data class RelativeVelocityDto(
    @SerializedName("kilometers_per_hour")
    val kilometersPerHour: String?
)

data class MissDistanceDto(
    val kilometers: String?
)
