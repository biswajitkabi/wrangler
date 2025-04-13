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
package io.cdap.wrangler.executor;

import io.cdap.wrangler.api.Row;
import io.cdap.wrangler.utils.testing.TestingRig;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class AggregateStatsDirectiveTest {

    @Test
    public void testAggregateStatsDirective() throws Exception {
        // Sample input rows
        List<Row> rows = Arrays.asList(
                new Row("data_transfer_size", "10KB").add("response_time", "500ms"),
                new Row("data_transfer_size", "1.5MB").add("response_time", "2s")
        );

        // Recipe to run
        String[] recipe = new String[]{
            "aggregate-stats :data_transfer_size :response_time total_size_mb total_time_sec"
        };

        // Run directive
        List<Row> results = TestingRig.execute(recipe, rows);

        // Validate output
        Assert.assertEquals(1, results.size());
        Row result = results.get(0);

        double expectedTotalSizeMB = (10 * 1024 + 1.5 * 1024 * 1024) / (1024.0 * 1024.0); // ~1.509765625 MB
        double expectedTotalTimeSec = (500 + 2000) / 1000.0; // 2.5 seconds

        Assert.assertEquals(expectedTotalSizeMB, (double) result.getValue("total_size_mb"), 0.001);
        Assert.assertEquals(expectedTotalTimeSec, (double) result.getValue("total_time_sec"), 0.001);
    }
}
