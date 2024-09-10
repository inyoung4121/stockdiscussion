package in.userservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/send")
    public String sendMessage() {
        try {
            kafkaTemplate.send("topic1", "hello");
            return "Message sent to server2";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to send message", e);
        }
    }
}