package com.nebulahotel.repository

import com.nebulahotel.model.Pagamentos
import org.springframework.data.mongodb.repository.MongoRepository

interface PagamentosRepository : MongoRepository<Pagamentos, Int>
