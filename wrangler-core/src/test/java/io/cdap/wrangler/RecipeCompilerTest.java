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

package io.cdap.wrangler;

import io.cdap.wrangler.api.Directive;
import io.cdap.wrangler.parser.RecipeCompiler;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RecipeCompilerTest {

    @Test
    public void testAggregateStatsParsing() throws Exception {
        String[] recipe = new String[] {
            "aggregate-stats :col1 :col2 total_size_mb total_time_sec"
        };

        RecipeCompiler compiler = new RecipeCompiler();
        List<Directive> directives = compiler.compile(recipe);

        Assert.assertEquals("aggregate-stats", directives.get(0).directive());
    }
}
