import fs from "node:fs";

const content = fs.readFileSync("initial_food/data.json", "utf8");
const food = JSON.parse(content);
console.log(food.length);
const ids = new Set();
const food2 = food.filter(({ id }) => !ids.has(id) && ids.add(id));

fs.writeFileSync("initial_food/data_dedup.json", JSON.stringify(food2));
