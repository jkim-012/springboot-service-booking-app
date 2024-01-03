package com.example.bookingsystem.service.dto;

import com.example.bookingsystem.business.domain.Business;
import com.example.bookingsystem.business.dto.BusinessDetailDto;
import com.example.bookingsystem.service.domain.ServiceItem;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceItemDetailDto{

    private Long ServiceItemId;
    private String description;
    private Double price;
    private Integer duration;
    private BusinessDetailDto businessDetailDto;

    // ServiceItem entity -> ServiceItemDetailDto
    public static ServiceItemDetailDto of(ServiceItem serviceItem) {
        return ServiceItemDetailDto.builder()
                .ServiceItemId(serviceItem.getId())
                .description(serviceItem.getDescription())
                .price(serviceItem.getPrice())
                .businessDetailDto(BusinessDetailDto.of(serviceItem.getBusiness()))
                .build();
    }
}
