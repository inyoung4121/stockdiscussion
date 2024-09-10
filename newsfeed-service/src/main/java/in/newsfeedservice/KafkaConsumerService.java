package in.newsfeedservice;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(topics = "topic1", groupId = "newsfeed-service-group")
    public void listen(String message) {
        System.out.println("Received message in server2: " + message);
        System.out.println("서버2 확인");
    }
}