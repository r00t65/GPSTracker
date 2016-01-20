package de.hof_university.gpstracker.Model.mapoverlays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import de.hof_university.gpstracker.Model.position.Location;
import de.hof_university.gpstracker.R;

/**
 * Created by alex on 07.01.16.
 * GPSTracker
 */
public class MyPositionMapOverlay extends org.osmdroid.views.overlay.Overlay {

    private final Context context;
    private final Location location;

    public MyPositionMapOverlay(Context context, Location point) {
        super(context);
        this.context = context;
        this.location = point;
    }

    @Override
    protected void draw(Canvas canvas, MapView mapView, boolean shadow) {
        if (shadow) {
            return;
        }
        Point out = new Point();
        mapView.getProjection().toPixels(new GeoPoint(this.location.getLocation()), out);
        Paint paint = new Paint();
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);
        Bitmap b = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.person);
        canvas.drawBitmap(b, out.x - b.getWidth() / 2, out.y - b.getHeight() / 2, paint);
        //canvas.drawPoint(out.x,out.y,paint);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setStrokeWidth(5);
        canvas.drawText(this.location.getDate().toString(), out.x, out.y + 22, textPaint);
    }
}
