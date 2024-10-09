package com.rs.demo2.mapper;

import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.request.UserUpdateRequest;
import com.rs.demo2.dto.response.UserResponse;
import com.rs.demo2.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
//nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE no co tac dung chi update nhung truong co gia tri,
//khi khong co gia tri thi no van lay gia tri cu, thuong dung cho update data

//componentModel = "spring" dung de dua no vao Spring bean hay la tiem su phu thuoc cua no, khi can su dung chi can goi den Autowired
@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    User toUser(UserCreateRequest request);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

//    @Mapping(target = "id", ignore = true) //nghia la gia tri id se duoc hien thi la null
    //@Mapping(source = "firstName", target = "lastName") // nghia la thang lastName cua UserResponse tra ve gia tri firstName cua user
    //co the dung nhieu thang mapping o tren chu khong chi phai duoc dung 1 thang duy nhat.
    UserResponse toUserResponse(User user);

    // Add this method for mapping a list of Users to UserResponses
    List<UserResponse> toUserResponse(List<User> users);
}
