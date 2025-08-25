package io.github.ravenzip.composia.function

internal fun <T> searchElementsByQuery(
    source: Collection<T>,
    sourceItemToString: (T) -> String,
    query: String,
): List<T> {
    val loverCaseValue = query.lowercase()
    return source.filter { item -> sourceItemToString(item).lowercase().contains(loverCaseValue) }
}
