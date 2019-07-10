package com.joker.core.ext

import java.text.SimpleDateFormat
import java.util.*

/**
 * DateTimeExt
 *
 * @author joker
 * @date 2019/6/15.
 */

const val PATTERN_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
const val PATTERN_yyyyMMdd = "yyyy-MM-dd"

/**
 *  字符串日期格式
 *  @param pattern 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss
 */
fun String.toDateMills(pattern: String = PATTERN_yyyyMMddHHmmss): Long =
    SimpleDateFormat(pattern, Locale.getDefault()).parse(this).time


/**
 * Long类型时间戳转为字符串的日期格式
 * @param pattern 模式，默认是按照yyyy-MM-dd HH:mm:ss来转换
 */
fun Long.toDateString(pattern: String = PATTERN_yyyyMMddHHmmss): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(Date(this))