#!/bin/bash

spark-submit --class "TwitterPopularTags" --master local[2] ./target/scala-2.10/TwitterPopularTags-assembly-0.1-SNAPSHOT.jar
