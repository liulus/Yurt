package com.github.liulus.yurt.model

import com.github.liulus.yurt.convention.data.PageQuery
import java.time.LocalDateTime

/**
 *
 * @author liulu
 * @version V1.0
 * @since 2020/11/6
 */
class GoodsQuery : PageQuery() {

    var fullName: String? = null
    var code: String? = null
    var inventory: Int? = null
    var price: Double? = null
    var startTime: LocalDateTime? = null
}