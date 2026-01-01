package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Address;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import com.cosmeticPlatform.CosmeticPlatform.model.request.AddressRequestDTO;
import com.cosmeticPlatform.CosmeticPlatform.repository.AddressRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public Address addAddress(AddressRequestDTO addressRequestDTO) {
        User user = userRepository.findById(addressRequestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = new Address();
        address.setTitle(addressRequestDTO.getTitle());
        address.setCity(addressRequestDTO.getCity());
        address.setDistrict(addressRequestDTO.getDistrict());
        address.setFullAddress(addressRequestDTO.getFullAddress());
        address.setUser(user);

        return addressRepository.save(address);
    }

    public List<Address> getUserAddresses(Integer userId) {
        return addressRepository.findByUserIdAndActiveTrue(userId);
    }

    public void deleteAddress(Long addressId) {
        addressRepository.findById(addressId).ifPresent(address -> {
            address.setActive(false);
            addressRepository.save(address);
        });
    }
}
