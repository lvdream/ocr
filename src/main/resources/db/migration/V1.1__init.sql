CREATE TABLE IF NOT EXISTS `tstb_user_content` (
  `tuc_ic` INT NOT NULL AUTO_INCREMENT COMMENT '序号',
  `mu_id` INT NULL COMMENT '用户序号',
  `tuc_content` VARCHAR(500) NULL COMMENT '发送信息内容',
  `tuc_createtime` VARCHAR(20) NULL COMMENT '发送信息时间',
  `tuc_reply` VARCHAR(100) NULL COMMENT '回复内容',
  PRIMARY KEY (`tuc_ic`))
ENGINE = InnoDB
COMMENT = '用户发送信息表';

CREATE TABLE IF NOT EXISTS `mstb_user` (
  `mu_id` INT NOT NULL AUTO_INCREMENT COMMENT '序号',
  `mu_code` VARCHAR(100) NULL COMMENT '用户代码,来自微信',
  `mu_createtime` VARCHAR(20) NULL COMMENT '用户注册时间',
  `mu_updatetime` VARCHAR(20) NULL COMMENT '用户上一次活动时间',
  PRIMARY KEY (`mu_id`))
  ENGINE = InnoDB
  COMMENT = '注册用户';