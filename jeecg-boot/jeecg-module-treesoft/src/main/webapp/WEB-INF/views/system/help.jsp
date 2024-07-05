<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>  

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title></title>
 

<style>

 .table_border{  
    border: solid 1px #B4B4B4;  
    border-collapse: collapse;     --折叠样式.  
  }  
.table_border tr th{  
     
    padding-left:4px;  
    height:27px;  
    border: solid 1px #B4B4B4;  
}  
.table_border tr td{  
    height:25px;  
    padding:4px;  
    border: solid 1px #B4B4B4;  
}  

</style>

</head>


<body>
<div>
	 
<table class="table_border"  >
 
<tbody>
 
     <th width="27%" align="left" valign="top"> <b> SQL 语句  </b>  </th>
      <th width="73%" align="left" valign="top"> <b> 语法  </b>  </th>
 
<tr>
<td align="left" valign="top">AND / OR</td>
      <td align="left" valign="top">SELECT column_name(s)<br>
		FROM table_name<br>
		WHERE condition<br>
		AND|OR condition</td>
    </tr>
<tr>
<td align="left" valign="top">ALTER TABLE</td>
      <td align="left" valign="top">ALTER TABLE table_name <br>
		ADD column_name datatype<p>or</p>
		<p>ALTER TABLE table_name <br>
		DROP COLUMN column_name</p>
</td>
    </tr>
<tr>
<td align="left" valign="top">AS (alias)</td>
      <td align="left" valign="top">SELECT column_name AS column_alias<br>
		FROM table_name<p>or</p>
		<p>SELECT column_name<br>
		FROM table_name  AS table_alias</p>
</td>
    </tr>
<tr>
<td align="left" valign="top">BETWEEN</td>
      <td align="left" valign="top">SELECT column_name(s)<br>
		FROM table_name<br>
		WHERE column_name<br>
		BETWEEN value1 AND value2</td>
    </tr>
<tr>
<td align="left" valign="top">CREATE DATABASE</td>
      <td align="left" valign="top">CREATE DATABASE database_name</td>
    </tr>
<tr>
<td align="left" valign="top">CREATE TABLE</td>
      <td align="left" valign="top">CREATE TABLE table_name<br>
		(<br>
		column_name1 data_type,<br>
		column_name2 data_type,<br>
		column_name2 data_type,<br>
		...<br>
		)</td>
    </tr>
<tr>
<td align="left" valign="top">CREATE INDEX</td>
      <td align="left" valign="top">CREATE INDEX index_name<br>
		ON table_name (column_name)<p>or</p>
		<p>CREATE UNIQUE INDEX index_name<br>
		ON table_name (column_name)</p>
</td>
    </tr>
<tr>
<td align="left" valign="top">CREATE VIEW</td>
      <td align="left" valign="top">CREATE VIEW view_name AS<br>
		SELECT column_name(s)<br>
		FROM table_name<br>
		WHERE condition</td>
    </tr>
<tr>
<td align="left" valign="top">DELETE</td>
      <td align="left" valign="top">DELETE FROM table_name<br>
		WHERE some_column=some_value<p>or</p>
		<p>DELETE FROM table_name <br>
		(<b>Note: </b>Deletes the entire table!!)</p>
		<p>DELETE * FROM table_name <br>
		(<b>Note: </b>Deletes the entire table!!)</p>
</td>
    </tr>
<tr>
<td align="left" valign="top">DROP DATABASE</td>
      <td align="left" valign="top">DROP DATABASE database_name</td>
    </tr>
<tr>
<td align="left" valign="top">DROP INDEX</td>
      <td align="left" valign="top">DROP INDEX table_name.index_name (SQL 
		Server)<br>
		DROP INDEX index_name ON table_name (MS Access)<br>
		DROP INDEX index_name (DB2/Oracle)<br>
		ALTER TABLE table_name<br>
		DROP INDEX index_name (MySQL)</td>
    </tr>
<tr>
<td align="left" valign="top">DROP TABLE</td>
      <td align="left" valign="top">DROP TABLE table_name</td>
    </tr>
<tr>
<td align="left" valign="top">GROUP BY</td>
      <td align="left" valign="top">SELECT column_name, 
		aggregate_function(column_name)<br>
		FROM table_name<br>
		WHERE column_name operator value<br>
		GROUP BY column_name</td>
    </tr>
<tr>
<td align="left" valign="top">HAVING</td>
      <td align="left" valign="top">SELECT column_name, 
		aggregate_function(column_name)<br>
		FROM table_name<br>
		WHERE column_name operator value<br>
		GROUP BY column_name<br>
		HAVING aggregate_function(column_name) operator value</td>
    </tr>
<tr>
<td align="left" valign="top">IN</td>
      <td align="left" valign="top">SELECT column_name(s)<br>
		FROM table_name<br>
		WHERE column_name<br>
		IN (value1,value2,..)</td>
    </tr>
<tr>
<td align="left" valign="top">INSERT INTO</td>
      <td align="left" valign="top">INSERT INTO table_name<br>
		VALUES (value1, value2, value3,....)<p><i>or</i></p>
		<p>INSERT INTO table_name<br>
		(column1, column2, column3,...)<br>
		VALUES (value1, value2, value3,....)</p>
</td>
    </tr>
<tr>
<td align="left" valign="top">INNER JOIN</td>
      <td align="left" valign="top">SELECT column_name(s)<br>
		FROM table_name1<br>
		INNER JOIN table_name2 <br>
		ON table_name1.column_name=table_name2.column_name</td>
    </tr>
<tr>
<td align="left" valign="top">LEFT JOIN</td>
      <td align="left" valign="top">SELECT column_name(s)<br>
		FROM table_name1<br>
		LEFT JOIN table_name2 <br>
		ON table_name1.column_name=table_name2.column_name</td>
    </tr>
<tr>
<td align="left" valign="top">RIGHT JOIN</td>
      <td align="left" valign="top">SELECT column_name(s)<br>
		FROM table_name1<br>
		RIGHT JOIN table_name2 <br>
		ON table_name1.column_name=table_name2.column_name</td>
    </tr>
<tr>
<td align="left" valign="top">FULL JOIN</td>
      <td align="left" valign="top">SELECT column_name(s)<br>
		FROM table_name1<br>
		FULL JOIN table_name2 <br>
		ON table_name1.column_name=table_name2.column_name</td>
    </tr>
<tr>
<td align="left" valign="top">LIKE</td>
      <td align="left" valign="top">SELECT column_name(s)<br>
		FROM table_name<br>
		WHERE column_name
		LIKE pattern</td>
    </tr>
<tr>
<td align="left" valign="top">ORDER BY</td>
      <td align="left" valign="top">SELECT column_name(s)<br>
		FROM table_name<br>
		ORDER BY column_name [ASC|DESC]</td>
    </tr>
<tr>
<td align="left" valign="top">SELECT</td>
      <td align="left" valign="top">SELECT column_name(s)<br>
		FROM table_name</td>
    </tr>
<tr>
<td align="left" valign="top">SELECT *</td>
      <td align="left" valign="top">SELECT *<br>
		FROM table_name</td>
    </tr>
<tr>
<td align="left" valign="top">SELECT DISTINCT</td>
      <td align="left" valign="top">SELECT DISTINCT column_name(s)<br>
		FROM table_name</td>
    </tr>
<tr>
<td align="left" valign="top">SELECT INTO</td>
      <td align="left" valign="top">SELECT *<br>
		INTO new_table_name [IN externaldatabase]<br>
		FROM old_table_name<p><i>or</i></p>
		<p>SELECT column_name(s)<br>
		INTO new_table_name [IN externaldatabase]<br>
		FROM old_table_name</p>
</td>
    </tr>
<tr>
<td align="left" valign="top">SELECT TOP</td>
      <td align="left" valign="top">SELECT TOP number|percent column_name(s)<br>
		FROM table_name</td>
    </tr>
<tr>
<td align="left" valign="top">TRUNCATE TABLE</td>
      <td align="left" valign="top">TRUNCATE TABLE table_name</td>
    </tr>
<tr>
<td align="left" valign="top">UNION</td>
      <td align="left" valign="top">SELECT column_name(s) FROM table_name1<br>
		UNION<br>
		SELECT column_name(s) FROM table_name2</td>
    </tr>
<tr>
<td align="left" valign="top">UNION ALL</td>
      <td align="left" valign="top">SELECT column_name(s) FROM table_name1<br>
		UNION ALL<br>
		SELECT column_name(s) FROM table_name2</td>
    </tr>
<tr>
<td align="left" valign="top">UPDATE</td>
      <td align="left" valign="top">UPDATE table_name<br>
		SET column1=value, column2=value,...<br>
		WHERE some_column=some_value</td>
    </tr>
<tr>
<td align="left" valign="top">WHERE</td>
      <td align="left" valign="top">SELECT column_name(s)<br>
		FROM table_name<br>
		WHERE column_name operator value</td>
    </tr>
</tbody> 
			       
              
                
		</table>
	</form>
</div>
  

</body>
</html>