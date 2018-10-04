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

import com.ibm.icu.text.Transliterator;

/**
 * Transliterate common alphabets into the Latin alphabet using ICU4J
 *
 * Currently supports Arabic, Cyrillic, Hebrew, and Katakana.
 */
public class Icu4JTransliterator {

  private static final Transliterator arabic = Transliterator.getInstance("Arabic-Latin");
  //Using Russian-Latin/BGN rather than Cyrillic-Latin gives a better mapping with fewer accents (i.e. closer to a Latin keyboard)
  private static final Transliterator cyrillic = Transliterator.getInstance("Russian-Latin/BGN");
  private static final Transliterator hebrew = Transliterator.getInstance("Hebrew-Latin");
  private static final Transliterator katakana = Transliterator.getInstance("Katakana-Latin");

  private Icu4JTransliterator(){
    // Empty constructor
  }

  /**
   * Transliterate a String from non-Latin characters to Latin, leaving any unmapped characters
   * untouched
   */
  public static String transliterate(String message){

    String transliterated = arabic.transliterate(message);
    transliterated = cyrillic.transliterate(transliterated);
    transliterated = hebrew.transliterate(transliterated);
    transliterated = katakana.transliterate(transliterated);

    return transliterated;
  }
}
