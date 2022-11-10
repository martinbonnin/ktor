/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.io

import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.errors.*

public class Packet : Closeable {
    private val state = ArrayDeque<ReadableBuffer>()
    private var writeBuffer: Buffer = createBuffer()

    public var availableForRead: Int = state.sumOf { it.availableForRead }
        private set

    public fun peek(): ReadableBuffer {
        return state.first()
    }

    public fun readBuffer(): ReadableBuffer {
        val result = state.first()
        state.removeFirst()
        availableForRead -= result.availableForRead
        return result
    }

    public fun readByte(): Byte {
        check(availableForRead >= 1) { "Not enough bytes available for readByte: $availableForRead" }
        val result = state.first().readByte()
        availableForRead--
        if (state.first().availableForRead == 0) {
            state.removeFirst()
        }
        return result
    }

    public fun readLong(): Long {
        check(availableForRead >= 8) { "Not enough bytes available for readLong: $availableForRead" }
        val result = state.first().readLong()
        availableForRead -= 8
        if (state.first().availableForRead == 0) {
            state.removeFirst()
        }
        return result
    }

    public fun readInt(): Int {
        check(availableForRead >= 4) { "Not enough bytes available for readInt: $availableForRead" }
        val result = state.first().readInt()
        availableForRead -= 4
        if (state.first().availableForRead == 0) {
            state.removeFirst()
        }
        return result
    }

    public fun readShort(): Short {
        check(availableForRead >= 2) { "Not enough bytes available for readShort: $availableForRead" }
        val result = state.first().readShort()
        availableForRead -= 2
        if (state.first().availableForRead == 0) {
            state.removeFirst().close()
        }
        return result
    }

    public fun discard(limit: Int): Int {
        if (limit > availableForRead) {
            val result = availableForRead
            close()
            return result
        }

        var remaining = limit
        while (remaining > 0) {
            val current = state.first()
            if (current.availableForRead > remaining) {
                current.discard(remaining)
                break
            }

            state.removeFirst()
            current.close()
            remaining -= current.availableForRead
        }

        return limit
    }

    public fun writeBuffer(buffer: ReadableBuffer) {
        state.addLast(buffer)
        writeBuffer = Buffer.Empty
        availableForRead += buffer.availableForRead
    }

    public fun writeByte(value: Byte) {
        prepareWriteBuffer().writeByte(value)
        availableForRead += 1
    }

    private fun prepareWriteBuffer(count: Int = 1): Buffer {
        if (writeBuffer.availableForWrite < count) {
            writeBuffer = createBuffer()
            state.addLast(writeBuffer)
        }

        return writeBuffer
    }

    private fun createBuffer(): Buffer = ByteArrayBuffer(ByteArray(16 * 1024)).apply {
        writeIndex = 0
    }

    public fun writeShort(value: Short) {
        prepareWriteBuffer().writeShort(value)
        availableForRead += 2
    }

    public fun writeInt(value: Int) {
        prepareWriteBuffer().writeInt(value)
        availableForRead += 4
    }

    public fun writeLong(value: Long) {
        prepareWriteBuffer().writeLong(value)
        availableForRead += 8
    }

    public fun toByteArray(): ByteArray {
        val result = ByteArray(availableForRead)
        val arrays = state.map { it.readArray() }

        var offset = 0
        for (array in arrays) {
            array.copyInto(result, offset)
            offset += array.size
        }

        return result
    }

    public fun readByteArray(length: Int): ByteArray {
        TODO()
    }

    public fun writeByteArray(array: ByteArray, offset: Int = 0, length: Int = array.size - offset) {
        val buffer = ByteArrayBuffer(array, offset, length)
        writeBuffer(buffer)
    }

    public fun readString(charset: Charset = Charsets.UTF_8): String {
        TODO("Not yet implemented")
    }

    public fun writeString(
        value: CharSequence,
        offset: Int = 0,
        length: Int = value.length - offset,
        charset: Charset = Charsets.UTF_8
    ) {
        writeString(value.substring(offset, offset + length), charset = charset)
    }

    public fun writeString(
        value: String,
        offset: Int = 0,
        length: Int = value.length - offset,
        charset: Charset = Charsets.UTF_8
    ) {
        if (charset == Charsets.UTF_8) {
            val data = value.encodeToByteArray(offset, offset + length)
            writeByteArray(data)
            return
        }

        TODO("Unsupported charset: $charset")
    }

    public fun readPacket(length: Int): Packet {
        var remaining = length
        val result = Packet()
        while (remaining > state.first().availableForRead) {
            remaining -= state.first().availableForRead
            result.writeBuffer(state.first())
            state.removeFirst()
        }

        if (remaining > 0) {
            result.writeBuffer(state.first().readBuffer(remaining))
        }

        return result
    }

    public fun writePacket(value: Packet) {
        state.addAll(value.state)
        availableForRead += value.availableForRead

        value.state.clear()
        value.availableForRead = 0
    }

    public fun writeUByte(value: UByte) {
        writeByte(value.toByte())
    }

    public fun writeDouble(value: Double) {
        writeLong(value.toBits())
    }

    public fun writeFloat(value: Float) {
        writeInt(value.toBits())
    }

    public fun readDouble(): Double {
        return Double.fromBits(readLong())
    }

    public fun readFloat(): Float {
        return Float.fromBits(readInt())
    }

    public fun readLine(charset: Charset = Charsets.UTF_8): String? {
        TODO()
    }

    public fun discardExact(count: Int): Int {
        if (count > availableForRead) {
            throw EOFException("Not enough bytes available for discardExact: $availableForRead")
        }
        return discard(count)
    }

    public fun steal(): Packet {
        return Packet().also { it.writePacket(this) }
    }

    public fun clone(): Packet {
        val result = Packet()
        state.forEach { result.writeBuffer(it.clone()) }
        return result
    }

    override fun close() {
        availableForRead = 0
        state.forEach { it.close() }
        state.clear()
    }

    public companion object {
        public val Empty: Packet = Packet()
    }
}
