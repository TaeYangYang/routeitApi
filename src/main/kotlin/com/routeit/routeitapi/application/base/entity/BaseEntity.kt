package com.routeit.routeitapi.application.base.entity

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.Comment
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity{

  @CreationTimestamp
  @Column(updatable = false)
  @Comment("입력일시")
  var createDate: LocalDateTime = LocalDateTime.now()
    protected set

  @CreatedBy
  @Column(updatable = false, length = 100)
  @Comment("입력 유저")
  var createdBy: String? = null
    protected set

  @UpdateTimestamp
  @Comment("수정일시")
  var lastModifiedDate: LocalDateTime = LocalDateTime.now()
    protected set

  @LastModifiedBy
  @Column(length = 100)
  @Comment("수정 유저")
  var lastModifiedBy: String? = null
    protected set
}