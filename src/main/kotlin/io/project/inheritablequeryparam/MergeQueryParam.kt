package io.project.inheritablequeryparam

import kotlin.collections.LinkedHashMap

class MergeQueryParam {

    fun mergeQueryParam(
            currentQueryParam: String?,
            parentQueryParam: String?,
            isInheritable: Boolean
    ): String {
        var mergedQueryParam: String
        var mergedMap: MutableMap<String, String> = LinkedHashMap()
        val currentQueryParamMap: MutableMap<String, String> = LinkedHashMap()

        val currentQueryParam = currentQueryParam ?: ""
        val parentQueryParam = parentQueryParam ?: ""

        if (isInheritable) mergedMap = setParentQueryParam(parentQueryParam, mergedMap)

        currentQueryParam?.splitToSequence("&")
                ?.map { it.split("=") }
                ?.filter { it.size == 2 }
                ?.filter { (key, _) -> !currentQueryParamMap.containsKey(key) }
                ?.forEach { (key, value) ->
                    currentQueryParamMap.putIfAbsent(key, value)
                    mergedMap.merge(key, value) { oldValue, newValue -> "$oldValue,$newValue" }
                }

        mergedQueryParam = buildQueryString(mergedMap)
        return mergedQueryParam
    }

    private fun setParentQueryParam(parentQueryParam: String?, mergedMap: MutableMap<String, String> = LinkedHashMap()): MutableMap<String, String> {
        parentQueryParam?.splitToSequence("&")
                ?.map { it.split("=") }
                ?.filter { it.size == 2 }
                ?.forEach { (key, value) ->
                    mergedMap.putIfAbsent(key, value)
                }

        return mergedMap
    }

    private fun buildQueryString(mergedMap: Map<String, String>): String =
        mergedMap.entries.joinToString("&") {
            (key, value) -> val valueSet = value.split(",").toMutableSet()
            "$key=${valueSet.joinToString(",")}"
    }

}