/*******************************************************************************
 *     ___                  _   ____  ____
 *    / _ \ _   _  ___  ___| |_|  _ \| __ )
 *   | | | | | | |/ _ \/ __| __| | | |  _ \
 *   | |_| | |_| |  __/\__ \ |_| |_| | |_) |
 *    \__\_\\__,_|\___||___/\__|____/|____/
 * <p>
 *  Copyright (c) 2014-2019 Appsicle
 *  Copyright (c) 2019-2023 QuestDB
 * <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/

package io.questdb.griffin.engine.functions.cast;

import io.questdb.cairo.CairoConfiguration;
import io.questdb.cairo.sql.Function;
import io.questdb.cairo.sql.Record;
import io.questdb.griffin.FunctionFactory;
import io.questdb.griffin.SqlExecutionContext;
import io.questdb.griffin.engine.functions.constants.StrConstant;
import io.questdb.std.*;
import io.questdb.std.str.CharSink;
import io.questdb.std.str.StringSink;
public class CastIPv4ToStrFunctionFactory implements FunctionFactory {

    @Override
    public String getSignature() {
        return "cast(Xs)";
    }

    @Override
    public Function newInstance(int position, ObjList<Function> args, IntList argPositions, CairoConfiguration configuration, SqlExecutionContext sqlExecutionContext) {
        Function IPv4Func = args.getQuick(0);
        if (IPv4Func.isConstant()) {
            StringSink sink = Misc.getThreadLocalBuilder();
            sink.put(IPv4Func.getInt(null));
            return new StrConstant(Chars.toString(sink));
        }
        return new CastIPv4ToStrFunctionFactory.CastIPv4ToStrFunction(args.getQuick(0));
    }

    public static class CastIPv4ToStrFunction extends AbstractCastToStrFunction {
        private final StringSink sinkA = new StringSink();
        private final StringSink sinkB = new StringSink();

        public CastIPv4ToStrFunction(Function arg) {
            super(arg);
        }

        @Override
        public CharSequence getStr(Record rec) {
            final int value = arg.getInt(rec);
            sinkA.clear();
            Numbers.intToIPv4Sink(sinkA, value);
            return sinkA;
        }

        @Override
        public void getStr(Record rec, CharSink sink) {
            final int value = arg.getInt(rec);

            Numbers.intToIPv4Sink(sink, value);
        }

        @Override
        public CharSequence getStrB(Record rec) {
            final int value = arg.getInt(rec);
            sinkB.clear();
            Numbers.intToIPv4Sink(sinkB, value);
            return sinkB;
        }
    }
}