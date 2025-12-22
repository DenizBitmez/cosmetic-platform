package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.Address;
import com.cosmeticPlatform.CosmeticPlatform.model.request.AddressRequestDTO;
import com.cosmeticPlatform.CosmeticPlatform.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    private final AddressService addressService;

    @Autowired
    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public Address addAddress(@Valid @RequestBody AddressRequestDTO addressRequestDTO) {
        return addressService.addAddress(addressRequestDTO);
    }

    @GetMapping("/user/{userId}")
    public List<Address> getUserAddresses(@PathVariable Integer userId) {
        return addressService.getUserAddresses(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
    }
}
