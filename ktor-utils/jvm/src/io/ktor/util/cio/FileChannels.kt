/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.util.cio

import io.ktor.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.jvm.nio.*
import io.ktor.utils.io.pool.*
import kotlinx.coroutines.*
import java.io.*
import java.nio.*
import java.nio.channels.*
import kotlin.coroutines.*

/**
 * Launch a coroutine to open a read channel for a file and fill it.
 * Please note that file reading is blocking so if you are starting it on [Dispatchers.Unconfined] it may block
 * your async code and freeze the whole application when runs on a pool that is not intended for blocking operations.
 * This is why [coroutineContext] should have [Dispatchers.IO] or
 * a coroutine dispatcher that is properly configured for blocking IO.
 */
public fun File.readChannel(
    start: Long = 0,
    endInclusive: Long = -1,
    coroutineContext: CoroutineContext = Dispatchers.IO
): ByteReadChannel {
    val fileLength = length()
    TODO()
}

/**
 * Open a write channel for the file and launch a coroutine to read from it.
 * Please note that file writing is blocking so if you are starting it on [Dispatchers.Unconfined] it may block
 * your async code and freeze the whole application when runs on a pool that is not intended for blocking operations.
 * This is why [coroutineContext] should have [Dispatchers.IO] or
 * a coroutine dispatcher that is properly configured for blocking IO.
 */
public fun File.writeChannel(
    coroutineContext: CoroutineContext = Dispatchers.IO
): ByteWriteChannel = TODO()
