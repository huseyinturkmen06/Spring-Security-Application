package api.Repositories;

import api.Entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    //repository neden interface olmak zorunda

    List<Comment> findByUserIdAndPostId(Long userId,Long postId);
    List<Comment> findByUserId(Long userId);
    List<Comment> findByPostId(Long postId);


}
