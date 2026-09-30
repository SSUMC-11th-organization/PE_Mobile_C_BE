package com.umc.study.service;

import com.umc.study.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;

    public List<Map<String, Object>> getRentals(Long rentalId) {
        return rentalRepository.findRental(rentalId);
    }

    public void createRent(Map<String, Object> body){
        rentalRepository.saveRent(body);
    }

    public void returnRent(Long rentalId) {
        rentalRepository.returnRent(rentalId);
    }
}
