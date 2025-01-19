package app.jaba.services;

import app.jaba.entities.AddressEntity;
import app.jaba.exceptions.AddressMandatoryFieldException;
import app.jaba.exceptions.SaveAddressException;
import app.jaba.repositories.AddressRepository;
import app.jaba.services.validations.address.CreateAddressValidation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
@Slf4j

public class AddressService {
    AddressRepository repository;
    List<CreateAddressValidation> validations;

    public AddressEntity save(AddressEntity addressEntity) {
        validations.forEach(validation -> validation.validate(addressEntity));
        return repository.save(addressEntity)
                .orElseThrow(() -> new SaveAddressException("Error saving address"));
    }

    public AddressEntity update(UUID userId, AddressEntity addressEntity) {
        if (Objects.isNull(userId)) {
            throw new AddressMandatoryFieldException("User id is required");
        }

        if (Objects.isNull(addressEntity)) {
            throw new AddressMandatoryFieldException("Address is required");
        }
        addressEntity.setUserId(userId);
        validations.forEach(validation -> validation.validate(addressEntity));

        addressEntity.setId(repository.findByUserId(userId).map(AddressEntity::getId).orElse(null));
        return repository.update(addressEntity)
                .orElseThrow(() -> new SaveAddressException("Error updating address"));
    }

}
