package in.userservice.comments.repository;


import in.userservice.comments.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostIdOrderByCreatedAtDesc(Long postId);
}
