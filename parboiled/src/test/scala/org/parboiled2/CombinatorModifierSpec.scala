/*
 * Copyright (C) 2009-2013 Mathias Doenitz, Alexander Myltsev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.parboiled2

class CombinatorModifierSpec extends TestParserSpec {

  "The Parser should correctly recognize/reject input for the" >> {

    "`~` combinator" in new TestParser0 {
      def targetRule = rule { 'a' ~ 'b' }
      "ab" must beMatched
      "ac" must beMismatched
      "a" must beMismatched
      "b" must beMismatched
      "" must beMismatched
    }

    "`|` combinator" in new TestParser0 {
      def targetRule = rule { ch('a') | 'b' }
      "a" must beMatched
      "b" must beMatched
      "c" must beMismatched
      "" must beMismatched
    }

    "`zeroOrMore(Rule0)` modifier" in new TestParser0 {
      def targetRule = rule { zeroOrMore("a") ~ EOI }
      "a" must beMatched
      "aa" must beMatched
      "b" must beMismatched
      "" must beMatched
    }

    "`zeroOrMore(Rule1[T])` modifier" in new TestParser1[Seq[String]] {
      def targetRule = rule { zeroOrMore(capture("a")) ~ EOI }
      "a" must beMatchedBy(Seq("a"))
      "aa" must beMatchedBy(Seq("a", "a"))
      "b" must beMismatched
      "" must beMatchedBy(Seq.empty)
    }

    "`zeroOrMore(Rule[I, O <: I])` modifier" in new TestParser1[String] {
      def targetRule = rule { capture("a") ~ zeroOrMore(ch('x') ~> ((_: String) + 'x')) ~ EOI }
      "a" must beMatchedBy("a")
      "ax" must beMatchedBy("ax")
      "axx" must beMatchedBy("axx")
    }

    "`oneOrMore(Rule0)` modifier" in new TestParser0 {
      def targetRule = rule { oneOrMore("a") ~ EOI }
      "a" must beMatched
      "aa" must beMatched
      "b" must beMismatched
      "" must beMismatched
    }

    "`oneOrMore(Rule1[T])` modifier" in new TestParser1[Seq[String]] {
      def targetRule = rule { oneOrMore(capture("a")) ~ EOI }
      "a" must beMatchedBy(Seq("a"))
      "aa" must beMatchedBy(Seq("a", "a"))
      "b" must beMismatched
      "" must beMismatched
    }

    "`oneOrMore(Rule[I, O <: I])` modifier" in new TestParser1[String] {
      def targetRule = rule { capture("a") ~ oneOrMore(ch('x') ~> ((_: String) + 'x')) ~ EOI }
      "a" must beMismatched
      "ax" must beMatchedBy("ax")
      "axx" must beMatchedBy("axx")
    }

    "`optional(Rule0)` modifier" in new TestParser0 {
      def targetRule = rule { optional("a") ~ EOI }
      "a" must beMatched
      "b" must beMismatched
      "" must beMatched
    }

    "`optional(Rule1[T])` modifier" in new TestParser1[Option[String]] {
      def targetRule = rule { optional(capture("a")) ~ EOI }
      "a" must beMatchedBy(Some("a"))
      "" must beMatchedBy(None)
      "b" must beMismatched
      "ab" must beMismatched
    }

    "`optional(Rule[I, O <: I])` modifier" in new TestParser1[String] {
      def targetRule = rule { capture("a") ~ optional(ch('x') ~> ((_: String) + 'x')) ~ EOI }
      "a" must beMatchedBy("a")
      "ax" must beMatchedBy("ax")
      "axx" must beMismatched
    }

    "`!(Rule0)` modifier" in new TestParser0 {
      def targetRule = rule { !"a" }
      "a" must beMismatched
      "b" must beMatched
      "" must beMatched
    }

    "`&` modifier" in new TestParser0 {
      def targetRule = rule { &("a") }
      "a" must beMatched
      "b" must beMismatched
      "" must beMismatched
    }

    "`nTimes(0, Rule0)` modifier" in new TestParser0 {
      def targetRule = rule { nTimes(0, "a") ~ EOI }
      "" must beMatched
      "x" must beMismatched
    }

    "`nTimes(2, ..., separator = '|')` modifier" in new TestParser0 {
      def targetRule = rule { nTimes(2, "a", '|') ~ EOI }
      "a" must beMismatched
      "a|" must beMismatched
      "a|a" must beMatched
      "a|a|" must beMismatched
      "a|a|a" must beMismatched
    }
  }
}