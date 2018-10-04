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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ShrinkParallelCorpus {
  private static final double DEFAULT_PROBABILITY = 0.8;

  public static void main(String[] args){
    //Get and parse command line options
    Options options = new Options();

    Option optI = new Option("i", "input", true, "The root name of the parallel corpus");
    optI.setRequired(true);
    options.addOption(optI);

    Option optO = new Option("o", "output", true, "The root name of the output files");
    optO.setRequired(true);
    options.addOption(optO);

    Option optL1 = new Option("l1", "sourceLanguage", true, "The extension used for the source language");
    optL1.setRequired(true);
    options.addOption(optL1);

    Option optL2 = new Option("l2", "targetLanguage", true, "The extension used for the target language");
    optL2.setRequired(true);
    options.addOption(optL2);

    Option optProb = new Option("p", "probability", true, "The probability of a given line being included, defaults to "+DEFAULT_PROBABILITY);
    options.addOption(optProb);

    CommandLine cmd;
    try {
      CommandLineParser parser = new DefaultParser();
      cmd = parser.parse(options, args);
    } catch (ParseException e) {
      System.err.println("Unable to parse command line options");

      System.exit(1);
      return;
    }

    double prob;
    if(cmd.hasOption('p')) {
      try {
        prob = Double.parseDouble(cmd.getOptionValue('p'));
      } catch (NumberFormatException nfe) {
        System.out.println("Couldn't parse value for probability - defaulting to "+DEFAULT_PROBABILITY);
        prob = DEFAULT_PROBABILITY;
      }
    }else {
      prob = DEFAULT_PROBABILITY;
    }

    Random rand = new Random();

    try (
        BufferedReader br1 = new BufferedReader(new FileReader(new File(cmd.getOptionValue("i") +  "." + cmd.getOptionValue("l1"))));
        BufferedReader br2 = new BufferedReader(new FileReader(new File(cmd.getOptionValue("i") +  "." + cmd.getOptionValue("l2"))));
        BufferedWriter bw1 = new BufferedWriter(new FileWriter(new File(cmd.getOptionValue("o") +  "." + cmd.getOptionValue("l1"))));
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File(cmd.getOptionValue("o") +  "." + cmd.getOptionValue("l2"))))
    ) {
      String line1;
      while ((line1 = br1.readLine()) != null) {
        String line2 = br2.readLine();
        if(line2 == null || line2.trim().isEmpty() || line1.trim().isEmpty())
          continue;

        if(rand.nextDouble() > prob){
          continue;
        }

        // Write to files
        bw1.write(line1);
        bw1.newLine();
        bw2.write(line2);
        bw2.newLine();

      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
