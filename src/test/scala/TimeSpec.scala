/*
 * Copyright 2011 Weigle Wilczek GmbH
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scalatrain

import org.scalacheck.Gen._
import org.scalacheck.Prop._
import org.specs.{ ScalaCheck, Specification }

class TimeSpec extends Specification with ScalaCheck {

  // Testing the singleton object

  "Calling TimeXmlFormat.fromXml" should {
    import Time.TimeXmlFormat

    "throw an IllegalArgumentException for a null xml" in {
      TimeXmlFormat fromXml null must throwA[IllegalArgumentException]
    }

    "return the correct result" in {
      TimeXmlFormat fromXml <time hours="01" minutes="02" /> mustEqual Time(1, 2)
    }
  }

  "Calling TimeXmlFormat.toXml" should {
    import Time.TimeXmlFormat

    "throw an IllegalArgumentException for a null time" in {
      TimeXmlFormat toXml null must throwA[IllegalArgumentException]
    }

    "return the correct result" in {
      TimeXmlFormat toXml Time(1, 2) mustEqual <time hours="01" minutes="02" />
    }
  }

  "Using the XmlSerializable features of Time and TimeXmlFormat" should {
    "lead to the correct results" in {
      Time fromXml Time(12, 34).toXml mustEqual Time(12, 34)
      val time: Time = Time(12, 34).toXml
      time mustEqual Time(12, 34)
    }
  }

  "Calling stringToTime" should {

    "throw an IllegalArgumentException for a null String" in {
      Time stringToTime null must throwA[IllegalArgumentException]
    }

    "throw an IllegalArgumentException for a String not matching the time pattern" in {
      Time stringToTime "abc" must throwA[IllegalArgumentException]
      Time stringToTime "25:00" must throwA[IllegalArgumentException]
    }

    "return the correct results" in {
      Time stringToTime "00:00" mustEqual Time()
      Time stringToTime "00:01" mustEqual Time(minutes = 1)
      Time stringToTime "22:22" mustEqual Time(22, 22)
    }
  }

  "A String" should {

    "be implicitly converted into a Time" in {
      import Time._
      "10:30" - "08:00" mustEqual 150
    }
  }

  "Calling fromMinutes" should {

    "throw an IllegalArgumentException for negative minutes" in {
      forAll(choose(Int.MinValue, -1)) { (minutes: Int) =>
        Time fromMinutes minutes must throwA[IllegalArgumentException]
      } must pass
    }

    "return a correctly initialized Time instance for minutes within [0, 24 * 60 - 1)" in {
      forAll(choose(0, 24 * 60 - 1)) { (minutes: Int) =>
        val result = Time fromMinutes minutes
        result.hours mustEqual minutes / 60
        result.minutes mustEqual minutes % 60
      } must pass
    }
  }

  "Calling fromXml" should {

    "throw an IllegalArgumentException for a null xml" in {
      Time fromXml null must throwA[IllegalArgumentException]
    }

    "return a correctly initialized Time instance for a valid xml" in {
      Time fromXml <time hours="01" minutes="02" /> mustEqual Time(1, 2)
    }
  }

  // Testing the class

  "Creating a Time" should {

    "throw an IllegalArgumentException for negative hours" in {
      forAll(choose(Int.MinValue, -1)) { (hours: Int) =>
        Time(hours, 0) must throwA[IllegalArgumentException]
      } must pass
    }

    "throw an IllegalArgumentException for hours >= 24" in {
      forAll(choose(24, Int.MaxValue)) { (hours: Int) =>
        Time(hours, 0) must throwA[IllegalArgumentException]
      } must pass
    }

    "throw an IllegalArgumentException for negative minutes" in {
      forAll(choose(Int.MinValue, -1)) { (minutes: Int) =>
        Time(0, minutes) must throwA[IllegalArgumentException]
      } must pass
    }

    "throw an IllegalArgumentException for minutes >= 60" in {
      forAll(choose(60, Int.MaxValue)) { (minutes: Int) =>
        Time(0, minutes) must throwA[IllegalArgumentException]
      } must pass
    }

    "return an instance with correct defaults" in {
      val time = new Time
      time.hours mustEqual 0
      time.minutes mustEqual 0
    }
  }

  "Calling minus or -" should {
    val time1 = Time(2, 20)
    val time2 = Time(1, 10)

    "throw an IllegalArgumentException for a null that" in {
      time1 minus null must throwA[IllegalArgumentException]
    }

    "return the correct time difference" in {
      time1 - time2 mustEqual 70
      time2 - time1 mustEqual -70
    }
  }

  "Calling asMinutes" should {

    "return the correct value" in {
      Time(0, 10).asMinutes mustEqual 10
      Time(1, 10).asMinutes mustEqual 70
    }
  }

  "Calling toXml" should {

    "return a correct XML representation" in {
      Time(1, 2).toXml mustEqual <time hours="01" minutes="02" />
    }

    "compose to identy with Time.fromXml" in {
      Time fromXml Time(1, 2).toXml mustEqual Time(1, 2)
    }
  }

  "Calling toString" should {

    "return a string formatted like hh:mm" in {
      Time().toString mustEqual "00:00"
      Time(1, 1).toString mustEqual "01:01"
      Time(20, 20).toString mustEqual "20:20"
    }
  }

  "Comparing Time instances" should {

    "throw an IllegalArgumentException for a null that" in {
      Time() compare null must throwA[IllegalArgumentException]
    }

    "return expected results when calling the likes of < and >" in {
      Time(0) < Time(1) mustBe true
    }
  }
}
