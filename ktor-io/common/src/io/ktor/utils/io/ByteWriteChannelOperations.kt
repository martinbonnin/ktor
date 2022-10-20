/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.utils.io

import io.ktor.io.*
import io.ktor.utils.io.charsets.*

public fun ByteWriteChannel.writeBuffer(buffer: Buffer) {
    writablePacket.writeBuffer(buffer)
}

public fun ByteWriteChannel.writePacket(packet: Packet) {
    writablePacket.writePacket(packet)
}

/**
 * Writes as much as possible and only suspends if buffer is full
 */
public suspend fun ByteWriteChannel.writeAvailable(src: ByteArray, offset: Int, length: Int): Int {
    TODO()
}

/**
 * Writes long number and suspends until written.
 * Crashes if channel get closed while writing.
 */
public suspend fun ByteWriteChannel.writeLong(l: Long) {
    TODO()
}

/**
 * Writes int number and suspends until written.
 * Crashes if channel get closed while writing.
 */
public suspend fun ByteWriteChannel.writeInt(i: Int) {
    TODO()
}

/**
 * Writes short number and suspends until written.
 * Crashes if channel get closed while writing.
 */
public suspend fun ByteWriteChannel.writeShort(s: Short) {
    TODO()
}

/**
 * Writes byte and suspends until written.
 * Crashes if channel get closed while writing.
 */
public suspend fun ByteWriteChannel.writeByte(b: Byte) {
    TODO()
}

/**
 * Writes double number and suspends until written.
 * Crashes if channel get closed while writing.
 */
public suspend fun ByteWriteChannel.writeDouble(d: Double) {
    TODO()
}

/**
 * Writes float number and suspends until written.
 * Crashes if channel get closed while writing.
 */
public suspend fun ByteWriteChannel.writeFloat(f: Float) {
    TODO()
}

public suspend fun ByteWriteChannel.writeByteArray(
    value: ByteArray,
    offset: Int = 0,
    length: Int = value.size - offset
) {
    TODO()
}

public suspend fun ByteWriteChannel.writeString(value: String, charset: Charset = Charsets.UTF_8) {
    TODO("Not yet implemented")
}
