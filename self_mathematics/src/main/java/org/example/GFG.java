package org.example;

/**
 * @author kxh
 * @description 根据两点的坐标找到对应所在的方程
 * @date 20211209_15:42
 */
// Java Implementation to find the line passing through two points
class GFG {
    // This pair is used to store the X and Y coordinate of a point respectively
    static class Pair {
        double first, second;

        public Pair(double first, double second) {
            this.first = first;
            this.second = second;
        }
    }

    // Function to find the line given two points
    //Ax + By + C = 0
    //(y1 – y2)x + (x2 – x1)y + (x1y2 – x2y1) = 0
    static void lineFromPoints(Pair P, Pair Q) {
        double a = Q.second - P.second;
        double b = P.first - Q.first;
        double c = Q.first * P.second - Q.second * P.first;

        System.out.println(
                "The line passing through points P and Q is: " + a + "x + " + b + "y " + c + " = 0");
    }

    // Driver code
    public static void main(String[] args) {
        String tableName = "zws-etl.mdm_ph-ase_c1";
        //龙湖 在mysqlwriter时候,如果tableName="dbName.tableName" 而且dbName有中线"-"的情况下会有sql解析异常.
        if(tableName.contains("-")){
            if(tableName.contains(".")){
                String [] dbTableArr = tableName.split("\\.");
                String dbName = dbTableArr[0];
                String tableName2 = dbTableArr[1];
                if(dbName.contains("-")){
                    dbName = "`" + dbName + "`";
                }
                if(tableName.contains("-")){
                    tableName2 = "`" + tableName2 + "`";
                }
                tableName = dbName + "." + tableName2;
            }else{
                tableName = "`" + tableName + "`";
            }
        }

        String sql = "truncate table zws-etl.mdm_ph-ase_c1";
        StringBuilder sqlSb = new StringBuilder();
        String currentWord = "";
        String[] sqlSplitArr = sql.split(" ");
        for (int i = 0; i < sqlSplitArr.length; i++) {
            currentWord = sqlSplitArr[i];
            if(currentWord.contains("-")){
                if(sqlSplitArr[i].contains(".")){
                    String [] dbTableArr = currentWord.split("\\.");
                    String dbName = dbTableArr[0];
                    String tableName2 = dbTableArr[1];
                    if(dbName.contains("-")){
                        dbName = "`" + dbName + "`";
                    }
                    if(tableName.contains("-")){
                        tableName2 = "`" + tableName2 + "`";
                    }
                    currentWord = dbName + "." + tableName2;
                }else{
                    currentWord = "`" + currentWord + "`";
                }
            }
            sqlSb.append(currentWord + " ");
        }
        System.out.println(sqlSb);
        System.out.println(tableName);
        Pair P = new Pair(0.3, 60);
        Pair Q = new Pair(-0.5, 80);
        lineFromPoints(P, Q);
    }
}