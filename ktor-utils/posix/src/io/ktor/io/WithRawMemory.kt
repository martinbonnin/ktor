/*
 * Copyright 2014-2022 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

package io.ktor.io

import kotlinx.cinterop.*

public interface WithRawMemory {
    public fun raw(block: (address: CPointer<*>, length: Int) -> Int): Int
}
