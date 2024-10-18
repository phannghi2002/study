package com.rs.demo2.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.rs.demo2.dto.request.UserCreateRequest;
import com.rs.demo2.dto.request.UserUpdateRequest;
import com.rs.demo2.dto.response.UserResponse;
import com.rs.demo2.entity.User;

// componentModel = "spring" dung de dua no vao Spring bean hay la tiem su phu thuoc cua no, khi can su dung chi can goi
// den Autowired
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
	// chuyen doi kieu du kieu tu UserCreateRequest -> User
	User toUser(UserCreateRequest request);

	// boi vi UserUpdateRequest chua List<String> roles, con User chua Set<Role> roles, 2 cai nay
	// thu nhat la khac nhau kieu du lieu la List va Set, thu hai la khac nhau kieu tham so truyen vao
	// chi can mot trong hai cai nay khac nhau la ta khong the mapping thong thuong ma ta phai mapping
	// theo kieu thu cong, do do ta ignore roles de ta mapping kieu thu cong
	@Mapping(target = "roles", ignore = true)
	void updateUser(@MappingTarget User user, UserUpdateRequest request);

	//   @Mapping(target = "id", ignore = true) //nghia la gia tri id se duoc hien thi la null
	// @Mapping(source = "firstName", target = "lastName") // nghia la thang lastName cua UserResponse tra ve gia tri
	// firstName cua user
	// co the dung nhieu thang mapping o tren chu khong chi phai duoc dung 1 thang duy nhat.
	UserResponse toUserResponse(User user);

	// Add this method for mapping a list of Users to UserResponses
	List<UserResponse> toUserResponse(List<User> users);
}
