package app.jaba.mappers;

import app.jaba.dtos.AddressDto;
import app.jaba.entities.AddressEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressEntity map(AddressDto addressDto);

    AddressDto map(AddressEntity addressEntity);
}
