package com.yupi.yupao.model.enums;

/**
 * 队伍状态枚举
 *
 * @author Ethan
 */
public enum TeamStatusEnum {

    PUBLIC(0, "公开"),
    PRIVATE(1, "私有"),
    SECRET(2, "加密");

    private int value;

    private String text;

    /**
     * 根据 value 获取对应的枚举值。
     *
     * @param value 状态值（0 公开 / 1 私有 / 2 加密）
     * @return 对应的枚举；找不到则返回 null
     */
    public static TeamStatusEnum getEnumByValue(Integer value) {
        if (value == null) {
            return null;
        }
        TeamStatusEnum[] values = TeamStatusEnum.values();
        for (TeamStatusEnum teamStatusEnum : values) {
            if (teamStatusEnum.getValue() == value) {
                return teamStatusEnum;
            }
        }
        return null;
    }

    TeamStatusEnum(int value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 获取状态值。
     *
     * @return 状态值
     */
    public int getValue() {
        return value;
    }

    /**
     * 设置状态值。
     *
     * @param value 状态值
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * 获取状态文本。
     *
     * @return 状态文本
     */
    public String getText() {
        return text;
    }

    /**
     * 设置状态文本。
     *
     * @param text 状态文本
     */
    public void setText(String text) {
        this.text = text;
    }
}
