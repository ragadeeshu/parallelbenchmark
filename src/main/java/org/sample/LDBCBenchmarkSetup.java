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
public class LDBCBenchmarkSetup
{
    @State( Scope.Benchmark )
    public static class DatabaseLDBC
    {

        @Param( {} )
        public String neo4jLDBCStoreLocation;

        public GraphDatabaseService dbLDBC;
        public Map<String, Object>[] ldbcq2args;
        public Map<String, Object>[] ldbcq8args;
        @Setup
        public void setup() throws IOException
        {
            List<String> lines= Files.readAllLines( Paths.get("query_2_param.txt"), Charset.forName("UTF-8"));

            ldbcq2args = new Map[lines.size()];
            int i =0;
            for(String line:lines){
                Map<String, Object> row = new HashMap<>(  );
                String[] rowargs = line.split( "[|]" );
                row.put( "1", Long.parseLong( rowargs[0] ) );
                row.put( "2", Long.parseLong( rowargs[1] ) );
//                row.put( "3", 20);
                ldbcq2args[i++]=row;
            }
            lines = Files.readAllLines( Paths.get("query_8_param.txt"), Charset.forName("UTF-8"));

            ldbcq8args = new Map[lines.size()];
            i =0;
            for(String line:lines){
                Map<String, Object> row = new HashMap<>(  );
                row.put( "1", Long.parseLong( line ) );
//                row.put( "2", 20);
                ldbcq8args[i++]=row;
            }
            dbLDBC = new EnterpriseGraphDatabaseFactory().newEmbeddedDatabaseBuilder( new File( neo4jLDBCStoreLocation ) )
                    .setConfig( GraphDatabaseSettings.pagecache_memory, "200G" )
//                .loadPropertiesFromFile( storeAndConfig.config().toFile().getAbsolutePath() )
                    .newGraphDatabase();
        }

        @TearDown
        public void tearDown()
        {
            dbLDBC.shutdown();
        }

    }

    @State( Scope.Thread )
    public static class TransacitonLDBC
    {

        private Transaction transaction;

        @Setup
        public void setup(DatabaseLDBC db)
        {
            transaction = db.dbLDBC.beginTx();
        }

        @TearDown
        public void tearDown()
        {
            transaction.success();
            transaction.close();
        }
    }
}
