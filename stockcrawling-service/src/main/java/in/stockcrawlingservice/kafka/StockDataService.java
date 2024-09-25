package in.stockcrawlingservice.kafka;

import in.stockcrawlingservice.dto.dailydatadto.StockPriceDataListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockDataService {
    private final KafkaTemplate<String, StockPriceDataListDTO> kafkaTemplate;

    public void sendStockData(StockPriceDataListDTO stockDataList) {
        kafkaTemplate.send("stock-price-data-list", stockDataList.getSymbol(), stockDataList);
    }
}