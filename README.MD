# 小文件系统 mini-file-system

该项目是实现一套分布式小文件系统的代码，项目代号: mfs(mini-file-system)

重在熟悉分布式项目的实现

## 设计


## 工程模块

- namenode: 维护元数据，维护各个文件的元数据。
- backup namenode: 用以做元数据的image，和主namenode的backup
- datanode: 部署在各个节点，用以管理具体的文件
- rpc: namenode和datanode的通信模块，使用的gRPC实现
- common: 用来放公共的模块，比如配置类等等
- client: 用来客户端集成使用。