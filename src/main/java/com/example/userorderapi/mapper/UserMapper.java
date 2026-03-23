package com.example.userorderapi.mapper;

import java.util.List;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.example.userorderapi.dto.UserProfileResponse;
import com.example.userorderapi.dto.UserResponse;
import com.example.userorderapi.dto.UserUpdateRequest;
import com.example.userorderapi.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phone", source = "phone")
    User toUser(UserUpdateRequest request);

    UserResponse toUserResponse(User user);

    UserProfileResponse toUserProfileResponse(User user);

    List<UserResponse> toUserResponses(List<User> users);
}
