import fs from "node:fs";

const DEFAULT_PATH =
  "/../app/schemas/com.thisthatdc.calorytracker.data.AppDatabase/";

export function read_schema(path) {
  const folder = !path ? process.cwd() + DEFAULT_PATH : path;

  const data = fs.readdirSync(folder);
  data.sort();
  const db_schema = data[data.length - 1];
  const content = fs.readFileSync(`${folder}/${db_schema}`, "utf8");
  const json = JSON.parse(content);

  const queries = [];
  // add setup
  for (const v of json.database.setupQueries) {
    queries.push(v);
  }
  // add tables
  for (const v of json.database.entities) {
    const tableName = v.tableName;
    const sql = v.createSql;
    queries.push(sql.replace("${TABLE_NAME}", tableName));
  }
  return queries;
}
