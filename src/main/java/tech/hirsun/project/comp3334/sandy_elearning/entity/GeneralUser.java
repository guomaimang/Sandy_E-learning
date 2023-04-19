package tech.hirsun.project.comp3334.sandy_elearning.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

/*
 * 使用 Lombok 注解：在您的 Java 类中使用 Lombok 提供的注解来自动生成样板代码。以下是一些常用的 Lombok 注解：
 * @Getter 和 @Setter：为类的字段自动生成 getter 和 setter 方法。
 * @ToString：为类自动生成 toString 方法，该方法返回类的字段及其值的字符串表示。
 * @EqualsAndHashCode：为类自动生成 equals 和 hashCode 方法，这些方法基于类的字段来判断相等和计算哈希值。
 * @NoArgsConstructor、@RequiredArgsConstructor 和 @AllArgsConstructor：为类自动生成无参构造函数、部分参数构造函数（仅包含标记为 @NonNull 的字段）和全参构造函数。
 * @Data：为类自动生成所有上述注解提供的功能（getter、setter、toString、equals、hashCode 和构造函数）。
 */

import java.util.Date;

@Getter
@Setter
public class GeneralUser {
    private Long id;
    private String userName;
    private String password;
    private String userToken;
    private int isDeleted;
    private int isStudent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Override
    public String toString() {
        return "GeneralUser{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", userToken='" + userToken + '\'' +
                ", isDeleted=" + isDeleted +
                ", createTime=" + createTime +
                '}';
    }
}
