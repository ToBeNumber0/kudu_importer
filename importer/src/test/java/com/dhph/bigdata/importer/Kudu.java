package com.dhph.bigdata.importer;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: KuduUtil
 * @Description:用于操作kudu的示例代码
 */
@Ignore
public class Kudu{
    private static final String KUDU_MASTER = "10.10.10.21";
    private static String tableName = "TestKudu";

    @Test
    public void kuduCreateTableTest(){
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTER).build();
        try {
            List<ColumnSchema> columns = new ArrayList(2);
            columns.add(new ColumnSchema.ColumnSchemaBuilder("key", Type.STRING)
                    .key(true)
                    .build());
            columns.add(new ColumnSchema.ColumnSchemaBuilder("value", Type.STRING)
                    .build());
            List<String> rangeKeys = new ArrayList<>();
            rangeKeys.add("key");
            Schema schema = new Schema(columns);
            client.createTable(tableName, schema,
                    new CreateTableOptions().setRangePartitionColumns(rangeKeys));
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                client.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void kuduSaveTest(){
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTER).build();
        try{
            KuduTable table = client.openTable(tableName);
            KuduSession session = client.newSession();
            System.out.println("-------start--------"+System.currentTimeMillis());
            for (int i = 40000; i < 50000; i++) {
                Insert insert = table.newInsert();
                PartialRow row = insert.getRow();
                row.addString(0, i+"");
                row.addString(1, "aaa");
                OperationResponse operationResponse =  session.apply(insert);
            }
            System.out.println("-------end--------"+System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                client.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void kuduUpdateTest(){

        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTER).build();
        try {
            KuduTable table = client.openTable(tableName);
            KuduSession session = client.newSession();
            Update update = table.newUpdate();
            PartialRow row = update.getRow();
            row.addString("key", 30000+"");
            row.addString("value", "value " + 10);
            OperationResponse operationResponse =  session.apply(update);

            System.out.print(operationResponse.getRowError());

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                client.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void kuduSearchTest(){
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTER).build();

        try {
            KuduTable table = client.openTable(tableName);
            List<String> projectColumns = new ArrayList<>(1);
            projectColumns.add("value");
            KuduScanner scanner = client.newScannerBuilder(table)
                    .setProjectedColumnNames(projectColumns)
                    .build();
            while (scanner.hasMoreRows()) {
                RowResultIterator results = scanner.nextRows();
                while (results.hasNext()) {
                    RowResult result = results.next();
                    System.out.println(result.getString(0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                client.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void kuduDelTableTest(){
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTER).build();
        try {
            client.deleteTable("java_example_1555578532700");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    public void kuduShowTableTest(){
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTER).build();
        try {
            KuduTable table = client.openTable("impala::kudu_database.pd_product");
            Schema schema = table.getSchema();
            System.out.println(schema.getPrimaryKeyColumns().get(0).getName());
            for(ColumnSchema columnSchema: schema.getColumns()){
                System.out.println(columnSchema.getName()+ " "+ columnSchema.getType()+ " ");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void kuduTransferTest(){
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTER).build();
        try {
            KuduTable table = client.openTable("impala::kudu_database. pd_product_0422");
            Schema schema = table.getSchema();
            Map<String, Type> typeMap = new HashMap<>();
            System.out.println(schema.getPrimaryKeyColumns().get(0).getName());
            for(ColumnSchema columnSchema: schema.getColumns()){
                typeMap.put(columnSchema.getName(), columnSchema.getType());
//                System.out.println(columnSchema.getName()+ " "+ columnSchema.getType()+ " ");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                client.shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}