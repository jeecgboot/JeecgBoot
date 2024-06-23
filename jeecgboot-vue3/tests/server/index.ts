import Koa from 'koa';
import path from 'path';
import Router from 'koa-router';
import body from 'koa-body';
import cors from 'koa2-cors';
import koaStatic from 'koa-static';
import websockify from 'koa-websocket';
import route from 'koa-route';

import AppRoutes from './routes';

const PORT = 3300;

const app = websockify(new Koa());

app.ws.use(function (ctx, next) {
  ctx.websocket.send('connection succeeded!');
  return next(ctx);
});

app.ws.use(
  route.all('/test', function (ctx) {
    // ctx.websocket.send('Hello World');
    ctx.websocket.on('message', function (message) {
      // do something with the message from client

      if (message !== 'ping') {
        const data = JSON.stringify({
          id: Math.ceil(Math.random() * 1000),
          time: new Date().getTime(),
          res: `${message}`,
        });
        ctx.websocket.send(data);
      }
      console.log(message);
    });
  })
);

const router = new Router();

// router
AppRoutes.forEach((route) => router[route.method](route.path, route.action));

app.use(cors());
app.use(
  body({
    encoding: 'gzip',
    multipart: true,
    formidable: {
      // uploadDir: path.join(__dirname, '/upload/'), // 设置文件上传目录
      keepExtensions: true,
      maxFieldsSize: 20 * 1024 * 1024,
    },
  })
);
app.use(router.routes());
app.use(router.allowedMethods());
app.use(koaStatic(path.join(__dirname)));

app.listen(PORT, () => {
  console.log(`Application started successfully: http://localhost:${PORT}`);
});
