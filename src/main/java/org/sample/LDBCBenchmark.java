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

import org.neo4j.graphdb.Result;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class LDBCBenchmark extends LDBCBenchmarkSetup
{

//    @Scope(Scope.Thread)


    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void ldbcq2( Blackhole bh, DatabaseLDBC db, TransacitonLDBC tx, RNGState rng )
    {
        Map<String,Object> args = db.ldbcq2args[rng.rng.nextInt( db.ldbcq2args.length )];
        Result result = db.dbLDBC.execute( "CYPHER 3.0 MATCH (:Person {id:{1}})-[:KNOWS]-(friend:Person) " +
                "<-[:POST_HAS_CREATOR|COMMENT_HAS_CREATOR]-(message:Message) " +
                "WHERE message.creationDate <= {2} " +
                "RETURN friend.id AS personId, friend.firstName AS personFirstName, friend.lastName AS personLastName," +
                " message.id AS messageId," +
                " CASE exists(message.content) WHEN true THEN message.content " +
                " ELSE message.imageFile END AS messageContent," +
                " message.creationDate AS messageDate " +
                "ORDER BY messageDate DESC, messageId ASC ", args );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void ldbcq2parallel( Blackhole bh, DatabaseLDBC db, TransacitonLDBC tx, RNGState rng )
    {
        Map<String,Object> args = db.ldbcq2args[rng.rng.nextInt( db.ldbcq2args.length )];
        Result result = db.dbLDBC.execute( "CYPHER 3.0 RUNTIME=parallel MATCH (:Person {id:{1}})-[:KNOWS]-" +
                "(friend:Person) " +
                "<-[:POST_HAS_CREATOR|COMMENT_HAS_CREATOR]-(message:Message) " +
                "WHERE message.creationDate <= {2} " +
                "RETURN friend.id AS personId, friend.firstName AS personFirstName, friend.lastName AS personLastName," +
                " message.id AS messageId," +
                " CASE exists(message.content) WHEN true THEN message.content " +
                " ELSE message.imageFile END AS messageContent," +
                " message.creationDate AS messageDate " +
                "ORDER BY messageDate DESC, messageId ASC ", args );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }

    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void ldbcq8( Blackhole bh, DatabaseLDBC db, TransacitonLDBC tx, RNGState rng )
    {
        Map<String,Object> args = db.ldbcq8args[rng.rng.nextInt( db.ldbcq2args.length )];
        Result result = db.dbLDBC.execute(
                "CYPHER 3.0 MATCH (start:Person {id:{1}})<-[:POST_HAS_CREATOR|COMMENT_HAS_CREATOR]-(:Message)<-[:REPLY_OF_POST|REPLY_OF_COMMENT]-(comment:Comment)-[:COMMENT_HAS_CREATOR]->(person:Person)\n" +
                        "RETURN person.id AS personId, person.firstName AS personFirstName, person.lastName AS personLastName, comment.id AS commentId, comment.creationDate AS commentCreationDate, comment.content AS commentContent\n" +
                        "ORDER BY commentCreationDate DESC, commentId ASC\n", args );
        while ( result.hasNext() )
        {
            bh.consume( result.next() );
        }
    }
    @Benchmark
    @BenchmarkMode( {Mode.SampleTime} )
    public void ldbcq8parallel( Blackhole bh, DatabaseLDBC db, TransacitonLDBC tx, RNGState rng )
    {
        Map<String,Object> args = db.ldbcq8args[rng.rng.nextInt( db.ldbcq2args.length )];
        Result result = db.dbLDBC.execute(
                "CYPHER 3.0 RUNTIME=parallel MATCH (start:Person {id:{1}})<-[:POST_HAS_CREATOR|COMMENT_HAS_CREATOR]-" +
                        "(:Message)<-[:REPLY_OF_POST|REPLY_OF_COMMENT]-(comment:Comment)-[:COMMENT_HAS_CREATOR]->(person:Person)\n" +
                        "RETURN person.id AS personId, person.firstName AS personFirstName, person.lastName AS personLastName, comment.id AS commentId, comment.creationDate AS commentCreationDate, comment.content AS commentContent\n" +
                        "ORDER BY commentCreationDate DESC, commentId ASC\n", args );
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
