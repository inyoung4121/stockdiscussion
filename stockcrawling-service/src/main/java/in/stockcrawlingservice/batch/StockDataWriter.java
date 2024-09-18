package in.stockcrawlingservice.batch;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class StockDataWriter implements ItemWriter<StockData> {

    @Override
    public void write(Chunk<? extends StockData> chunk) throws Exception {
        for (StockData item : chunk) {
            // 데이터베이스에 저장하거나 다른 처리를 수행
            System.out.println("Processed: " + item);
        }
    }
}