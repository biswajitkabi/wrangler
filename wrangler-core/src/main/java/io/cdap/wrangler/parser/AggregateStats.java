/*
 * Copyright © 2025 Cask Data, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package io.cdap.wrangler.parser;

import io.cdap.wrangler.api.*;
import io.cdap.wrangler.api.parser.*;
import io.cdap.wrangler.api.row.Row;
import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.api.DirectiveExecutionException;
import io.cdap.wrangler.api.DirectiveParseException;
import io.cdap.wrangler.api.executor.ExecutorContext;
import io.cdap.wrangler.api.parser.usage.UsageDefinition;

import java.util.Collections;
import java.util.List;

/**
 * A custom directive that aggregates byte sizes and time durations from rows.
 */
public class AggregateStats implements Directive {

    private String sourceByteCol;
    private String sourceTimeCol;
    private String targetSizeCol;
    private String targetTimeCol;
    private long totalBytes = 0;
    private long totalTimeMs = 0;

    @Override
    public void define(DirectiveRegistry registry) {
        registry.define("aggregate-stats",
                usage("Aggregate byte sizes and durations")
                        .define("sourceByteCol", "string", "Column with byte values")
                        .define("sourceTimeCol", "string", "Column with time values")
                        .define("targetSizeCol", "string", "Output column for total size")
                        .define("targetTimeCol", "string", "Output column for total time")
        );
    }

    @Override
    public UsageDefinition usage() {
        return usage("aggregate-stats")
                .define("sourceByteCol", "string", "Column with byte values")
                .define("sourceTimeCol", "string", "Column with time values")
                .define("targetSizeCol", "string", "Output column for total size")
                .define("targetTimeCol", "string", "Output column for total time");
    }

    @Override
    public void initialize(Arguments arguments, ExecutorContext context) throws DirectiveParseException {
        this.sourceByteCol = arguments.value("sourceByteCol").value();
        this.sourceTimeCol = arguments.value("sourceTimeCol").value();
        this.targetSizeCol = arguments.value("targetSizeCol").value();
        this.targetTimeCol = arguments.value("targetTimeCol").value();
    }

    @Override
    public List<Row> execute(List<Row> rows, ExecutorContext context) throws DirectiveExecutionException {
        long totalBytes = 0;
        long totalMillis = 0;

        for (Row row : rows) {
            Object byteVal = row.getValue(sourceByteCol);
            Object timeVal = row.getValue(sourceTimeCol);

            if (byteVal instanceof ByteSize) {
                totalBytes += ((ByteSize) byteVal).getBytes();
            }

            if (timeVal instanceof TimeDuration) {
                totalMillis += ((TimeDuration) timeVal).getMillis();
            }
        }

        Row result = new Row();
        result.add(targetSizeCol, new ByteSize(totalBytes));
        result.add(targetTimeCol, new TimeDuration(totalMillis));

        return Collections.singletonList(result);
    }
}
