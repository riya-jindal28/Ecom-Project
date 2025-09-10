package com.ecomm.Project.Service;

import java.util.List;

import com.ecomm.Project.Model.User;
import com.ecomm.Project.Payload.AddressDTO;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressesById(Long addressId);

    List<AddressDTO> getUserAddresses(User user);

    AddressDTO updateAddress(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}
