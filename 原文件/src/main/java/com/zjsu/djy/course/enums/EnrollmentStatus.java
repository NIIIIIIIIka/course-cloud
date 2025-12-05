package com.zjsu.djy.course.enums;

import lombok.Getter;

/**
 * 选课状态枚举
 * 用于规范Enrollment（选课记录）的状态管理，避免随意字符串导致的状态混乱
 */
@Getter
public enum EnrollmentStatus {

    /**
     * 正常选课状态：学生成功选课且未退课，处于有效状态
     */
    ACTIVE("ACTIVE", "正常选课"),

    /**
     * 已退课状态：学生主动退课，选课记录标记为已失效
     */
    DROPPED("DROPPED", "已退课"),

    /**
     * 过期状态：课程结束或学期结束，选课记录自动变为过期（非主动操作）
     */
    EXPIRED("EXPIRED", "已过期"),

    /**
     * 暂停状态：特殊情况暂停（如学生休学、课程临时停开），可恢复为正常
     */
    COMPLETED("COMPLETED", "已结束");


    /**
     * 数据库存储值：用简短英文标识（如ACTIVE），兼顾存储效率和可读性
     * 避免直接存储中文或数字（数字需对照文档，可读性差；中文占存储且易乱码）
     */
    private final String code;

    /**
     * 状态描述：详细说明状态含义，用于日志打印、接口返回提示等场景
     */
    private final String description;


    /**
     * 枚举构造方法（默认private，无需手动指定访问修饰符）
     * @param code 数据库存储的状态标识
     * @param description 状态的中文描述
     */
    EnrollmentStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }


    /**
     * 工具方法：根据数据库存储的code获取对应的枚举对象
     * 避免直接用valueOf()（需完全匹配枚举名，灵活性低），支持自定义code匹配
     * @param code 数据库中存储的状态标识（如"ACTIVE"）
     * @return 对应的EnrollmentStatus枚举对象
     * @throws IllegalArgumentException 当code不存在时抛出异常，快速定位非法数据
     */
    public static EnrollmentStatus getByCode(String code) {
        // 遍历所有枚举值，匹配code（忽略大小写可添加：.equalsIgnoreCase(code)，根据业务需求决定）
        for (EnrollmentStatus status : EnrollmentStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        // 非法code时抛出明确异常，便于排查问题（如数据库中存在未定义的状态值）
        throw new IllegalArgumentException("未定义的选课状态：" + code + "，请检查枚举类EnrollmentStatus是否包含该状态");
    }


    /**
     * 重写toString()：返回"code(description)"格式，便于日志打印和调试
     * 示例：ACTIVE(正常选课)
     */
    @Override
    public String toString() {
        return this.code + "(" + this.description + ")";
    }
}