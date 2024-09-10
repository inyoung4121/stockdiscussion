package in.stockcrawlingservice.kafka;


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

    public CompletableFuture<SymbolResponseDTO> requestSymbols() {
        String requestId = UUID.randomUUID().toString();
        SymbolRequestDTO request = new SymbolRequestDTO();
        request.setRequestId(requestId);

        CompletableFuture<SymbolResponseDTO> future = new CompletableFuture<>();
        pendingRequests.put(requestId, future);

        kafkaTemplate.send("symbol-request", request);

        return future;
    }

    @KafkaListener(topics = "symbol-response", groupId = "stockcrawling-group")
    public void listenSymbolResponses(SymbolResponseDTO response) {
        CompletableFuture<SymbolResponseDTO> future = pendingRequests.remove(response.getRequestId());
        if (future != null) {
            future.complete(response);
        }
    }
}
