package com.umc.study.controller;

import com.umc.study.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    public String createRent(@RequestBody Map<String, Object> body){
        rentalService.createRent(body);
        return "도서 대여 기록 등록이 완료되었습니다!";
    }

    @PatchMapping("/{rentalId}/return")
    public String returnRent(@PathVariable Long rentalId) {
        rentalService.returnRent(rentalId);
        return "도서 반납 처리가 완료되었습니다!";
    }
}
