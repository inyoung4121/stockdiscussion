package in.userservice.follow.repository;

import in.userservice.follow.domain.Follow;
import in.userservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(User follower, User following);
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    @Query("SELECT f.follower FROM Follow f WHERE f.following = :following")
    List<User> findFollowersByFollowing(User following);

    long countByFollowingId(Long userId);

    long countByFollowerId(Long userId);
}
