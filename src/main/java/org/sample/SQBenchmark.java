/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.sample;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.neo4j.graphdb.Result;

public class SQBenchmark extends SQBenchmarkSetup
{

//    @Scope(Scope.Thread)


    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void sq1( Blackhole bh, DatabaseSQ dbSQ, TransacitonSQ tx, RNGState rng )
    {
        Result result = dbSQ.dbSQ.execute( "CYPHER 3.0 MATCH (n) RETURN n" );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void sq1parallel( Blackhole bh, DatabaseSQ dbSQ, TransacitonSQ tx, RNGState rng )
    {
        Result result = dbSQ.dbSQ.execute( "CYPHER 3.0 runtime=parallel MATCH (n) RETURN n" );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void sq2( Blackhole bh, DatabaseSQ dbSQ, TransacitonSQ tx, RNGState rng )
    {
        Result result = dbSQ.dbSQ.execute(
                "CYPHER 3.0 MATCH (n) WHERE (n.id % 7) / (n.id % 5 + 1) > n.id % 3 RETURN n" );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void sq2parallel( Blackhole bh, DatabaseSQ dbSQ, TransacitonSQ tx, RNGState rng )
    {
        Result result = dbSQ.dbSQ.execute(
                "CYPHER 3.0 runtime=parallel MATCH (n) WHERE (n.id % 7) / (n.id % 5 + 1) > n.id % 3 RETURN n" );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void sq3( Blackhole bh, DatabaseSQ dbSQ, TransacitonSQ tx, RNGState rng )
    {
        Result result =
                dbSQ.dbSQ.execute( "CYPHER 3.0 MATCH (:LBL_ALL {id: 777} )--()--()--()--" +
                        "(n) RETURN n" );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void sq3parallel( Blackhole bh, DatabaseSQ dbSQ, TransacitonSQ tx, RNGState rng )
    {
        Result result = dbSQ.dbSQ.execute( "CYPHER 3.0 runtime=parallel MATCH (:LBL_ALL {id: 777} )--()--()--()--(n) " +
                "RETURN n" );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void sq4( Blackhole bh, DatabaseSQ dbSQ, TransacitonSQ tx, RNGState rng )
    {
        Result result =
                dbSQ.dbSQ.execute( "CYPHER 3.0 MATCH (n:LBL_10M) RETURN n ORDER BY n.idMod7 DESC, n.id ASC " );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void sq4parallel( Blackhole bh, DatabaseSQ dbSQ, TransacitonSQ tx, RNGState rng )
    {
        Result result = dbSQ.dbSQ.execute(
                "CYPHER 3.0 runtime=parallel MATCH (n:LBL_10M) RETURN n ORDER BY n.idMod7 DESC, n.id ASC" );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void sq5( Blackhole bh, DatabaseSQ dbSQ, TransacitonSQ tx, RNGState rng )
    {
        Result result = dbSQ.dbSQ.execute( "CYPHER 3.0 MATCH (m)--(n) WHERE (m.id % 1113) = 7 RETURN n ORDER BY m" +
                ".idMod7 DESC" );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void sq5parallel( Blackhole bh, DatabaseSQ dbSQ, TransacitonSQ tx, RNGState rng )
    {
        Result result = dbSQ.dbSQ
                .execute( "CYPHER 3.0 runtime=parallel MATCH (m)--(n) WHERE (m.id % 1113) = 7 RETURN n ORDER BY m" +
                        ".idMod7 DESC" );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @State( Scope.Thread )
    public static class RNGState
    {
        public final ThreadLocalRandom rng = ThreadLocalRandom.current();
    }

}
