package com.foodhub.user.dto;

import com.foodhub.user.entity.AddressType;
import lombok.Data;

@Data
public class AddressRequest {
    private AddressType addressType;
    private String fullName;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;
    private String landmark;
    private Boolean isDefault = false;
    private String label;
}