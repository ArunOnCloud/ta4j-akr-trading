/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2025 Ta4j Organization & respective
 * authors (see AUTHORS)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.ta4j.core;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.ta4j.core.num.Num;

/**
 * Indicator over a {@link BarSeries bar series}.
 *
 * <p>
 * Returns a value of type <b>T</b> for each index of the bar series.
 *
 * @param <T> the type of the returned value (Double, Boolean, etc.)
 */
public interface Indicator<T> {

    /**
     * @param index the bar index
     * @return the value of the indicator
     */
    T getValue(int index);

    /**
     * Returns {@code true} once {@code this} indicator has enough bars to
     * accurately calculate its value. Otherwise, {@code false} will be returned,
     * which means the indicator will give incorrect values ​​due to insufficient
     * data. This method determines stability using the formula:
     *
     * <pre>
     * isStable = {@link BarSeries#getBarCount()} >= {@link #getCountOfUnstableBars()}
     * </pre>
     *
     * @return true if the calculated indicator value is correct
     */
    default boolean isStable() {
        return getBarSeries().getBarCount() >= getCountOfUnstableBars();
    }

    /**
     * Returns the number of bars up to which {@code this} Indicator calculates
     * wrong values.
     *
     * @return unstable bars
     */
    int getCountOfUnstableBars();

    /**
     * @return the related bar series
     */
    BarSeries getBarSeries();

    /**
     * @return all values from {@code this} Indicator over {@link #getBarSeries()}
     *         as a Stream
     */
    default Stream<T> stream() {
        return IntStream.range(getBarSeries().getBeginIndex(), getBarSeries().getEndIndex() + 1)
                .mapToObj(this::getValue);
    }

    /**
     * Returns all values of an {@link Indicator} within the given {@code index} and
     * {@code barCount} as an array of Doubles. The returned doubles could have a
     * minor loss of precision, if {@link Indicator} was based on {@link Num Num}.
     *
     * @param ref      the indicator
     * @param index    the index
     * @param barCount the barCount
     * @return array of Doubles within {@code index} and {@code barCount}
     */
    static Double[] toDouble(Indicator<Num> ref, int index, int barCount) {
        int startIndex = Math.max(0, index - barCount + 1);
        return IntStream.range(startIndex, startIndex + barCount)
                .mapToObj(ref::getValue)
                .map(Num::doubleValue)
                .toArray(Double[]::new);
    }
}
