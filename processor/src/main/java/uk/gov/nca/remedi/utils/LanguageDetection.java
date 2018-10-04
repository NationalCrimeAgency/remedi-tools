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

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import java.util.List;
import java.util.Locale;

public class LanguageDetection {
  private LanguageDetection(){
    // Private constructor
  }

  public static String getLanguage (String text){
    String language = "Unknown";

    try {
      List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

      LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
          .withProfiles(languageProfiles)
          .build();

      TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

      TextObject textObject = textObjectFactory.forText(text);

      Optional<LdLocale> lang = languageDetector.detect(textObject);
      if(lang.isPresent()){
        language = lang.get().getLanguage();
      }
    }catch (Exception e){
      System.err.println("Exception thrown during language detection");
    }

    return Locale.forLanguageTag(language).getDisplayLanguage();
  }
}
