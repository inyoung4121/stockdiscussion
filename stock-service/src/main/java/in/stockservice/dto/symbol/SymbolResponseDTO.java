package in.stockservice.dto.symbol;

import lombok.Data;
import java.util.List;

@Data
public class SymbolResponseDTO {
    private String requestId;
    private List<String> symbols;
}