package com.routeit.routeitapi.application.user.mapper

import com.routeit.routeitapi.application.base.mapper.GenericMapper
import com.routeit.routeitapi.application.user.dto.UserDto
import com.routeit.routeitapi.application.user.entity.User
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
interface UserMapper: GenericMapper<UserDto, User>{
}