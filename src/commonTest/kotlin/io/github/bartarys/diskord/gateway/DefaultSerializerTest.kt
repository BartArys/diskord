package io.github.bartarys.diskord.gateway

import io.github.bartarys.diskord.gateway.data.HelloData
import io.github.bartarys.diskord.gateway.data.IdentifyData
import io.github.bartarys.diskord.gateway.data.IdentifyProperties
import io.github.bartarys.diskord.gateway.data.ResumedData
import io.github.bartarys.diskord.gateway.entity.*
import io.github.bartarys.diskord.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultSerializerTest {

    @Test
    fun `can serialize HelloPayload`() = runTest {
        val payload = HelloPayload(HelloData(5, listOf("test")))
        val json = PayloadWriter.default().write(payload)
        val jsonPayload = PayloadReader.default().read(json)
        assertEquals(payload, jsonPayload, "payload and serialized version aren't equal")
    }

    @Test
    fun `can serialize HeartBeatPayload`() = runTest {
        val payload = HeartBeatPayload("sequence number")
        val json = PayloadWriter.default().write(payload)
        val jsonPayload = PayloadReader.default().read(json)
        assertEquals(payload, jsonPayload, "payload and serialized version aren't equal")
    }

    @Test
    fun `can serialize HeartBeatAckPayload`() = runTest {
        val payload = HeartBeatAckPayload(null)
        val json = PayloadWriter.default().write(payload)
        val jsonPayload = PayloadReader.default().read(json)
        assertEquals(payload, jsonPayload, "payload and serialized version aren't equal")
    }

    @Test
    fun `can serialize IdentifyPayload`() = runTest {
        val payload = IdentifyPayload(IdentifyData("", IdentifyProperties("os", "browser", "device")))
        val json = PayloadWriter.default().write(payload)
        val jsonPayload = PayloadReader.default().read(json)
        assertEquals(payload, jsonPayload, "payload and serialized version aren't equal")
    }

    @Test
    fun `can serialize ResumedPayload`() = runTest {
        val payload = ResumedPayload(ResumedData(emptyList()))
        val json = PayloadWriter.default().write(payload)
        val jsonPayload = PayloadReader.default().read(json)
        assertEquals(payload, jsonPayload, "payload and serialized version aren't equal")
    }

}