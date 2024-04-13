package com.hosing0926.chartrecovery.service;

import com.hosing0926.chartrecovery.entity.mysql.Symbol;
import com.hosing0926.chartrecovery.repository.SymbolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SymbolService {

    private final SymbolRepository symbolRepository;

    public List<Symbol> getSymbols() {
        return symbolRepository.findAll();
    }

    public Symbol getBySymbol(String symbol) {
        return symbolRepository.findBySymbol(symbol);
    }
}
