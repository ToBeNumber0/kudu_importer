package com.dhph.bigdata.importer;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

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
            for (int i = 30000; i < 31000; i++) {
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
            row.addString("key", 4+"");
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
    public void kuduDelTabletest(){
        KuduClient client = new KuduClient.KuduClientBuilder(KUDU_MASTER).build();
        try {
            client.deleteTable("java_example-1555481634070");
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