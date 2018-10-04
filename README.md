# REMEDI Tools

This project contains a number of tools that can be used in conjunction with [REMEDI](https://github.com/ivan-zapreev/Distributed-Translation-Infrastructure), for example tools
to do the pre-/post- processing of text prior to translation.

## Filter

Takes a directory of JSON files and filters them based on certain properties.
The expected format of the JSON is as follows:


```
{
  "source": "The source text in a foreign language",
  "translation": "The translated text in the target language",
  "rating": 4,   # The rating of the translation
  "correctedTranslation": "Optional corrected translation (null if translation hasn't been corrected)",
  "sourceLanguage": "The source language of the text",
  "targetLanguage": "The target language of the text",
  "author": "The person providing the rating and/or translation",
  "timestamp": 1531304578240    # The timestamp that the rating/correction was provided
}

```

The command to filter the data is as follows:

```
java -cp filter-1.0-SNAPSHOT.jar uk.gov.nca.remedi.filter.JsonFilter \
    -i [Input Directory] \
    -o [Output file root, excluding the language extension] \
    -l [Language code for the foreign language] \
    -n [Name of the foreign language] \
    -c [Cut off (minimum rating) - optional]
```

The filter will output two files (one English, and one foreign language) containing sentence pairs (one per line),
that match the specified language, and have a corrected translation provided or a rating equal to or greater than the cut off.

This tool is licensed under ASLv2.

## Processor

Performs pre- and post- processing tasks on text, primarily for use in preparing training data,
and being used by the REMEDI processor server.

Contains 5 separate classes which can be ran individually:

* `CleanParallelCorpus` - Takes a parallel corpus and performs some cleaning tasks on it such as deduplication and removing empty lines
* `PostProcessor` - Takes a translated string, detokenizes it and performs true casing on it
* `PrepareTrainingData` - Tokenizes text from a file, and transliterates Cyrillic characters using the GOST 2002b standard
* `PreProcessor` - Tokenizes and transliterates text, and detects language if required
* `ShrinkParallelCorpus` - Randomly selects a subset of sentences from a parallel corpus to reduce the size of a dataset

This tool is licensed under ASLv2.

## Stanford Truecaser

Uses Stanford CoreNLP to perform true casing of lower case text.
It implements the `uk.gov.nca.remedi.utils.TrueCaser` interface, allowing it to be used as part of the post processor
for correcting the case on translation output. 

This tool is licensed under GPLv3.
