package in.userservice.like.service;

import in.userservice.like.entity.Like;
import in.userservice.like.repository.LikeRepository;
import in.userservice.like.comm.LikeType;
import in.userservice.newsfeed.service.NewsfeedService;
import in.userservice.posts.repository.PostsRepository;
import in.userservice.user.entity.User;
import in.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class LikeService {


    private final LikeRepository likeRepository;
    private final PostsRepository postRepository;
    private final UserRepository userRepository;
    private final NewsfeedService newsfeedService;

    @Transactional
    public boolean toggleLike(Long userId, Long targetId, LikeType targetType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        boolean alreadyLiked = likeRepository.existsByUserIdAndTargetIdAndTargetType(userId, targetId, targetType);

        if (alreadyLiked) {
            // 이미 좋아요를 누른 경우, 좋아요 취소
            likeRepository.deleteByUserIdAndTargetIdAndTargetType(userId, targetId, targetType);
            return false; // 좋아요를 취소했음을 나타내기 위해 false 반환
        } else {
            // 새로운 좋아요 추가
            Like like = Like.builder()
                    .user(user)
                    .targetId(targetId)
                    .targetType(targetType)
                    .createdAt(LocalDateTime.now())
                    .build();

            if(targetType==LikeType.POST){
                newsfeedService.handleLikeEvent(like);
            }else{
                newsfeedService.handleLikeCommentEvent(like);
            }

            likeRepository.save(like);
            return true;
        }
    }

    public long getLikeCount(Long targetId, LikeType targetType) {
        return likeRepository.countByTargetIdAndTargetType(targetId, targetType);
    }

    public boolean hasUserLiked(Long targetId, Long userId, LikeType targetType) {
        return likeRepository.existsByUserIdAndTargetIdAndTargetType(userId, targetId, targetType);
    }

}
