package in.stockservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stockPriceData")
@IdClass(StockPriceDataId.class)
public class StockPriceData {
    @Id
    @ManyToOne
    @JoinColumn(name = "stockId", nullable = false)
    private Stock stock;

    @Id
    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    private BigDecimal openPrice;

    @Column(nullable = false)
    private BigDecimal highPrice;

    @Column(nullable = false)
    private BigDecimal lowPrice;

    @Column(nullable = false)
    private BigDecimal closePrice;

    @Column(nullable = false)
    private Long volume;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DataType dataType;

    public enum DataType {
        MINUTE, DAILY
    }
}