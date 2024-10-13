package com.example.appcultural.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.example.appcultural.R
import com.example.appcultural.adapters.ArtListAdapter
import com.example.appcultural.data.MockArtRepository
import com.example.appcultural.databinding.ActivityArtDetailBinding

class ArtDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityArtDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityArtDetailBinding.inflate(layoutInflater)
        setContentView(binding.main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val repository = MockArtRepository()
        val id = intent.getIntExtra("id", 0)
        val art = repository.findById(id)
        if (art == null) {
            finish()
            return
        }

        val image = binding.artImageDetail
        Glide.with(this).load(art.imageUrl).into(image)
        image.requestLayout()

        var description = art.description
        if (art.description.length > 80) {
            description = art.description.slice(0..80)
        }
        binding.artDescriptionDetail.text = description


        setSupportActionBar(binding.topAppBar);
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)

        val viewManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.recycleView.layoutManager = viewManager
        binding.recycleView.adapter = ArtListAdapter(this, repository.list())

        val buttonComment = binding.commentButton
        buttonComment.setOnClickListener {
            val intent = Intent(this, CommentsActivity::class.java)
            intent.putExtra("id", art.id)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.home -> {
            finish()
            true
        }

        else -> {
            finish()
            true
        }
    }
}