package com.hosing0926.chartrecovery.repository;

import com.hosing0926.chartrecovery.entity.mysql.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolRepository extends JpaRepository<Symbol, Long> {
    Symbol findBySymbol(String symbol);
}
