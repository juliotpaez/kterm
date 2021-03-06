package es.jtp.kterm.logger

import es.jtp.kterm.*


/**
 * Mapper to structure a [Throwable] like a [Logger].
 */
internal object ExceptionTreeMapper {
    /**
     * Maps an exception to the specify [Logger].
     */
    fun mapException(builder: Logger, exception: Throwable) {
        val causes = mutableListOf<TreeNode>()
        treeFromException(exception, 0, 0, causes)

        builder.setStack {
            mapTree(causes[0], this)
        }
    }

    /**
     * Makes a tree from an exception to map it to a [Logger].
     */
    private fun treeFromException(exception: Throwable, globalCommonCount: Int, upperCommonCount: Int,
            causes: MutableList<TreeNode>): Throwable? {
        val thisNode = TreeNode(exception, globalCommonCount, upperCommonCount, mutableListOf())
        causes.add(thisNode)

        // Analyze causes.
        var currentCause = exception.cause
        while (currentCause != null) {
            val lowerCommonCount = compareStackTraces(exception, currentCause, upperCommonCount)

            // Cause with no common stack elements with me.
            if (lowerCommonCount <= 0) {
                return currentCause
            }

            currentCause = treeFromException(currentCause, upperCommonCount + lowerCommonCount, lowerCommonCount,
                    thisNode.causes)
        }

        return null
    }

    private fun mapTree(treeNode: TreeNode, builder: StackLogger) {
        builder.apply {
            val stackTrace = treeNode.exception.stackTrace

            var index = 0
            for (i in stackTrace.size - treeNode.globalCommonCount - 1 downTo 0) {
                var cause = treeNode.causes.findLast { it.upperCommonCount <= index }
                while (cause != null) {
                    treeNode.causes.remove(cause)

                    addCause(cause.exception.message ?: "") {
                        mapTree(cause!!, this)
                    }

                    cause = treeNode.causes.findLast { it.upperCommonCount <= index }
                }

                val lvl = stackTrace[i]
                if (lvl.lineNumber < 0) {
                    addStackTrace(lvl.fileName ?: "<undefined>") {
                        className = lvl.className
                        methodName = lvl.methodName
                    }
                } else {
                    addStackTrace(lvl.fileName ?: "<undefined>", lvl.lineNumber) {
                        className = lvl.className
                        methodName = lvl.methodName
                    }
                }

                index += 1
            }
        }
    }

    private fun compareStackTraces(exception: Throwable, cause: Throwable, upperCommonCount: Int): Int {
        var exceptionIndex = exception.stackTrace.lastIndex - upperCommonCount
        var causeIndex = cause.stackTrace.lastIndex - upperCommonCount

        while (exceptionIndex >= 0 && causeIndex >= 0) {
            if (exception.stackTrace[exceptionIndex] != cause.stackTrace[causeIndex]) {
                return exception.stackTrace.size - upperCommonCount - exceptionIndex - 1
            }

            exceptionIndex -= 1
            causeIndex -= 1
        }

        return -1
    }
}

private data class TreeNode(val exception: Throwable, val globalCommonCount: Int, val upperCommonCount: Int,
        val causes: MutableList<TreeNode>)
