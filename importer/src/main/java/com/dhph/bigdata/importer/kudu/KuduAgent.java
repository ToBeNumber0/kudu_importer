package com.dhph.bigdata.importer.kudu;

import com.alibaba.fastjson.JSONObject;
import com.dhph.bigdata.importer.config.SysConfig;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Schema;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Wyq
 * @create 2019/4/17
 * @Description
 **/
@Slf4j
@Component
public class KuduAgent {

    @Resource
    private SysConfig config;


    private final static SessionConfiguration.FlushMode FLASH_MODE_MULT = SessionConfiguration.FlushMode.MANUAL_FLUSH;
    private final static SessionConfiguration.FlushMode FLASH_MODE_SINGLE = SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC;
    private final static int BUFFER_SPACE = 1000;
    private static KuduClient client;
    private static Map<String, Type> typeMap;
    private static List<String> keyList;
    private String tableName;

    @PostConstruct
    private void init(){
        this.tableName = config.tableName;
        client = new KuduClient.KuduClientBuilder(config.master).build();
        typeMap = getTypeMap(tableName);
        keyList = getKeyList(tableName);

    }

    private static KuduAgent kuduAgent;
    private KuduAgent(){
        log.debug("init KuduAgent");
    }

    public static synchronized KuduAgent getInstance(){
        if(kuduAgent==null){
            kuduAgent = new KuduAgent();
        }
        return kuduAgent;
    }



    /**
     * 将json数据转换为指定kudu数据库结构的某一行
     * @param msg 要转换的json数据
     * @return 封装好的 KuduRow 对象
     */
    public KuduRow transfer(String msg){
        try {
            KuduRow kuduRow = new KuduRow();
            List<KuduColumn> rows = Lists.newArrayList();
            JSONObject obj = JSONObject.parseObject(msg);
            for(String key :obj.keySet()){
                KuduColumn column = new KuduColumn();
                if(keyList.contains(key)){
                    column.setPrimaryKey(Boolean.TRUE);
                }
                column.setColumnName(key);
                column.setColumnType(typeMap.get(key)).setUpdate(false);
                column.setColumnValue(obj.get(key));
                rows.add(column);
//            System.out.println(key);
            }
            kuduRow.setRows(rows);
            return kuduRow;
        }catch (Exception e){
            log.error("将json转换为kudu对象异常", e);
            return null;
        }
//        System.out.println(obj);
    }

    /**
     * 单条插入
     *
     * @param entity
     * @throws KuduException
     */
    public void insert(KuduRow entity) throws KuduException {
        insert(tableName, entity);
    }

    /**
     * 查询 返回多条数据
     *
     * @param table
     * @param entitys
     * @return
     */
    public List<Map<String, Object>> select(String table, List<KuduColumn> entitys) {
        KuduTable kuduTable = null;
        KuduScanner build = null;
        KuduScanner.KuduScannerBuilder kuduScannerBuilder = null;
        List<Map<String, Object>> resList = new ArrayList<>();
        try {
            kuduTable = client.openTable(table);
            List<String> columnNames = KuduAgentUtils.getColumnNames(entitys);
            kuduScannerBuilder = client.newScannerBuilder(kuduTable);
            kuduScannerBuilder = kuduScannerBuilder.setProjectedColumnNames(columnNames);
            KuduAgentUtils.setKuduPredicates(kuduTable, entitys, kuduScannerBuilder);
            build = kuduScannerBuilder.build();
            while (build.hasMoreRows()) {
                RowResultIterator results = build.nextRows();
                while (results.hasNext()) {
                    RowResult result = results.next();
                    Map<String, Object> rowsResult = KuduAgentUtils.getRowsResult(result, entitys);
                    resList.add(rowsResult);
                }
            }
        } catch (KuduException e) {
            e.printStackTrace();
            log.error("kudu执查询操作失败，失败信息:cause-->{},message-->{}", e.getCause(), e.getMessage());
        } finally {
            KuduAgentUtils.close(build, client);
        }
        return resList;
    }


    /**
     * 批量插入
     *
     * @param table
     * @param entitys
     * @throws KuduException
     */
    public void insert(String table, List<KuduRow> entitys) {
        KuduSession session = null;
        try {
            KuduTable kuduTable = client.openTable(table);
            session = client.newSession();
            session.setFlushMode(FLASH_MODE_MULT);
            session.setMutationBufferSpace(BUFFER_SPACE);
            for (KuduRow entity : entitys) {
                Insert insert = kuduTable.newInsert();
                KuduAgentUtils.operate(entity, insert, session);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("kudu执行插入操作失败，失败信息:cause-->{},message-->{}", e.getCause(), e.getMessage());
        } finally {
            List<OperationResponse> res = KuduAgentUtils.close(session, client);
        }
    }

    /**
     * 单条插入
     *
     * @param table
     * @param entity
     * @throws KuduException
     */
    public void insert(String table, KuduRow entity) throws KuduException {
        KuduSession session = null;
        try {
            KuduTable kuduTable = client.openTable(table);
            session = client.newSession();
            session.setFlushMode(FLASH_MODE_SINGLE);
            Insert insert = kuduTable.newInsert();
            OperationResponse operate = KuduAgentUtils.operate(entity, insert, session);
            if(operate.getRowError() != null){
                log.info("insert 插入数据失败:{}", operate.getRowError());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("kudu执行插入操作失败，失败信息:cause-->{},message-->{}", e.getCause(), e.getMessage());
        } finally {
            KuduAgentUtils.close(session, null);
        }

    }

    /**
     * 批量更新
     *
     * @param table
     * @param entitys
     * @throws KuduException
     */
    public void update(String table, List<KuduRow> entitys) {
        KuduSession session = null;
        try {
            KuduTable kuduTable = client.openTable(table);
            session = client.newSession();
            session.setFlushMode(FLASH_MODE_MULT);
            session.setMutationBufferSpace(BUFFER_SPACE);
            for (KuduRow entity : entitys) {
                Update update = kuduTable.newUpdate();
                KuduAgentUtils.operate(entity, update, session);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("kudu执行更新操作失败，失败信息:cause-->{},message-->{}", e.getCause(), e.getMessage());
        } finally {
            List<OperationResponse> res = KuduAgentUtils.close(session, client);
        }
    }

    /**
     * 单条更新
     *
     * @param table
     * @param entity
     * @throws KuduException
     */
    public void update(String table, KuduRow entity) throws KuduException {
        KuduSession session = null;
        try {
            KuduTable kuduTable = client.openTable(table);
            session = client.newSession();
            session.setFlushMode(FLASH_MODE_SINGLE);
            Update update = kuduTable.newUpdate();
            OperationResponse operate = KuduAgentUtils.operate(entity, update, session);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("kudu执行更新操作失败，失败信息:cause-->{},message-->{}", e.getCause(), e.getMessage());
        } finally {
            KuduAgentUtils.close(session, client);
        }
    }

    /**
     * 批量删除 删除只能是主键
     *
     * @param table
     * @param entitys
     * @throws KuduException
     */
    public void delete(String table, List<KuduRow> entitys) throws KuduException {
        KuduSession session = null;
        try {
            KuduTable kuduTable = client.openTable(table);
            session = client.newSession();
            session.setFlushMode(FLASH_MODE_MULT);
            session.setMutationBufferSpace(BUFFER_SPACE);
            for (KuduRow entity : entitys) {
                Delete delete = kuduTable.newDelete();
                KuduAgentUtils.operate(entity, delete, session);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("kudu执行删除操作失败，失败信息:cause-->{},message-->{}", e.getCause(), e.getMessage());
        } finally {
            KuduAgentUtils.close(session, client);
        }

    }

    /**
     * 单条删除 删除只能是主键
     *
     * @param table
     * @param entity
     * @throws KuduException
     */
    public void delete(String table, KuduRow entity) throws KuduException {
        KuduSession session = null;
        try {
            KuduTable kuduTable = client.openTable(table);
            session = client.newSession();
            session.setFlushMode(FLASH_MODE_SINGLE);
            Delete delete = kuduTable.newDelete();
            OperationResponse operate = KuduAgentUtils.operate(entity, delete, session);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("kudu执行删除操作失败，失败信息:cause-->{},message-->{}", e.getCause(), e.getMessage());
        } finally {
            KuduAgentUtils.close(session, client);
        }
    }

    /**
     * 针对表的操作
     * 修改表名 删除表  增加字段 删除字段
     *
     * @param entitys
     * @throws KuduException
     */
    public void alter(List<KuduRow> entitys) throws KuduException {
        try {
            for (KuduRow entity : entitys) {
                if (entity.getAlterTableEnum().equals(KuduRow.AlterTableEnum.RENAME_TABLE)) {
                    AlterTableResponse alterTableResponse = renameTable(client, entity);
                    continue;
                }
                if (entity.getAlterTableEnum().equals(KuduRow.AlterTableEnum.DROP_TABLE)) {
                    DeleteTableResponse deleteTableResponse = dropTable(client, entity);
                    continue;
                }
                AlterTableResponse alterTableResponse = alterColumn(client, entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("kudu执行表alter操作失败，失败信息:cause-->{},message-->{}", e.getCause(), e.getMessage());
        } finally {
            KuduAgentUtils.close((KuduSession) null, client);
        }

    }

    /**
     * 针对表的操作
     * 修改表名 删除表  增加字段 删除字段
     *
     * @param client
     * @param entity
     * @throws KuduException
     */
    public void alter(KuduClient client, KuduRow entity) throws KuduException {
        try {
            if (entity.getAlterTableEnum().equals(KuduRow.AlterTableEnum.RENAME_TABLE)) {
                AlterTableResponse alterTableResponse = renameTable(client, entity);
                return;
            }
            if (entity.getAlterTableEnum().equals(KuduRow.AlterTableEnum.DROP_TABLE)) {
                DeleteTableResponse deleteTableResponse = dropTable(client, entity);
                return;
            }
            AlterTableResponse alterTableResponse = alterColumn(client, entity);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("kudu执行表alter操作失败，失败信息:cause-->{},message-->{}", e.getCause(), e.getMessage());
        } finally {
            KuduAgentUtils.close((KuduSession) null, client);
        }

    }

    /**
     * 修改表名
     *
     * @param client
     * @param entity
     * @return
     * @throws KuduException
     */
    private AlterTableResponse renameTable(KuduClient client, KuduRow entity) throws KuduException {
        AlterTableOptions ato = new AlterTableOptions();
        ato.renameTable(entity.getNewTableName());
        return client.alterTable(entity.getTableName(), ato);
    }

    /**
     * 删除表
     *
     * @param client
     * @param entity
     * @return
     * @throws KuduException
     */
    private DeleteTableResponse dropTable(KuduClient client, KuduRow entity) throws KuduException {
        return client.deleteTable(entity.getTableName());
    }


    /**
     * 列级别的alter
     *
     * @param client
     * @param entity
     * @return
     * @throws KuduException
     */
    private AlterTableResponse alterColumn(KuduClient client, KuduRow entity) throws KuduException {
        AlterTableOptions ato = new AlterTableOptions();
        for (KuduColumn column : entity.getRows()) {
            if (column.getAlterColumnEnum().equals(KuduColumn.AlterColumnEnum.ADD_COLUMN) && !column.isNullAble()) {
                ato.addColumn(column.getColumnName(), column.getColumnType(), column.getDefaultValue());
            } else if (column.getAlterColumnEnum().equals(KuduColumn.AlterColumnEnum.ADD_COLUMN) && column.isNullAble()) {
                ato.addNullableColumn(column.getColumnName(), column.getColumnType());
            } else if (column.getAlterColumnEnum().equals(KuduColumn.AlterColumnEnum.DROP_COLUMN)) {
                ato.dropColumn(column.getColumnName());
            } else if (column.getAlterColumnEnum().equals(KuduColumn.AlterColumnEnum.RENAME_COLUMN)) {
                ato.renameColumn(column.getColumnName(), column.getNewColumnName());
            } else {
                continue;
            }
        }
        AlterTableResponse alterTableResponse = client.alterTable(entity.getTableName(), ato);
        return alterTableResponse;
    }

    /**
     * 返回指定table的所有字段及类型
     * @param table
     * @return
     */
    public Map<String, Type> getTypeMap(String table){
        try {
            KuduTable kuduTable = client.openTable(table);
            Schema schema = kuduTable.getSchema();
            Map<String, Type> typeMap = new HashMap<>();
//            System.out.println(schema.getPrimaryKeyColumns().get(0).getName());
            for(ColumnSchema columnSchema: schema.getColumns()){
                typeMap.put(columnSchema.getName(), columnSchema.getType());
//                System.out.println(columnSchema.getName()+ " "+ columnSchema.getType()+ " ");
            }
            return typeMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 返回指定table的主键
     * @param tableName
     * @return
     */
    public List<String> getKeyList(String tableName){
        try {
            KuduTable table = client.openTable(tableName);
            return table.getSchema().getPrimaryKeyColumns().stream().map(ColumnSchema::getName).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}