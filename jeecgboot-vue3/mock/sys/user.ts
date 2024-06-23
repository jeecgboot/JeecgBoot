import { MockMethod } from 'vite-plugin-mock';
import { resultError, resultSuccess, getRequestToken, requestParams, baseUrl } from '../_util';
export function createFakeUserList() {
  return [
    {
      userId: '1',
      username: 'admin',
      realname: '管理员',
      avatar: 'https://q1.qlogo.cn/g?b=qq&nk=190848757&s=640',
      desc: 'manager',
      password: '123456',
      token: 'fakeToken1',
      homePath: '/dashboard/analysis',
      roles: [
        {
          roleName: 'Super Admin',
          value: 'super',
        },
      ],
    },
    {
      userId: '2',
      username: 'jeecg',
      password: '123456',
      realname: '测试用户',
      avatar: 'https://q1.qlogo.cn/g?b=qq&nk=339449197&s=640',
      desc: 'tester',
      token: 'fakeToken2',
      homePath: '/dashboard/workbench',
      roles: [
        {
          roleName: 'Tester',
          value: 'test',
        },
      ],
    },
  ];
}

const fakeCodeList: any = {
  '1': ['1000', '3000', '5000'],

  '2': ['2000', '4000', '6000'],
};

export default [
  // mock user login
  {
    url: `${baseUrl}/sys/login`,
    timeout: 200,
    method: 'post',
    response: ({ body }) => {
      const { username, password } = body;
      const checkUser = createFakeUserList().find(
        (item) => item.username === username && password === item.password
      );
      if (!checkUser) {
        return resultError('Incorrect account or password！');
      }
      const { userId, username: _username, token, realname, desc, roles } = checkUser;
      return resultSuccess({
        roles,
        userId,
        username: _username,
        token,
        realname,
        desc,
      });
    },
  },
  {
    url: `${baseUrl}/sys/user/getUserInfo`,
    method: 'get',
    response: (request: requestParams) => {
      const token = getRequestToken(request);
      if (!token) return resultError('Invalid token');
      const checkUser = createFakeUserList().find((item) => item.token === token);
      if (!checkUser) {
        return resultError('The corresponding user information was not obtained!');
      }
      return resultSuccess(checkUser);
    },
  },
  {
    url: `${baseUrl}/sys/permission/getPermCode`,
    timeout: 200,
    method: 'get',
    response: (request: requestParams) => {
      const token = getRequestToken(request);
      if (!token) return resultError('Invalid token');
      const checkUser = createFakeUserList().find((item) => item.token === token);
      if (!checkUser) {
        return resultError('Invalid token!');
      }
      const codeList = fakeCodeList[checkUser.userId];

      return resultSuccess(codeList);
    },
  },
  {
    url: `${baseUrl}/sys/logout`,
    timeout: 200,
    method: 'get',
    response: (request: requestParams) => {
      const token = getRequestToken(request);
      if (!token) return resultError('Invalid token');
      const checkUser = createFakeUserList().find((item) => item.token === token);
      if (!checkUser) {
        return resultError('Invalid token!');
      }
      return resultSuccess(undefined, { message: 'Token has been destroyed' });
    },
  },
  {
    url: `${baseUrl}/sys/randomImage/1629428467008`,
    timeout: 200,
    method: 'get',
    response: (request: requestParams) => {
      const result =
        'data:image/jpg;base64,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';
      return resultSuccess(result);
    },
  },
] as MockMethod[];
