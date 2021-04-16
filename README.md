# WeSwallows
2020冬季雏燕项目——基于springboot的校内信息交互系统

## 项目设计

### 总体设计

- **本项目用到的技术和框架**<br>

|  工具 | 名称 
| ------------ | ------------
| 开发工具  | IDEA、vscode
| 项目构建 | Maven
| UI框架 | layui、semantic UI
|  语言 | JDK1.8、HTML、css、js
| 内容展示 | wangEditor.md
| 数据库  | Mysql5.6
| 项目框架  | SSH
| ORM  | JPA
| 安全框架  | JWT
| 运行环境  | 腾讯云Centos7

## API

### 首页

#### 所有问题和推荐问题

GET http://localhost:8080/

#### 查询问题

POST http://localhost:8080/seachQuestions

#### 问题内容

GET http://localhost:8080/question/{questionId}

#### 点赞

GET http://localhost:8080/question/{questionId}/approve

#### 点踩

GET http://localhost:8080/question/{questionId}/disapprove

### 个人主页

#### 个人发布的问题

GET http://localhost:8080/customer/questions

#### 新增问题

GET http://localhost:8080/customer/questions/tags

POST http://localhost:8080/customer/questions

#### 查询问题

POST http://localhost:8080/customer/questions/search

#### 删除问题

GET http://localhost:8080/customer/question/{questionId}/delete