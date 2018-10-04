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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.gov.nca.remedi.utils.Icu4JTransliterator;
import uk.gov.nca.remedi.utils.LanguageDetection;
import uk.gov.nca.remedi.utils.Tokenization;

public class PreProcessor {
  public static void main(String[] args){
    //Get and parse command line options
    Options options = new Options();

    Option optJ = new Option("j", "jobUid", true, "Unique identifier for the current job");
    optJ.setRequired(true);
    options.addOption(optJ);

    Option optD = new Option("d", "workDir", true, "Work directory where the files are stored");
    optD.setRequired(true);
    options.addOption(optD);

    Option optL = new Option("l", "language", true, "The language of the current job");
    optL.setRequired(false);
    options.addOption(optL);


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

    //Read input file
    File inputFile = new File(cmd.getOptionValue('d'), cmd.getOptionValue('j') + ".pre.in.txt");
    List<String> lines;
    try {
      lines = Files.readAllLines(inputFile.toPath());
    } catch (IOException e) {
      System.err.println("Unable to read input file");

      System.exit(1);
      return;
    }

    //Perform tokenization
    String text = lines.stream().collect(Collectors.joining("\n"));
    List<List<String>> tokens;
    if(cmd.hasOption('t')){
      tokens = Tokenization.tokenize(Icu4JTransliterator.transliterate(text));
    }else{
      tokens = Tokenization.tokenize(text);
    }

    //Write output file
    StringJoiner sj = new StringJoiner("\n");
    for(List<String> sentence : tokens){
      sj.add(sentence.stream().collect(Collectors.joining(" ")));
    }

    File outputFile = new File(cmd.getOptionValue('d'), cmd.getOptionValue('j') + ".pre.out.txt");
    try {
      Files.write(outputFile.toPath(), sj.toString().getBytes());
    } catch (IOException e) {
      System.err.println("Unable to write output file");

      System.exit(1);
      return;
    }

    String language = cmd.getOptionValue('l');
    if("auto".equals(language)){
      language = LanguageDetection.getLanguage(text);
    }

    //Print language - required by REMEDI
    System.out.println(language);
  }
}
