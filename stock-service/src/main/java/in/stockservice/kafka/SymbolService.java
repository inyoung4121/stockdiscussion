package in.stockservice.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.stockservice.dto.symbol.SymbolRequestDTO;
import in.stockservice.dto.symbol.SymbolResponseDTO;
import in.stockservice.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SymbolService {
    private final KafkaTemplate<String, SymbolResponseDTO> kafkaTemplate;
    private final StockRepository symbolRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "symbol-request", groupId = "stock-service-group")
    public void listenSymbolRequests(String message) throws JsonProcessingException {
        SymbolResponseDTO response = new SymbolResponseDTO();
        SymbolRequestDTO request = objectMapper.readValue(message, SymbolRequestDTO.class);
        response.setRequestId(request.getRequestId());
        response.setSymbols(symbolRepository.findAllSymbol());
        kafkaTemplate.send("symbol-response", response);
    }
}
