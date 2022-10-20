package io.ktor.utils.io.charsets

import io.ktor.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import java.nio.*
import java.nio.charset.*

@Suppress("NO_ACTUAL_CLASS_MEMBER_FOR_EXPECTED_CLASS")
public actual typealias Charset = java.nio.charset.Charset

public actual val Charset.name: String get() = name()

public actual typealias CharsetEncoder = java.nio.charset.CharsetEncoder

public actual val CharsetEncoder.charset: Charset get() = charset()

public actual fun CharsetEncoder.encodeToByteArray(input: CharSequence, fromIndex: Int, toIndex: Int): ByteArray {
    @Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")
    if (input is String) {
        if (fromIndex == 0 && toIndex == input.length) {
            return (input as java.lang.String).getBytes(charset())
        }
        return (input.substring(fromIndex, toIndex) as java.lang.String).getBytes(charset())
    }

    return encodeToByteArraySlow(input, fromIndex, toIndex)
}

private fun CharsetEncoder.encodeToByteArraySlow(input: CharSequence, fromIndex: Int, toIndex: Int): ByteArray {
    val result = encode(CharBuffer.wrap(input, fromIndex, toIndex))

    val existingArray = when {
        result.hasArray() && result.arrayOffset() == 0 -> result.array().takeIf { it.size == result.remaining() }
        else -> null
    }

    return existingArray ?: ByteArray(result.remaining()).also { result.get(it) }
}

public actual fun CharsetEncoder.encodeUTF8(input: Packet, dst: Packet) {
    TODO()
}

public actual typealias CharsetDecoder = java.nio.charset.CharsetDecoder

public actual val CharsetDecoder.charset: Charset get() = charset()!!

public actual fun CharsetDecoder.decode(input: Packet, dst: Appendable, max: Int): Int {
    TODO()
}

public actual fun CharsetDecoder.decodeExactBytes(input: Packet, inputLength: Int): String {
    TODO()
}

private fun CoderResult.throwExceptionWrapped() {
    try {
        throwException()
    } catch (original: java.nio.charset.MalformedInputException) {
        throw MalformedInputException(original.message ?: "Failed to decode bytes")
    }
}

// ----------------------------------

public actual typealias Charsets = kotlin.text.Charsets

@Suppress("ACTUAL_WITHOUT_EXPECT")
public actual open class MalformedInputException
actual constructor(message: String) : java.nio.charset.MalformedInputException(0) {
    private val _message = message

    override val message: String?
        get() = _message
}

private val EmptyCharBuffer = CharBuffer.allocate(0)
private val EmptyByteBuffer = ByteBuffer.allocate(0)!!

internal actual fun CharsetEncoder.encodeImpl(
    input: CharSequence,
    fromIndex: Int,
    toIndex: Int,
    dst: Packet
): Int {
    TODO("Not yet implemented")
}

internal actual fun CharsetEncoder.encodeComplete(dst: Packet): Boolean {
    TODO("Not yet implemented")
}

internal actual fun CharsetDecoder.decodeBuffer(
    input: Packet,
    out: Appendable,
    lastBuffer: Boolean,
    max: Int
): Int {
    TODO()
}
