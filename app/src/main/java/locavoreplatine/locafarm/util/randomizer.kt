package locavoreplatine.locafarm.util

import java.util.concurrent.ThreadLocalRandom

/**
 * Created by toulouse on 22/01/18.
 */
fun ClosedRange<Char>.random(): Char =
(ThreadLocalRandom.current().nextInt(endInclusive.toInt() + 1 - start.toInt()) + start.toInt()).toChar()

fun ClosedRange<Int>.random() =
        ThreadLocalRandom.current().nextInt(endInclusive + 1 - start) + start