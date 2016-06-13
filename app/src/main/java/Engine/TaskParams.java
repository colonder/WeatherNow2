package Engine;

/**
 * Created by Jakub on 13.06.2016.
 */
public class TaskParams
{
    private double lat;
    private double lon;

    public TaskParams(double lat, double lon)
    {
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
