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

package uk.gov.nca.remedi;

import com.google.common.base.CharMatcher;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.gov.nca.remedi.utils.Icu4JTransliterator;
import uk.gov.nca.remedi.utils.Tokenization;

public class PrepareTrainingData {
  public static void main(String[] args) {
    //Get and parse command line options
    Options options = new Options();

    Option optI = new Option("i", "input", true, "The input file to read from");
    optI.setRequired(true);
    options.addOption(optI);

    Option optO = new Option("o", "output", true, "The output file to write to");
    optO.setRequired(true);
    options.addOption(optO);

    Option optTranslit = new Option("t", "transliteration", false, "Enable transliteration");
    options.addOption(optTranslit);

    CommandLine cmd;
    try {
      CommandLineParser parser = new DefaultParser();
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.err.println("Unable to parse command line options");

      System.exit(1);
      return;
    }

    File inputFile = new File(cmd.getOptionValue('i'));
    File outputFile = new File(cmd.getOptionValue('o'));

    boolean firstLine = true;

    try (
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
    ) {
      String line;
      while ((line = br.readLine()) != null) {
        List<List<String>> tokens;
        if(cmd.hasOption('t')){
          tokens = Tokenization.tokenize(Icu4JTransliterator.transliterate(line));
        }else{
          tokens = Tokenization.tokenize(line);
        }

        StringJoiner sj = new StringJoiner(" ");
        for(List<String> sentence : tokens){
          sentence.forEach(sj::add);
        }

        //Remove control characters
        String cleaned = CharMatcher.javaIsoControl().removeFrom(sj.toString());

        if(!firstLine){   //Do it like this so we don't have a trailing empty line at the end of the file
          bw.newLine();
        }
        bw.write(cleaned);

        firstLine = false;
      }
    }catch (IOException ioe){
      System.err.println("IOException occurred: "+ioe.getMessage());

      System.exit(1);
      return;
    }
  }
}
