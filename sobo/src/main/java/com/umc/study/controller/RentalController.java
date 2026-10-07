package com.umc.study.controller;

import com.umc.study.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Map;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final RentalService rentalService;

    @PostMapping
    public Map<String, String> createRental(@RequestBody Map<String, Long> body) {
        rentalService.createRental(body.get("userId"), body.get("bookId"));
        return Map.of("message", "대여 기록이 생성되었습니다!");
    }

    @PatchMapping("/{rentalId}/return")
    public Map<String, String> returnRental(
            @PathVariable("rentalId") Long rentalId
    ){
        if(!rentalService.returnRental(rentalId)){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "대여기록을 찾을 수 없습니다."
            );
        }
        return Map.of("message","반납 처리가 완료되었습니다!");
    }

}