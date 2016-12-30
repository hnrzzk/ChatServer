# ChatServer
完美世界实习期作业-聊天服务器

考虑实现.一个聊天服务器.，拥有以下功能：
1. 好友之间聊天的基本功能。
2. 好友列列表，增加好友，删除好友，好友分组，.黑名单，搜索.用户（通过.用户名或者id）。
3. .用户数据需要存储在MySQL数据库中。
4. 登录需要用户名密码，使.用挑战应答机制。
5. 好友上线和下线通知，.支持离线消息。
6. .自.己实现客户端。
7. 聊天室功能，多.人同时在线聊天。
8. 系统管理理员功能：发送全服广播，封禁用户登录，封禁用户聊天。


技术要求
1. 使.用Apache Mina作为.网络库处理理客户端的连接，使.用tcp连接。
2. 数据库使.用MySQL，通过JDBC连接，需要实现.自.己的数据库连接池。
3. 服务器.必须是多线程的。
4. .支持.至少5000个客户端同时连接，每个连接间隔1秒不不断发送消息。
5. 服务器.处理理消息的逻辑必须是异步的。
6. 自己定义消息格式。
7. 客户端可以是无图形界面实现（命令行行）。


测试
自己编写机器人，来测试多连接，定时发送消息，得到CPU和内存占用情况。


要求
1. 在2周内完成这个工作，越快越好。

其他进阶要求：
1. 了了解Hibernate和JPA，把上面的数据库存储和读取相关部分改成使用JPA+Hibernate的实现方式。这个
希望在3天左右完成。
2. 了了解java对象序列列化成xml（提示：simple, http://simple.sourceforge.net）
3. 了了解Lua和Python
