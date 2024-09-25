package in.stockservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.stockservice.entity.Stock;
import in.stockservice.entity.StockPriceData;
import in.stockservice.entity.StockPriceDataId;
import in.stockservice.dto.dailydatadto.StockPriceDataDTO;
import in.stockservice.dto.dailydatadto.StockPriceDataListDTO;
import in.stockservice.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockDataService {

    private final StockRepository stockRepository;
    private final ObjectMapper objectMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @KafkaListener(topics = "stock-price-data-list", groupId = "stock-group")
    @Transactional
    public void consume(String messageJson) {
        try {
            log.debug("Received message: {}", messageJson);
            StockPriceDataListDTO message = objectMapper.readValue(messageJson, StockPriceDataListDTO.class);
            processMessage(message);
        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", messageJson, e);
        }
    }

    private void processMessage(StockPriceDataListDTO message) {
        String symbol = message.getSymbol();
        log.info("Processing stock data for symbol: {}", symbol);

        try {
            Stock stock = getOrCreateStock(symbol);

            List<StockPriceData> stockPriceDataList = new ArrayList<>();
            for (StockPriceDataDTO dto : message.getDataList()) {
                StockPriceData stockPriceData = convertToStockPriceData(dto, stock);
                stockPriceDataList.add(stockPriceData);
            }

            for (StockPriceData data : stockPriceDataList) {
                saveOrUpdateStockPriceData(data);
            }

            log.info("Successfully processed {} stock price data entries for symbol: {}",
                    stockPriceDataList.size(), symbol);
        } catch (Exception e) {
            log.error("Error processing stock data for symbol: {}", symbol, e);
            throw new RuntimeException("Failed to process stock data", e);
        }
    }

    public Stock getOrCreateStock(String symbol) {
        return stockRepository.findBySymbol(symbol)
                .orElseGet(() -> {
                    Stock newStock = new Stock();
                    newStock.setSymbol(symbol);
                    return stockRepository.save(newStock);
                });
    }

    public void saveOrUpdateStockPriceData(StockPriceData data) {
        StockPriceDataId id = new StockPriceDataId(data.getStock(), data.getDateTime());
        StockPriceData existingData = entityManager.find(StockPriceData.class, id);

        if (existingData != null) {
            updateExistingData(existingData, data);
        } else {
            entityManager.persist(data);
        }
    }

    private void updateExistingData(StockPriceData existingData, StockPriceData newData) {
        existingData.setOpenPrice(newData.getOpenPrice());
        existingData.setHighPrice(newData.getHighPrice());
        existingData.setLowPrice(newData.getLowPrice());
        existingData.setClosePrice(newData.getClosePrice());
        existingData.setVolume(newData.getVolume());
        existingData.setDataType(newData.getDataType());
    }

    private StockPriceData convertToStockPriceData(StockPriceDataDTO dto, Stock stock) {
        return StockPriceData.builder()
                .stock(stock)
                .dateTime(dto.getDatetime() != null ? dto.getDatetime() : LocalDateTime.now())
                .openPrice(dto.getOpenPrice())
                .highPrice(dto.getHighPrice())
                .lowPrice(dto.getLowPrice())
                .closePrice(dto.getClosePrice())
                .volume(dto.getVolume())
                .dataType(dto.getDataType())
                .build();
    }
}