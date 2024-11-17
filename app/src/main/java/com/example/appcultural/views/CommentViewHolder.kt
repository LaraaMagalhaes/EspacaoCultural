package com.example.appcultural.views
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appcultural.R
import com.example.appcultural.views.CommentsActivity.Comment


class CommentViewHolder(view: View, private val onLikeClick: (Comment) -> Unit) : RecyclerView.ViewHolder(view) {
    private val commentAuthor: TextView = view.findViewById(R.id.comment_author_name)
    private val commentContent: TextView = view.findViewById(R.id.comment_content_text)
    private val likeCount: TextView = view.findViewById(R.id.like_count_text)
    val likeButton: ImageButton = view.findViewById(R.id.like_button)

    fun bind(comment: Comment) {
        commentAuthor.text = comment.username
        commentContent.text = comment.commentText
        likeCount.text = comment.likes.toString()

        // Define comportamento do botão de curtir
        likeButton.setOnClickListener {
            onLikeClick(comment) // Chama o método para curtir o comentário
        }
    }
}
