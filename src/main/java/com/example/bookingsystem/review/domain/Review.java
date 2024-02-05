package com.example.bookingsystem.review.domain;


import com.example.bookingsystem.booking.domain.Booking;
import com.example.bookingsystem.review.dto.UpdateReviewDto;
import com.example.bookingsystem.service.domain.ServiceItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;
    private Double rate;

    @ManyToOne
    @JoinColumn(name = "service_item_id")
    private ServiceItem serviceItem;

    @OneToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    // review create & update - date and time
    @Column(nullable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;


    // update review
    public void changeReviewDetails(UpdateReviewDto updateReviewDto) {
        if (updateReviewDto.getTitle() != null){
            this.title = updateReviewDto.getTitle();
        }
        if (updateReviewDto.getContent() != null){
            this.content = updateReviewDto.getContent();
        }
        if (updateReviewDto.getRate() != null){
            this.rate = updateReviewDto.getRate();
        }
    }
}
