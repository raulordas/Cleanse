package com.cleanseproject.cleanse.services;

import android.app.IntentService;
import android.content.res.Resources;

import com.cleanseproject.cleanse.R;
import com.google.android.gms.location.GeofenceStatusCodes;

public class GeofenceErrorMessages {

    private GeofenceErrorMessages() {}

    public static String getErrorString(IntentService intentService, int errorCode) {
        Resources mResources = intentService.getResources();
        switch (errorCode) {
            case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                return mResources.getString(R.string.geofence_not_available);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                return mResources.getString(R.string.geofence_too_many_geofences);
            case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                return mResources.getString(R.string.geofence_too_many_pending_intents);
            default:
                return mResources.getString(R.string.unknown_geofence_error);
        }
    }
}
