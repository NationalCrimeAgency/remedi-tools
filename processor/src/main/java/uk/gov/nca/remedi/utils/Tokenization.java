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

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Tokenization {

  private Tokenization(){
    //Private constructor for utility class
  }

  /**
   * Use an ICU Sentence BreakIterator to split a document into a
   * collection of sentences (returned in order).
   */
  public static List<String> getSentences(String document){
    List<String> sentences = new ArrayList<>();

    BreakIterator boundary = BreakIterator.getSentenceInstance();
    boundary.setText(document);

    int start = boundary.first();
    for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
      sentences.add(document.substring(start,end));
    }

    return sentences;
  }

  /**
   * Use an ICU Word BreakIterator to split a sentence into a
   * collection of words (returned in order).
   */
  public static List<String> getWords(String sentence){
    List<String> words = new ArrayList<>();

    BreakIterator boundary = BreakIterator.getWordInstance();
    boundary.setText(sentence);

    int start = boundary.first();
    for (int end = boundary.next(); end != BreakIterator.DONE; start = end, end = boundary.next()) {
      words.add(sentence.substring(start,end));
    }

    return words;
  }

  /**
   * Remove whitespace from the start and end of each token, and
   * remove any subsequently empty entries from the list
   */
  public static List<String> removeWhitespace(List<String> tokens){
    return tokens.stream().map(String::trim)
        .filter(s -> !s.isEmpty())
        .collect(Collectors.toList());
  }

  /**
   * Fully tokenizes a document, splitting it into sentences and words,
   * removing white space and BBCode, and lower-casing everything.
   */
  public static List<List<String>> tokenize(String document){
    List<List<String>> results = new ArrayList<>();

    List<String> sentences = removeWhitespace(getSentences(removeBBCode(document)));
    for(String sentence : sentences){
      List<String> words = removeWhitespace(getWords(sentence));

      results.add(words.stream().map(String::toLowerCase).collect(Collectors.toList()));
    }

    return results;
  }

  /**
   * Remove (valid) BBCode tags from text. A tag is valid if it has a
   * matching opening and closing tag, and contains only alphanumeric
   * in the tag name.
   */
  public static String removeBBCode(String s){
    return s.replaceAll("\\[([a-zA-Z0-9]+)(=.*)?](.*?)\\[/\\1]", "$3"); //FIXME: Add a space either side of replacement?
  }
}
