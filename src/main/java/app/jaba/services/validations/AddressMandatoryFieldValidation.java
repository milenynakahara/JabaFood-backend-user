package app.jaba.services.validations;

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
    }

}
