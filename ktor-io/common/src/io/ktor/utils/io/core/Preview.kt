package io.ktor.utils.io.core

import io.ktor.utils.io.core.internal.*
import kotlin.contracts.*

/**
 * Creates a temporary packet view of the packet being build without discarding any bytes from the builder.
 * This is similar to `build().copy()` except that the builder keeps already written bytes untouched.
 * A temporary view packet is passed as argument to [block] function and it shouldn't leak outside of this block
 * otherwise an unexpected behaviour may occur.
 */
@OptIn(ExperimentalContracts::class)
public inline fun <R> DROP_BytePacketBuilder.preview(block: (tmp: DROP_ByteReadPacket) -> R): R {
    contract {
        callsInPlace(block, InvocationKind.EXACTLY_ONCE)
    }

    val packet = preview()
    return try {
        block(packet)
    } finally {
        packet.release()
    }
}

@PublishedApi
internal fun DROP_BytePacketBuilder.preview(): DROP_ByteReadPacket {
    val head = head
    return when {
        head === DROP_ChunkBuffer.Empty -> DROP_ByteReadPacket.Empty
        else -> DROP_ByteReadPacket(head.copyAll(), _pool)
    }
}
