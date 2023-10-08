package com.example.nasaspaceapps_proyect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class FireClusterRenderer extends DefaultClusterRenderer<FireClusterItem> {

    private final Drawable mRedDotDrawable;
    private final int mDotSize;

    public FireClusterRenderer(Context context, GoogleMap map, ClusterManager<FireClusterItem> clusterManager) {
        super(context, map, clusterManager);

        mRedDotDrawable = ContextCompat.getDrawable(context, R.drawable.red_dot);
        mDotSize = (int) context.getResources().getDimension(R.dimen.dot_size);
    }

    @Override
    protected void onBeforeClusterItemRendered(FireClusterItem item, MarkerOptions markerOptions) {
        BitmapDescriptor icon = getBitmapDescriptorFromDrawable(mRedDotDrawable);
        markerOptions.icon(icon).snippet(item.getSnippet());
    }

    private BitmapDescriptor getBitmapDescriptorFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(mDotSize, mDotSize, Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, mDotSize, mDotSize);
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
