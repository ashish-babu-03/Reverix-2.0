package com.reverix.reverix.domain.port.output

import com.reverix.reverix.domain.model.Theatre

interface TheatreRepository {
    fun findById(id: Long): Theatre?
}
