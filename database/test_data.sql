-- 插入测试资源数据
INSERT INTO `resource` (
    `title`, 
    `description`, 
    `content`,
    `cover_image`,
    `category_id`,
    `tags`,
    `platform`,
    `language`,
    `status`,
    `is_recommend`,
    `is_hot`,
    `create_user_id`
) VALUES
(
    '塞尔达传说：王国之泪',
    '《塞尔达传说：王国之泪》是任天堂出品的开放世界动作冒险游戏，作为《旷野之息》的正统续作，游戏加入了全新的空岛探索玩法。',
    '游戏详细介绍内容...',
    'https://example.com/zelda.jpg',
    3, -- 电脑游戏分类
    '动作,冒险,开放世界',
    'Switch',
    '中文',
    1,
    1,
    1,
    1
),
(
    '原神 3.8版本',
    '《原神》是米哈游开发的开放世界冒险RPG，游戏拥有精致的画面和独特的元素战斗系统。',
    '版本更新内容...',
    'https://example.com/genshin.jpg',
    2, -- 手机游戏分类
    'RPG,开放世界,二次元',
    'PC,Mobile,PS4,PS5',
    '中文',
    1,
    1,
    1,
    1
),
(
    'Minecraft 1.20更新',
    '《我的世界》是一款沙盒游戏，玩家可以在游戏中自由建造和探索。',
    '更新内容详情...',
    'https://example.com/minecraft.jpg',
    3, -- 电脑游戏分类
    '沙盒,建造,生存',
    'PC,Mobile,Console',
    '中文',
    1,
    1,
    1,
    1
); 