package in.stockcrawlingservice.batch;

import in.stockcrawlingservice.DateRangeFormatter;
import in.stockcrawlingservice.SymbolCacheService;
import in.stockcrawlingservice.dto.dailydatadto.StockPriceDataListDTO;
import in.stockcrawlingservice.kafka.StockDataService;
import in.stockcrawlingservice.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrawlingScheduler {

    private final CrawlingService crawlingService;
    private final SymbolCacheService symbolCacheService;
    private final StockDataService stockDataService;


    @Scheduled(cron = "0 * 9-15 * * MON-FRI")
    public void crawlMinuteData() {
        LocalTime currentTime = LocalTime.now();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(15, 30);

        if (currentTime.isBefore(startTime) || currentTime.isAfter(endTime)) {
            return;
        }

        List<String> symbols = symbolCacheService.getCachedSymbols();

        for (String symbol : symbols) {
            try {
                String[] dateRange = DateRangeFormatter.getMinusOneMinuteDateRange();
                String startDateTime = dateRange[0];
                String endDateTime = dateRange[1];
                String url = "https://api.stock.naver.com/chart/domestic/item/" +
                        symbol + "/minute?startDateTime=" + startDateTime + "&endDateTime=" + endDateTime;
                String rawData = crawlingService.fetchDataFromUrl(url);
                StockPriceDataListDTO stockDataList = crawlingService.parseJSONData(rawData, symbol);
                stockDataService.sendStockData(stockDataList);
                log.info("분단위 배치 작업 완료: {}", symbol);
            } catch (Exception e) {
                log.error("Error crawling minute data for symbol {}: {}", symbol, e.getMessage(), e);
            }
        }
    }

    @Scheduled(cron = "0 0 16 * * MON-FRI")
    public void crawlOneDayData() {
        List<String> symbols = symbolCacheService.getCachedSymbols();

        for (String symbol : symbols) {
            try {
                String url = "https://fchart.stock.naver.com/sise.nhn?symbol=" + symbol + "&timeframe=day&count=1&requestType=0";
                String rawData = crawlingService.fetchDataFromUrl(url);
                StockPriceDataListDTO stockDataList = crawlingService.parseXMLData(rawData, symbol);
                stockDataService.sendStockData(stockDataList);
                log.info("일단위 배치 작업 완료: {}", symbol);
            } catch (Exception e) {
                log.error("Error crawling minute data for symbol {}: {}", symbol, e.getMessage(), e);
            }
        }
    }
}