import path from 'path';
import fs from 'fs-extra';

const uploadUrl = 'http://localhost:3300/static/upload';
const filePath = path.join(__dirname, '../static/upload/');

fs.ensureDir(filePath);
export default class UserService {
  async upload(ctx, files, isMultiple) {
    let fileReader, fileResource, writeStream;

    const fileFunc = function (file) {
      fileReader = fs.createReadStream(file.path);
      fileResource = filePath + `/${file.name}`;
      console.log(fileResource);

      writeStream = fs.createWriteStream(fileResource);
      fileReader.pipe(writeStream);
    };

    const returnFunc = function (flag) {
      if (flag) {
        let url = '';
        for (let i = 0; i < files.length; i++) {
          url += uploadUrl + `/${files[i].name},`;
        }
        url = url.replace(/,$/gi, '');
        ctx.body = {
          url: url,
          code: 0,
          message: 'upload Success!',
        };
      } else {
        ctx.body = {
          url: uploadUrl + `/${files.name}`,
          code: 0,
          message: 'upload Success!',
        };
      }
    };
    console.log(isMultiple, files.length);

    if (isMultiple) {
      for (let i = 0; i < files.length; i++) {
        const f1 = files[i];
        fileFunc(f1);
      }
    } else {
      fileFunc(files);
    }
    fs.ensureDir(filePath);
    returnFunc(isMultiple);
  }
}
