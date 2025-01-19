package app.jaba.services.validations.address;

import app.jaba.entities.AddressEntity;

public interface CreateAddressValidation {
    void validate(AddressEntity addressEntity);
}
