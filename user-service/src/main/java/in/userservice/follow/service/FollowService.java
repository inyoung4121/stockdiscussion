package in.userservice.follow.service;

import in.userservice.follow.domain.Follow;
import in.userservice.follow.repository.FollowRepository;
import in.userservice.newsfeed.domain.Newsfeed;
import in.userservice.newsfeed.service.NewsfeedService;
import in.userservice.user.domain.User;
import in.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final NewsfeedService newsfeedService;

    @Transactional
    public void follow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid follower ID"));
        User following = userRepository.findById(followingId).orElseThrow(() -> new IllegalArgumentException("Invalid following ID"));

        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            throw new IllegalStateException("Already following this user");
        }
        Follow follow = new Follow().builder()
                .follower(follower)
                .following(following)
                .createdAt(LocalDateTime.now())
                .build();

        newsfeedService.handleFollowEvent(follow);

        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid follower ID"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid following ID"));

        Optional<Follow> follow = followRepository.findByFollowerAndFollowing(follower, following);

        follow.ifPresent(followRepository::delete);
    }

    public boolean isFollowing(Long followerId, Long followingId) {
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid follower ID"));
        User following = userRepository.findById(followingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid following ID"));

        return followRepository.existsByFollowerAndFollowing(follower, following);
    }
}
