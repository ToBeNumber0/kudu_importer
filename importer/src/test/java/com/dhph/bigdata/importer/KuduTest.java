package com.dhph.bigdata.importer;

import com.dhph.bigdata.importer.kudu.KuduAgent;
import com.dhph.bigdata.importer.kudu.KuduColumn;
import com.dhph.bigdata.importer.kudu.KuduRow;
import org.apache.kudu.Type;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduPredicate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Wyq
 * @create 2019/4/23
 * @Description
 **/
@SpringBootTest(classes = ImporterApplication.class)
@RunWith(SpringRunner.class)
public class KuduTest {

    @Resource
    private KuduAgent agent;

    @Test
    public void selectTest() {
        KuduColumn column01 = new KuduColumn();
        column01.setColumnName("name").setColumnType(Type.STRING).setSelect(true).setComparisonOp(KuduPredicate.ComparisonOp.EQUAL).setComparisonValue("lijie003");
        KuduColumn column02 = new KuduColumn();
        column02.setColumnName("id").setSelect(true).setColumnType(Type.INT64);
        KuduColumn column03 = new KuduColumn();
        column03.setColumnName("sex").setSelect(true).setColumnType(Type.INT64);
        List<KuduColumn> list = new ArrayList<>();
        list.add(column01);
        list.add(column02);
        list.add(column03);
        List<Map<String, Object>> select = agent.select("impala::impala_kudu.my_first_table", list);
        System.out.println("-----------------" + select);
    }

    @Test
    public void alterColumnTEST() throws KuduException {
        KuduRow myrows01 = new KuduRow();
        myrows01.setTableName("impala::impala_kudu.my_first_table");
        KuduColumn c01 = new KuduColumn();
        c01.setColumnName("newsex").setNewColumnName("sex");
        c01.setAlterColumnEnum(KuduColumn.AlterColumnEnum.RENAME_COLUMN);
        KuduColumn c02 = new KuduColumn();
        c02.setColumnName("myadd").setAlterColumnEnum(KuduColumn.AlterColumnEnum.DROP_COLUMN);
        List<KuduColumn> rows01 = new ArrayList<>();
        rows01.add(c01);
        rows01.add(c02);
        myrows01.setRows(rows01);

        KuduRow myrows11 = new KuduRow();
        myrows11.setTableName("impala::impala_kudu.my_first_table");
        KuduColumn c11 = new KuduColumn();
        c11.setColumnName("newname").setNewColumnName("name");
        c11.setAlterColumnEnum(KuduColumn.AlterColumnEnum.RENAME_COLUMN);
        KuduColumn c12 = new KuduColumn();
        c12.setColumnName("nickName").setAlterColumnEnum(KuduColumn.AlterColumnEnum.ADD_COLUMN).setNullAble(false).setColumnType(Type.STRING).setDefaultValue("aaa");
        List<KuduColumn> rows11 = new ArrayList<>();
        rows11.add(c11);
        rows11.add(c12);
        myrows11.setRows(rows11);

        List<KuduRow> list = new ArrayList<>();
        list.add(myrows01);
        list.add(myrows11);

        agent.alter(list);
    }

    @Test
    public void renameTEST() throws KuduException {
        KuduRow myrows01 = new KuduRow();
        myrows01.setTableName("impala::impala_kudu.my_first_table");
        myrows01.setNewTableName("impala::impala_kudu.my_first_table1");
        myrows01.setAlterTableEnum(KuduRow.AlterTableEnum.RENAME_TABLE);

        KuduRow myrows02 = new KuduRow();
        myrows02.setTableName("impala::impala_kudu.my_first_table1");
        myrows02.setNewTableName("impala::impala_kudu.my_first_table");
        myrows02.setAlterTableEnum(KuduRow.AlterTableEnum.RENAME_TABLE);

        List<KuduRow> list = new ArrayList<>();
        list.add(myrows01);
        list.add(myrows02);

        agent.alter(list);
    }

    @Test
    public void deleteMultTEST() throws KuduException {
        //第一行
        KuduColumn c01 = new KuduColumn();
        c01.setColumnName("id").setColumnValue(1000001).setColumnType(Type.INT64).setUpdate(false).setPrimaryKey(true);
        KuduColumn c02 = new KuduColumn();
        c02.setColumnName("name").setColumnValue("lijie123").setColumnType(Type.STRING).setUpdate(false);
        List<KuduColumn> row01 = new ArrayList<>();
        row01.add(c01);
//        row01.add(c02);
        KuduRow myrows01 = new KuduRow();
        myrows01.setRows(row01);

        //第一行
        KuduColumn c11 = new KuduColumn();
        c11.setColumnName("id").setColumnValue(1000002).setColumnType(Type.INT64).setUpdate(false).setPrimaryKey(true);
        KuduColumn c12 = new KuduColumn();
        c12.setColumnName("name").setColumnValue("lijie123").setColumnType(Type.STRING).setUpdate(false);
        List<KuduColumn> row11 = new ArrayList<>();
        row11.add(c11);
//        row11.add(c12);
        KuduRow myrows11 = new KuduRow();
        myrows11.setRows(row11);

        List<KuduRow> rows = new ArrayList<>();
        rows.add(myrows01);
        rows.add(myrows11);
        agent.delete("impala::impala_kudu.my_first_table", rows);
    }

    @Test
    public void deleteSingleTEST() throws KuduException {
        //第一行
        KuduColumn c01 = new KuduColumn();
        c01.setColumnName("id").setColumnValue(1000003).setColumnType(Type.INT64).setUpdate(false).setPrimaryKey(true);
        KuduColumn c02 = new KuduColumn();
        c02.setColumnName("name").setColumnValue("lijie789").setColumnType(Type.STRING).setUpdate(false);
        List<KuduColumn> row01 = new ArrayList<>();
        row01.add(c01);
//        row01.add(c02);
        KuduRow myrows01 = new KuduRow();
        myrows01.setRows(row01);
        agent.delete("impala::impala_kudu.my_first_table", myrows01);
    }

    @Test
    public void updateMultTEST() throws KuduException {
        //第一行
        KuduColumn c01 = new KuduColumn();
        c01.setColumnName("id").setColumnValue(1000001).setColumnType(Type.INT64).setUpdate(false).setPrimaryKey(true);
        KuduColumn c02 = new KuduColumn();
        c02.setColumnName("name").setColumnValue("lijie123").setColumnType(Type.STRING).setUpdate(false);
        List<KuduColumn> row01 = new ArrayList<>();
        row01.add(c01);
        row01.add(c02);
        KuduRow myrows01 = new KuduRow();
        myrows01.setRows(row01);
        //第二行
        KuduColumn c11 = new KuduColumn();
        c11.setColumnName("id").setColumnValue(1000002).setColumnType(Type.INT64).setUpdate(false).setPrimaryKey(true);
        KuduColumn c12 = new KuduColumn();
        c12.setColumnName("name").setColumnValue("lijie456").setColumnType(Type.STRING).setUpdate(false);
        List<KuduColumn> row11 = new ArrayList<>();
        row11.add(c11);
        row11.add(c12);
        KuduRow myrows11 = new KuduRow();
        myrows11.setRows(row11);

        List<KuduRow> rows = new ArrayList<>();
        rows.add(myrows01);
        rows.add(myrows11);
        agent.update("impala::impala_kudu.my_first_table", rows);
    }

    @Test
    public void updateSingleTEST() throws KuduException {
        //第一行
        KuduColumn c01 = new KuduColumn();
        c01.setColumnName("id").setColumnValue(12).setColumnType(Type.INT64).setUpdate(false).setPrimaryKey(true);
        KuduColumn c02 = new KuduColumn();
        c02.setColumnName("name").setColumnValue("lijie789").setColumnType(Type.STRING).setUpdate(false);
        KuduColumn c03 = new KuduColumn();
        c03.setColumnName("sex").setColumnValue("lijie789").setColumnType(Type.STRING).setUpdate(false);
        List<KuduColumn> row01 = new ArrayList<>();
        row01.add(c01);
        row01.add(c02);
        row01.add(c03);
        KuduRow myrows01 = new KuduRow();
        myrows01.setRows(row01);
        agent.update("impala::impala_kudu.my_first_table", myrows01);
    }

    @Test
    public void insertMultTEST() throws KuduException {
        //第一行
        KuduColumn c01 = new KuduColumn();
        c01.setColumnName("id").setColumnValue(1000001).setColumnType(Type.INT64).setUpdate(false);
        KuduColumn c02 = new KuduColumn();
        c02.setColumnName("name").setColumnValue("lijie001").setColumnType(Type.STRING).setUpdate(false);
        List<KuduColumn> row01 = new ArrayList<>();
        row01.add(c01);
        row01.add(c02);
        KuduRow myrows01 = new KuduRow();
        myrows01.setRows(row01);

        //第二行
        KuduColumn c11 = new KuduColumn();
        c11.setColumnName("id").setColumnValue(1000002).setColumnType(Type.INT64).setUpdate(false);
        KuduColumn c12 = new KuduColumn();
        c12.setColumnName("name").setColumnValue("lijie002").setColumnType(Type.STRING).setUpdate(false);
        List<KuduColumn> row02 = new ArrayList<>();
        row02.add(c11);
        row02.add(c12);
        KuduRow myrows02 = new KuduRow();
        myrows02.setRows(row02);

        List<KuduRow> rows = new ArrayList<>();
        rows.add(myrows01);
        rows.add(myrows02);

        agent.insert("impala::impala_kudu.my_first_table", rows);
    }

    @Test
    public void insertSingleTEST() throws KuduException {
        //第一行
        KuduColumn c01 = new KuduColumn();
        c01.setColumnName("id").setColumnValue(1000003).setColumnType(Type.INT64).setUpdate(false);
        KuduColumn c02 = new KuduColumn();
        c02.setColumnName("name").setColumnValue("lijie003").setColumnType(Type.STRING).setUpdate(false);
        List<KuduColumn> row01 = new ArrayList<>();
        row01.add(c01);
        row01.add(c02);
        KuduRow myrows01 = new KuduRow();
        myrows01.setRows(row01);
        agent.insert("impala::impala_kudu.my_first_table", myrows01);
    }

    @Test
    public  void testKafkaInsert() throws KuduException {
        String msg = "{\"id\": \"2083\",\"name\": \"票据质押第392期-201611291010\",\"serial_num\": \"2083\",\"borrower_id\": \"133850\",\"loan_type\": \"10040001\",\"loan_sub_type\": \"10060004\",\"pack_type\": \"10050001\",\"poundage_percent\": \"5.3\",\"poundage\": \"1465.27\",\"amount\": \"57200\",\"surplus_amount\": \"0\",\"frozen_amount\": \"0\",\"annual_yield\": \"10.7\",\"service_fee_percent\": \"2.4\",\"term\": \"174\",\"term_unit\": \"99000001\",\"period\": \"6\",\"total_period\": \"6\",\"min_investment_amount\": \"100\",\"max_investment_amount\": \"0\",\"interest_start_mode\": \"10070001\",\"repay_mode\": \"10080001\",\"intelligent_allocation_type\": \"10090001\",\"publish_days\": \"7\",\"published_time\": \"29/11/2016 15:00:00\",\"publish_mode\": \"10100001\",\"finished_time\": \"30/11/2016 11:02:53\",\"expire_time\": \"\",\"recommendation\": \"0\",\"status\": \"10110007\",\"interest_start_time\": \"1/12/2016 11:10:27\",\"auto_brimmed\": \"0\",\"expect_end_time\": \"24/5/2017 11:10:27\",\"payoff_time\": \"24/5/2017 11:10:27\",\"virtual_order\": \"1\",\"level\": \"R3\",\"expect_interest_amount\": \"2958.19\",\"expect_additional_amount\": \"0\",\"paid_principal_amount\": \"57200\",\"paid_interest_amount\": \"2958.19\",\"matched\": \"0\",\"frozen_on_loan_amount\": \"0\",\"paid_additional_amount\": \"0\",\"risk_report_image_status\": \"0\",\"update_timestamp\": \"26/12/2018 13:26:12\",\"enabled\": \"1\",\"deleted\": \"0\",\"create_timestamp\": \"29/11/2016 10:11:09\",\"creator\": \"\",\"creator_name\": \"\",\"modify_timestamp\": \"26/12/2018 13:26:12\",\"modifier\": \"\",\"modifier_name\": \"\",\"audit_status\": \"99010004\",\"auditor\": \"\",\"auditor_name\": \"\",\"audit_timestamp\": \"0/1/2000 00:00:00\",\"lock_version\": \"0\",\"available_coupon\": \"\",\"serial_num_bak\": \"PJ-ZY-201611291010\",\"poundage_flag\": \"1\",\"full_audit_status\": \"\",\"retreat\": \"0\",\"operator\": \"\"}\n";
        String tableName = "impala::kudu_database.pd_product_0422";
        KuduRow rows = agent.transfer(msg);
        System.out.println(rows);
        agent.insert(tableName, rows);
    }
}
