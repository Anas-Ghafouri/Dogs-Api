package com.polaris.model.mapper;

import com.polaris.model.dto.DogRequest;
import com.polaris.model.dto.DogResponse;
import com.polaris.model.entity.Dog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jsr330")
public interface DogMapper {

    DogResponse entityToResponse(Dog entity);

    void updateEntityFromRequest(DogRequest request, @MappingTarget Dog dog);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Dog requestToEntity(DogRequest dogRequest);
}
