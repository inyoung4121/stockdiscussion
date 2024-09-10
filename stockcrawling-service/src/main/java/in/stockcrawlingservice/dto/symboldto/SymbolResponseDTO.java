package in.stockcrawlingservice.dto.symboldto;

import lombok.Data;

import java.util.List;

@Data
public class SymbolResponseDTO{
    private String requestId;
    private List<String> symbols;
}