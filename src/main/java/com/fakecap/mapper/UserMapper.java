package com.fakecap.mapper;


import com.fakecap.dto.UserDto;
import com.fakecap.model.User;
import org.bson.types.ObjectId;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.CDI,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserDto userDto);

    @Mapping(target = "id", qualifiedByName = "mapToString")
    UserDto toDto(User user);

    @Named("mapToString")
    default String mapToString(ObjectId id) {
        return id.toString();
    }

}
