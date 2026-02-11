-- 初始化示例数据（可重复执行）

use `friend_match`;

START TRANSACTION;

-- 用户数据（密码已加盐 MD5）
INSERT INTO `user`
(`id`, `username`, `userAccount`, `avatarUrl`, `gender`, `userPassword`, `phone`, `email`, `userStatus`, `userRole`, `planetCode`, `tags`)
VALUES
(1001, 'Ethan', 'ethan01', 'https://images.pexels.com/photos/2739792/pexels-photo-2739792.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1', 0, '946e101716ca9b5a09c70b4ae9cf79d4', '13800000001', 'ethan@example.com', 0, 0, '1001', '["Java","Spring","MySQL"]'),
(1002, 'Luna', 'luna01', 'https://images.pexels.com/photos/2739742/pexels-photo-2739742.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 1, '00539df1b5570e8a2b01456c21d3891f', '13800000002', 'luna@example.com', 0, 0, '1002', '["Vue","Vite","Vant"]'),
(1003, 'Ming', 'ming01', 'https://images.pexels.com/photos/30292064/pexels-photo-30292064/free-photo-of-elegant-woman-posing-amidst-floral-background.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 0, '378cd8386db6b2a431afd87a2c307755', '13800000003', 'ming@example.com', 0, 0, '1003', '["Python","AI","Flask"]'),
(1004, 'Ava', 'ava01', 'https://images.pexels.com/photos/8578933/pexels-photo-8578933.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 1, '946e101716ca9b5a09c70b4ae9cf79d4', '13800000004', 'ava@example.com', 0, 0, '1004', '["Product","Design","UI"]'),
(1005, 'Kai', 'kai01', 'https://images.pexels.com/photos/2741759/pexels-photo-2741759.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 0, '00539df1b5570e8a2b01456c21d3891f', '13800000005', 'kai@example.com', 0, 0, '1005', '["Go","Redis","Docker"]')
ON DUPLICATE KEY UPDATE
`username`=VALUES(`username`),
`userAccount`=VALUES(`userAccount`),
`avatarUrl`=VALUES(`avatarUrl`),
`gender`=VALUES(`gender`),
`userPassword`=VALUES(`userPassword`),
`phone`=VALUES(`phone`),
`email`=VALUES(`email`),
`userStatus`=VALUES(`userStatus`),
`userRole`=VALUES(`userRole`),
`planetCode`=VALUES(`planetCode`),
`tags`=VALUES(`tags`);

-- 更多用户（头像来自公开可用图片源）
INSERT INTO `user`
(`id`, `username`, `userAccount`, `avatarUrl`, `gender`, `userPassword`, `phone`, `email`, `userStatus`, `userRole`, `planetCode`, `tags`)
VALUES
(1010, 'Noah', 'noah01', 'https://images.pexels.com/photos/8578926/pexels-photo-8578926.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 0, '946e101716ca9b5a09c70b4ae9cf79d4', '13800000010', 'noah@example.com', 0, 0, '1010', '["Node","React","TypeScript"]'),
(1011, 'Ivy', 'ivy01', 'https://images.pexels.com/photos/12380444/pexels-photo-12380444.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 1, '00539df1b5570e8a2b01456c21d3891f', '13800000011', 'ivy@example.com', 0, 0, '1011', '["UI","Figma","Design"]'),
(1012, 'Leo', 'leo01', 'https://images.pexels.com/photos/12380445/pexels-photo-12380445.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 0, '378cd8386db6b2a431afd87a2c307755', '13800000012', 'leo@example.com', 0, 0, '1012', '["Java","SpringBoot","Redis"]'),
(1013, 'Mia', 'mia01', 'https://images.pexels.com/photos/16588566/pexels-photo-16588566.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 1, '946e101716ca9b5a09c70b4ae9cf79d4', '13800000013', 'mia@example.com', 0, 0, '1013', '["Product","UX","Growth"]'),
(1014, 'Evan', 'evan01', 'https://images.pexels.com/photos/14592946/pexels-photo-14592946.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 0, '00539df1b5570e8a2b01456c21d3891f', '13800000014', 'evan@example.com', 0, 0, '1014', '["Go","K8s","Cloud"]'),
(1015, 'Nora', 'nora01', 'https://images.pexels.com/photos/16122268/pexels-photo-16122268.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 1, '378cd8386db6b2a431afd87a2c307755', '13800000015', 'nora@example.com', 0, 0, '1015', '["Vue","Vite","Pinia"]'),
(1016, 'Owen', 'owen01', 'https://images.pexels.com/photos/6833849/pexels-photo-6833849.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 0, '946e101716ca9b5a09c70b4ae9cf79d4', '13800000016', 'owen@example.com', 0, 0, '1016', '["C++","Algorithm","OS"]'),
(1017, 'Zoe', 'zoe01', 'https://images.pexels.com/photos/2857947/pexels-photo-2857947.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 1, '00539df1b5570e8a2b01456c21d3891f', '13800000017', 'zoe@example.com', 0, 0, '1017', '["AI","NLP","Python"]'),
(1018, 'Ryan', 'ryan01', 'https://images.pexels.com/photos/2739792/pexels-photo-2739792.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1', 0, '378cd8386db6b2a431afd87a2c307755', '13800000018', 'ryan@example.com', 0, 0, '1018', '["Data","SQL","BI"]'),
(1019, 'Sara', 'sara01', 'https://images.pexels.com/photos/30292064/pexels-photo-30292064/free-photo-of-elegant-woman-posing-amidst-floral-background.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 1, '946e101716ca9b5a09c70b4ae9cf79d4', '13800000019', 'sara@example.com', 0, 0, '1019', '["Testing","QA","Automation"]'),
(1020, 'Jude', 'jude01', 'https://images.pexels.com/photos/8578933/pexels-photo-8578933.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 0, '00539df1b5570e8a2b01456c21d3891f', '13800000020', 'jude@example.com', 0, 0, '1020', '["Rust","Systems","Wasm"]'),
(1021, 'Lily', 'lily01', 'https://images.pexels.com/photos/2741759/pexels-photo-2741759.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 1, '378cd8386db6b2a431afd87a2c307755', '13800000021', 'lily@example.com', 0, 0, '1021', '["Mobile","Android","Kotlin"]'),
(1022, 'Finn', 'finn01', 'https://images.pexels.com/photos/2739742/pexels-photo-2739742.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 0, '946e101716ca9b5a09c70b4ae9cf79d4', '13800000022', 'finn@example.com', 0, 0, '1022', '["iOS","Swift","Xcode"]'),
(1023, 'Ella', 'ella01', 'https://images.pexels.com/photos/12380445/pexels-photo-12380445.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 1, '00539df1b5570e8a2b01456c21d3891f', '13800000023', 'ella@example.com', 0, 0, '1023', '["Finance","Quant","Python"]'),
(1024, 'Axel', 'axel01', 'https://images.pexels.com/photos/8578926/pexels-photo-8578926.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500', 0, '378cd8386db6b2a431afd87a2c307755', '13800000024', 'axel@example.com', 0, 0, '1024', '["DevOps","CI/CD","Linux"]')
ON DUPLICATE KEY UPDATE
`username`=VALUES(`username`),
`userAccount`=VALUES(`userAccount`),
`avatarUrl`=VALUES(`avatarUrl`),
`gender`=VALUES(`gender`),
`userPassword`=VALUES(`userPassword`),
`phone`=VALUES(`phone`),
`email`=VALUES(`email`),
`userStatus`=VALUES(`userStatus`),
`userRole`=VALUES(`userRole`),
`planetCode`=VALUES(`planetCode`),
`tags`=VALUES(`tags`);

-- 队伍数据
INSERT INTO `team`
(`id`, `name`, `description`, `maxNum`, `expireTime`, `userId`, `status`, `password`)
VALUES
(2001, 'Java学习小队', '一起刷题、面试与项目交流', 5, DATE_ADD(NOW(), INTERVAL 30 DAY), 1001, 0, NULL),
(2002, '前端冲刺队', 'Vue/Vite/Vant 技术栈共学', 6, DATE_ADD(NOW(), INTERVAL 45 DAY), 1002, 0, NULL),
(2003, 'AI研究组', '机器学习与工程落地讨论', 4, DATE_ADD(NOW(), INTERVAL 60 DAY), 1003, 2, 'ai2024'),
(2004, '产品设计讨论组', '产品思维与体验设计', 8, DATE_ADD(NOW(), INTERVAL 20 DAY), 1004, 0, NULL)
ON DUPLICATE KEY UPDATE
`name`=VALUES(`name`),
`description`=VALUES(`description`),
`maxNum`=VALUES(`maxNum`),
`expireTime`=VALUES(`expireTime`),
`userId`=VALUES(`userId`),
`status`=VALUES(`status`),
`password`=VALUES(`password`);

-- 更多队伍
INSERT INTO `team`
(`id`, `name`, `description`, `maxNum`, `expireTime`, `userId`, `status`, `password`)
VALUES
(2010, '全栈提升营', '前后端全栈项目实战', 6, DATE_ADD(NOW(), INTERVAL 40 DAY), 1010, 0, NULL),
(2011, '算法刷题组', 'LeetCode 每日打卡', 8, DATE_ADD(NOW(), INTERVAL 50 DAY), 1016, 0, NULL),
(2012, '移动端开发组', 'Android/iOS 协作学习', 5, DATE_ADD(NOW(), INTERVAL 35 DAY), 1021, 0, NULL),
(2013, '数据工程组', '数据建模与可视化', 6, DATE_ADD(NOW(), INTERVAL 55 DAY), 1018, 0, NULL),
(2014, '设计共学会', 'UI/UX 作品互评', 7, DATE_ADD(NOW(), INTERVAL 25 DAY), 1011, 0, NULL),
(2015, 'DevOps实践队', 'CI/CD 与部署实践', 5, DATE_ADD(NOW(), INTERVAL 45 DAY), 1024, 2, 'devops2024')
ON DUPLICATE KEY UPDATE
`name`=VALUES(`name`),
`description`=VALUES(`description`),
`maxNum`=VALUES(`maxNum`),
`expireTime`=VALUES(`expireTime`),
`userId`=VALUES(`userId`),
`status`=VALUES(`status`),
`password`=VALUES(`password`);

-- 用户队伍关系（队长默认加入）
INSERT INTO `user_team`
(`id`, `userId`, `teamId`, `joinTime`)
VALUES
(3001, 1001, 2001, NOW()),
(3002, 1002, 2002, NOW()),
(3003, 1003, 2003, NOW()),
(3004, 1004, 2004, NOW()),
(3005, 1005, 2001, NOW()),
(3006, 1005, 2002, NOW()),
(3007, 1002, 2001, NOW()),
(3008, 1003, 2002, NOW())
ON DUPLICATE KEY UPDATE
`userId`=VALUES(`userId`),
`teamId`=VALUES(`teamId`),
`joinTime`=VALUES(`joinTime`);

-- 更多用户队伍关系
INSERT INTO `user_team`
(`id`, `userId`, `teamId`, `joinTime`)
VALUES
(3010, 1010, 2010, NOW()),
(3011, 1011, 2014, NOW()),
(3012, 1016, 2011, NOW()),
(3013, 1021, 2012, NOW()),
(3014, 1018, 2013, NOW()),
(3015, 1024, 2015, NOW()),
(3016, 1012, 2010, NOW()),
(3017, 1013, 2014, NOW()),
(3018, 1014, 2015, NOW()),
(3019, 1015, 2012, NOW()),
(3020, 1017, 2013, NOW()),
(3021, 1019, 2011, NOW()),
(3022, 1020, 2010, NOW()),
(3023, 1022, 2012, NOW()),
(3024, 1023, 2014, NOW())
ON DUPLICATE KEY UPDATE
`userId`=VALUES(`userId`),
`teamId`=VALUES(`teamId`),
`joinTime`=VALUES(`joinTime`);

COMMIT;
