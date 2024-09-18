package in.stockcrawlingservice.batch;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockData {
    private String symbol;
    private LocalDateTime dateTime;
    private double openPrice;
    private double highPrice;
    private double lowPrice;
    private double closePrice;
    private long volume;
}