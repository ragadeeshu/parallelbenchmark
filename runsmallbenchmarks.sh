#!/bin/bash
SQstore="/home/ragnar/Neo4j/smallqueryset10m.db"
# ldbcq2params="query_2_param.txt"
# java -jar target/benchmarks.jar MyBenchmark -p neo4jStoreLocation=$store
java -jar target/benchmarks.jar SQBenchmark -jvmArgs \"-Xmx2g\" -jvmArgsAppend \"-Xms2g\" -wi 4 -i 2 -f 1 -r 100s -w 100s -o sqreslults.txt -rf json -rff sqreslults.json -p neo4jSQStoreLocation=$SQstore
