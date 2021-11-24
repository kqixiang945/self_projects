package com.summerchill.dbutils.po;

/**
 * 该JavaBean对应的表的结构如下:
 * sch_project | CREATE TABLE `sch_project` (
 * `project_id` char(36) NOT NULL,
 * `project_name` char(36) NOT NULL,
 * `extend` text,
 * `create_time` int(11) NOT NULL,
 * `update_time` int(11) NOT NULL,
 * `creator` varchar(50) NOT NULL,
 * `last_modifier` varchar(50) NOT NULL,
 * `comment` text,
 * `active_flg` char(1) NOT NULL,
 * PRIMARY KEY (`project_id`),
 * UNIQUE KEY `sch_project_u1` (`project_name`)
 * ) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC |
 * <p>
 * 要实现从数据库中查询出来的sch_project表中的一条记录自动转换成对应的SchProject的javabean对象.
 * 查询多条对应一个List<SchProject> 对像的效果.
 */


public class SchProject {
    private String projectId;
    private String projectName;
    private String extend;
    private String createTime;
    private String updateTime;
    private String creator;
    private String lastModifier;
    private String comment;
    private String activeFlg;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(String lastModifier) {
        this.lastModifier = lastModifier;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getActiveFlg() {
        return activeFlg;
    }

    public void setActiveFlg(String activeFlg) {
        this.activeFlg = activeFlg;
    }
}
