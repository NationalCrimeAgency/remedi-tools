/*
National Crime Agency (c) Crown Copyright 2018

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package uk.gov.nca.remedi.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LanguageDetectionTest {

  /**
   * Test detection of different languages including different character sets
   */
  @Test
  public void testLanguageDetection(){
    assertEquals("English", LanguageDetection.getLanguage("The construction of a third runway at the UK's largest airport is likely to lead to hundreds of homes being demolished in the nearby villages of Longford, Harmondsworth and Sipson."));
    assertEquals("Dutch", LanguageDetection.getLanguage("Poesje zit naast het vuur. Hoe kan zij braaf zijn? Dan komt het kleine hondje binnen. Poesje, ben je daar? Zo, zo, mejuffrouw Poesje, Zeg me, hoe gaat het met je? Dank je, dank je, Hondje, Het gaat heel goed met me op dit moment."));
    assertEquals("Russian", LanguageDetection.getLanguage("Кавани выходил один-на-один, но Игнашевич в последний момент выбил в подкате мяч у него из-под ноги! Тот требовал пенальти, но тщетно - мяч был выбит абсолютно чисто."));
  }
}
