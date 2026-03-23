package com.example.userorderapi.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.userorderapi.dto.UserResponse;
import com.example.userorderapi.dto.UserUpdateRequest;
import com.example.userorderapi.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "phone", ignore = true)
    User toUser(UserUpdateRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toUserResponses(List<User> users);
}
