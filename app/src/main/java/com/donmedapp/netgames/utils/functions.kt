package com.donmedapp.netgames.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.donmedapp.netgames.extensions.hideSoftKeyboard


fun roundedImg(it: Int,resources : Resources): RoundedBitmapDrawable {
    val bitmap = BitmapFactory.decodeResource(resources, it)
    val mDrawable =
        RoundedBitmapDrawableFactory.create(resources, bitmap)
    mDrawable.isCircular = true
    return mDrawable
}

  fun isNetDisponible(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
      val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
      return activeNetwork?.isConnectedOrConnecting == true
}
