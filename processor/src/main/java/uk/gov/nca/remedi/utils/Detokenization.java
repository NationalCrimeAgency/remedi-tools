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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Detokenization {

  private static final Pattern PUNCTUATION = Pattern.compile("[,!@().?\\[\\]]+");

  private static final Pattern EMAIL = Pattern.compile("[a-z0-9]+((\\. )[a-z0-9]+)*@[a-z0-9]+((\\. )[a-z0-9]+)*(\\. com|\\. edu|\\. gov|\\. mil|\\. net|\\. org|\\. [a-z]{2})");

  private Detokenization(){
    //Private constructor for utility class
  }

  /**
   * Detokenize a list of tokens by joining them back together (with a space),
   * taking into account the fact that punctuation should go directly after
   * the preceding token with no space.
   */
  public static String detokenize(List<String> tokens){
    StringBuilder sb = new StringBuilder();

    boolean firstDoubleQuote = true;
    boolean skipSpace = false;

    for(String token : tokens){
      if(isPunctuation(token)){
        sb.append(token);
      }else if("\"".equals(token)){
        if(firstDoubleQuote){
          sb.append(" \"");
          skipSpace = true;
        }else{
          sb.append("\"");
        }

        firstDoubleQuote = !firstDoubleQuote;
      }else if(skipSpace) {
        sb.append(token);
        skipSpace = false;
      }else{
        sb.append(" ");
        sb.append(token);
      }
    }

    //TODO: True casing of sentences

    String text = sb.toString().trim();
    text = text.replaceAll(" ' (d|ll|m|s|t)([,.!? ])", "'$1$2"); //Fix things like "we ' ll" -> "we'll"
    text = text.replaceAll(" ([:;]) ", "$1 ");
    text = text.replaceAll("[ ]?@[ ]?", "@");
    text = text.replaceAll("\\( ", "(");
    text = text.replaceAll("([^ ])\\(", "$1 (");
    text = text.replaceAll(",([^ ])", ", $1");

    //Find e-mail addresses and remove spaces
    StringBuilder sb2 = new StringBuilder();

    Matcher m = EMAIL.matcher(text);
    int prev = 0;
    while(m.find()){
      sb2.append(text.substring(prev, m.start()));
      sb2.append(text.substring(m.start(), m.end()).replaceAll(" ", ""));
      prev = m.end();
    }

    sb2.append(text.substring(prev));

    return sb2.toString();
  }

  private static boolean isPunctuation(String token){
    return PUNCTUATION.matcher(token).matches();
  }
}
