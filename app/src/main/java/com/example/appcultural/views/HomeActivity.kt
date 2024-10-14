package com.example.appcultural.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.appcultural.R
import com.example.appcultural.adapters.ArtListAdapter
import com.example.appcultural.data.MockArtRepository
import com.example.appcultural.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val repository = MockArtRepository()
        val viewManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.recycleView.layoutManager = viewManager
        binding.recycleView.adapter = ArtListAdapter(this, repository.list())

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.albums -> {
                    startActivity(Intent(this, AlbumsListActivity::class.java))
                    true
                }
                R.id.schedule -> {
                    startActivity(Intent(this, ScheduleActivity::class.java))
                    true
                }
                R.id.settings -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    true
                }

                else -> true
            }
        }
    }
}