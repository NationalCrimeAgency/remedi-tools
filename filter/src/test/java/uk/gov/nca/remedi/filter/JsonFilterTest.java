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

package uk.gov.nca.remedi.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

public class JsonFilterTest {
  @Test
  public void testNullOrEmpty(){
    assertTrue(JsonFilter.isNullOrEmpty(null));
    assertTrue(JsonFilter.isNullOrEmpty(""));
    assertFalse(JsonFilter.isNullOrEmpty("Hello"));
  }

  @Test
  public void testParseCutoff(){
    assertEquals(5, JsonFilter.parseCutoff(null));
    assertEquals(5, JsonFilter.parseCutoff("hello"));
    assertEquals(4, JsonFilter.parseCutoff("4"));
  }

  @Test
  public void testParse(){
    Rating r = JsonFilter.parse(new File("src/test/resources/1.json").getAbsoluteFile().toPath());

    assertNotNull(r);

    assertEquals("bonjour monsieur", r.getSource());
    assertEquals("hello mr", r.getTranslation());
    assertEquals(Integer.valueOf(4), r.getRating());
    assertEquals("hello sir", r.getCorrectedTranslation());
    assertEquals("French", r.getSourceLanguage());
    assertEquals("English", r.getTargetLanguage());
    assertEquals("bakerj", r.getAuthor());
    assertEquals(Long.valueOf(1531305701967L), r.getTimestamp());
  }

  @Test
  public void testPopulate() throws IOException {
    List<String> source = new ArrayList<>();
    List<String> target = new ArrayList<>();

    JsonFilter.populateLists(source, target, new File("src/test/resources/").getAbsoluteFile().toPath(), "French", 5);

    //Should return 2 - one that has a rating of 5, and one that has a corrected translation
    assertEquals(2, source.size());
    assertEquals(2, target.size());
  }
}
