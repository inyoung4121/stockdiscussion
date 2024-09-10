package in.stockcrawlingservice.dto.dailydatadto;

import lombok.Data;

import java.util.List;

@Data
public class StockDailyDataListDTO {
    private String symbol;
    private List<StockDailyDataDTO> dataList;
}
