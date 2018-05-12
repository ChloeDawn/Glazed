@file:JvmName("Pairs")

package net.insomniakitten.glazed.extensions

infix fun <K, V> K.of(other: V) = other to this

infix fun <K, V> K.from(other: V) = other to this

infix fun <K, V> K.and(other: V) = this to other

infix fun <K, V> K.with(other: V) = this to other

infix fun <K, V> K.on(other: V) = this to other
