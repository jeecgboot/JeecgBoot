# Testing Strategy - JeecgBoot

[English](#english) | [中文](#中文)

---

## English

### Table of Contents
- [Overview](#overview)
- [Testing Philosophy](#testing-philosophy)
- [Backend Testing (Java)](#backend-testing-java)
- [Frontend Testing (Vue3)](#frontend-testing-vue3)
- [Test Coverage Requirements](#test-coverage-requirements)
- [Running Tests](#running-tests)
- [Writing Tests](#writing-tests)
- [CI/CD Integration](#cicd-integration)
- [Best Practices](#best-practices)

---

### Overview

JeecgBoot is an enterprise-grade AI low-code platform that requires comprehensive testing to ensure reliability, maintainability, and quality. This document outlines our testing strategy, requirements, and best practices.

**Current Status:**
- ⚠️ Tests are currently disabled in Maven builds (`skipTests=true`)
- 📊 Test coverage is minimal (~14 test files)
- 🎯 Goal: Achieve 70%+ backend coverage, 50%+ frontend coverage

---

### Testing Philosophy

**Our Testing Pyramid:**
```
        /\        E2E Tests (10%)
       /  \       - Critical user workflows
      /    \      - Cross-system integration
     /------\     
    / Integr \    Integration Tests (20%)
   /  ation   \   - API testing
  /   Tests    \  - Database interactions
 /--------------\ 
/  Unit Tests   \ Unit Tests (70%)
/                \ - Business logic
------------------  - Utility functions
```

**Key Principles:**
1. **Test First**: Write tests before or alongside code
2. **Fast Feedback**: Unit tests should run in seconds
3. **Isolated**: Tests should not depend on each other
4. **Readable**: Tests are documentation
5. **Maintainable**: Keep tests simple and DRY

---

### Backend Testing (Java)

#### Technology Stack
- **JUnit 5**: Primary testing framework
- **Mockito**: Mocking framework
- **Spring Boot Test**: Integration testing
- **AssertJ**: Fluent assertions
- **TestContainers**: Database integration tests
- **RestAssured**: API testing

#### Test Structure
```
src/
├── main/java/
│   └── org/jeecg/modules/
│       └── system/
│           ├── controller/
│           │   └── SysUserController.java
│           ├── service/
│           │   └── ISysUserService.java
│           └── entity/
│               └── SysUser.java
└── test/java/
    └── org/jeecg/modules/
        └── system/
            ├── controller/
            │   └── SysUserControllerTest.java
            ├── service/
            │   └── SysUserServiceTest.java
            └── integration/
                └── SysUserIntegrationTest.java
```

#### Unit Test Example
```java
@ExtendWith(MockitoExtension.class)
class SysUserServiceTest {
    
    @Mock
    private SysUserMapper userMapper;
    
    @InjectMocks
    private SysUserServiceImpl userService;
    
    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        // Given
        SysUser user = new SysUser();
        user.setUsername("testuser");
        user.setRealname("Test User");
        
        when(userMapper.insert(any(SysUser.class))).thenReturn(1);
        
        // When
        boolean result = userService.save(user);
        
        // Then
        assertThat(result).isTrue();
        verify(userMapper).insert(user);
    }
    
    @Test
    @DisplayName("Should throw exception when username exists")
    void shouldThrowExceptionWhenUsernameExists() {
        // Given
        SysUser user = new SysUser();
        user.setUsername("existing");
        
        when(userMapper.selectOne(any())).thenReturn(new SysUser());
        
        // When & Then
        assertThatThrownBy(() -> userService.save(user))
            .isInstanceOf(JeecgBootException.class)
            .hasMessageContaining("Username already exists");
    }
}
```

#### Integration Test Example
```java
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SysUserIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    @DisplayName("Should create user via API")
    void shouldCreateUserViaAPI() throws Exception {
        // Given
        SysUser user = new SysUser();
        user.setUsername("apitest");
        user.setRealname("API Test");
        
        // When & Then
        mockMvc.perform(post("/sys/user/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.message").value("添加成功!"));
    }
}
```

#### What to Test

**✅ DO Test:**
- Business logic in services
- Data validation
- Error handling
- Edge cases and boundary conditions
- Security checks (permissions, authentication)
- Data transformations
- Complex algorithms

**❌ DON'T Test:**
- Framework code (Spring, MyBatis)
- Simple getters/setters
- Configuration classes (unless complex logic)
- Third-party libraries

---

### Frontend Testing (Vue3)

#### Technology Stack
- **Vitest**: Fast unit testing framework
- **Vue Test Utils**: Vue component testing
- **Testing Library**: User-centric testing
- **Playwright**: E2E testing
- **MSW**: API mocking

#### Test Structure
```
src/
├── components/
│   └── Button/
│       ├── Button.vue
│       └── __tests__/
│           └── Button.spec.ts
├── views/
│   └── system/
│       ├── UserList.vue
│       └── __tests__/
│           └── UserList.spec.ts
└── utils/
    ├── dateUtil.ts
    └── __tests__/
        └── dateUtil.spec.ts
```

#### Component Test Example
```typescript
import { describe, it, expect, vi } from 'vitest';
import { mount } from '@vue/test-utils';
import Button from '../Button.vue';

describe('Button Component', () => {
  it('renders button text correctly', () => {
    const wrapper = mount(Button, {
      slots: {
        default: 'Click Me'
      }
    });
    
    expect(wrapper.text()).toBe('Click Me');
  });
  
  it('emits click event when clicked', async () => {
    const wrapper = mount(Button);
    
    await wrapper.trigger('click');
    
    expect(wrapper.emitted('click')).toHaveLength(1);
  });
  
  it('is disabled when disabled prop is true', () => {
    const wrapper = mount(Button, {
      props: {
        disabled: true
      }
    });
    
    expect(wrapper.attributes('disabled')).toBeDefined();
  });
});
```

#### Composable Test Example
```typescript
import { describe, it, expect, beforeEach } from 'vitest';
import { useUserStore } from '@/store/modules/user';
import { setActivePinia, createPinia } from 'pinia';

describe('useUserStore', () => {
  beforeEach(() => {
    setActivePinia(createPinia());
  });
  
  it('should set user info correctly', () => {
    const store = useUserStore();
    const userInfo = {
      id: '1',
      username: 'admin',
      realname: 'Administrator'
    };
    
    store.setUserInfo(userInfo);
    
    expect(store.userInfo).toEqual(userInfo);
  });
});
```

#### E2E Test Example (Playwright)
```typescript
import { test, expect } from '@playwright/test';

test.describe('User Management', () => {
  test('should login and create new user', async ({ page }) => {
    // Login
    await page.goto('http://localhost:3100');
    await page.fill('input[name="username"]', 'admin');
    await page.fill('input[name="password"]', '123456');
    await page.click('button[type="submit"]');
    
    // Navigate to user management
    await page.click('text=系统管理');
    await page.click('text=用户管理');
    
    // Create new user
    await page.click('text=新增');
    await page.fill('input[name="username"]', 'testuser');
    await page.fill('input[name="realname"]', 'Test User');
    await page.click('button:has-text("确定")');
    
    // Verify
    await expect(page.locator('text=操作成功')).toBeVisible();
  });
});
```

---

### Test Coverage Requirements

#### Minimum Coverage Targets

| Component | Unit Tests | Integration Tests | E2E Tests | Total Coverage |
|-----------|-----------|------------------|-----------|----------------|
| **Backend** |
| Core Services | 80% | 60% | - | **70%+** |
| Controllers | 60% | 80% | - | **70%+** |
| Utilities | 90% | - | - | **90%+** |
| **Frontend** |
| Components | 70% | - | - | **70%+** |
| Stores | 80% | - | - | **80%+** |
| Utils | 90% | - | - | **90%+** |
| Views | 40% | - | 60% | **50%+** |

#### Critical Paths (90%+ Coverage Required)
- User authentication and authorization
- Data permissions and RBAC
- Code generator core logic
- Online form builder
- Workflow engine
- AI integration modules
- Payment processing (if applicable)
- Data encryption/decryption

---

### Running Tests

#### Backend (Maven)

```bash
# Run all tests
mvn test

# Run tests with coverage
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=SysUserServiceTest

# Run tests in specific module
mvn test -pl jeecg-module-system

# Skip tests (not recommended)
mvn install -DskipTests
```

#### Frontend (npm/pnpm)

```bash
# Run all tests
pnpm test

# Run tests in watch mode
pnpm test:watch

# Run tests with coverage
pnpm test:coverage

# Run E2E tests
pnpm test:e2e

# Run specific test file
pnpm test Button.spec.ts
```

#### IDE Integration

**IntelliJ IDEA:**
- Right-click test file → Run 'TestName'
- Use Coverage tool window for coverage reports
- Install JUnit plugin for better test visualization

**VS Code:**
- Install "Vitest" extension
- Install "Playwright Test for VSCode" extension
- Use Test Explorer for running tests

---

### Writing Tests

#### Test Naming Convention

**Java (JUnit 5):**
```java
@Test
@DisplayName("Should [expected behavior] when [condition]")
void should[ExpectedBehavior]When[Condition]() {
    // Test implementation
}
```

**TypeScript (Vitest):**
```typescript
describe('ComponentName or FunctionName', () => {
  it('should [expected behavior] when [condition]', () => {
    // Test implementation
  });
});
```

#### AAA Pattern (Arrange-Act-Assert)

```java
@Test
void shouldCalculateTotalPrice() {
    // Arrange - Set up test data
    Order order = new Order();
    order.addItem(new Item("Product", 100.0, 2));
    
    // Act - Execute the behavior
    double total = order.calculateTotal();
    
    // Assert - Verify the result
    assertThat(total).isEqualTo(200.0);
}
```

#### Test Data Builders

```java
public class SysUserBuilder {
    private String username = "testuser";
    private String realname = "Test User";
    private String email = "test@example.com";
    
    public SysUserBuilder withUsername(String username) {
        this.username = username;
        return this;
    }
    
    public SysUserBuilder withEmail(String email) {
        this.email = email;
        return this;
    }
    
    public SysUser build() {
        SysUser user = new SysUser();
        user.setUsername(username);
        user.setRealname(realname);
        user.setEmail(email);
        return user;
    }
}

// Usage
SysUser user = new SysUserBuilder()
    .withUsername("admin")
    .withEmail("admin@jeecg.com")
    .build();
```

---

### CI/CD Integration

#### GitHub Actions Workflow

```yaml
name: Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  backend-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
          
      - name: Run tests with Maven
        run: |
          cd jeecg-boot
          mvn clean test jacoco:report
          
      - name: Upload coverage to Codecov
        uses: codecov/codecov-action@v3
        with:
          files: ./jeecg-boot/target/site/jacoco/jacoco.xml
          
  frontend-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      
      - name: Setup Node.js
        uses: actions/setup-node@v3
        with:
          node-version: '20'
          
      - name: Install pnpm
        uses: pnpm/action-setup@v2
        with:
          version: 9
          
      - name: Install dependencies
        run: |
          cd jeecgboot-vue3
          pnpm install
          
      - name: Run tests
        run: |
          cd jeecgboot-vue3
          pnpm test:coverage
          
      - name: Upload coverage
        uses: codecov/codecov-action@v3
        with:
          files: ./jeecgboot-vue3/coverage/coverage-final.json
```

#### Quality Gates

**Minimum requirements to merge PR:**
- ✅ All tests pass
- ✅ No decrease in code coverage
- ✅ New code has ≥70% coverage
- ✅ No critical security vulnerabilities
- ✅ Code review approved

---

### Best Practices

#### DO's ✅

1. **Write tests for bug fixes**: Add a test that reproduces the bug before fixing it
2. **Keep tests independent**: Each test should run in isolation
3. **Use meaningful assertions**: `assertThat(user.isActive()).isTrue()` not `assertTrue(user.isActive())`
4. **Test edge cases**: Empty lists, null values, boundary conditions
5. **Mock external dependencies**: Databases, APIs, file systems
6. **Use test fixtures**: Reusable test data setup
7. **Clean up after tests**: Use `@AfterEach` or `@AfterAll`
8. **Test error scenarios**: Not just happy paths
9. **Keep tests fast**: Unit tests should run in milliseconds
10. **Review test code**: Tests need code review too

#### DON'Ts ❌

1. **Don't test implementation details**: Test behavior, not internals
2. **Don't use real databases**: Use in-memory or TestContainers
3. **Don't ignore flaky tests**: Fix or remove them
4. **Don't skip tests**: Fix failing tests immediately
5. **Don't write tests that depend on order**: Tests should be independent
6. **Don't test private methods**: Test through public API
7. **Don't use Thread.sleep()**: Use proper waiting mechanisms
8. **Don't commit commented-out tests**: Remove or fix them
9. **Don't test getters/setters**: Unless they have logic
10. **Don't write tests just for coverage**: Write meaningful tests

#### Common Pitfalls

**❌ Bad Test:**
```java
@Test
void testUser() {
    SysUser user = new SysUser();
    user.setUsername("test");
    assertTrue(user.getUsername().equals("test"));
}
```

**✅ Good Test:**
```java
@Test
@DisplayName("Should validate username format correctly")
void shouldValidateUsernameFormat() {
    // Given
    SysUser user = new SysUserBuilder()
        .withUsername("invalid@username")
        .build();
    
    // When
    ValidationResult result = userValidator.validate(user);
    
    // Then
    assertThat(result.isValid()).isFalse();
    assertThat(result.getErrors())
        .containsKey("username")
        .extracting("username")
        .asString()
        .contains("Username can only contain letters and numbers");
}
```

---

### Resources

#### Documentation
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Vitest Documentation](https://vitest.dev/)
- [Vue Test Utils](https://test-utils.vuejs.org/)
- [Playwright Documentation](https://playwright.dev/)

#### Tools
- [JaCoCo](https://www.jacoco.org/) - Java code coverage
- [SonarQube](https://www.sonarqube.org/) - Code quality analysis
- [Codecov](https://codecov.io/) - Coverage reporting
- [TestContainers](https://www.testcontainers.org/) - Integration testing

---

## 中文

### 目录
- [概述](#概述-1)
- [测试理念](#测试理念-1)
- [后端测试 (Java)](#后端测试-java-1)
- [前端测试 (Vue3)](#前端测试-vue3-1)
- [测试覆盖率要求](#测试覆盖率要求-1)
- [运行测试](#运行测试-1)
- [编写测试](#编写测试-1)
- [CI/CD集成](#cicd集成-1)
- [最佳实践](#最佳实践-1)

---

### 概述

JeecgBoot是一个企业级AI低代码平台，需要全面的测试来确保可靠性、可维护性和质量。本文档概述了我们的测试策略、要求和最佳实践。

**当前状态:**
- ⚠️ Maven构建中测试当前被禁用 (`skipTests=true`)
- 📊 测试覆盖率很低(约14个测试文件)
- 🎯 目标:实现70%+后端覆盖率,50%+前端覆盖率

---

### 测试理念

**测试金字塔:**
```
        /\        E2E测试 (10%)
       /  \       - 关键用户工作流
      /    \      - 跨系统集成
     /------\     
    / 集成测试 \    集成测试 (20%)
   /          \   - API测试
  /   Integration\  - 数据库交互
 /--------------\ 
/   单元测试     \ 单元测试 (70%)
/                \ - 业务逻辑
------------------  - 工具函数
```

**核心原则:**
1. **测试优先**: 在编写代码之前或同时编写测试
2. **快速反馈**: 单元测试应在几秒内运行
3. **隔离性**: 测试之间不应相互依赖
4. **可读性**: 测试即文档
5. **可维护性**: 保持测试简单和DRY原则

---

### 后端测试 (Java)

#### 技术栈
- **JUnit 5**: 主要测试框架
- **Mockito**: 模拟框架
- **Spring Boot Test**: 集成测试
- **AssertJ**: 流式断言
- **TestContainers**: 数据库集成测试
- **RestAssured**: API测试

#### 需要测试的内容

**✅ 应该测试:**
- 服务层的业务逻辑
- 数据验证
- 错误处理
- 边界情况和边界条件
- 安全检查(权限、认证)
- 数据转换
- 复杂算法

**❌ 不应该测试:**
- 框架代码(Spring、MyBatis)
- 简单的getter/setter
- 配置类(除非有复杂逻辑)
- 第三方库

---

### 前端测试 (Vue3)

#### 技术栈
- **Vitest**: 快速单元测试框架
- **Vue Test Utils**: Vue组件测试
- **Testing Library**: 以用户为中心的测试
- **Playwright**: E2E测试
- **MSW**: API模拟

---

### 测试覆盖率要求

#### 最低覆盖率目标

| 组件 | 单元测试 | 集成测试 | E2E测试 | 总覆盖率 |
|------|---------|---------|---------|----------|
| **后端** |
| 核心服务 | 80% | 60% | - | **70%+** |
| 控制器 | 60% | 80% | - | **70%+** |
| 工具类 | 90% | - | - | **90%+** |
| **前端** |
| 组件 | 70% | - | - | **70%+** |
| 状态管理 | 80% | - | - | **80%+** |
| 工具函数 | 90% | - | - | **90%+** |
| 视图 | 40% | - | 60% | **50%+** |

#### 关键路径(需要90%+覆盖率)
- 用户认证和授权
- 数据权限和RBAC
- 代码生成器核心逻辑
- 在线表单构建器
- 工作流引擎
- AI集成模块
- 支付处理(如适用)
- 数据加密/解密

---

### 运行测试

#### 后端 (Maven)

```bash
# 运行所有测试
mvn test

# 运行测试并生成覆盖率报告
mvn test jacoco:report

# 运行特定测试类
mvn test -Dtest=SysUserServiceTest

# 运行特定模块的测试
mvn test -pl jeecg-module-system

# 跳过测试(不推荐)
mvn install -DskipTests
```

#### 前端 (npm/pnpm)

```bash
# 运行所有测试
pnpm test

# 监视模式运行测试
pnpm test:watch

# 运行测试并生成覆盖率报告
pnpm test:coverage

# 运行E2E测试
pnpm test:e2e

# 运行特定测试文件
pnpm test Button.spec.ts
```

---

### 最佳实践

#### 应该做的 ✅

1. **为bug修复编写测试**: 在修复bug之前添加能重现bug的测试
2. **保持测试独立**: 每个测试应该独立运行
3. **使用有意义的断言**: `assertThat(user.isActive()).isTrue()` 而不是 `assertTrue(user.isActive())`
4. **测试边界情况**: 空列表、null值、边界条件
5. **模拟外部依赖**: 数据库、API、文件系统
6. **使用测试固件**: 可重用的测试数据设置
7. **测试后清理**: 使用 `@AfterEach` 或 `@AfterAll`
8. **测试错误场景**: 不仅仅是正常流程
9. **保持测试快速**: 单元测试应该在毫秒内运行
10. **审查测试代码**: 测试代码也需要代码审查

#### 不应该做的 ❌

1. **不要测试实现细节**: 测试行为,而不是内部实现
2. **不要使用真实数据库**: 使用内存数据库或TestContainers
3. **不要忽略不稳定的测试**: 修复或删除它们
4. **不要跳过测试**: 立即修复失败的测试
5. **不要编写依赖顺序的测试**: 测试应该独立
6. **不要测试私有方法**: 通过公共API测试
7. **不要使用Thread.sleep()**: 使用适当的等待机制
8. **不要提交被注释的测试**: 删除或修复它们
9. **不要只为覆盖率而测试**: 编写有意义的测试
10. **不要测试getter/setter**: 除非它们有逻辑

---

### 资源

#### 文档
- [JUnit 5 用户指南](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito 文档](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Vitest 文档](https://vitest.dev/)
- [Vue Test Utils](https://test-utils.vuejs.org/)
- [Playwright 文档](https://playwright.dev/)

#### 工具
- [JaCoCo](https://www.jacoco.org/) - Java代码覆盖率
- [SonarQube](https://www.sonarqube.org/) - 代码质量分析
- [Codecov](https://codecov.io/) - 覆盖率报告
- [TestContainers](https://www.testcontainers.org/) - 集成测试

---

## Contributing

When contributing to JeecgBoot:
1. Write tests for all new features
2. Ensure existing tests pass
3. Maintain or improve code coverage
4. Follow the testing guidelines in this document

For questions or suggestions about testing, please open an issue or discussion.

---

**Last Updated:** 2025-11-27  
**Version:** 1.0.0  
**Maintainers:** JeecgBoot Team