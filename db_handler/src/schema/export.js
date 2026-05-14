import fs from "node:fs";

const MAGIC_NUMBER = [0xf0, 0x9f, 0x96, 0x95];

export function read_export(file) {
  const content = fs.readFileSync(file, "utf8");
  const magic = content.substring(0, 2);
  const buffer = Buffer.from(magic);
  const value = content.substring(2);
  let flag = true;
  for (let i = 0; i < MAGIC_NUMBER.length; i++) {
    if (buffer[i] != MAGIC_NUMBER[i]) {
      flag = false;
    }
  }
  if (!flag) {
    throw new Error("Not a valid file");
  }
  // process value
  const output = {
    settings: [],
    food: [],
    foodEaten: [],
  };
  const json = JSON.parse(value);
  output.settings.push(
    `
    INSERT INTO "settings"("uid","daily_calories","daily_fat","daily_protein","daily_carbs") VALUES 
  (${json.settings.uid},${json.settings.calories},${json.settings.fat},${json.settings.protein},${json.settings.carbs});
    `,
  );

  for (const f of json.food) {
    output.food.push(
      `
    INSERT INTO "food"("uid","name","servingUnit","calories","carbs","fat","protein","sugar","fiber","defined_by") VALUES 
    (${f.uid},'${f.name}','${f.unit}',${f.calories},
    '${f.carbs}','${f.fat}','${f.protein}','${f.sugar}',
    '${f.fiber}','${f.definedBy}');
      `,
    );
  }
  for (const f of json.foodEaten) {
    output.foodEaten.push(
      `
    INSERT INTO "food_eaten"("uid","food_data","created_at","quantity","meal") VALUES 
    (${f.uid},${f.foodId},${f.createdAt},${f.quantity},'${f.meal}');
    `,
    );
  }
  return output;
}
