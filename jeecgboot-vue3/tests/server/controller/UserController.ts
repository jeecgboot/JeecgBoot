import UserService from '../service/UserService';

class UserController {
  private service: UserService = new UserService();

  login = async (ctx) => {
    ctx.body = await this.service.login();
  };

  getUserInfoById = async (ctx) => {
    ctx.body = await this.service.getUserInfoById();
  };
}

export default new UserController();
