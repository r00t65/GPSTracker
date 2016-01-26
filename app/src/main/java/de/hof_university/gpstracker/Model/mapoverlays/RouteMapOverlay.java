package de.hof_university.gpstracker.Model.mapoverlays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import de.hof_university.gpstracker.Model.track.Track;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import de.hof_university.gpstracker.Model.position.Location;

/**
 * Created by alex on 08.01.16.
 * GPSTracker
 */
public class RouteMapOverlay extends org.osmdroid.views.overlay.Overlay {

    private final Context context;
    private final Track track;

    public RouteMapOverlay(Context context, Track track) {
        super(context);
        this.context = context;
        this.track = track;
    }

    @Override
    protected void draw(Canvas canvas, MapView mapView, boolean shadow) {
        if (shadow) {
            return;
        }
        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setStrokeWidth(5);

        Paint paint = new Paint();
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);
        for(Location location:track.getTracks()){
            Point out = new Point();
            out = mapView.getProjection().toPixels(new GeoPoint(location.getLocation()), out);
            Bitmap b = BitmapFactory.decodeResource(this.context.getResources(), org.osmdroid.library.R.drawable.marker_default);
            canvas.drawBitmap(b, out.x - b.getWidth() / 2, out.y - b.getHeight(), paint);
            canvas.drawText(location.getDate().toString(), out.x, out.y + 22, textPaint);
        }
    }
}
