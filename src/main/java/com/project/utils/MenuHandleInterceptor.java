package com.project.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.Option;

import com.project.entity.Banner;
import com.project.entity.Role;
import com.project.entity.User;
import com.project.repository.BannerRepository;
import com.project.repository.RoleRepository;
import com.project.repository.UserRepository;
import com.project.service.BannerService;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.project.service.CategoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class MenuHandleInterceptor implements HandlerInterceptor {
	
	@Autowired
	private CategoryService categoryService;

	@Autowired
	private BannerService bannerService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BannerRepository bannerRepository;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {	

		request.setAttribute("menu", categoryService.findByIsDisplay(true));
		request.setAttribute("banners",bannerService.findAll());
		return true;
	}

	private void addAdmin(){
		Role role1 = new Role();
		role1.setRole("ADMIN");
		roleRepository.save(role1);
		Role role2 = new Role();
		role2.setRole("USER");
		roleRepository.save(role2);

		Optional<User> user = userRepository.findUserByUserName("admin");
		if (!user.isPresent()){
			User u = new User();
			u.setUserName("admin");
			u.setPassword("123546");
			u.setAddress("Hà nội");
			u.setPhone("03456789101");
			u.setEmail("admin@gmail.com");
			u.setFullName("Tôi là admin");
			u.setIsEnable(true);
			Role role = roleRepository.findRoleByRole("ADMIN").get();
			u.addRole(role);
			userRepository.save(u);
		}

		List<Banner> lst = new ArrayList<>();
		lst.add(new Banner(1,"Converse","Chào mừng bạn đến với ngôi nhà Converse!" +
				" Tại đây, mỗi một dòng chữ, mỗi chi tiết và hình ảnh đều là những bằng chứng mang dấu " +
				"ấn lịch sử Converse 100 năm, và đang không ngừng phát triển lớn mạnh. " +
				"Tất cả các thành tựu đáng tự hào của Converse đều không thể tách rời bởi 4 dòn" +
				"g sản phẩm kinh điển.","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2Fcon2.jpg?alt=media&token=9d1f7a59-9a94-44ba-9b47-785d6dc63ecb","url"));
		lst.add(new Banner(2,"Nike","58 items","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2Fnike.jpg?alt=media&token=121f37a7-d6b5-4e5b-8024-4601ea1a4a47","/product"));
		lst.add(new Banner(3,"Adias","73 items","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2Fadidas.jpg?alt=media&token=b9b386f9-5b68-43f2-923c-1a7909fab4c7","/product"));
		lst.add(new Banner(4,"Van","15 items","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2Fvan.jpg?alt=media&token=0271d7d3-fd40-46b6-8f20-16b90c8efaf6","/product"));
		lst.add(new Banner(5,"Accessories","32 items","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2Fac.jpg?alt=media&token=b045b880-9037-48fe-b4e6-66171a675fd1","/product"));
		lst.add(new Banner(6,"BannerFoot1","desciption","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2Fbanner-1.jpg?alt=media&token=017155eb-9858-4c00-b3f9-31008ca2f31f","/product"));
		lst.add(new Banner(7,"BannerInstar1","desciption","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2F1.jpg?alt=media&token=c20f2f56-2d7c-49f3-aed6-3c7127594db3","/product"));
		lst.add(new Banner(8,"BannerFoot3","desciption","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2F1.jpg?alt=media&token=c20f2f56-2d7c-49f3-aed6-3c7127594db3","/product"));
		lst.add(new Banner(9,"BannerFoot4","desciption","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2F3.jpg?alt=media&token=d7e86a31-90d5-4b84-8417-02c58eb30621","/product"));
		lst.add(new Banner(10,"BannerFoot5","desciption","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2F4.jpg?alt=media&token=e45d502c-0529-4bd4-b28a-3904a3a2fedf","/product"));
		lst.add(new Banner(11,"BannerFoot6","desciption","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2F5.jpg?alt=media&token=48b48229-bb14-4526-a535-65b05ced4600","/product"));
		lst.add(new Banner(12,"BannerFoot7","desciption","https://firebasestorage.googleapis.com/v0/b/shoe-mock-project.appspot.com/o/banner%2F6.jpg?alt=media&token=6e182249-9bc2-41ed-bd17-6767519b230a","/product"));

		bannerRepository.saveAll(lst);
	}

}
