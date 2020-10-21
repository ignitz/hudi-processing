package utils

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

object DataFrameTools {
  def deduplicateRows(
      df: DataFrame,
      idCol: String,
      sortColumn: String = "ts_ms",
      repartitionBy: String = "created_at"
  ): DataFrame = {
    return df
      .repartition(col(repartitionBy))
      .sort(col(sortColumn).desc)
      .dropDuplicates(idCol)
  }
}
