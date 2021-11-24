package com.summerchill.dbutils;

import com.summerchill.dbutils.po.SchProject;
import org.apache.commons.dbutils.*;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DbUtilsApp {
    static Connection con = null;

    public static void main(String[] args) {
        List<SchProject> projectAll = getProjectAll();
        List<SchProject> projectAll2 = getProjectAll2();
    }

    /**
     * 做数据库字段 和 对应实体类不同的话,需要做对应的映射
     *
     * @return
     */
    public static List<SchProject> getProjectAll() {
        List<SchProject> schProjectList = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select * from sch_project";
            Map<String, String> mapColumnsToProperties = new HashMap<>();
            //Part1:
            //mapping you database to entity here 做数据库字段 和 对应实体类不同的映射配置
            mapColumnsToProperties.put("project_id", "projectId");
            mapColumnsToProperties.put("database_column", "projectName");
            mapColumnsToProperties.put("extend", "extend");
            mapColumnsToProperties.put("create_time", "createTime");
            mapColumnsToProperties.put("update_time", "updateTime");
            mapColumnsToProperties.put("creator", "creator");
            mapColumnsToProperties.put("lastModifier", "lastModifier");
            mapColumnsToProperties.put("comment", "comment");
            mapColumnsToProperties.put("active_flg", "activeFlg");
            BeanProcessor beanProcessor = new BeanProcessor(mapColumnsToProperties);
            RowProcessor rowProcessor = new BasicRowProcessor(beanProcessor);
            ResultSetHandler<List<SchProject>> resultSetHandler = new BeanListHandler<>(SchProject.class, rowProcessor);

            schProjectList = queryRunner.query(con, sql, resultSetHandler);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return schProjectList;
    }

    /**
     * 做数据库字段 和 对应实体类属性如果完全一样不需要做映射
     *
     * @return
     */
    public static List<SchProject> getProjectAll2() {
        List<SchProject> schProjectList = null;
        try {
            QueryRunner queryRunner = new QueryRunner();
            String sql = "select * from sch_project";
            schProjectList = queryRunner.query(con, sql, new BeanListHandler<>(SchProject.class));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return schProjectList;
    }
}

