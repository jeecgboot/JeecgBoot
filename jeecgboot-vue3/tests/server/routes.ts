import UserController from './controller/UserController';
import FileController from './controller/FileController';

export default [
  // user
  {
    path: '/login',
    method: 'post',
    action: UserController.login,
  },
  {
    path: '/getUserInfoById',
    method: 'get',
    action: UserController.getUserInfoById,
  },

  // file
  {
    path: '/upload',
    method: 'post',
    action: FileController.upload,
  },
];
