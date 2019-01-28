#!/bin/bash
LDBCstore="/home/ragnar/git/ldbc_snb_workload_interactive_neo4j/sf010_p064_regular_utc_30.db"
# ldbcq2params="query_2_param.txt"
# java -jar target/benchmarks.jar MyBenchmark -p neo4jStoreLocation=$store
java -jar target/benchmarks.jar LDBCBenchmark.ldbcq2parallel -wi 4 -i 2 -f 1 -r 100s -w 100s -o reslults.txt -rf json -rff reslults.json -p neo4jLDBCStoreLocation=$LDBCstore
