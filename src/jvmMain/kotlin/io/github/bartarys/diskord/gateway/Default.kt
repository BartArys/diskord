package io.github.bartarys.diskord.gateway

import com.beust.klaxon.*
import io.github.bartarys.diskord.gateway.entity.*
import kotlinx.io.core.ByteReadPacket

actual fun PayloadWriter.Companion.default(): PayloadWriter = MoshiPayloadSerializer
actual fun PayloadReader.Companion.default(): PayloadReader = MoshiPayloadSerializer

private object MoshiPayloadSerializer : PayloadWriter, PayloadReader, Converter {
    private val klaxon = Klaxon().converter(OpcodeConverter).converter(this)

    override suspend fun read(packet: ByteReadPacket): Payload<*> {
        return klaxon.parse(packet.readText())!!
    }

    override suspend fun <T : Any> write(payload: T): ByteReadPacket {
        val json = klaxon.toJsonString(payload)
        return ByteReadPacket(json.toByteArray())
    }

    override fun canConvert(cls: Class<*>): Boolean = cls.isAssignableFrom(Payload::class.java)

    @Throws(Nothing::class)
    override fun fromJson(jv: JsonValue): Any? {
        val obj = jv.obj!!
        val opCode = OpcodeConverter.toOpCode(obj.int("op") ?: throw KlaxonException("expected op code"))
        val eventName = obj.string("t")


        when (opCode) {
            OpCode.Hello -> return HelloPayload(obj.obj("d")!!.parseData())
            OpCode.Heartbeat -> return HeartBeatPayload(obj.string("d")!!)
            OpCode.HeartbeatACK -> return HeartBeatAckPayload(null)
            OpCode.Identify -> return IdentifyPayload(obj.obj("d")!!.parseData())
            OpCode.Dispatch -> when (eventName) {
                "READY" -> return ReadyPayload(obj.obj("d")!!.parseData())
                "RESUMED" -> return ResumedPayload(obj.obj("d")?.parseData())
            }
        }

        throw KlaxonException("couldn't find a suitable payload")
    }

    inline fun <reified T> JsonObject.parseData() = klaxon.fromJsonObject(this, T::class.java, T::class) as T

    override fun toJson(value: Any): String = DefaultConverter(klaxon, hashMapOf()).toJson(value)

//
//    @ToJson
//    fun toJson(payload: HeartBeatPayload): JsonPayload {
//        return JsonPayload(payload.opCode.id, payload.data)
//    }
//
//    @ToJson
//    fun toJson(payload: ResumedPayload): JsonPayload {
//        return JsonPayload(payload.opCode.id, payload.data, payload.eventName, payload.sequenceNumber)
//    }
//
//    @ToJson
//    fun toJson(payload: HelloPayload): JsonPayload {
//        return JsonPayload(payload.opCode.id, payload.data)
//    }
//
//    @ToJson
//    fun toJson(payload: HeartBeatAckPayload): JsonPayload {
//        return JsonPayload(payload.opCode.id, payload.data, payload.eventName, payload.sequenceNumber)
//    }

//    @ToJson
//    fun toJson(payload: IdentifyPayload): JsonPayload {
//        return JsonPayload(payload.opCode.id, payload.data)
//    }

//    @FromJson
//    fun fromJson(reader: JsonReader): Payload<*> {
//        reader.beginObject()
//        lateinit var opCode: OpCode
//        lateinit var eventName: String
//
//        while (reader.hasNext()) {
//            when (reader.nextName()) {
//                "op" -> opCode = fromJson(reader.nextInt())
//                "t" -> eventName = reader.nextString()
//                "d" -> when (opCode) {
//                    OpCode.Hello -> return HelloPayload(reader.parseData())
//                    OpCode.Heartbeat -> return HeartBeatPayload(reader.nextString())
//                    OpCode.HeartbeatACK -> return HeartBeatAckPayload(null)
//                    OpCode.Dispatch -> when (eventName) {
//                        "READY" -> return ReadyPayload(reader.parseData())
//                    }
//                }
//            }
//        }
//
//        throw IllegalArgumentException("couldn't parse opcode: ${opCode.id}")
//    }

//    private inline fun <reified T> JsonReader.parseData(): T = moshi.adapter<T>(T::class.java).fromJson(this)!!

//    @FromJson
//    fun fromJson(opCode: Int): OpCode {
//        return when (opCode) {
//            0 -> OpCode.Dispatch
//            1 -> OpCode.Heartbeat
//            2 -> OpCode.Identify
//            3 -> OpCode.StatusUpdate
//            4 -> OpCode.VoiceStateUpdate
//            6 -> OpCode.Resume
//            7 -> OpCode.Reconnect
//            8 -> OpCode.RequestGuildMembers
//            9 -> OpCode.InvalidSession
//            10 -> OpCode.Hello
//            11 -> OpCode.HeartbeatACK
//            else -> throw IllegalArgumentException("opcode $opCode not recognized")
//        }
//    }

}

internal object OpcodeConverter : Converter {
    override fun canConvert(cls: Class<*>): Boolean = cls.isAssignableFrom(OpCode::class.java)

    override fun fromJson(jv: JsonValue): Any? {
        val opCode = jv.int ?: throw IllegalArgumentException("expect int but got $jv")
        return toOpCode(opCode)
    }

    fun toOpCode(opCode: Int): OpCode {
        return when (opCode) {
            0 -> OpCode.Dispatch
            1 -> OpCode.Heartbeat
            2 -> OpCode.Identify
            3 -> OpCode.StatusUpdate
            4 -> OpCode.VoiceStateUpdate
            6 -> OpCode.Resume
            7 -> OpCode.Reconnect
            8 -> OpCode.RequestGuildMembers
            9 -> OpCode.InvalidSession
            10 -> OpCode.Hello
            11 -> OpCode.HeartbeatACK
            else -> throw IllegalArgumentException("opcode $opCode not recognized")
        }
    }

    override fun toJson(value: Any): String = (value as OpCode).id.toString()
}
