package in.stockservice.repository;

import in.stockservice.Entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    @Query("SELECT s.symbol FROM Stock s")
    List<String> findAllSymbol();

    Optional<Stock> findBySymbol(String symbol);
}
