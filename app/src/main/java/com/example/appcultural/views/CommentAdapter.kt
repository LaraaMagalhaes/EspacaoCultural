import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appcultural.R
import com.example.appcultural.views.CommentsActivity


class CommentAdapter(
    private val comments: List<CommentsActivity.Comment>,
    private val onLikeClick: (CommentsActivity.Comment) -> Unit // callback para curtir o comentário
) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    // ViewHolder para exibir um comentário
    class CommentViewHolder(view: android.view.View, private val onLikeClick: (CommentsActivity.Comment) -> Unit) : RecyclerView.ViewHolder(view) {
        private val commentAuthor: android.widget.TextView = view.findViewById(R.id.comment_author_name)
        private val commentContent: android.widget.TextView = view.findViewById(R.id.comment_content_text)
        private val likeCount: android.widget.TextView = view.findViewById(R.id.like_count_text)
        val likeButton: android.widget.ImageButton = view.findViewById(R.id.like_button)

        fun bind(comment: CommentsActivity.Comment) {
            commentAuthor.text = comment.username
            commentContent.text = comment.commentText
            likeCount.text = comment.likes.toString()

            // Define o ícone do coração baseado no estado de curtido
            if (comment.likedByUser) {
                likeButton.setImageResource(R.drawable.ic_favorite_filled) // Ícone vermelho se curtido
            } else {
                likeButton.setImageResource(R.drawable.outline_favorite_24) // Ícone preto se não curtido
            }

            // Define comportamento do botão de curtir
            likeButton.setOnClickListener {
                onLikeClick(comment) // Chama o método para curtir o comentário
            }
        }
    }

    // Criação do ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_comment_item, parent, false)
        return CommentViewHolder(view, onLikeClick) // Passa o callback para o ViewHolder
    }

    // Vinculação de dados do comentário no ViewHolder
    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment) // Atualiza a exibição do comentário
    }

    // Retorna a quantidade de itens na lista de comentários
    override fun getItemCount() = comments.size
}
