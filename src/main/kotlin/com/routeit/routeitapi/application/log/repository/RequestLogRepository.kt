package com.routeit.routeitapi.application.log.repository

import com.routeit.routeitapi.application.log.entity.RequestLog
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RequestLogRepository: MongoRepository<RequestLog, String>