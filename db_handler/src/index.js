import { DatabaseSync } from "node:sqlite";
import { read_schema } from "./schema/dbschema.js";
import { read_export } from "./schema/export.js";
import fs from "node:fs";

const init_queries = read_schema();

const DB_PATH = "./db_output/";
fs.unlinkSync(DB_PATH + "test.db");
const database = new DatabaseSync(DB_PATH + "test.db");

for (const q of init_queries) {
  database.exec(q);
}

const queries = read_export("./input_file/test-1778740202362.txt");

// settings
for (const q of queries.settings) {
  database.exec(q);
}

for (const q of queries.food) {
  database.exec(q);
}

for (const q of queries.foodEaten) {
  database.exec(q);
}
