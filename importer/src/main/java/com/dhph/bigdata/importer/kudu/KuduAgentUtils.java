package com.dhph.bigdata.importer.kudu;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.dhph.bigdata.common.utils.CommonUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.kudu.Type;
import org.apache.kudu.client.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class KuduAgentUtils {


    /**
     * 为指定类型的字段转换并赋值
     * @param entity
     * @param operate
     * @return
     * @throws ClassCastException
     */
    public static Operation WrapperKuduOperation(KuduColumn entity, Operation operate) throws ClassCastException{

        Type rowType = entity.getColumnType();
        String columnName = entity.getColumnName();
        Object columnValue = entity.getColumnValue();
//        log.info("kudu操作对象包装，列名:{},列值:{}", columnName, columnValue);
        if(!CommonUtil.isObjectNotEmpty(columnValue)){
//            log.debug("{} 列为空，跳过", columnName);
            return operate;
        }
        switch (rowType){
            case STRING:
                operate.getRow().addString(columnName, TypeUtils.castToString(columnValue));
                break;
            case INT64:
            case UNIXTIME_MICROS:
                operate.getRow().addLong(columnName, TypeUtils.castToLong(columnValue));
                break;
            case DOUBLE:
                operate.getRow().addDouble(columnName, TypeUtils.castToDouble(columnValue));
                break;
            case INT32:
                operate.getRow().addInt(columnName, TypeUtils.castToInt(columnValue));
                break;
            case INT16:
                operate.getRow().addShort(columnName, TypeUtils.castToShort(columnValue));
                break;
            case INT8:
                operate.getRow().addByte(columnName, TypeUtils.castToByte(columnValue));
                break;
            case BOOL:
                operate.getRow().addBoolean(columnName, TypeUtils.castToBoolean(columnValue));
                break;
            case BINARY:
                operate.getRow().addBinary(columnName, TypeUtils.castToBytes(columnValue));
                break;
            case FLOAT:
                operate.getRow().addFloat(columnName, TypeUtils.castToFloat(columnValue));
                break;
            case DECIMAL:
                BigDecimal decimal = new BigDecimal(columnValue.toString());
                decimal = decimal.setScale(2, BigDecimal.ROUND_DOWN);
                operate.getRow().addDecimal(columnName, decimal);
            default:
                break;
        }
        return operate;
    }

    /**
     * 返回查询的一行 map
     *
     * @param row
     * @param entitys
     * @return
     */
    public static Map<String, Object> getRowsResult(RowResult row, List<KuduColumn> entitys) {
        Map<String, Object> result = new HashMap<>();
        for (KuduColumn entity : entitys) {
            if (entity.getColumnType() != null) {
                switch (entity.getColumnType()) {
                    case BOOL:
                        result.put(entity.getColumnName(), row.getBoolean(entity.getColumnName()));
                        break;
                    case BINARY:
                        result.put(entity.getColumnName(), row.getBinary(entity.getColumnName()));
                        break;
                    case STRING:
                        result.put(entity.getColumnName(), row.getString(entity.getColumnName()));
                        break;
                    case INT8:
                        result.put(entity.getColumnName(), row.getByte(entity.getColumnName()));
                        break;
                    case INT16:
                        result.put(entity.getColumnName(), row.getShort(entity.getColumnName()));
                        break;
                    case INT32:
                        result.put(entity.getColumnName(), row.getInt(entity.getColumnName()));
                        break;
                    case INT64:
                        result.put(entity.getColumnName(), row.getLong(entity.getColumnName()));
                        break;
                    case DOUBLE:
                        result.put(entity.getColumnName(), row.getDouble(entity.getColumnName()));
                        break;
                    case FLOAT:
                        result.put(entity.getColumnName(), row.getFloat(entity.getColumnName()));
                        break;
                    case UNIXTIME_MICROS:
                        result.put(entity.getColumnName(), row.getLong(entity.getColumnName()));
                        break;
                    default:
                        result.put(entity.getColumnName(),null);
                        break;
                }
            }
        }
        return result;
    }

    /**
     * 通用方法
     *
     * @param entity
     * @param operate
     * @param session
     * @return
     * @throws KuduException
     */
    public static OperationResponse operate(KuduRow entity, Operation operate, KuduSession session) throws KuduException {
        for (KuduColumn column : entity.getRows()) {
            KuduAgentUtils.WrapperKuduOperation(column, operate);
        }
        OperationResponse apply = session.apply(operate);
        return apply;
    }

    /**
     * 返回column的string list
     *
     * @param entitys
     * @return
     */
    public static List<String> getColumnNames(List<KuduColumn> entitys) {
        List<String> result = new ArrayList<>();
        for (KuduColumn entity : entitys) {
            if (entity.isSelect()) {
                result.add(entity.getColumnName());
            }
        }
        return result;
    }

    /**
     * 设置条件
     *
     * @param kuduTable
     * @param entitys
     * @param kuduScannerBuilder
     */
    public static void setKuduPredicates(KuduTable kuduTable, List<KuduColumn> entitys, KuduScanner.KuduScannerBuilder kuduScannerBuilder) {
        for (KuduColumn entity : entitys) {
            if (entity.getComparisonOp() != null) {
                KuduPredicate kuduPredicate = null;
                switch (entity.getColumnType()) {
                    case BOOL:
                        kuduPredicate = KuduPredicate.newComparisonPredicate(kuduTable.getSchema().getColumn(entity.getColumnName()), entity.getComparisonOp(), (Boolean) entity.getComparisonValue());
                        break;
                    case FLOAT:
                        kuduPredicate = KuduPredicate.newComparisonPredicate(kuduTable.getSchema().getColumn(entity.getColumnName()), entity.getComparisonOp(), (Float) entity.getComparisonValue());
                        break;
                    case DOUBLE:
                        kuduPredicate = KuduPredicate.newComparisonPredicate(kuduTable.getSchema().getColumn(entity.getColumnName()), entity.getComparisonOp(), (Double) entity.getComparisonValue());
                        break;
                    case BINARY:
                        kuduPredicate = KuduPredicate.newComparisonPredicate(kuduTable.getSchema().getColumn(entity.getColumnName()), entity.getComparisonOp(), (byte[]) entity.getComparisonValue());
                        break;
                    case STRING:
                        kuduPredicate = KuduPredicate.newComparisonPredicate(kuduTable.getSchema().getColumn(entity.getColumnName()), entity.getComparisonOp(), (String) entity.getComparisonValue());
                        break;
                    case UNIXTIME_MICROS:
                        kuduPredicate = KuduPredicate.newComparisonPredicate(kuduTable.getSchema().getColumn(entity.getColumnName()), entity.getComparisonOp(), (Long) entity.getComparisonValue());
                        break;
                    default:
                        kuduPredicate = KuduPredicate.newComparisonPredicate(kuduTable.getSchema().getColumn(entity.getColumnName()), entity.getComparisonOp(), (Double) entity.getComparisonValue());
                        break;
                }
                kuduScannerBuilder.addPredicate(kuduPredicate);
            }
        }
    }


    /**
     * 如果是update事件并且是更新字段就设置，如果非update事件都设置
     * 如果是delete事件是主键就设置，不是主键就不设置
     *
     * @param entity
     * @param operate
     * @return
     */
    public static boolean isSetLogic(KuduColumn entity, Operation operate) {
        return ((operate instanceof Update && entity.isUpdate()) || (operate instanceof Update && entity.isPrimaryKey())) || (operate instanceof Delete && entity.isPrimaryKey()) || (!(operate instanceof Update) && !(operate instanceof Delete));
    }

    public static List<OperationResponse> close(KuduSession session, KuduClient client) {
        if (null != session) {
            try {
                session.flush();
            } catch (KuduException e) {
                e.printStackTrace();
            }
        }
        List<OperationResponse> responses = null;
        if (null != session) {
            try {
                responses = session.close();
            } catch (KuduException e) {
                e.printStackTrace();
            }
        }
        if (null != client) {
            try {
                client.close();
            } catch (KuduException e) {
                e.printStackTrace();
            }
        }
        return responses;
    }

    public static void close(KuduScanner build, KuduClient client) {
        if (null != build) {
            try {
                build.close();
            } catch (KuduException e) {
                e.printStackTrace();
            }
        }
        if (null != client) {
            try {
                client.close();
            } catch (KuduException e) {
                e.printStackTrace();
            }
        }
    }


}