import { DatabaseSync } from "node:sqlite";
import { read_schema } from "./schema/dbschema.js";
import fs from "node:fs";

const DB_PATH = "./db_output/";
fs.unlinkSync(DB_PATH + "test.db");
const database = new DatabaseSync(DB_PATH + "test.db");

const init_queries = read_schema();
for (const q of init_queries) {
  database.exec(q);
}

const content = fs.readFileSync("initial_food/data.json", "utf8");
const food = JSON.parse(content);

const queries = [];
let i = 0;
for (const f of food) {
  queries.push(`
    INSERT INTO "food"("uid","name","servingUnit","calories","carbs","fat","protein","sugar","fiber","defined_by") VALUES 
    (${i++},'${f.name.replace("'", "''")}','${f.unit}',${f.calories},
    '${f.carbs}','${f.fat}','${f.protein}','${f.sugar}',
    '${f.fiber}','SYSTEM');
      `);
}

for (const q of queries) {
  try {
    database.exec(q);
  } catch (error) {
    console.log(q);
    throw error;
  }
}
