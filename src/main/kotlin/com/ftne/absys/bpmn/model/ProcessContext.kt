package com.ftne.absys.bpmn.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.encodeToMap

@ExperimentalSerializationApi
@Serializable
data class ProcessContext(
    val type: String,
    val number4Fact: Short
) {
    val asMap: Map<String, Any> by lazy { Properties.encodeToMap(this) }
}
