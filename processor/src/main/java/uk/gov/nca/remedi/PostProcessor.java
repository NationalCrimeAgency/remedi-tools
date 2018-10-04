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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import uk.gov.nca.remedi.utils.Detokenization;
import uk.gov.nca.remedi.utils.TrueCaser;
import uk.gov.nca.remedi.utils.TrueCaserSelector;

public class PostProcessor {
  public static void main(String[] args) {
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

    Option optC = new Option("c", "truecaser", true, "The class name of the True Caser to use");
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

    //Read input file
    File inputFile = new File(cmd.getOptionValue('d'), cmd.getOptionValue('j') + ".post.in.txt");
    List<String> lines;
    try {
      lines = Files.readAllLines(inputFile.toPath());
    } catch (IOException e) {
      System.err.println("Unable to read input file");

      System.exit(1);
      return;
    }

    //Perform de-tokenization on each sentence
    TrueCaser trueCaser = TrueCaserSelector.getTrueCaser(cmd.getOptionValue('c'));

    List<String> sentences = lines.stream()
        .map(s -> trueCaser.trueCase(s, cmd.getOptionValue('l')))
        .map(s -> Detokenization.detokenize(Arrays.asList(s.split(" "))))
        .collect(Collectors.toList());
    String output = sentences.stream().collect(Collectors.joining("\n"));

    //Write output file

    File outputFile = new File(cmd.getOptionValue('d'), cmd.getOptionValue('j') + ".post.out.txt");
    try {
      Files.write(outputFile.toPath(), output.getBytes());
    } catch (IOException e) {
      System.err.println("Unable to write output file");

      System.exit(1);
      return;
    }

    //Print language - required by REMEDI
    System.out.println(cmd.getOptionValue('l'));
  }
}
