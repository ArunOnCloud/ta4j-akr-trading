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
package org.ta4j.core.rules;

import org.ta4j.core.Bar;
import org.ta4j.core.Trade;
import org.ta4j.core.Trade.TradeType;
import org.ta4j.core.TradingRecord;

/**
 * A rule that waits for a number of {@link Bar bars} after a trade of a
 * specified type.
 *
 * <p>
 * Satisfied after a fixed number of bars have passed since the last trade.
 */
public class WaitForRule extends AbstractRule {

    /** The trade type to wait for. */
    private final TradeType tradeType;

    /** The number of bars to wait for. */
    private final int numberOfBars;

    /**
     * Constructor.
     *
     * @param tradeType    the trade type to wait for
     * @param numberOfBars the number of bars to wait for
     */
    public WaitForRule(TradeType tradeType, int numberOfBars) {
        this.tradeType = tradeType;
        this.numberOfBars = numberOfBars;
    }

    /** This rule uses the {@code tradingRecord}. */
    @Override
    public boolean isSatisfied(int index, TradingRecord tradingRecord) {
        boolean satisfied = false;
        // No trading history, no need to wait
        if (tradingRecord != null) {
            Trade lastTrade = tradingRecord.getLastTrade(tradeType);
            if (lastTrade != null) {
                int currentNumberOfBars = index - lastTrade.getIndex();
                satisfied = currentNumberOfBars >= numberOfBars;
            }
        }
        traceIsSatisfied(index, satisfied);
        return satisfied;
    }
}
