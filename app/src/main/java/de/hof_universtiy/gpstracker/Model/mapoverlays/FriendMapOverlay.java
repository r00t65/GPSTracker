package de.hof_universtiy.gpstracker.Model.mapoverlays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.NonNull;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import de.hof_universtiy.gpstracker.Model.radar.Friend;
import de.hof_universtiy.gpstracker.R;

/**
 * Created by alex on 12.01.16 um 17:08
 * GPSTracker
 */
public class FriendMapOverlay extends org.osmdroid.views.overlay.Overlay {

    private final Friend friend;
    private final Context context;

    public FriendMapOverlay(@NonNull final Context context, @NonNull final Friend friend) {
        super(context);
        this.context = context;
        this.friend = friend;
    }

    @Override
    protected void draw(Canvas canvas, MapView mapView, boolean shadow) {
        if (shadow) {
            return;
        }
        Point out = new Point();
        mapView.getProjection().toPixels(new GeoPoint(this.friend.getLocation().getLocation()), out);
        Paint paint = new Paint();
        paint.setStrokeWidth(20);
        paint.setColor(Color.RED);
        Bitmap b = BitmapFactory.decodeResource(this.context.getResources(), R.drawable.com_facebook_profile_picture_blank_portrait);
        canvas.drawBitmap(b, out.x - b.getWidth() / 2, out.y - b.getHeight() / 2, paint);
        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setStrokeWidth(5);
        canvas.drawText(this.friend.getLocation().getDate().toString(), out.x, out.y + 22, textPaint);
    }
}
