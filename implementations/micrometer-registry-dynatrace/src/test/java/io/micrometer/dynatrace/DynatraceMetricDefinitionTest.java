/**
 * Copyright 2017 Pivotal Software, Inc.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micrometer.dynatrace;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class DynatraceMetricDefinitionTest {

    @Test
    void usesMetricIdAsDescriptionWhenDescriptionIsNotAvailable() {
        final DynatraceMetricDefinition metric = new DynatraceMetricDefinition("custom:test.metric", null, null, null);

        assertThat(metric.asJson()).isEqualTo("{\"displayName\":\"custom:test.metric\"}");
    }

    @Test
    void escapesStringsInDescription() {
        final DynatraceMetricDefinition metric = new DynatraceMetricDefinition(
            "custom:test.metric",
            "The /\"recent cpu usage\" for the Java Virtual Machine process",
            null, null);

        assertThat(metric.asJson()).isEqualTo("{\"displayName\":\"The \\/\\\"recent cpu usage\\\" for the Java Virtual Machine process\"}");
    }

    @Test
    void addsUnitWhenAvailable() {
        final DynatraceMetricDefinition metric = new DynatraceMetricDefinition("custom:test.metric", "my test metric", DynatraceMetricDefinition.DynatraceUnit.Count, null);

        assertThat(metric.asJson()).isEqualTo("{\"displayName\":\"my test metric\",\"unit\":\"Count\"}");
    }

    @Test
    void addsDimensionsWhenAvailable() {
        final Set<String> dimensions = new HashSet<>();
        dimensions.add("first");
        dimensions.add("second");
        dimensions.add("unknown");
        final DynatraceMetricDefinition metric = new DynatraceMetricDefinition("custom:test.metric", "my test metric", null, dimensions);

        assertThat(metric.asJson()).isEqualTo("{\"displayName\":\"my test metric\",\"dimensions\":[\"first\",\"second\",\"unknown\"]}");
    }
}