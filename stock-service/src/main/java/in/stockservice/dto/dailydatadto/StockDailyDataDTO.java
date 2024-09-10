package in.stockservice.dto.dailydatadto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class StockDailyDataDTO {
    private String symbol;
    private LocalDate date;
    private BigDecimal openPrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal closePrice;
    private Long volume;
}
