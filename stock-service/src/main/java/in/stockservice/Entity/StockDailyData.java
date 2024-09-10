package in.stockservice.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "daily_stock_data")
public class StockDailyData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private LocalDate date;

    //시가
    @Column(name = "open_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal openPrice;

    //고가
    @Column(name = "high_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal highPrice;

    //저가
    @Column(name = "low_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal lowPrice;

    //종가
    @Column(name = "close_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal closePrice;

    //거래량
    @Column(nullable = false)
    private Long volume;
}