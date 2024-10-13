package com.example.appcultural.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appcultural.R
import com.example.appcultural.entities.Comment

class CommentListAdapter(private val data: ArrayList<Comment>): RecyclerView.Adapter<CommentListAdapter.CommentViewHolder>() {
    class CommentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_comment_item, parent, false)

        return CommentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = data[position]
        val authorText = holder.itemView.findViewById<TextView>(R.id.comment_author)
        authorText.text = comment.user.name

        val commentContent = holder.itemView.findViewById<TextView>(R.id.comment_content)
        commentContent.text = comment.content
    }
}