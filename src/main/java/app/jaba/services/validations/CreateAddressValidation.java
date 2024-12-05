package app.jaba.services.validations;

import app.jaba.entities.AddressEntity;

public interface CreateAddressValidation {
    void validate(AddressEntity addressEntity);
}
