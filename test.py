def hello():
    password = "123456"  # 故意写个安全问题让 AI 发现
    eval(input())        # 再来个危险操作
    return password
