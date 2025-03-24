package com.routeit.routeitapi.util

import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.ReportingPolicy
import org.mapstruct.factory.Mappers

/**
 * Entity, Dto간 변환을 위한 인터페이스
 */
@Mapper(componentModel = "spring")
interface GenericMapper {
  companion object {
    val INSTANCE: GenericMapper = Mappers.getMapper(GenericMapper::class.java)
  }

  fun <E, D> toDto(entity: E): D
  fun <E, D> toEntity(dto: D): E
  fun <E, D> updateEntityFromDto(dto: D, @MappingTarget entity: E)
}