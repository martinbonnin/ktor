/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.io

import kotlin.math.*

public class ConcatBuffer(
    public val buffers: ArrayDeque<Buffer>
) : ReadableBuffer {
    override var capacity: Int = buffers.sumOf { it.availableForRead }
    override var writeIndex: Int = capacity
    override var readIndex: Int = 0

    override fun getByteAt(index: Int): Byte {
        var bufferIndex = 0
        var offset = 0

        while (buffers[bufferIndex].availableForRead < index - offset) {
            offset += buffers[bufferIndex].availableForRead
            bufferIndex++
        }

        return buffers[bufferIndex].getByteAt(index - offset)
    }

    public fun appendLast(buffer: Buffer) {
        buffers.add(buffer)
    }

    public fun discardFirst(): Buffer {
        val result = buffers.removeFirst()
        capacity -= result.availableForRead
        readIndex = max(0, readIndex - result.availableForRead)

        return result
    }

    override fun close() {
        buffers.forEach { it.close() }
    }
}
