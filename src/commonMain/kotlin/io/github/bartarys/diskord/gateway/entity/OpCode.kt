package io.github.bartarys.diskord.gateway.entity

annotation class Send
annotation class Receive

sealed class OpCode(val id: Int) {

    @Receive
    object Dispatch : OpCode(0)

    @Send
    @Receive
    object Heartbeat : OpCode(1)

    @Send
    object Identify : OpCode(2)

    @Send
    object StatusUpdate : OpCode(3)

    @Send
    object VoiceStateUpdate : OpCode(4)

    @Send
    object Resume : OpCode(6)

    @Receive
    object Reconnect : OpCode(7)

    @Send
    object RequestGuildMembers : OpCode(8)

    @Receive
    object InvalidSession : OpCode(9)

    @Receive
    object Hello : OpCode(10)

    @Receive
    object HeartbeatACK : OpCode(11)
}