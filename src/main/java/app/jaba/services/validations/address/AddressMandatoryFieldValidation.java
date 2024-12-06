package app.jaba.services.validations.address;

import app.jaba.entities.AddressEntity;
import app.jaba.exceptions.AddressMandatoryFieldException;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AddressMandatoryFieldValidation implements CreateAddressValidation {

    @Override
    public void validate(AddressEntity addressEntity) {
        if (Objects.isNull(addressEntity.getUserId())) {
            throw new AddressMandatoryFieldException("User id is required");
        }
        if (Objects.isNull(addressEntity.getStreet())) {
            throw new AddressMandatoryFieldException("Street is required");
        }
        if (Objects.isNull(addressEntity.getNumber())) {
            throw new AddressMandatoryFieldException("Number is required");
        }
        if (Objects.isNull(addressEntity.getCity())) {
            throw new AddressMandatoryFieldException("City is required");
        }
        if (Objects.isNull(addressEntity.getState())) {
            throw new AddressMandatoryFieldException("State is required");
        }
        if (Objects.isNull(addressEntity.getZip())) {
            throw new AddressMandatoryFieldException("Country is required");
        }

    }

}
