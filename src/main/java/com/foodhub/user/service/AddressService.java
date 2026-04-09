package com.foodhub.user.service;

import com.foodhub.user.dto.AddressRequest;
import com.foodhub.user.entity.Address;
import com.foodhub.user.entity.User;
import com.foodhub.user.repository.AddressRepository;
import com.foodhub.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findByUserIdOrderByIsDefaultDescCreatedAtDesc(userId);
    }

    @Transactional
    public Address addAddress(Long userId, AddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (request.getIsDefault()) {
            addressRepository.clearDefaultForUser(userId);
        }

        Address address = new Address();
        address.setUser(user);
        address.setAddressType(request.getAddressType());
        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setLandmark(request.getLandmark());
        address.setIsDefault(request.getIsDefault());
        address.setLabel(request.getLabel());

        return addressRepository.save(address);
    }

    @Transactional
    public Address updateAddress(Long addressId, Long userId, AddressRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        if (request.getIsDefault() && !address.getIsDefault()) {
            addressRepository.clearDefaultForUser(userId);
        }

        address.setAddressType(request.getAddressType());
        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPincode(request.getPincode());
        address.setLandmark(request.getLandmark());
        address.setIsDefault(request.getIsDefault());
        address.setLabel(request.getLabel());

        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long addressId, Long userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        addressRepository.delete(address);
    }

    @Transactional
    public Address setDefaultAddress(Long addressId, Long userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        addressRepository.clearDefaultForUser(userId);
        address.setIsDefault(true);
        return addressRepository.save(address);
    }
}