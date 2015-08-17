package de.konqi.fitapi.rest.openfitapi.resources;

import com.fasterxml.jackson.databind.JsonNode;
import de.konqi.fitapi.db.Lap;

import java.util.Date;

/**
 * Created by konqi on 16.08.2015.
 */
public class Workout {
    Date start_time; // (ISO 8601 DateTime)	Start time of the workout
    String type; //	string (OpenFit Workout Type)	Type of the workout
    String sharing; //	string (OpenFit Workout Sharing Type)	Sharing option of the workout
    String name; //	string	Display name of the workout
    Double total_distance; //	real number (meters)	Total distance (cumulative) of the workout
    Double duration; //	real number (seconds)	Total duration (recorded) of the workout
    Double clock_duration; //	real number (seconds)	Actual duration (including pauses) of the workout
    Double calories; // real number (kilojoules)	Energy burnt during the workout
    Double elevation_gain; //	real number (meters)	Sum gain in elevation
    Double elevation_loss; //	real number (meters)	Sum loss in elevation
    String notes;//	string	Workout notes
    Double avg_speed; //	real number (meters per second)	Maximum speed of the workout
    Double max_speed; //	real number (meters per second)	Maximum speed of the workout
    Double avg_heartrate;//	real number (beats per minute)	Average heart rate during this workout
    Double max_heartrate;//	real number (beats per minute)	Maximum heart rate during this workout
    Double avg_cadence;//	real number (revolutions per minute)	Average cadence during this workout
    Double max_cadence;//	real number (revolutions per minute)	Maximum cadence during this workout
    Double avg_power;//	real number (watts)	Average power during this workout
    Double max_power;//	real number (watts)	Maximum power during this workout
    Lap laps[]; //	array	An array of Laps
    JsonNode location[];//	array (OpenFit Track Data)	List of lat/lng pairs associated with this workout
    String elevation[];//	array (OpenFit Track Data)	List of altitude data associated with this workout
    String distance[];//	array (OpenFit Track Data)	List of meters moved associated with this workout
    String heartrate[];//	array (OpenFit Track Data)	List of heartrate data associated with this workout
    String cadence[];//	array (OpenFit Track Data)	List of cadence data associated with this workout
    String power[];//	array (OpenFit Track Data)	List of power data associated with this workout
    String timer_stops[];//	array	An array of Timer Stops
    String uri;//	string [read only]	URI of the detail information for this workout
    String activity;//	string [read only]	A URL which points to the web page details for this workout

    public Double getAvg_speed() {
        return avg_speed;
    }

    public void setAvg_speed(Double avg_speed) {
        this.avg_speed = avg_speed;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSharing() {
        return sharing;
    }

    public void setSharing(String sharing) {
        this.sharing = sharing;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTotal_distance() {
        return total_distance;
    }

    public void setTotal_distance(Double total_distance) {
        this.total_distance = total_distance;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Double getClock_duration() {
        return clock_duration;
    }

    public void setClock_duration(Double clock_duration) {
        this.clock_duration = clock_duration;
    }

    public Double getCalories() {
        return calories;
    }

    public void setCalories(Double calories) {
        this.calories = calories;
    }

    public Double getElevation_gain() {
        return elevation_gain;
    }

    public void setElevation_gain(Double elevation_gain) {
        this.elevation_gain = elevation_gain;
    }

    public Double getElevation_loss() {
        return elevation_loss;
    }

    public void setElevation_loss(Double elevation_loss) {
        this.elevation_loss = elevation_loss;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getMax_speed() {
        return max_speed;
    }

    public void setMax_speed(Double max_speed) {
        this.max_speed = max_speed;
    }

    public Double getAvg_heartrate() {
        return avg_heartrate;
    }

    public void setAvg_heartrate(Double avg_heartrate) {
        this.avg_heartrate = avg_heartrate;
    }

    public Double getMax_heartrate() {
        return max_heartrate;
    }

    public void setMax_heartrate(Double max_heartrate) {
        this.max_heartrate = max_heartrate;
    }

    public Double getAvg_cadence() {
        return avg_cadence;
    }

    public void setAvg_cadence(Double avg_cadence) {
        this.avg_cadence = avg_cadence;
    }

    public Double getMax_cadence() {
        return max_cadence;
    }

    public void setMax_cadence(Double max_cadence) {
        this.max_cadence = max_cadence;
    }

    public Double getAvg_power() {
        return avg_power;
    }

    public void setAvg_power(Double avg_power) {
        this.avg_power = avg_power;
    }

    public Double getMax_power() {
        return max_power;
    }

    public void setMax_power(Double max_power) {
        this.max_power = max_power;
    }

    public Lap[] getLaps() {
        return laps;
    }

    public void setLaps(Lap[] laps) {
        this.laps = laps;
    }

    public JsonNode[] getLocation() {
        return location;
    }

    public void setLocation(JsonNode[] location) {
        this.location = location;
    }

    public String[] getElevation() {
        return elevation;
    }

    public void setElevation(String[] elevation) {
        this.elevation = elevation;
    }

    public String[] getDistance() {
        return distance;
    }

    public void setDistance(String[] distance) {
        this.distance = distance;
    }

    public String[] getHeartrate() {
        return heartrate;
    }

    public void setHeartrate(String[] heartrate) {
        this.heartrate = heartrate;
    }

    public String[] getCadence() {
        return cadence;
    }

    public void setCadence(String[] cadence) {
        this.cadence = cadence;
    }

    public String[] getPower() {
        return power;
    }

    public void setPower(String[] power) {
        this.power = power;
    }

    public String[] getTimer_stops() {
        return timer_stops;
    }

    public void setTimer_stops(String[] timer_stops) {
        this.timer_stops = timer_stops;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
