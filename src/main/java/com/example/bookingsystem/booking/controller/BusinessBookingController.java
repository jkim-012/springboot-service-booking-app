package com.example.bookingsystem.booking.controller;

import com.example.bookingsystem.booking.domain.Booking;
import com.example.bookingsystem.booking.domain.BookingStatus;
import com.example.bookingsystem.booking.dto.UpdateBookingDto;
import com.example.bookingsystem.booking.dto.business.BusinessBookingDetailDto;
import com.example.bookingsystem.booking.dto.business.BusinessBookingListDto;
import com.example.bookingsystem.booking.service.BusinessBookingService;
import com.example.bookingsystem.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class BusinessBookingController {

    private final BusinessBookingService businessService;

    // API endpoint for updating a booking schedule or memo by business (only business can use this feature)
    @PutMapping("/business/bookings/{bookingId}")
    public ResponseEntity<BusinessBookingDetailDto> updateBookingByBusiness(
            @PathVariable Long bookingId,
            @RequestBody UpdateBookingDto updateBookingDto,
            @AuthenticationPrincipal Member member) {

        BusinessBookingDetailDto businessBookingDetailDto =
                businessService.updateBookingByBusiness(bookingId, updateBookingDto, member);
        return ResponseEntity.ok(businessBookingDetailDto);
    }

    // API endpoint for updating booking status(cancel/complete) by business (only business can use this feature)
    @PatchMapping("/business/bookings/{bookingId}/status/{newStatus}")
    public ResponseEntity<BusinessBookingDetailDto> updateBookingStatusByBusiness(
            @PathVariable Long bookingId,
            @PathVariable BookingStatus newStatus,
            @AuthenticationPrincipal Member member) {

        BusinessBookingDetailDto businessBookingDetailDto =
                businessService.updateStatusByBusiness(bookingId, newStatus, member);
        return ResponseEntity.ok(businessBookingDetailDto);
    }

    // API endpoint for reading booking details by business (only business can use this feature)
    @GetMapping("/business/bookings/{bookingId}")
    public ResponseEntity<BusinessBookingDetailDto> getBookingDetailsForBusiness(
            @PathVariable Long bookingId,
            @AuthenticationPrincipal Member member) {

        BusinessBookingDetailDto businessBookingDetailDto =
                businessService.getBookingForBusiness(bookingId, member);
        return ResponseEntity.ok(businessBookingDetailDto);
    }

    // API endpoint for reading booking list for a business (only business can use this feature)
    @GetMapping("/business/{businessId}/bookings/list")
    public ResponseEntity<BusinessBookingListDto> getAllBookingsForBusiness(
            @PathVariable Long businessId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "scheduledAt") String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = "ASC") String sortOrder,
            @AuthenticationPrincipal Member member) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Booking> result = businessService.getAllBookingsForBusiness(businessId, pageable, member);
        return ResponseEntity.ok(BusinessBookingListDto.of(result));
    }

}
