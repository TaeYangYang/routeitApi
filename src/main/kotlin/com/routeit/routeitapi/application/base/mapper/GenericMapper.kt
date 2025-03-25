package com.routeit.routeitapi.application.base.mapper

import org.mapstruct.BeanMapping
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

/**
 * Entity, Dto간 변환을 위한 Generic 인터페이스
 */
interface GenericMapper<D, E> {

  fun toDto(entity: E): D
  fun toEntity(dto: D): E

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  fun updateEntityFromDto(dto: D, @MappingTarget entity: E)
}