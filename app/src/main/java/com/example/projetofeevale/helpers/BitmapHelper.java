// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.example.projetofeevale.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class BitmapHelper {
    /**
     * Demonstrates converting a [Drawable] to a [BitmapDescriptor], for use as a marker icon.
     * Taken from ApiDemos on GitHub: https://github.com/googlemaps/android-samples/blob/master/ApiDemos/kotlin/app/src/main/java/com/example/kotlindemos/MarkerDemoActivity.kt
     */
    public static BitmapDescriptor vectorToBitmap(Context context, @DrawableRes int id, @ColorInt int color) {
        android.graphics.drawable.Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), id, null);
        if (vectorDrawable == null) {
            Log.e("BitmapHelper", "Resource not found");
            return BitmapDescriptorFactory.defaultMarker();
        }
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        DrawableCompat.setTint(vectorDrawable, color);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}

