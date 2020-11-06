package com.github.liulus.yurt.model

import java.time.LocalDateTime
import javax.persistence.Table

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/6
 */
@Table(name = "sign_product")
data class Goods(
        var id: Long = 0L,
        var code: String = "",
) {

    var fullName: String? = null
    var inventory: Int? = null
    var price: Double? = null
    var gmtCreated: LocalDateTime? = null
}

