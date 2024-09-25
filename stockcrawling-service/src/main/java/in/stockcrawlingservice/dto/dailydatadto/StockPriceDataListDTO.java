package in.stockcrawlingservice.dto.dailydatadto;

import lombok.Data;

import java.util.List;

@Data
public class StockPriceDataListDTO {
    private String symbol;
    private String dataType;
    private List<StockPriceDataDTO> dataList;
}