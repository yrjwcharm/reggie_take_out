#### SpringBoot Controller入参

Get请求的话，注解@GetMapping 入参直接使用实体类 不用加前缀注解，如果用单个参数去接受的话，需要写多个，


此外还有一种方式是使用Map<String,Object>接受，前边必须添加注解 get 用@RequstParam
post请求 要改成@RequstBody
同理List也一样





但允许我对安装过程做一些更详细的说明：

BIOS/UEFI设置： 开机后，如果没有可引导的操作系统，服务器可能会进入BIOS或UEFI设置界面。你需要确保设置了正确的启动顺序，以便将服务器引导到U盘上的ISO镜像文件。

通过U盘启动： 插入装有操作系统安装ISO镜像文件的U盘。在BIOS/UEFI设置中，确保将U盘设置为首选启动设备。重启服务器，让其从U盘启动。

操作系统安装： 服务器从U盘启动后，会加载ISO镜像文件，进入操作系统安装向导。你可以选择安装的目标位置（通常是服务器上的某个硬盘或分区），并进行安装操作系统的配置。

安装到硬盘上： 安装过程将操作系统文件（如Linux的文件系统）复制到你选择的目标位置，通常是服务器上的硬盘驱动器或指定的分区。

启动顺序设置： 安装完成后，你需要确保在BIOS/UEFI设置中将服务器的启动顺序修改回默认设置，使其首先从安装好的硬盘或分区启动。

无需U盘启动： 安装完操作系统后，服务器会默认从硬盘启动，除非你再次更改启动顺序或手动选择其他引导设备。即使在没有U盘的情况下，服务器也应该能够从已安装的硬盘正常引导并启动操作系统。

在安装操作系统时，确保你了解和正确配置服务器的启动顺序，以便服务器可以顺利地从指定的设备启动，避免因引导问题而无法正常启动。


mysql linux下
rpm -ivh mysql-community-common-8.0.33-1.el7.x86_64.rpm
rpm -ivh mysql-community-client-plugins-8.0.33-1.el7.x86_64.rpm
rpm -ivh mysql-community-libs-8.0.33-1.el7.x86_64.rpm
rpm -ivh mysql-community-client-8.0.33-1.el7.x86_64.rpm
rpm -ivh mysql-community-icu-data-files-8.0.33-1.el7.x86_64.rpm
rpm -ivh mysql-community-server-8.0.33-1.el7.x86_64.rpm