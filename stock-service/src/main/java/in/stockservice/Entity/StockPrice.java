package in.stockservice.Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "stock_price")
@Entity
public class StockPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal openPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal closePrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal highPrice;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal lowPrice;

    @Column(nullable = false)
    private Long volume;
}
