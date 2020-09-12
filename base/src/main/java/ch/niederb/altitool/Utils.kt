package ch.niederb.altitool

import android.content.Context
import android.location.Location
import androidx.core.content.edit
import kotlin.math.absoluteValue
import kotlin.math.pow
import kotlin.math.sqrt

data class ChCoordinates(val x: Double, val y : Double, val z: Double)

data class DataIntegration(val distance: Double, val metersUp : Double, val metersDown: Double)

fun calcDelta(oldLocation: ChCoordinates, newLocation: ChCoordinates): DataIntegration {
    val deltaX = newLocation.x - oldLocation.x
    val deltaY = newLocation.y - oldLocation.y
    val deltaZ = newLocation.z - oldLocation.z
    val distance = sqrt(deltaX.pow(2) + deltaY.pow(2) + deltaZ.pow(2))
    if (deltaZ > 0) {
        return DataIntegration(distance, deltaZ, 0.0)
    } else {
        return DataIntegration(distance, 0.0, deltaZ.absoluteValue)
    }
}

fun updateIntegration(oldData: DataIntegration, oldLocation: ChCoordinates, newLocation: ChCoordinates): DataIntegration {
    val dataUpdate = calcDelta(oldLocation, newLocation)
    return DataIntegration(oldData.distance + dataUpdate.distance, oldData.metersUp + dataUpdate.metersUp, oldData.metersDown + dataUpdate.metersDown)
}

fun convertCoordinates(location: Location): ChCoordinates {
    val phi = (3600 * location.latitude - 169028.66) / 10000.0;
    val lambda =(3600 * location.longitude - 26782.5) / 10000.0;
    val e = (2600072.37
            + 211455.93 * lambda
            - 10938.51 * lambda * phi
            - 0.36 * lambda * phi * phi
            - 44.54 * lambda * lambda * lambda)
    val y = e - 2000000.00
    val n = (1200147.07
            + 308807.95 * phi
            + 3745.25 * lambda * lambda
            + 76.63 * phi * phi
            - 194.56 * lambda * lambda * phi
            + 119.79 * phi * phi * phi)
    val x = n - 1000000.00
    val h = (location.altitude - 49.55
            + 2.73 * lambda
            + 6.94 * phi)
    return ChCoordinates(x, y, h)
}

/**
 * Returns the `location` object as a human readable string.
 */
fun Location?.toText():String {
    return if (this != null) {
        "($latitude, $longitude)"
    } else {
        "Unknown location"
    }
}

/**
 * Provides access to SharedPreferences for location to Activities and Services.
 */
internal object SharedPreferenceUtil {

    const val KEY_FOREGROUND_ENABLED = "tracking_foreground_location"

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The [Context].
     */
    fun getLocationTrackingPref(context: Context): Boolean =
        context.getSharedPreferences(
            context.getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            .getBoolean(KEY_FOREGROUND_ENABLED, false)

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    fun saveLocationTrackingPref(context: Context, requestingLocationUpdates: Boolean) =
        context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE).edit {
            putBoolean(KEY_FOREGROUND_ENABLED, requestingLocationUpdates)
        }
}
