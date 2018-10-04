/*
National Crime Agency (c) Crown Copyright 2018

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package uk.gov.nca.remedi.truecaser;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TrueCaseTextAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;
import uk.gov.nca.remedi.utils.TrueCaser;

public class StanfordTrueCaser implements TrueCaser {

  StanfordCoreNLP pipeline;

  public StanfordTrueCaser(){
    Properties props = new Properties();
    props.put("annotators", "tokenize, ssplit, truecase");
    pipeline = new StanfordCoreNLP(props);
  }

  @Override
  public String trueCase(String originalText, String language){
    if(!"english".equalsIgnoreCase(language) && !"en".equalsIgnoreCase(language)){
      //Only process English
      return originalText;
    }

    StringJoiner sj = new StringJoiner(" ");

    Annotation document = new Annotation(originalText);
    pipeline.annotate(document);

    List<CoreMap> sentences = document.get(SentencesAnnotation.class);

    // capture the true cased tokens for evaluation.
    List<String> tcTokens = new ArrayList<String>();

    for (CoreMap sentence : sentences) {
      // traversing the words in the current sentence
      // a CoreLabel is a CoreMap with additional token-specific methods
      for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
        String trueCaseText = token.get(TrueCaseTextAnnotation.class);
        sj.add(trueCaseText);
        tcTokens.add(trueCaseText);
      }
    }

    return sj.toString();
  }

}
