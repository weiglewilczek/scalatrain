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

object Time {

  def fromMinutes(minutes: Int): Time = {
    require(minutes >= 0, "minutes must not be negative!")
    new Time(minutes / 60, minutes % 60)
  }
}

case class Time(hours: Int = 0, minutes: Int = 0) {
  require(hours >= 0, "hours must not be negative!")
  require(hours < 24, "hours must be less than 24!")
  require(minutes >= 0, "minutes must not be negative!")
  require(minutes < 60, "minutes must be less than 60!")

  lazy val asMinutes: Int =
    minutes + 60 * hours

  def -(that: Time): Int =
    minus(that)

  def minus(that: Time): Int = {
    require(that != null, "that must not be null!")
    this.asMinutes - that.asMinutes
  }
}
