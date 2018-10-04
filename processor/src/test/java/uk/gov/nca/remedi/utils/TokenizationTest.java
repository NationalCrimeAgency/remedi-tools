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
import uk.gov.nca.remedi.utils.Tokenization;

/**
 * Test tokenization of different character sets
 */
public class TokenizationTest {
  @Test
  public void testSentenceTokenizationEnglish(){
    String document = "The blaze started on an upper floor of the Winter Cherry complex during school holidays. The mall's shops, cinema and bowling alley were packed at the time.";

    List<String> sentences = Tokenization.getSentences(document);

    assertEquals(2, sentences.size());
    assertEquals("The blaze started on an upper floor of the Winter Cherry complex during school holidays. ", sentences.get(0));
    assertEquals("The mall's shops, cinema and bowling alley were packed at the time.", sentences.get(1));
  }

  @Test
  public void testSentenceTokenizationEdgeCasesEnglish(){
    String document = "I went to the U.S. last year.";

    List<String> sentences = Tokenization.getSentences(document);

    assertEquals(1, sentences.size());
    assertEquals("I went to the U.S. last year.", sentences.get(0));
  }

  @Test
  public void testSentenceTokenizationRussian(){
    String document = "Ранее агентство Интерфакс со ссылкой на заместителя главы управления МЧС по Кемеровской области Евгения Дедюхина сообщило, что среди погибших - девять детей. В Кемеровской области объявлен трехдневный траур, который начнется со вторника.";

    List<String> sentences = Tokenization.getSentences(document);

    assertEquals(2, sentences.size());
    assertEquals("Ранее агентство Интерфакс со ссылкой на заместителя главы управления МЧС по Кемеровской области Евгения Дедюхина сообщило, что среди погибших - девять детей. ", sentences.get(0));
    assertEquals("В Кемеровской области объявлен трехдневный траур, который начнется со вторника.", sentences.get(1));
  }

  @Test
  public void testSentenceTokenizationChinese(){
    String document = "两个月前遭受恐怖袭击的法国巴黎的犹太超市在装修之后周日重新开放，法国内政部长以及超市的管理者都表示，这显示了生命力要比野蛮行为更强大。该超市1月9日遭受枪手袭击，导致4人死亡，据悉这起事件与法国《查理周刊》杂志社恐怖袭击案有关。";

    List<String> sentences = Tokenization.getSentences(document);

    assertEquals(2, sentences.size());
    assertEquals("两个月前遭受恐怖袭击的法国巴黎的犹太超市在装修之后周日重新开放，法国内政部长以及超市的管理者都表示，这显示了生命力要比野蛮行为更强大。", sentences.get(0));
    assertEquals("该超市1月9日遭受枪手袭击，导致4人死亡，据悉这起事件与法国《查理周刊》杂志社恐怖袭击案有关。", sentences.get(1));
  }

  @Test
  public void testWordTokenizationEnglish(){
    String sentence = "The mall's shops, cinema and bowling alley were packed at the time.";

    List<String> words = Tokenization.getWords(sentence);

    assertEquals(25, words.size());
    assertEquals("The", words.get(0));
    assertEquals(" ", words.get(1));
    assertEquals("mall's", words.get(2));
    assertEquals(" ", words.get(3));
    assertEquals("shops", words.get(4));
    assertEquals(",", words.get(5));
    assertEquals(" ", words.get(6));
    assertEquals("cinema", words.get(7));
    assertEquals(" ", words.get(8));
    assertEquals("and", words.get(9));
    assertEquals(" ", words.get(10));
    assertEquals("bowling", words.get(11));
    assertEquals(" ", words.get(12));
    assertEquals("alley", words.get(13));
  }

  @Test
  public void testWordTokenizationEdgeCasesEnglish(){
    String sentence = "Mr Jones paid £3.99 for his cheese-burger in the U.S.";

    List<String> words = Tokenization.getWords(sentence);

    assertEquals(20, words.size());
    assertEquals("Mr", words.get(0));
    assertEquals(" ", words.get(1));
    assertEquals("Jones", words.get(2));
    assertEquals(" ", words.get(3));
    assertEquals("paid", words.get(4));
    assertEquals(" ", words.get(5));
    assertEquals("£3.99", words.get(6));
    assertEquals(" ", words.get(7));
    assertEquals("for", words.get(8));
    assertEquals(" ", words.get(9));
    assertEquals("his", words.get(10));
    assertEquals(" ", words.get(11));
    assertEquals("cheese-burger", words.get(12));
    assertEquals(" ", words.get(13));
    assertEquals("in", words.get(14));
    assertEquals(" ", words.get(15));
    assertEquals("the", words.get(16));
    assertEquals(" ", words.get(17));
    assertEquals("U.S", words.get(18));
    assertEquals(".", words.get(19));
  }

  @Test
  public void testWordTokenizationRussian(){
    String sentence = "В Кемеровской области объявлен трехдневный траур, который начнется со вторника.";

    List<String> words = Tokenization.getWords(sentence);

    assertEquals(21, words.size());
    assertEquals("В", words.get(0));
    assertEquals(" ", words.get(1));
    assertEquals("Кемеровской", words.get(2));
    assertEquals(" ", words.get(3));
    assertEquals("области", words.get(4));
    assertEquals(" ", words.get(5));
    assertEquals("объявлен", words.get(6));
    assertEquals(" ", words.get(7));
    assertEquals("трехдневный", words.get(8));
    assertEquals(" ", words.get(9));
    assertEquals("траур", words.get(10));
    assertEquals(",", words.get(11));
    assertEquals(" ", words.get(12));
    assertEquals("который", words.get(13));
  }

  @Test
  public void testRemoveWhitespaceEnglish(){
    List<String> tokens = Arrays.asList("The", " ", " mall's", " ", "shops ");

    List<String> cleanTokens = Tokenization.removeWhitespace(tokens);

    assertEquals(3, cleanTokens.size());
    assertEquals("The", cleanTokens.get(0));
    assertEquals("mall's", cleanTokens.get(1));
    assertEquals("shops", cleanTokens.get(2));
  }

  @Test
  public void testRemoveWhitespaceRussian(){
    List<String> tokens = Arrays.asList("В", " ", " Кемеровской", " ", "области ");

    List<String> cleanTokens = Tokenization.removeWhitespace(tokens);

    assertEquals(3, cleanTokens.size());
    assertEquals("В", cleanTokens.get(0));
    assertEquals("Кемеровской", cleanTokens.get(1));
    assertEquals("области", cleanTokens.get(2));
  }

  @Test
  public void testTokenizeEnglish(){
    List<List<String>> output = Tokenization.tokenize("John went to London, and met Jane. Jane isn't from Manchester.");

    assertEquals(2, output.size());

    List<String> sentence1 = output.get(0);
    assertEquals(9, sentence1.size());
    assertEquals("john", sentence1.get(0));
    assertEquals("went", sentence1.get(1));
    assertEquals("to", sentence1.get(2));
    assertEquals("london", sentence1.get(3));
    assertEquals(",", sentence1.get(4));
    assertEquals("and", sentence1.get(5));
    assertEquals("met", sentence1.get(6));
    assertEquals("jane", sentence1.get(7));
    assertEquals(".", sentence1.get(8));

    List<String> sentence2 = output.get(1);
    assertEquals(5, sentence2.size());
    assertEquals("jane", sentence2.get(0));
    assertEquals("isn't", sentence2.get(1));
    assertEquals("from", sentence2.get(2));
    assertEquals("manchester", sentence2.get(3));
    assertEquals(".", sentence2.get(4));
  }

  @Test
  public void testTokenizeRussian(){
    List<List<String>> output = Tokenization.tokenize("Джон отправился в Лондон и познакомился с Джейн. Джейн не из Манчестера.");

    assertEquals(2, output.size());

    List<String> sentence1 = output.get(0);
    assertEquals(9, sentence1.size());
    assertEquals("джон", sentence1.get(0));
    assertEquals("отправился", sentence1.get(1));
    assertEquals("в", sentence1.get(2));
    assertEquals("лондон", sentence1.get(3));
    assertEquals("и", sentence1.get(4));
    assertEquals("познакомился", sentence1.get(5));
    assertEquals("с", sentence1.get(6));
    assertEquals("джейн", sentence1.get(7));
    assertEquals(".", sentence1.get(8));

    List<String> sentence2 = output.get(1);
    assertEquals(5, sentence2.size());
    assertEquals("джейн", sentence2.get(0));
    assertEquals("не", sentence2.get(1));
    assertEquals("из", sentence2.get(2));
    assertEquals("манчестера", sentence2.get(3));
    assertEquals(".", sentence2.get(4));
  }

  @Test
  public void testRemoveBBCode(){
    assertEquals("www.google.com", Tokenization.removeBBCode("[url]www.google.com[/url]"));
    assertEquals("Google", Tokenization.removeBBCode("[url=www.google.com]Google[/url]"));
    assertEquals("E-mail Sally", Tokenization.removeBBCode("[EMAIL=sally@example.com]E-mail Sally[/EMAIL]"));
    assertEquals("[email=sally@example.com]E-mail Sally[/url]", Tokenization.removeBBCode("[email=sally@example.com]E-mail Sally[/url]"));
  }
}
