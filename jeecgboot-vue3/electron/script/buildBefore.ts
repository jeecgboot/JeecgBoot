import path from 'path';
import fs from 'fs';

const root = path.join(__dirname, '../../');
const electronDistRoot = path.join(root, 'dist/electron');

let yamlName = 'electron-builder.yaml';
const sourcePath = fs.readFileSync(path.join(root, yamlName), 'utf-8');

try {
  // 通过正则表达式匹配 appId 和 productName
  const appIdMatch = sourcePath.match(/appId:\s*['"]([^'"]+)['"]/);
  const productNameMatch = sourcePath.match(/productName:\s*['"]([^'"]+)['"]/);
  if (appIdMatch && productNameMatch) {
    const fileContent = `${appIdMatch[0]}\n${productNameMatch[0]}`;
    yamlName = 'env.yaml';
    const targetPath = path.join(electronDistRoot, yamlName);
    fs.writeFileSync(targetPath, fileContent, 'utf-8');
    console.log(`✨ write dist ${yamlName} successfully.`);
  } else {
    throw new Error('appId or productName not found');
  }
} catch (e) {
  console.error(e);
  console.error(`请检查 ${yamlName} 是否存在，或者内容是否正确`);
  process.exit(1);
}
