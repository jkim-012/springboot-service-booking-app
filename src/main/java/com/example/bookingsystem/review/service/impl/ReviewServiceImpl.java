package com.example.bookingsystem.review.service.impl;

import com.example.bookingsystem.booking.domain.Booking;
import com.example.bookingsystem.booking.domain.BookingStatus;
import com.example.bookingsystem.booking.repository.BookingRepository;
import com.example.bookingsystem.exception.BookingNotFoundException;
import com.example.bookingsystem.exception.ReviewAlreadyExistException;
import com.example.bookingsystem.exception.ReviewNotFoundException;
import com.example.bookingsystem.exception.UnauthorizedUserException;
import com.example.bookingsystem.member.domain.Member;
import com.example.bookingsystem.review.domain.Review;
import com.example.bookingsystem.review.dto.NewReviewDto;
import com.example.bookingsystem.review.dto.ReviewDetailDto;
import com.example.bookingsystem.review.dto.UpdateReviewDto;
import com.example.bookingsystem.review.repository.ReviewRepository;
import com.example.bookingsystem.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ReviewDetailDto createReview(Long bookingId, NewReviewDto newReviewDto, Member member) {
        // find the booking
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new BookingNotFoundException("Booking not found with ID: " + bookingId));
        // check authorization (currently logged-in user should be the owner of the booking)
        if (!booking.getMember().getId().equals(member.getId())) {
            throw new UnauthorizedUserException("Unauthorized: You do not have permission to write a review for the booking");
        }
        // check if there is already a review for the booking id
        Optional<Review> byBookingId = reviewRepository.findByBookingId(bookingId);
        if (byBookingId.isPresent()) {
            throw new ReviewAlreadyExistException("A review already exists for the booking.");
        }
        // check the booking status
        if (!booking.getStatus().equals(BookingStatus.COMPLETED)){
            throw new UnauthorizedUserException("Check the booking status: " + booking.getStatus());
        }
        // create review
        Review review = Review.builder()
                .title(newReviewDto.getTitle())
                .content(newReviewDto.getContent())
                .rate(newReviewDto.getRate())
                .booking(booking)
                .build();
        // save
        reviewRepository.save(review);
        return ReviewDetailDto.of(review);
    }

    @Override
    @Transactional
    public ReviewDetailDto updateReview(Long reviewId, UpdateReviewDto updateReviewDto, Member member) {
        // find the review
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()-> new ReviewNotFoundException("Booking not found with ID: " + reviewId));
        // check authorization (currently logged-in user should be the author of the review)
        if (!review.getBooking().getMember().getId().equals(member.getId())) {
            throw new UnauthorizedUserException("Unauthorized: You do not have permission to update the review");
        }
        // update
        review.changeReviewDetails(updateReviewDto);
        return ReviewDetailDto.of(review);
    }

    @Override
    public ReviewDetailDto getReview(Long reviewId) {
        // find the review
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(()-> new ReviewNotFoundException("Booking not found with ID: " + reviewId));
        return ReviewDetailDto.of(review);
    }

    @Override
    public Page<Review> getAllReviewsByServiceName(String keyword, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByBooking_ServiceItem_NameContaining(pageable, keyword);
        return reviews;
    }

    @Override
    public Page<Review> getAllReviewsByBusiness(Pageable pageable, Long businessId) {
        Page<Review> reviews = reviewRepository.findAllByBooking_Business_Id(pageable, businessId);
        return reviews;
    }

}
