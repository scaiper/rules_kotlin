/*
 * Copyright 2020 The Bazel Authors. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package io.bazel.kotlin.builder.tasks.jvm;

import io.bazel.kotlin.builder.Deps;
import io.bazel.kotlin.builder.KotlinJvmTestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class KotlinBuilderJvmAbiTest {
  private static final KotlinJvmTestBuilder ctx = new KotlinJvmTestBuilder();

  @Test
  public void testGeneratesAbiOnly() {
    Deps.Dep d = ctx.runCompileTask(
        c -> {
          c.addSource("AClass.kt", "package something;" + "class AClass{}");
          c.addSource("AnotherClass.kt", "package something;", "", "class AnotherClass{}");
          c.outputJar();
          c.outputAbiJar();
          c.compileKotlin();
          c.outputJdeps();
        });
    ctx.runCompileTask(
        c -> {
          c.addDirectDependencies(d);
          c.addSource("Dependent.kt",
              "package dep;",
              "import something.AClass",
              "class Dependent{}");
          c.outputJar().outputJdeps().compileKotlin();
        });
  }

  @Test
  public void testGeneratesAbiJarSource() {
    ctx.runCompileTask(
        c -> {
          c.addSource("AClass.kt", "package something;" + "class AClass{}");
          c.addSource("AnotherClass.kt", "package something;", "", "class AnotherClass{}");
          c.outputJar();
          c.outputJdeps();
          c.outputAbiJar();
          c.compileKotlin();
        });
  }
}
