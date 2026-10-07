package com.umc.study.service;

import com.umc.study.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RentalService {
    private final RentalRepository rentalRepository;

    public void createRental(Long userId, Long bookId) {
        rentalRepository.save(userId, bookId);
    }

    // 수정 행이 있으면 true, 없으면 false
    public boolean returnRental(Long rentalId) {
        return rentalRepository.markReturned(rentalId) > 0;
    }
}