package in.stockcrawlingservice.kafka;

import in.stockcrawlingservice.dto.dailydatadto.StockDailyDataDTO;
import in.stockcrawlingservice.dto.dailydatadto.StockDailyDataListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StockDailyDataService {

    private final KafkaTemplate<String, StockDailyDataListDTO> kafkaTemplate;

    public void sendStockData(StockDailyDataListDTO stockDataList) {
        kafkaTemplate.send("stock-daily-data-list", stockDataList.getSymbol(), stockDataList);
    }
}
