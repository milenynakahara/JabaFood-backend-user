package app.jaba.services;

import app.jaba.entities.AddressEntity;
import app.jaba.exceptions.SaveAddressException;
import app.jaba.repositories.AddressRepository;
import app.jaba.services.validations.CreateAddressValidation;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
public class AddressService {
    AddressRepository repository;
    List<CreateAddressValidation> validations;

    public AddressEntity save(AddressEntity addressEntity) {
        validations.forEach(validation -> validation.validate(addressEntity));
        return repository.save(addressEntity)
                .orElseThrow(() -> new SaveAddressException("Error saving address"));
    }

    public AddressEntity update(UUID userId, AddressEntity addressEntity) {
        validations.forEach(validation -> validation.validate(AddressEntity.builder().userId(userId).build()));

        if (Objects.isNull(addressEntity)) {
            repository.deleteByUserId(userId);
            return addressEntity;
        }

        addressEntity.setUserId(userId);
        var address = repository.findByUserId(userId);
        if (address.isEmpty()) {
            return save(addressEntity);
        }

        return repository.update(addressEntity)
                .orElseThrow(() -> new SaveAddressException("Error updating address"));
    }

    public void deleteByUserId(UUID userId) {
        repository.deleteByUserId(userId);
    }

}
