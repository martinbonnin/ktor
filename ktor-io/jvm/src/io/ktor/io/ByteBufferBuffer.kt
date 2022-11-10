/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.io

import java.nio.*

public class ByteBufferBuffer(
    override val state: ByteBuffer
) : Buffer, WithByteBuffer {
    override var writeIndex: Int
        get() = state.limit()
        set(value) {
            state.limit(value)
        }

    override fun setByteAt(index: Int, value: Byte) {
        state.put(index, value)
    }

    override fun readBuffer(size: Int): ReadableBuffer {
        TODO("Not yet implemented")
    }

    override fun readArray(): ByteArray {
        TODO("Not yet implemented")
    }

    override fun clone(): Buffer {
        TODO()
    }

    override var readIndex: Int
        get() = state.position()
        set(value) {
            state.position(value)
        }

    override val capacity: Int
        get() = state.capacity()

    override fun getByteAt(index: Int): Byte {
        return state[index]
    }

    override fun close() {
    }
}
