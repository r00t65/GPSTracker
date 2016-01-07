package de.hof_universtiy.gpstracker.Model.position;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.Date;

/**
 * Created by alex on 07.01.16.
 * GPSTracker
 */
public class MapOverlay extends org.osmdroid.views.overlay.Overlay {

    private final GeoPoint point;
    private final Date date;

    public MapOverlay(Context ctx, GeoPoint point) {
        super(ctx);
        this.point = point;
        this.date = new Date();
    }

    @Override
    protected void draw(Canvas canvas, MapView mapView, boolean shadow) {
        if(shadow){
            return;
        }
        Point out = new Point();
        mapView.getProjection().toPixels(point, out);

        Paint paint = new Paint();
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);
        canvas.drawPoint(out.x,out.y,paint);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setStrokeWidth(5);
        canvas.drawText(this.date.toString(),out.x,out.y+22,textPaint);
    }
}
