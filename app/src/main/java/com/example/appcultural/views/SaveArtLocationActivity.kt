package com.example.appcultural.views

import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.appcultural.R
import com.example.appcultural.databinding.ActivitySaveArtLocationBinding

class SaveArtLocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySaveArtLocationBinding
    private var currentCircleView: CirclePointView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySaveArtLocationBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imageView = findViewById<ImageView>(R.id.image_location)
        imageView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val x = event.x
                val y = event.y
                currentCircleView?.let {
                    it.visibility = View.GONE
                    (v.parent as ViewGroup).removeView(it)
                }
                addCircle(x + imageView.x, y + imageView.y)
            }
            true
        }

        binding.saveLocationButton.setOnClickListener {
            finish()
        }
    }

    private fun addCircle(x: Float, y: Float) {
        val circleView = CirclePointView(this, x, y)
        val layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        addContentView(circleView, layoutParams)
        currentCircleView = circleView
    }
}