package in.stockcrawlingservice.batch;

import org.springframework.batch.item.ItemProcessor;

import java.time.LocalDateTime;

public class StockDataProcessor implements ItemProcessor<String, StockData> {
    @Override
    public StockData process(String symbol) throws Exception {
        String url = "https://fchart.stock.naver.com/sise.nhn?symbol=" + symbol + "&timeframe=day&count=1303&requestType=0";
        // URL에서 데이터를 가져와 파싱하는 로직
        // 여기서는 간단한 예시만 제공합니다
        return new StockData(symbol, LocalDateTime.now(), 100.0, 110.0, 90.0, 105.0, 1000000);
    }
}