#!/bin/bash

rm -fR ./output
/usr/local/bin/pyspark ./wordcount.py

echo ">>>>> result"
cat ./output/*
