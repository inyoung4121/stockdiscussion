package in.stockcrawlingservice.dto.dailydatadto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class StockPriceDataDTO {
    private String symbol;
    private LocalDateTime datetime;
    private String dataType; // "MINUTE" or "DAILY"
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private Long volume;
}