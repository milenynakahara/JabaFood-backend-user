package app.jaba.mappers;

import app.jaba.dtos.AddressDto;
import app.jaba.entities.AddressEntity;
import org.mapstruct.Mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressEntity map(AddressDto addressDto);

    AddressDto map(AddressEntity addressEntity);

    default AddressEntity map(ResultSet rs) throws SQLException {

        var addressId = rs.getObject("address_id", UUID.class);
        if (addressId != null) {
            AddressEntity address = new AddressEntity();
            address.setId(addressId);
            address.setStreet(rs.getString("address_street"));
            address.setCity(rs.getString("address_city"));
            address.setState(rs.getString("address_state"));
            address.setZip(rs.getString("address_zip"));
            address.setNumber(rs.getString("address_number"));
            return address;
        }

        return null;
    }
}
