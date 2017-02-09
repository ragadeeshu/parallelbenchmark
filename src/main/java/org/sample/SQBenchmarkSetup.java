package org.sample;

import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.EnterpriseGraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

/**
 * Created by ragnar on 2016-09-27.
 */
@State( Scope.Benchmark )
public class SQBenchmarkSetup
{
    @State( Scope.Benchmark )
    public static class DatabaseSQ
    {

        @Param( {} )
        public String neo4jSQStoreLocation;

        public GraphDatabaseService dbSQ;
        @Setup
        public void setup() throws IOException
        {
            dbSQ = new EnterpriseGraphDatabaseFactory().newEmbeddedDatabaseBuilder( new File( neo4jSQStoreLocation ) )
                    .setConfig( GraphDatabaseSettings.pagecache_memory, "400G" )
                    .newGraphDatabase();
//                .loadPropertiesFromFile( storeAndConfig.config().toFile().getAbsolutePath() )
        }

        @TearDown
        public void tearDown()
        {
            dbSQ.shutdown();
        }

    }

    @State( Scope.Thread )
    public static class TransacitonSQ
    {

        private Transaction transaction;

        @Setup
        public void setup(DatabaseSQ db)
        {
            transaction = db.dbSQ.beginTx();
        }

        @TearDown
        public void tearDown()
        {
            transaction.success();
            transaction.close();
        }
    }
}
