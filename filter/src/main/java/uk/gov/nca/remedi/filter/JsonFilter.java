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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class JsonFilter {
  private static final ObjectMapper mapper = new ObjectMapper();

  public static void main(String[] args){
    //Get and parse command line options
    Options options = new Options();

    Option optI = new Option("i", "input", true, "Input Directory");
    optI.setRequired(true);
    options.addOption(optI);

    Option optO = new Option("o", "output", true, "Output file root (language extension will be appended)");
    optO.setRequired(true);
    options.addOption(optO);

    Option optL = new Option("l", "language", true, "The code of the foreign language (e.g. nl)");
    optL.setRequired(true);
    options.addOption(optL);

    Option optN = new Option("n", "languagename", true, "The name of the foreign language (e.g. Dutch)");
    optN.setRequired(true);
    options.addOption(optN);

    Option optC = new Option("c", "cutoff", true, "The cutoff for accepting existing translations without corrections, default 5");
    optC.setRequired(false);
    options.addOption(optC);

    CommandLine cmd;
    try {
      CommandLineParser parser = new DefaultParser();
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.err.println("Unable to parse command line options");

      System.exit(1);
      return;
    }

    List<String> source = new ArrayList<>();
    List<String> target = new ArrayList<>();

    try{
      populateLists(source, target, Paths.get(cmd.getOptionValue('i')), cmd.getOptionValue('n'), parseCutoff(cmd.getOptionValue('c')));
    } catch (IOException e) {
      System.err.println("Unable to get/parse input files");

      System.exit(1);
      return;
    }

    Path outputSource = Paths.get(cmd.getOptionValue('o') + "." + cmd.getOptionValue('l'));
    Path outputTarget = Paths.get(cmd.getOptionValue('o') + ".en");

    try {
      Files.write(outputTarget, target, StandardCharsets.UTF_8);
      Files.write(outputSource, source, StandardCharsets.UTF_8);
    }catch (IOException ioe){
      try {
        Files.deleteIfExists(outputSource);
      } catch (IOException e) {
        System.err.println("Unable to delete incomplete source file");
      }
      try {
        Files.deleteIfExists(outputTarget);
      } catch (IOException e) {
        System.err.println("Unable to delete incomplete target file");
      }

      System.err.println("Could not write output files");
      System.exit(1);
    }
  }

  public static Rating parse(Path p){
    try {
      return mapper.readValue(p.toFile(), Rating.class);
    }catch (Exception e){
      System.err.println("Couldn't parse " + p.toString());
      return null;
    }
  }

  public static int parseCutoff(String cutoff){
    if(isNullOrEmpty(cutoff))
      return 5;

    try{
      return Integer.parseInt(cutoff);
    }catch (NumberFormatException nfe){
      System.err.println("Unable to parse cutoff value, defaulting to 5");
      return 5;
    }
  }

  public static boolean isNullOrEmpty(String s){
    return s == null || s.isEmpty();
  }

  public static void populateLists(List<String> source, List<String> target, Path path, String languageName, Integer cutoff) throws IOException{
    try (Stream<Path> paths = Files.walk(path)) {
      paths.filter(p -> p.toString().endsWith(".json"))
        .map(JsonFilter::parse)
        .filter(Objects::nonNull)
        .filter(r -> r.getSourceLanguage().equalsIgnoreCase(languageName))   //Select only correct language
        .filter(r -> r.getTargetLanguage().equalsIgnoreCase("English"))   //Select only English translations
        .filter(r -> !isNullOrEmpty(r.getCorrectedTranslation()) || r.getRatingNotNull() >= cutoff)   //Select only those with a corrected translation, or with a rating greater than the cutoff
        .forEach(r -> {
          source.add(r.getSource());
          if(isNullOrEmpty(r.getCorrectedTranslation())){
            target.add(r.getTranslation());
          }else{
            target.add(r.getCorrectedTranslation());
          }
        });
    }
  }
}
