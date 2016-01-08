package de.hof_universtiy.gpstracker.Model.mapoverlays;

import android.content.Context;
import android.graphics.*;
import de.hof_universtiy.gpstracker.Model.position.Location;
import de.hof_universtiy.gpstracker.R;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

/**
 * Created by alex on 08.01.16.
 * GPSTracker
 */
public class RouteMapOverlay extends org.osmdroid.views.overlay.Overlay {

    private final Context context;
    private final Location location;

    public RouteMapOverlay(Context context,Location location) {
        super(context);
        this.context = context;
        this.location = location;
    }

    @Override
    protected void draw(Canvas canvas, MapView mapView, boolean shadow) {
        if(shadow){
            return;
        }
        Point out = new Point();
        mapView.getProjection().toPixels(new GeoPoint(this.location.getLocation()), out);
        Paint paint = new Paint();
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);
        Bitmap b=BitmapFactory.decodeResource(this.context.getResources(), org.osmdroid.library.R.drawable.marker_default);
        canvas.drawBitmap(b, out.x-b.getWidth()/2,out.y-b.getHeight(), paint);
        //canvas.drawPoint(out.x,out.y,paint);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setStrokeWidth(5);
        canvas.drawText(this.location.getDate().toString(),out.x,out.y+22,textPaint);
    }
}
