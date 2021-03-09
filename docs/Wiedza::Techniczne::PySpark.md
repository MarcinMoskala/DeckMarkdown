If you want to start working with Spark SQL with PySpark, you’ll need to start a *SparkSession* first: you can use this to *create DataFrames, register DataFrames as tables, execute SQL over the tables and read parquet files*.
<pre><code>
from pyspark.sql import *SparkSession*
spark = *SparkSession* \
    .*builder* \
    .*appName*("Python Spark SQL basic example") \
    .*config*("spark.some.config.option", "some-value") \
    .*getOrCreate()*
</code></pre>

Q/A: df.show()
A/Q: Display the content of df

Q/A: df.head(n) and df.take(n)
A/Q: Two ways to return first n rows from df as a list

Q/A: df.first()
A/Q: Return first row from df

Q/A: df.dtypes
A/Q: Return df column names and data types
[('PassengerId', 'int'),
 ('Survived', 'int'),
 ('Pclass', 'int'),
 ('Name', 'string'),
 ('Sex', 'string'),
 ...

Q/A: df.schema
A/Q: Return the schema of df
StructType(List(StructField(PassengerId,IntegerType,true),StructField(Survived,IntegerType,true),...

Q/A: df.describe().show()
A/Q: Compute summary statistics of df
<img src="pyspark_describe.png" style="zoom:50%;" />

Q/A: df.printSchema()
A/Q: Print the schema of df
root
 |-- PassengerId: integer (nullable = true)
 |-- Survived: integer (nullable = true)
 |-- Name: string (nullable = true)
 ...

Q/A: df.columns
A/Q: Return the columns of df
['PassengerId',
 'Survived',
 'Pclass',
 ...

Q: What to ask for first, to understand data better?
A: df.printSchema(), df.describe().show(), df.count(), df.dropDuplicates().count()

Q/A: df.count()
A/Q: Count the number of rows in df

Q/A: df.dropDuplicates() or df.distinct()
A/Q: Drop duplicate from the df

Q/A: df.distinct().count() or df.dropDuplicates().count()
A/Q: Count the number of distinct rows in df

Q: dropDuplicates vs distinct
A: dropDuplicates() was introduced in 1.4 as a replacement for distinct(), as you can use it's overloaded methods to get unique rows based on subset of columns.

Q/A: Show columns firstName and lastName from df.
A/Q: df.select("firstName","lastName").show()

Q/A: Show columns firstName and age + 1 from df.
A/Q: df.select(df["firstName"],df["age"]+ 1).show()
+--------------------+---------+
|                Name|(Age + 1)|
+--------------------+---------+
|Braund, Mr. Owen ...|     23.0|
|Cumings, Mrs. Joh...|     39.0|
|Heikkinen, Miss. ...|     27.0|
...

Q/A: Show column firstName and 1 when age > 30, 0 otherwise
A/Q: from pyspark.sql.functions import when
df.select("firstName", when(df.age > 30, 1).otherwise(0)).show()

Q/A: Show df rows with firstName either "Jane" or "Boris"
A/Q: df[df.firstName.isin("Jane","Boris")].show()

Q/A: Return all the elements of the dataset as an array at the driver program. This is usually useful after a filter or other operation that returns a sufficiently small subset of the data.
A/Q: ds.collect()

Q/A: Show df minimal age
A/Q: from pyspark.sql import functions as F
df.agg(F.min(df.age)).show()

Q/A: df.filter(df["age"]>24).show() or df.where(df["age"]>24).show()
A/Q: Keep only rows with age > 24. (2 ways)

Q: filter vs where
A: Identical

Q/A: Adding columns in PySpark
A/Q: df = df.withColumn('city',df.address.city) \
   .withColumn('postalCode',df.address.postalCode) \
   .withColumn('state',df.address.state) \
   .withColumn('streetAddress',df.address.streetAddress) \

Q: How to rename a column in PySpark?
A: df = df.withColumnRenamed('telePhoneNumber', 'phoneNumber')

Q/A: Remove a column in pyspark
A/Q: df = df.drop("address", "phoneNumber")

Q/A: Count rows with each age in df
A/Q: df.groupBy("age").count().show()

Q: What does df.groupBy("something") return?
A: GroupedData

Q: Take rows from df with age between 22 and 24
A: df.select(df.age.between(22, 24)).show()

Q: Take column with only first 4 letters from firstName
A: df.select(df.firstName.substr(0, 3).alias("name")).collect()

Q: Take lastName ending with th
A: df.select(df.lastName.endswith("th"))

Q/A: Average survival rate for each age
A/Q: df.groupBy("Age").mean("Survived").show()

Q/A: Average survival rate for each age decade
A/Q: from pyspark.sql.functions import col
def age_group(age):
   return 10 * floor(age / 10)
df.groupBy(age_group(col('Age'))).mean("Survived").show()

Q: Two ways to sort descending by age
A: df.sort(df.age.desc()).collect()
df.sort("age", ascending=False).collect()

Q/A: Sort by age descending, and by city ascending
A/Q: df.orderBy(["age","city"],ascending=[0,1]).collect()

Q/A: Fill lack of values in df with ""
A/Q: df.na.fill("") or df.fillna("")

Q: Replace value in df
A: df.replace(0, 100).show()

Q: Convert df into an RDD
A: rdd1 = df.rdd

Q: Return the contents of df as Pandas DataFrame
A: df.toPandas()

Q: Read df from json
A: df = spark.read.json("customer.json")

Q: Read df from csv, where the first line is header
A: df = spark.read.csv("customer.json",header = 'True',inferSchema='True')

Q: Save df as json
A: df.write.save("data.json",format="json")

Q: Save df as csv
A: df.write.save("data.csv",format="csv")

Q: Stopping Spark Session
A: spark.stop()