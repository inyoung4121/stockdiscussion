package in.stockcrawlingservice.kafka;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.stockcrawlingservice.dto.symboldto.SymbolRequestDTO;
import in.stockcrawlingservice.dto.symboldto.SymbolResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class SymbolService {

    private final KafkaTemplate<String, SymbolRequestDTO> kafkaTemplate;
    private final ConcurrentHashMap<String, CompletableFuture<SymbolResponseDTO>> pendingRequests = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper;

    public CompletableFuture<SymbolResponseDTO> requestSymbols() {
        String requestId = UUID.randomUUID().toString();
        SymbolRequestDTO request = new SymbolRequestDTO();
        request.setRequestId(requestId);

        CompletableFuture<SymbolResponseDTO> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        kafkaTemplate.send("symbol-request", request);

        return future;
    }

    @KafkaListener(topics = "symbol-response", groupId = "stock-symbol-group")
    public void listenSymbolResponses(String message) {
        try {
            SymbolResponseDTO response = objectMapper.readValue(message, SymbolResponseDTO.class);
            CompletableFuture<SymbolResponseDTO> future = pendingRequests.remove(response.getRequestId());
            if (future != null) {
                future.complete(response);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}