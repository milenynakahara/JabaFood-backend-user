package app.jaba.services;

import app.jaba.entities.AddressEntity;
import app.jaba.exceptions.AddressNotFoundException;
import app.jaba.exceptions.SaveAddressException;
import app.jaba.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Transactional
public class AddressService {
    AddressRepository repository;


    public AddressEntity save(AddressEntity addressEntity) {
        // TODO: validations
        return repository.save(addressEntity).orElseThrow(() -> new SaveAddressException("Error saving address"));
    }

    public AddressEntity update(AddressEntity addressEntity) {
        // TODO: validations
        if(Objects.nonNull(addressEntity) && Objects.isNull(addressEntity.getUserId())) {
            throw new SaveAddressException("User id is required");
        }

        var address = repository.findByUserId(addressEntity.getUserId()).orElseThrow(() -> new AddressNotFoundException("Address not found"));
        addressEntity.setId(address.getId());
        return repository.update(addressEntity).orElseThrow(() -> new SaveAddressException("Error updating address"));
    }

    public void deleteByUserId(UUID userId) {
        repository.deleteByUserId(userId);
    }

}
