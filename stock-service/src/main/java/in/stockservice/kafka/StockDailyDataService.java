package in.stockservice.kafka;

import in.stockservice.Entity.Stock;
import in.stockservice.Entity.StockDailyData;
import in.stockservice.dto.dailydatadto.StockDailyDataDTO;
import in.stockservice.dto.dailydatadto.StockDailyDataListDTO;
import in.stockservice.repository.StockDailyDataRepository;
import in.stockservice.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class StockDailyDataService {
    private final StockDailyDataRepository stockDailyDataRepository;
    private final StockRepository stockRepository;

    @KafkaListener(topics = "stock-daily-data-list", groupId = "stock-service-group")
    @Transactional
    public void consume(StockDailyDataListDTO stockDataList) {
        System.out.println("Received stock data list for symbol: " + stockDataList.getSymbol());


        Stock stock = stockRepository.findBySymbol(stockDataList.getSymbol())
                .orElseThrow();

        List<StockDailyData> entities = stockDataList.getDataList().stream()
                .map(dto -> convertToEntity(dto, stock))
                .collect(Collectors.toList());

        stockDailyDataRepository.saveAll(entities);
    }

    private StockDailyData convertToEntity(StockDailyDataDTO dto,Stock stock) {
        StockDailyData entity = StockDailyData.builder()
                .stock(stock)
                .date(dto.getDate())
                .openPrice(dto.getOpenPrice())
                .highPrice(dto.getHighPrice())
                .lowPrice(dto.getLowPrice())
                .closePrice(dto.getClosePrice())
                .volume(dto.getVolume())
                .build();
        return entity;
    }
}
