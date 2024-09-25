package in.stockservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.stockservice.dto.symbol.SymbolRequestDTO;
import in.stockservice.dto.symbol.SymbolResponseDTO;
import in.stockservice.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SymbolService {
    private final KafkaTemplate<String, SymbolResponseDTO> kafkaTemplate;
    private final StockRepository symbolRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "symbol-request", groupId = "stock-symbol-group")
    public void listenSymbolRequests(String message) {
        try {
            SymbolRequestDTO request = objectMapper.readValue(message, SymbolRequestDTO.class);
            log.info("Received symbol request with ID: {}", request.getRequestId());

            SymbolResponseDTO response = new SymbolResponseDTO();
            response.setRequestId(request.getRequestId());
            response.setSymbols(symbolRepository.findAllSymbol());

            kafkaTemplate.send("symbol-response", response);
            log.info("Sent symbol response for request ID: {}", request.getRequestId());
        } catch (Exception e) {
            log.error("Error processing symbol request: {}", e.getMessage(), e);
        }
    }
}
