package io.github.bartarys.diskord.gateway

import kotlinx.coroutines.delay
import java.time.Duration

actual sealed class Retry {
    actual abstract val continueRetry: Boolean
    actual abstract suspend fun failure()
    actual abstract fun success()

    actual object None : Retry() {
        override val continueRetry: Boolean
            get() = false

        override suspend fun failure() {}
        override fun success() {}
    }

    actual class LinearBackoff(
        private val limit: Duration,
        private val backoffIncrement: Duration,
        private val backoffStart: Duration = backoffIncrement
    ) : Retry() {
        actual constructor() : this(Duration.ofSeconds(120), Duration.ofSeconds(5))

        private var currentLimit = backoffStart

        override val continueRetry: Boolean
            get() = currentLimit <= limit

        override suspend fun failure() {
            delay(currentLimit.toMillis())
            currentLimit += backoffIncrement
        }

        override fun success() {
            currentLimit = backoffStart
        }

    }

    actual class ExponentialBackoff(
        private val limit: Duration,
        private val backoffStart: Duration,
        private val transform: (Duration) -> Duration = { Duration.ofMillis(it.toMillis() * it.toMillis()) }
    ) : Retry() {
        actual constructor() : this(Duration.ofSeconds(120), Duration.ofSeconds(5))

        private var currentLimit = backoffStart

        override val continueRetry: Boolean
            get() = currentLimit <= limit

        override suspend fun failure() {
            delay(currentLimit.toMillis())
            currentLimit = transform(currentLimit)
        }

        override fun success() {
            currentLimit = backoffStart
        }

    }

}