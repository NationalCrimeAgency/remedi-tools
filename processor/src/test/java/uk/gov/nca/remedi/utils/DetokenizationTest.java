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

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import uk.gov.nca.remedi.utils.Detokenization;

public class DetokenizationTest {
  @Test
  public void testDetokenizationEnglish(){
    List<String> tokens1 = Arrays.asList("hello", ",", "world", "!");
    assertEquals("hello, world!", Detokenization.detokenize(tokens1));

    List<String> tokens2 = Arrays.asList("cheese-burgers", "are", "tasty");
    assertEquals("cheese-burgers are tasty", Detokenization.detokenize(tokens2));

    List<String> tokens3 = Arrays.asList("she", "said", "\"", "hello", "world", "\"", ".", "he", "said", "\"", "hi", "\"");
    assertEquals("she said \"hello world\". he said \"hi\"", Detokenization.detokenize(tokens3));

    List<String> tokens4 = Arrays.asList("i", "'", "m", "the", "king", "of", "the", "world", "!");
    assertEquals("i'm the king of the world!", Detokenization.detokenize(tokens4));

    List<String> tokens5 = Arrays.asList("test", ":", "hello", "world");
    assertEquals("test: hello world", Detokenization.detokenize(tokens5));

    List<String> tokens6 = Arrays.asList("uk", "(", "united", "kingdom", ")", "team");
    assertEquals("uk (united kingdom) team", Detokenization.detokenize(tokens6));

    List<String> tokens7 = Arrays.asList("his", "e-mail", "is", "john", ".", "smith", "@", "my", ".", "mail", ".", "com", "apparently");
    assertEquals("his e-mail is john.smith@my.mail.com apparently", Detokenization.detokenize(tokens7));
  }
}
