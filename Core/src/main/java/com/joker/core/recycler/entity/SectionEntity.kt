package com.joker.core.recycler.entity

import java.io.Serializable

/**
 * SectionEntity
 *
 * @author joker
 * @date 2019/1/20.
 */
abstract class SectionEntity<T> : Serializable {
    var isHeader: Boolean = false
    var t: T? = null
    var header: String? = null

    constructor(isHeader: Boolean, header: String) {
        this.isHeader = isHeader
        this.header = header
        this.t = null
    }

    constructor(t: T) {
        this.isHeader = false
        this.header = null
        this.t = t
    }
}